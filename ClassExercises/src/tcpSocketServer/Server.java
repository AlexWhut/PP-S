package tcpSocketServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private ServerSocket serverSocket;
	private Socket socket;
	private InputStream is;
	private OutputStream os;
	
	private InputStreamReader isr;
	private BufferedReader br;
	private PrintWriter pw;
	
	private tcpsocketserver (int puerto) throws IOException {
		serverSocket = new ServerSocket(puerto);
	}
	public void start() throws IOException {
		System.out.println("(Servidor) Esperando conexiones...");
		socket = serverSocket.accept();
		os = socket.getOutputStream();
		is = socket.getInputStream();
		System.out.println("(Servidor) Conexión establecida");
		
	}
	public void stop() throws IOException {
		System.out.println("(Servidor) Cerrando conexiones...");
		is.close();
		os.close();
		socket.close();
		serverSocket.close();
		System.out.println("(Servidor) Conexiones cerradas. ");
	}
	public void abrirCanalesdeTexto() {

	}
	public void cerrarCanalesdeTexto() throws IOException {
		System.out.println("(Servidor) Cerrando canales de texto...");
		br.close();
		isr.close();
		pw.close();
		System.out.println("(Servidor) Canales de texto cerrados.");
		
	}
	public String leerMensajeTexto() throws IOException {
		System.out.println("(Servidor) Leyendo mensaje... ");
		String mensaje = br.readLine();
		System.out.println("(Servidor) Mensaje leído.");
		return mensaje;
	}
	public void enviarMensajeTexto(String mensaje) {
		System.out.println("(Servidor) Enviando mensaje.");
		pw.println(mensaje);
		System.out.println("(Servidor) Mensaje enviado");
		
	}
	public static void main (String[] args) {
		try {
			
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
