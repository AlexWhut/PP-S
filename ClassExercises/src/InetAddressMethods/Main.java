package InetAddressMethods;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Main {
	public static void main(String[] args) {
		try {
			// Get address of google.es using getByAddress
			byte[] googleIp = {(byte)142, (byte)250, (byte)184, (byte)131}; // Example IP for google.es
			InetAddress google = InetAddress.getByAddress(googleIp);
			System.out.println("google.es (getByAddress):");
			System.out.println("  Host Address: " + google.getHostAddress());
			System.out.println("  Host Name:    " + google.getHostName());

			// Get address of bing.com using getByName
			InetAddress bing = InetAddress.getByName("bing.com");
			System.out.println("bing.com (getByName):");
			System.out.println("  Host Address: " + bing.getHostAddress());
			System.out.println("  Host Name:    " + bing.getHostName());

			// Get local host address
			InetAddress local = InetAddress.getLocalHost();
			System.out.println("Local Host:");
			System.out.println("  IP Address:   " + local.getHostAddress());
		} catch (UnknownHostException e) {
			System.err.println("Error: " + e.getMessage());
		}
	}
}
