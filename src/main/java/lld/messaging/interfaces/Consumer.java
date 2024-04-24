package lld.messaging.interfaces;

import java.util.List;

public interface Consumer {
    void registerTopic(String pattern);
    void consume(String message);
    void addDependency(Consumer dependant);
    boolean topicRegistered(String topic);
    List<Consumer> getDependencies();
}
