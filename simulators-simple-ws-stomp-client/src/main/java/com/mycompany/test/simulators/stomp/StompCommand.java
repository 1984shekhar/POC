package com.mycompany.test.simulators.stomp;

enum StompCommand {
    // client-commands
    CONNECT, STOMP, SEND, SUBSCRIBE, UNSUBSCRIBE, BEGIN, COMMIT, ABORT, ACK, NACK, DISCONNECT,
    // server-commands
    CONNECTED, MESSAGE, RECEIPT, ERROR, DISCONNECTED
}
