import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

public class ServerSocketExample {
	public static void main(String args[]) {
		try {
			// Creaet a SSLServersocket
			SSLServerSocketFactory factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
			SSLServerSocket sslserversocket = (SSLServerSocket) factory.createServerSocket(10025);
			System.out.println("SSLSocket connection open");
			SSLSocket sslsocket = (SSLSocket) sslserversocket.accept();

			DataInputStream is = new DataInputStream(sslsocket.getInputStream());
			PrintStream os = new PrintStream(sslsocket.getOutputStream());
			while (true) {

				String input = is.readUTF();

				os.println(input);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}