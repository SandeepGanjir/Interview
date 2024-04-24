package lld.messaging.interfaces;

import lld.messaging.model.Message;

public interface MessageQueue {
    boolean publish(Message message);
}
