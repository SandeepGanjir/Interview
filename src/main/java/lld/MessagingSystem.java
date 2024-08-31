package lld;

import com.google.gson.Gson;

import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Pattern;

/*
Time for Question Reading and understanding : 10 min.
Coding Time : 90 min.

Q. Design an efficient in-memory queueing system with low latency requirements and also write producer and consumer using multi threading with following constraints :

Must Have Tasks :

    There should be a queue, that can receive the message from the producer, and send the message to the consumer.
    Queue is bounded in size and completely held in-memory. Size should be configurable.
    Queue should only hold JSON messages.
    Queue will have at least one producer and multiple consumers.
    Consumers register callbacks that will be invoked whenever there is a new message
    Allow subscription of consumers to messages that match a particular expression
    Consumers might have dependency relationships between them.
    For ex :

if there are three consumers A, B and C. One dependency relationship can be that C cannot consume a particular message before A and B have consumed it.
C -> (A,B) (-> means must process after).

    Handle concurrent writes and reads consistently between producer and consumers.

Bonus Tasks :

    Provide retry mechanisms to handle failures in message processing. It could be failure in publishing or consumption.
    Handle the message TTL. means the message could be expired after some time T. if a message is expired, it should not be delivered to the consumer.

Suggestions :
Try completing the tasks one by one, run it, test it, then move on to the next. Pick the task in any order that you want.
Think about the extension of the problem before choosing your LLD. You might be asked to add some new features in this problem during evaluation time.

Restriction :
You are not allowed to use in-built queue data structure provided by any language. Expected to implement the queue.

Allowed :
You can use library for JSON

**How you will be evaluated **
Code should be working
Separation of concerns
Abstractions
Application of OO design principles
Testability
Code readability
Language proficiency
*/

interface Publisher {
    String getName();

    boolean publish(String json);

    boolean publish(String json, int timeToLive);
}

interface Subscriber {
    String getName();

    void consume(String json);
}

interface Callback {
    void execute(String json);
}

class Packet {
    private final String json;
    private final long created;
    private final Integer timeToLive;

    public Packet(String json, Integer timeToLive) {
        this.json = json;
        this.created = System.currentTimeMillis();
        this.timeToLive = timeToLive;
    }

    public boolean isLive() {
        return timeToLive == null || (created + timeToLive > System.currentTimeMillis());
    }

    public String getJson() {
        return this.json;
    }
}

class PacketQueue {
    private final Packet[] queue;
    private volatile int first, last;

    public PacketQueue(int capacity) {
        this.queue = new Packet[capacity + 1];
        this.first = 0;
        this.last = 0;
    }

    public boolean isEmpty() {
        return first == last;
    }

    private boolean isFull() {
        return ((last - first + queue.length) % queue.length) == (queue.length - 1);
    }

    public synchronized boolean publish(String json, Integer timeToLive) {
        if (isFull())
            return false;
        queue[last] = new Packet(json, timeToLive);
        last = (last + 1) % queue.length;
        return true;
    }

    public synchronized String consume() {
        Packet msg = null;
        do {
            if (isEmpty())
                return null;
            msg = queue[first];
            first = (first + 1) % queue.length;
        } while (!msg.isLive());
        return msg.getJson();
    }
}

class PacketBroker {
    private final PacketQueue queue;
    private final Map<String, Subscriber> consumerByConsumerName;
    private final Map<String, Set<Pattern>> patternsByConsumerName;
    private final Map<String, Set<String>> consumerDependency;
    private final CopyOnWriteArrayList<Set<String>> dependencyOrder;
    private final ScheduledExecutorService executor;
    private volatile boolean startConsuming = false;


    public PacketBroker(int capacity) {
        this.queue = new PacketQueue(capacity);
        this.consumerByConsumerName = new HashMap<>();
        this.patternsByConsumerName = new HashMap<>();
        this.consumerDependency = new HashMap<>();
        this.dependencyOrder = new CopyOnWriteArrayList<>();
        this.executor = Executors.newScheduledThreadPool(2);
    }

    public boolean publish(String json, Integer timeToLive) {
        return queue.publish(json, timeToLive);
    }

    public synchronized void subscribe(Subscriber consumer, String regex) {
        this.consumerByConsumerName.put(consumer.getName(), consumer);
        Set<Pattern> patterns = patternsByConsumerName.computeIfAbsent(consumer.getName(), n -> new HashSet<>());
        patterns.add(Pattern.compile(regex));
    }

    public synchronized void addDependency(Subscriber consumer1, Subscriber consumer2) {
        Set<String> dependsOn = this.consumerDependency.computeIfAbsent(consumer1.getName(), n -> new HashSet<>());
        dependsOn.add(consumer2.getName());
    }

