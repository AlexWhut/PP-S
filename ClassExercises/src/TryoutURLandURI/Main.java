package TryoutURLandURI;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.MalformedURLException;

public class Main {
	public static void main(String[] args) {
		// Tryout with URL
		try {
			URL url = new URL("https://www.example.com:8080/path/to/resource?query=123#fragment");
			System.out.println("URL:");
			System.out.println("  Protocol: " + url.getProtocol());
			System.out.println("  Host:     " + url.getHost());
			System.out.println("  Port:     " + url.getPort());
			System.out.println("  Path:     " + url.getPath());
			System.out.println("  Query:    " + url.getQuery());
			System.out.println("  Ref:      " + url.getRef());
		} catch (MalformedURLException e) {
			System.err.println("Malformed URL: " + e.getMessage());
		}

		// Tryout with URI
		try {
			URI uri = new URI("https://www.example.com:8080/path/to/resource?query=123#fragment");
			System.out.println("\nURI:");
			System.out.println("  Scheme:   " + uri.getScheme());
			System.out.println("  Host:     " + uri.getHost());
			System.out.println("  Port:     " + uri.getPort());
			System.out.println("  Path:     " + uri.getPath());
			System.out.println("  Query:    " + uri.getQuery());
			System.out.println("  Fragment: " + uri.getFragment());
		} catch (URISyntaxException e) {
			System.err.println("URI Syntax Error: " + e.getMessage());
		}
	}
}
