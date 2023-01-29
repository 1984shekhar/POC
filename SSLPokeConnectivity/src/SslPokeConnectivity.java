

import java.io.InputStream;
import java.io.OutputStream;
import java.time.Duration;
import java.time.Instant;

import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class SslPokeConnectivity {
  public static void main(String[] paramArrayOfString) {
    if (paramArrayOfString.length != 2) {
      System.err.println("Utility to debug Java connections to SSL servers");
      System.err.println("Usage: ");
      System.err.println("  java " + SslPokeConnectivity.class.getName() + " <host> <port>");
      System.err.println("or for more debugging:");
      System.err.println("  java -Djavax.net.debug=ssl " + SslPokeConnectivity.class.getName() + " <host> <port>");
      System.err.println();
      System.err.println("Eg. to test the SSL certificate at https://localhost, use");
      System.err.println("  java " + SslPokeConnectivity.class.getName() + " localhost 443");
      System.exit(1);
    } 
    try {
      SSLSocketFactory sSLSocketFactory = (SSLSocketFactory)SSLSocketFactory.getDefault();
      long startTime = System.nanoTime();
      SSLSocket sSLSocket = (SSLSocket)sSLSocketFactory.createSocket(paramArrayOfString[0], Integer.parseInt(paramArrayOfString[1]));
      InputStream inputStream = sSLSocket.getInputStream();
      OutputStream outputStream = sSLSocket.getOutputStream();
      outputStream.write(1);
      while (inputStream.available() > 0)
        System.out.print(inputStream.read());
      long stopTime = System.nanoTime(); 
      System.out.println(stopTime - startTime);
      System.out.println("Successfully connected");
      System.exit(0);
      
    } catch (SSLHandshakeException sSLHandshakeException) {
      if (sSLHandshakeException.getCause() != null) {
        sSLHandshakeException.getCause().printStackTrace();
      } else {
        sSLHandshakeException.printStackTrace();
      } 
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
    System.exit(1);
  }
}
