package org.example;
import java.net.*;
import java.io.*;

public class ProxyExample {
    public static void main(String[] args) {
        try {
            // Set the proxy host and port
            //String proxyHost = "127.0.0.1";
            String proxyHost = "::1";
            int proxyPort = 8888;

            // Create a Proxy object
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));

            // Create a URL object
            URL url = new URL("https://reqres.in/api/users");

            // Open a connection using the proxy
            HttpURLConnection connection = (HttpURLConnection) url.openConnection(proxy);
            //HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // Set request method (e.g., GET)
            connection.setRequestMethod("GET");
            connection.setRequestProperty("HOST","reqres.in:443");
            connection.setRequestProperty("Accept", "application/json");
            // Read the response
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();

            // Close the connection
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}