    public synchronized void buildDependencyOrder() {
        this.dependencyOrder.clear();
        Set<String> remaining = new HashSet<>(consumerByConsumerName.keySet());
        while (!remaining.isEmpty()) {
            Set<String> batch = new HashSet<>();
            for (String consumer: remaining) {
                List<String> pending = new LinkedList<>();
                for (String dependant: this.consumerDependency.getOrDefault(consumer, new HashSet<>())) {
                    if (remaining.contains(dependant))
                        pending.add(dependant);
                }
                if (pending.isEmpty()) batch.add(consumer);
            }
            if (batch.isEmpty()) throw new RuntimeException("Cyclic dependency between consumers: " + remaining);
            this.dependencyOrder.add(batch);
            remaining.removeAll(batch);
        }
    }

    public synchronized void start() {
        if (!startConsuming) {
            this.buildDependencyOrder();
            this.executor.scheduleAtFixedRate(this::processMessages, 10, 100, TimeUnit.MILLISECONDS);
            startConsuming = true;
        }
    }

    public synchronized void close() {
        if (startConsuming) {
            this.executor.shutdown();
            startConsuming = false;
        }
        this.processMessages();
    }

    private void processMessages() {
        while (!this.queue.isEmpty()) {
            String json = this.queue.consume();
            for (Set<String> consumers: this.dependencyOrder) {
                consumers.stream().parallel().forEach(consumerName -> {
                    boolean matches = this.patternsByConsumerName.get(consumerName).stream().anyMatch(pattern -> pattern.matcher(json).matches());
                    if (matches) this.consumerByConsumerName.get(consumerName).consume(json);
                });
            }
        }
    }

    String consume() {
        return queue.consume();
    }
}

class PublisherImpl implements Publisher {
    private static final int RETRY_COUNT = 3;
    private final String name;
    private final PacketBroker broker;

    public PublisherImpl(String name, PacketBroker broker) {
        this.name = name;
        this.broker = broker;
    }

    private boolean _publish(String json, Integer timeToLive) {
        boolean sent = broker.publish(json, timeToLive);
        for (int i = 0; i < RETRY_COUNT && !sent; i++) {
            try {
                System.out.println("Retrying: " + json);
                Thread.sleep(100);
                sent = broker.publish(json, timeToLive);
            } catch (Exception e) {
            }
        }
        if (!sent)
            System.out.println("Dropped: " + json);
        return sent;
    }

    @Override
    public boolean publish(String json) {
        return _publish(json, null);
    }

    @Override
    public boolean publish(String json, int timeToLive) {
        return _publish(json, timeToLive);
    }

    @Override
    public String getName() {
        return name;
    }
}

class SubscriberImpl implements Subscriber {
    private final String name;
    private final Callback callback;

    public SubscriberImpl(String name) {
        this(name, null);
    }

    public SubscriberImpl(String name, Callback callback) {
        this.name = name;
        this.callback = callback;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void consume(String json) {
        if (this.callback != null) {
            callback.execute(json);
        } else {
            System.out.println(getName() + " consumed : " + json);
        }
    }
}

public class MessagingSystem {
    Gson gson = new Gson();

    public static void main(String[] args) throws Exception {
        MessagingSystem ins = new MessagingSystem();
        ins.testProducer();
        ins.testTimeToLive();
        ins.testConsumer();
        ins.testBoth();
    }

    private void testProducer() {
        PacketBroker broker = new PacketBroker(2);
        Publisher producer1 = new PublisherImpl("Producer-1", broker);
        Map<String, String> messageObj = new HashMap<>();
        messageObj.put("name", producer1.getName());
        messageObj.put("message", "Message-1");
        boolean sent = producer1.publish(gson.toJson(messageObj));
        System.out.println("Published : " + sent + "\n" + gson.toJson(messageObj));
        assert sent == true;
        messageObj.put("message", "Message-2");
        sent = producer1.publish(gson.toJson(messageObj));
        System.out.println("Published : " + sent + "\n" + gson.toJson(messageObj));
        assert sent == true;
        messageObj.put("message", "Message-3");
        sent = producer1.publish(gson.toJson(messageObj));
        System.out.println("Published : " + sent + "\n" + gson.toJson(messageObj));
        assert sent == false;
        String message = broker.consume();
        Map<String, String> consumedObj = gson.fromJson(message, Map.class);
        assert consumedObj.get("message").equalsIgnoreCase("Message-1");
        sent = producer1.publish(gson.toJson(messageObj));
        System.out.println("Published : " + sent + "\n" + gson.toJson(messageObj));
        assert sent == true;
        messageObj.put("message", "Message-4");
        sent = producer1.publish(gson.toJson(messageObj));
        System.out.println("Published : " + sent + "\n" + gson.toJson(messageObj));
        assert sent == false;
        message = broker.consume();
        consumedObj = gson.fromJson(message, Map.class);
        assert consumedObj.get("message").equalsIgnoreCase("Message-2");
        sent = producer1.publish(gson.toJson(messageObj));
        System.out.println("Published : " + sent + "\n" + gson.toJson(messageObj));
        assert sent == true;
    }

