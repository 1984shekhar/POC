package com.mycompany.test.simulators.stomp;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import org.glassfish.tyrus.client.ClientManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ClientEndpoint
public class SimpleStompOverWsClient {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
    private static CountDownLatch latch;
     
    private static final int PING_PERIOD = 1000;
    private static final int PONG_PERIOD = 1000;

    private static final String BROKER_URL_PROPERTY = "brokerURL";
    private static final String QUEUE_NAME_PROPERTY = "queueName";
    private static final String LOGIN_PROPERTY = "brokerUserLogin";
    private static final String PASSCODE_PROPERTY = "brokerUserPasscode";

    private static final String DEFAULT_LOGIN = "admin";
    private static final String DEFAULT_PASSCODE = "admin";
    private static final String DEFAULT_BROKER_URL = "ws://localhost:61614";
    private static final String DEFAULT_QUEUE_NAME = "stompws";

    private static final String SUBSCRIPTION_ID_0 = "id-0";

    private final String login;
    private final String passcode;
    private final String queueName;
    private Timer pinger;
    private Session wsSession;

    public SimpleStompOverWsClient(
            String login,
            String passcode,
            String queueName) {
        this.login = login;
        this.passcode = passcode;
        this.queueName = queueName;
        wsSession = null;
    }

    // called on websocket opened  
    @OnOpen
    public void onOpen(Session aWsSession) throws IOException {
        System.out.println("Websocket connection opened");
        wsSession = aWsSession;
        System.out.println("Opened websocket session with id " + aWsSession.getId());
        stompConnect(StompCommand.CONNECT);
    }

    // called on incoming string message
    @OnMessage
    public String onMessage(String message, Session aWsSession) throws IOException {
        if (wsSession != aWsSession) {
            throw new IllegalStateException(
                    "WsSession in onMessage callback not the same as when initiating the ws connection");
        }

        if (message.equals("\n")) {
            System.out.println("STOMP: PONG <---");
            return null;
        }

        StompFrame frame = StompFrame.parse(message);
        onMessage(frame);
        return null;
    }

    // called on incoming binary message
    @OnMessage
    public String onMessage(ByteBuffer buffer, Session aWsSession) throws IOException {
        if (wsSession != aWsSession) {
            throw new IllegalStateException(
                    "WsSession in onMessage callback not the same as when initiating the ws connection");
        }
        StompFrame frame = StompFrame.parse(StompFrame.toString(buffer));
        onMessage(frame);
        return null;
    }

    // worker for both OnMessage methods
    private void onMessage(StompFrame frame) throws IOException {
        //System.out.println("Received STOMP frame:\n"+ frame.toString()+"\n");
    	 System.out.println("Received STOMP frame \n");
        switch (frame.getCommand()) {
            case CONNECTED:
                setupHeartbeat(PING_PERIOD);
                stompSubscribe(SUBSCRIPTION_ID_0, queueName, Ack.auto);
                break;
            case DISCONNECTED: // intentional fall-through as no special handling of these frames is implemented
            case MESSAGE:
            case RECEIPT:
            case ERROR:
                break;
            default:
                System.out.println("UNKNOWN FRAME");
                break;
        }
    }

    // called on websocket close
    @OnClose
    public void onClose(Session aWsSession, CloseReason closeReason) {
        if (wsSession != aWsSession) {
            throw new IllegalStateException(
                    "WsSession in onClose callback not the same as when initiating the ws connection");
        }
        if (pinger != null) {
            pinger.cancel();
        }
        latch.countDown();
        System.out.println("Session" +aWsSession.getId()+" close because of " + closeReason);
    }

    private void connectToBroker(String aBrokerURL) throws InterruptedException, ExecutionException {
        if (wsSession != null && wsSession.isOpen()) {
            throw new IllegalStateException(
                    "A WebSocket connection is already opened for this instance: " + this);
        }

        ClientManager clientManager = ClientManager.createClient();
        Session newWsSession;
        try {
            System.out.println("Websocket connection initiated towards [" + aBrokerURL + "]");
           clientManager.asyncConnectToServer(this, new URI(aBrokerURL)).get();
        } catch (URISyntaxException | DeploymentException ex) {
            logger.error("Error setting up WebSocket connection :", ex);
            throw new IllegalStateException("Error setting up WebSocket connection : " + ex);
        }
        
    }

