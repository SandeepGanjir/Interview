package lld.messaging;

import lld.messaging.impl.ConsumerImpl;
import lld.messaging.impl.MessageBrokerImpl;
import lld.messaging.impl.ProducerImpl;
import lld.messaging.interfaces.Consumer;
import lld.messaging.interfaces.MessageBroker;
import lld.messaging.interfaces.Producer;

import java.util.Arrays;
import java.util.List;

public class Driver {
    public static void main(String[] args) {
        System.out.println("Welcome");

        MessageBroker messageBroker = new MessageBrokerImpl(1000);

        Consumer c = registerAndGetConsumer("C", ".*", messageBroker, null);
        Consumer a = registerAndGetConsumer("A", "\\D*[1-5]+", messageBroker, Arrays.asList(c));
        Consumer d = registerAndGetConsumer("D", ".*", messageBroker, null);
        Consumer e = registerAndGetConsumer("E", "[a-zA-Z]+[13579]{2}", messageBroker, Arrays.asList(c));
        Consumer b = registerAndGetConsumer("B", ".*", messageBroker, Arrays.asList(a, d));

        Producer producer = new ProducerImpl();
        producer.addMessageQueue(messageBroker.addProducer(producer));

        for (int i = 0; i < 1000; i++) {
            producer.publish("Message" + i, i + "th Message");
            if (i == 100)
                messageBroker.unregisterConsumer(d);
        }
    }

    private static Consumer registerAndGetConsumer(String name, String topics, MessageBroker messageBroker, List<Consumer> dependants) {
        Consumer consumer = new ConsumerImpl(name);
        consumer.registerTopic(topics);
        if (dependants != null) dependants.forEach(d -> consumer.addDependency(d));
        messageBroker.registerConsumer(consumer);
        return consumer;
    }
}
