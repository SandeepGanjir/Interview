package lld.messaging.model;

public class Message {
    private final String topic;
    private final String jsonMessage;

    public Message(String topic, String jsonMessage) {
        this.topic = topic;
        this.jsonMessage = jsonMessage;
    }

    public String getTopic() {
        return topic;
    }

    public String getJsonMessage() {
        return jsonMessage;
    }
}
