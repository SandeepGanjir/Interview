package lld.messaging.interfaces;

import lld.messaging.model.Message;

public interface MessageHandler {
    void processMessage(Message message);
}
