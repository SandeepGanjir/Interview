package lld.messaging.impl;

import lld.messaging.interfaces.Consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ConsumerImpl implements Consumer {
    private final String name;
    private final List<Pattern> topics;
    private final List<Consumer> dependencies;

    public ConsumerImpl(String name) {
        this.name = name;
        this.topics = new ArrayList<>();
        this.dependencies = new ArrayList<>();
    }

    public void registerTopic(String strPattern) {
        Pattern pattern = Pattern.compile(strPattern);
        topics.add(pattern);
    }

    public void consume(String message) {
        System.out.println("Consumer:" + name + " consumed - " + message);
    }

    public void addDependency(Consumer dependant) {
        dependencies.add(dependant);
    }

    public boolean topicRegistered(String topic) {
        for (Pattern pattern : topics) {
            if (pattern.matcher(topic).matches()) {
                return true;
            }
        }
        return false;
    }

    public List<Consumer> getDependencies() {
        return dependencies;
    }
}
