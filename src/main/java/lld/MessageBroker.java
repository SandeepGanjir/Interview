package lld;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

interface Producer {
    void publish(String topic, String message);
}

interface Consumer {
    void subscribe(String topic);
    void consume(String message);

    void close();
}

class Message {
    private final String key;
    private final String message;
    private final LocalDateTime timestamp;
    Message(String key, String message) {
        this.key = key;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
    Message(String message) {
        this(null, message);
    }
    public String getMessage() {
        return message;
    }
}

class Node {
    Node next;
    Message message;
    Node(Message message) {
        this.next = null;
        this.message = message;
    }
}

class MessageQueue {
    Node head, tail;
    MessageQueue() {
        this.head = this.tail = null;
    }
    synchronized void add(Message message) {
        Node next = new Node(message);
        if (this.head == null) {
            this.head = next;
        } else {
            this.tail.next = next;
        }
        this.tail = next;
    }
}

class Broker {
    private final Map<String, MessageQueue> queueByTopic;
    private final Map<Consumer, Map<String, Node>> consumerState;
    private Broker() {
        this.queueByTopic = new ConcurrentHashMap<>();
        this.consumerState = new ConcurrentHashMap<>();
    }

    private static class Singleton {
        private static final Broker instance = new Broker();
    }

    public static Broker getInstance() {
        return Singleton.instance;
    }

    public void publish(String topic, Message message) {
        this.queueByTopic.computeIfAbsent(topic, t->new MessageQueue()).add(message);
    }

    public void subscribe(Consumer consumer, String topic) {
        Map<String, Node> offsetByTopic = this.consumerState.computeIfAbsent(consumer, c -> new ConcurrentHashMap<>());
        if (!offsetByTopic.containsKey(topic)) {
            Node node = new Node(null);
            offsetByTopic.put(topic, node);
        }
    }

    public void poll(Consumer consumer) {
        Map<String, Node> offsetByTopic = this.consumerState.get(consumer);
        for (String topic: offsetByTopic.keySet()) {
            Node visited = offsetByTopic.get(topic);
            if (visited.message == null && this.queueByTopic.get(topic).head != null) {
                visited = this.queueByTopic.get(topic).head;
                consumer.consume(visited.message.getMessage());
            }
            if (visited.message != null) {
                while (visited.next != null) {
                    visited = visited.next;
                    consumer.consume(visited.message.getMessage());
                }
            }
            offsetByTopic.put(topic, visited);
        }
    }
}

class ProducerImpl implements Producer {
    private final String name;

    public ProducerImpl(String name) {
        this.name = name;
    }

    @Override
    public void publish(String topic, String message) {
        Message msg = new Message(name, message);
        Broker.getInstance().publish(topic, msg);
    }
}

class ConsumerImpl implements Consumer {
    private static final String FORMAT = "%s received '%s'";
    private final String name;
    private final ScheduledExecutorService executor;

    public ConsumerImpl(String name) {
        this.name = name;
        this.executor = Executors.newScheduledThreadPool(1);
        this.executor.scheduleWithFixedDelay(this::processMessages, 10, 100, TimeUnit.MILLISECONDS);
    }

    private void processMessages() {
        Broker.getInstance().poll(this);
    }

    public void consume(String message) {
        try {
            Thread.sleep(new Random().nextInt(500));
        } catch (Exception e) {}
        System.out.println(String.format(FORMAT, name, message));
    }

    @Override
    public void subscribe(String topic) {
        Broker.getInstance().subscribe(this, topic);
    }

    public void close() {
        this.executor.shutdown();
        processMessages();
    }
}

public class MessageBroker {
    public static void main(String[] args) throws InterruptedException {
        MessageBroker ins = new MessageBroker();
        ins.test();
    }

    private void test() throws InterruptedException {
        Producer producer1 = new ProducerImpl("producer1");
        Producer producer2 = new ProducerImpl("producer2");

        Consumer consumer1 = new ConsumerImpl("consumer1");
        Consumer consumer2 = new ConsumerImpl("consumer2");
        Consumer consumer3 = new ConsumerImpl("consumer3");
        Consumer consumer4 = new ConsumerImpl("consumer4");
        Consumer consumer5 = new ConsumerImpl("consumer5");

        consumer1.subscribe("topic1");
        consumer2.subscribe("topic1");
        consumer3.subscribe("topic1");
        consumer4.subscribe("topic1");
        consumer5.subscribe("topic1");

        consumer1.subscribe("topic2");
        consumer3.subscribe("topic2");
        consumer4.subscribe("topic2");

        producer1.publish("topic1", "Message 1");
        producer1.publish("topic2", "Message 2");
        producer2.publish("topic1", "Message 3");
        producer1.publish("topic1", "Message 4");
        producer2.publish("topic2", "Message 5");
        Thread.sleep(1000);
        producer2.publish("topic1", "Message 6");
        producer2.publish("topic1", "Message 7");

        consumer1.close();
        consumer2.close();
        consumer3.close();
        consumer4.close();
        consumer5.close();
    }
}
