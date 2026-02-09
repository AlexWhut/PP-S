package Udpthreadpool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient {
    private static final Logger logger = LogManager.getLogger(UDPClient.class);

    public static void main(String[] args) {
        String mensaje = "Hola servidor UDP";
        int puertoServidor = 9876;

        try (DatagramSocket socket = new DatagramSocket()) {
            logger.info("Cliente UDP iniciado");
            InetAddress direccion = InetAddress.getByName("localhost");

            byte[] buffer = mensaje.getBytes();

            DatagramPacket paquete = new DatagramPacket(
                    buffer,
                    buffer.length,
                    direccion,
                    puertoServidor
            );

            socket.send(paquete);
            logger.info("Mensaje enviado al servidor: {}", mensaje);

            // Recibir respuesta
            byte[] bufferRespuesta = new byte[1024];
            DatagramPacket respuesta = new DatagramPacket(
                    bufferRespuesta,
                    bufferRespuesta.length
            );

            socket.receive(respuesta);
            logger.info("Respuesta recibida del servidor");

            String mensajeRespuesta = new String(
                    respuesta.getData(),
                    0,
                    respuesta.getLength()
            );

            System.out.println("Respuesta del servidor: " + mensajeRespuesta);
            logger.info("Respuesta del servidor: {}", mensajeRespuesta);

        } catch (Exception e) {
            logger.error("Error en el cliente UDP", e);
        }
    }
}
