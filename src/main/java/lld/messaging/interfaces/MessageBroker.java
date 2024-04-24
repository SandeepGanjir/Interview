package lld.messaging.interfaces;

public interface MessageBroker {
    MessageQueue addProducer(Producer producer);
    void registerConsumer(Consumer consumer);
    void unregisterConsumer(Consumer consumer);
}
