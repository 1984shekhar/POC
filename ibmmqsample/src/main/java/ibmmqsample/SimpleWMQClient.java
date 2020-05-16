package ibmmqsample;

import com.ibm.msg.client.wmq.*;

import com.ibm.mq.jms.*;

import javax.jms.*;

 

public class SimpleWMQClient

  {

  public static void main (String[] args)

        throws Exception

    {

    // Set up the W-MQ QueueConnectionFactory

    MQQueueConnectionFactory qcf = new MQQueueConnectionFactory();

 

    // Host and port settings have their usual meanings

    qcf.setHostName ("0.0.0.0");

    qcf.setPort (1414);

 

    // Queue manager and channel

    qcf.setQueueManager ("QM1");

    qcf.setChannel ("DEV.APP.SVRCONN ");

    
    // Although there are many possible values of transport type,

    //  only 'client' and 'bindings' work in a Java client. Bindings

    //  is a kind of in-memory transport and only works when the client

    //  and the queue manager are on the same physical host. In most

    //  cases we need 'client'.

  //  qcf.setTransportType (WMQConstants.WMQ_CM_CLIENT);
    qcf.setTransportType(WMQConstants.WMQ_CM_BINDINGS_THEN_CLIENT);
 

    // Create and start a connection

    // Note that queue manager is set up not to authenticate, and to

    //  treat all client connections as having full rights

    QueueConnection qc = qcf.createQueueConnection ("app", "");

    qc.start();

 

    // --- Everything below this line is generic JMS code ---

 

    // Create a queue and a session

    Queue q = new MQQueue ("DEV.QUEUE.1");

    QueueSession s = qc.createQueueSession (false, Session.AUTO_ACKNOWLEDGE);

 

  // Create and send a TextMessage

    QueueSender qs = s.createSender (q);

    Message m = s.createTextMessage ("Hello, World!");

    qs.send (m);

 

    // Create a QueueReceiver and wait for one message to be delivered

    QueueReceiver qr = s.createReceiver (q);

    Message m2 = qr.receive();

 

    System.out.println ("Received message: " + m);

 

    s.close();

    qc.close();

    }

  }

 