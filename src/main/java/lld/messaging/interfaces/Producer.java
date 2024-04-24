package lld.messaging.interfaces;

public interface Producer {
    void addMessageQueue(MessageQueue messageQueue);
    boolean publish(String topic, String message);
}
