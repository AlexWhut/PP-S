package ObtainIPAddress;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Main {
	public static void main(String[] args) {
		final String SERVER_HOST = "localhost";
		final int SERVER_PORT = 5000;
		try {
			// Get IP address of www.paraninfo.es
			InetAddress address = InetAddress.getByName("www.paraninfo.es");
			byte[] ipBytes = address.getAddress();
			System.out.println("IP address of www.paraninfo.es: " + address.getHostAddress());

			// Connect to server and send IP bytes as ints
			try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
				 DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {
				for (byte b : ipBytes) {
					out.writeInt(b & 0xFF); // send as unsigned int
				}
				System.out.println("IP address sent to server.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
