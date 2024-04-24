package lld.messaging.impl;

import lld.messaging.interfaces.Consumer;
import lld.messaging.interfaces.MessageBroker;
import lld.messaging.interfaces.MessageQueue;
import lld.messaging.interfaces.Producer;
import lld.messaging.model.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageBrokerImpl implements MessageBroker {
    private final MessageQueue messageQueue;
    private final List<Producer> producers;
    private List<Consumer> consumers;

    public MessageBrokerImpl(final int threshold) {
        producers = new ArrayList<>();
        consumers = new ArrayList<>();
        messageQueue = new MessageQueueImpl(this::processMessage, threshold);
    }

    public MessageQueue addProducer(Producer producer) {
        producers.add(producer);
        return messageQueue;
    }

    public synchronized void registerConsumer(Consumer consumer) {
        consumers.add(consumer);
        consumers = buildConsumersDAG();
    }

    public synchronized void unregisterConsumer(Consumer consumer) {
        consumers.remove(consumer);
        consumers = buildConsumersDAG();
    }

    private synchronized List<Consumer> buildConsumersDAG() {
        List<Consumer> dag = new ArrayList<>();
        while (dag.size() != consumers.size()) {
            int currentSize = dag.size();
            for (Consumer c : consumers) {
                if (dag.contains(c)) {
                    continue;
                }
                boolean noRemainingDependants = true;
                for (Consumer dep : c.getDependencies()) {
                    if (!dag.contains(dep) && consumers.contains(dep)) {
                        noRemainingDependants = false;
                        break;
                    }
                }
                if (noRemainingDependants) {
                    dag.add(c);
                }
            }
            if (currentSize >= dag.size()) {
                throw new RuntimeException("Cyclic Dependency among Consumers");
            }
        }
        return dag;
    }

    private synchronized void processMessage(Message message) {
        for (Consumer consumer : consumers) {
            if (consumer.topicRegistered(message.getTopic())) {
                consumer.consume(message.getJsonMessage());
            }
        }
    }
}