    //builds and sends stomp connect
    private void stompConnect(StompCommand stompCommand) throws IOException {
        if (stompCommand == StompCommand.STOMP || stompCommand == StompCommand.CONNECT) {
            StompFrame frame = new StompFrame(stompCommand);
            frame.getHeader().put("accept-version", "1.2,1.1,1.0");
            frame.getHeader().put("login", login);
            frame.getHeader().put("passcode", passcode);
            frame.getHeader().put("heart-beat", PING_PERIOD + "," + PONG_PERIOD);
            send(frame);
        } else {
            logger.error("STOMP: Illegal stomp command for connect " + stompCommand);
        }
    }

    //builds and sends stomp subscribe
    private void stompSubscribe(
            String subscribeId,
            String queueName,
            Ack ack) throws IOException {

        StompFrame frame = new StompFrame(StompCommand.SUBSCRIBE);
        frame.getHeader().put("id", subscribeId);             // Mandatory 
        frame.getHeader().put("destination", queueName);       // Mandatory 
        frame.getHeader().put("ack", ack.nameWithConversion()); // Optional
        send(frame);
        System.out.println("STOMP: subscribed to {}" + queueName);
    }
    
    private void send(StompFrame stompFrame) throws IOException {
        if (wsSession != null && wsSession.isOpen()) {
            System.out.println("send STOMP frame:\n{}" +stompFrame.toString()+"\n");
            wsSession.getBasicRemote().sendText(stompFrame.getText());
        } else {
            logger.error("Send failed, WsSession is not active");
        }
    }
    
    private void setupHeartbeat(int heartbeat) {

        if (heartbeat == 0) {
            return;
        }

        System.out.println("STOMP: PING every "+heartbeat+ " ms" );

        HeartBeatTimer heartBeatTimer = new HeartBeatTimer(wsSession);
        pinger = new Timer();
        pinger.scheduleAtFixedRate(heartBeatTimer, heartbeat, heartbeat);
    }   

    // heartbeat timer class 
    static class HeartBeatTimer extends TimerTask {

        private final Session wsSession;
        private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

        HeartBeatTimer(Session wsSession) {
            this.wsSession = wsSession;
        }

        @Override
        public void run() {
            try {
                wsSession.getBasicRemote().sendText("\n");
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(SimpleStompOverWsClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println(">>> PING");
        }

    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        latch = new CountDownLatch(1);
        System.setOut(new MyPrintStream(System.out));
        String login = System.getProperty(LOGIN_PROPERTY, DEFAULT_LOGIN);
        String passcode = System.getProperty(PASSCODE_PROPERTY, DEFAULT_PASSCODE);
        String queueName = System.getProperty(QUEUE_NAME_PROPERTY, DEFAULT_QUEUE_NAME);
        String brokerURL = System.getProperty(BROKER_URL_PROPERTY, DEFAULT_BROKER_URL);

        SimpleStompOverWsClient stompClient = new SimpleStompOverWsClient(login, passcode, queueName);
        stompClient.connectToBroker(brokerURL);

        // to keep client alive until latch is decremented                
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
    
    // The method below are unused and added as examples for future use
    
    //builds and sends stomp unsubscribe
    private void stompUnsubscribe(String aSubscribeId) throws IOException {
        StompFrame frame = new StompFrame(StompCommand.UNSUBSCRIBE);
        frame.getHeader().put("id", aSubscribeId);
        System.out.println("STOMP: sendAck "+ frame.toString());
        send(frame);
    }

    //builds and sends stomp ack
    private void stompAck(String anAckHeader)
            throws IOException {
        StompFrame frame = new StompFrame(StompCommand.ACK);
        frame.getHeader().put("id", anAckHeader);
        System.out.println("STOMP: sendAck "+ frame.toString());
        send(frame);
    }

    //builds and sends stomp nack
    private void stompNack(String anAckHeader)
            throws IOException {
        StompFrame frame = new StompFrame(StompCommand.NACK);
        frame.getHeader().put("id", anAckHeader);
        System.out.println("STOMP: sendNack "+ frame.toString());
        send(frame);
    }

    //builds and sends stomp send
    private void stompSend(
            String aQueueName,
            String aText) throws IOException {
        StompFrame frame = new StompFrame(StompCommand.SEND);
        frame.getHeader().put("destination", aQueueName);
        frame.setBody(aText);
        System.out.println("STOMP: send "+ frame.toString());
        send(frame);

    }

    //builds and sends stomp disconnect
    private void stompDisconnect() throws IOException {
        StompFrame frame = new StompFrame(StompCommand.DISCONNECT);
        pinger.cancel();
        System.out.println("STOMP: sendDisconnect "+ frame.toString());
        send(frame);
    }

}
