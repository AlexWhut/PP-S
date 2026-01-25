package tcpSocketServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.UnknownHostException;

public class Cliente {
	private String serverIP;
	private int serverPort;
	private Socket socket;
	private InputStream is;
	private OutputStream os;
	//objetos específicos para el envío y recepción de strings
	private InputStreamReader isr;
	private BufferedReader br;
	private PrintWriter pw;

	public Cliente(String serverIP, int serverPort) {
		this.serverIP = serverIP;
		this.serverPort = serverPort;
	}
	
	public void start() throws UnknownHostException, IOException {
		System.out.println("(Cliente) Estableciendo conexión...");
		socket = new Socket(serverIP, serverPort);
		os = socket.getOutputStream();
		is = socket.getInputStream();
	}
	
	public void stop() throws IOException{
		System.out.println("(Cliente) Cerrando conexiones...");
		is.close();
		os.close();
		socket.close();
		System.out.println("(Cliente) Conexiones cerradas.");
	}
	public void abrirCanalesdeTexto() {
		System.out.println("(Cliente) Abriendo canales de texto...");
		// Canales de lectura
		isr = new InputStreamReader(is);
		br = new BufferedReader(isr);
		//Canales de escritura
		pw = new PrintWriter(os, true);
		System.out.println("(Cliente) Conexiones cerradas");
	}
	public void cerrarCanalesdeTexto() throws IOException {
		System.out.println("(Cliente) Cerrando canales de texto");
		// Canal lectura
		br.close();
		isr.close();
		//Canal escritura
		pw.close();
		System.out.println("(Cliente) Canales de texto cerrados.");
	}
	public String leerMensajeTexto() throws IOException {
		System.out.println("(Cliente) Leyendo mensaje...");
		String mensaje = br.readLine();
		System.out.println("(Cliente) Mensaje leído.");
		return mensaje;
	}
	public void enviarMensajeTexto(String mensaje) {
		System.out.println("(Cliente) Enviando mensajes...");
		pw.println(mensaje);
		System.out.println("(Cliente) Mensaje enviado.");
	}
	public static void main(String[] args) {
		Cliente cliente = new Cliente("localhost", 49171);
		try {
			cliente.start();
			cliente.abrirCanalesdeTexto();

			BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
			String mensaje, respuesta;
			System.out.println("(Cliente) Escribe un mensaje para el servidor (escribe 'salir' para terminar):");
			do {
				System.out.print("Tú: ");
				mensaje = teclado.readLine();
				cliente.enviarMensajeTexto(mensaje);
				if (!mensaje.equalsIgnoreCase("salir")) {
					respuesta = cliente.leerMensajeTexto();
					System.out.println("Servidor: " + respuesta);
				}
			} while (!mensaje.equalsIgnoreCase("salir"));

			cliente.cerrarCanalesdeTexto();
			cliente.stop();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

