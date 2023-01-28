package com.mycompany;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.UnknownHostException;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class ServerSocketExample {

	public static void main(String[] args)  {
		// TODO Auto-generated method stub
		String host = "0.0.0.0";
		Integer port = 10025;
		SSLSocketFactory sslsocketfactory =  (SSLSocketFactory)SSLSocketFactory.getDefault();
		SSLSocket sslsocket;
		try {
			sslsocket = (SSLSocket) sslsocketfactory
			  .createSocket(host, port);
			InputStream in = sslsocket.getInputStream();
			OutputStream out = sslsocket.getOutputStream();

			out.write(1);
			while (in.available() > 0) {
			    System.out.print(in.read());
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	

		System.out.println("Secured connection performed successfully");


	}

}