    private void testTimeToLive() throws InterruptedException {
        PacketBroker broker = new PacketBroker(2);
        Publisher producer1 = new PublisherImpl("Producer-1", broker);
        Map<String, String> messageObj = new HashMap<>();
        messageObj.put("name", producer1.getName());
        messageObj.put("message", "Message-1");
        boolean sent = producer1.publish(gson.toJson(messageObj), 10);
        Thread.sleep(20);
        String message = broker.consume();
        assert message == null;
    }

    private void testConsumer() {
        PacketBroker broker = new PacketBroker(2);
        Publisher producer1 = new PublisherImpl("Producer-1", broker);
        Subscriber consumer1 = new SubscriberImpl("Consumer-1");
        broker.subscribe(consumer1, ".*");
        Subscriber consumer2 = new SubscriberImpl("Consumer-2");
        broker.subscribe(consumer2, ".*");
        Subscriber consumer3 = new SubscriberImpl("Consumer-3");
        broker.subscribe(consumer3, ".*");
        Subscriber consumer4 = new SubscriberImpl("Consumer-4");
        broker.subscribe(consumer4, ".*");
        Subscriber consumer5 = new SubscriberImpl("Consumer-5");
        broker.subscribe(consumer5, ".*");
        Subscriber consumer6 = new SubscriberImpl("Consumer-6");
        broker.subscribe(consumer6, ".*");
        Subscriber consumer7 = new SubscriberImpl("Consumer-7");
        broker.subscribe(consumer7, ".*");

        broker.addDependency(consumer3, consumer1);
        broker.addDependency(consumer5, consumer1);
        broker.addDependency(consumer2, consumer5);
        broker.addDependency(consumer4, consumer6);
        broker.addDependency(consumer7, consumer2);
        // broker.addDependency(consumer5, consumer7);
        broker.buildDependencyOrder();
        System.out.println("Done");
    }

    private void testBoth() throws InterruptedException {
        PacketBroker broker = new PacketBroker(5);
        Publisher producer1 = new PublisherImpl("Producer-1", broker);
        Map<String, String> messageObj1 = new HashMap<>();
        messageObj1.put("name", producer1.getName());
        Publisher producer2 = new PublisherImpl("Producer-2", broker);
        Map<String, String> messageObj2 = new HashMap<>();
        messageObj2.put("name", producer2.getName());

        // Consumers
        Subscriber consumer1 = new SubscriberImpl("Consumer-1");
        broker.subscribe(consumer1, ".*");
        Subscriber consumer2 = new SubscriberImpl("Consumer-2");
        broker.subscribe(consumer2, ".*");
        Subscriber consumer3 = new SubscriberImpl("Consumer-3");
        broker.subscribe(consumer3, "^.*Message-.*[13579]\".*$");
        Subscriber consumer4 = new SubscriberImpl("Consumer-4", json -> System.out.println("4th Consumer: " + json));
        broker.subscribe(consumer4, ".*");
        Subscriber consumer5 = new SubscriberImpl("Consumer-5");
        broker.subscribe(consumer5, "^.*-2.*$");
        Subscriber consumer6 = new SubscriberImpl("Consumer-6");
        broker.subscribe(consumer6, ".*");
        Subscriber consumer7 = new SubscriberImpl("Consumer-7");
        broker.subscribe(consumer7, ".*");

        // Add Dependency
        broker.addDependency(consumer3, consumer1);
        broker.addDependency(consumer5, consumer1);
        broker.addDependency(consumer2, consumer5);
        broker.addDependency(consumer4, consumer6);
        broker.addDependency(consumer7, consumer2);
        broker.buildDependencyOrder();
        broker.start();

        // Producers
        ForkJoinPool pool = ForkJoinPool.commonPool();
        pool.execute(() -> {
            for (int i=0; i<20; i++) {
                messageObj1.put("message", "Message-" + i);
                producer1.publish(gson.toJson(messageObj1));
            }
        });
        pool.execute(() -> {
            for (int i=0; i<20; i++) {
                messageObj2.put("message", "Message-" + i);
                producer2.publish(gson.toJson(messageObj2));
            }
        });

        Thread.sleep(2000);
        broker.close();
    }
}
