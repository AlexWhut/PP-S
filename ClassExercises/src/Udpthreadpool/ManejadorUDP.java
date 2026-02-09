package Udpthreadpool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ManejadorUDP implements Runnable {
    private static final Logger logger = LogManager.getLogger(ManejadorUDP.class);

    private DatagramSocket socket;
    private DatagramPacket paquete;

    public ManejadorUDP(DatagramSocket socket, DatagramPacket paquete) {
        this.socket = socket;
        this.paquete = paquete;
    }

    @Override
    public void run() {
        try {
            String mensaje = new String(
                    paquete.getData(),
                    0,
                    paquete.getLength()
            );

            logger.info("Hilo {} procesando: {}", Thread.currentThread().getName(), mensaje);

            // Simular procesamiento
            Thread.sleep(1000);
            logger.debug("Procesamiento simulado por 1 segundo");

            String respuesta = "OK -> " + mensaje;
            byte[] datosRespuesta = respuesta.getBytes();

            DatagramPacket paqueteRespuesta = new DatagramPacket(
                    datosRespuesta,
                    datosRespuesta.length,
                    paquete.getAddress(),
                    paquete.getPort()
            );

            socket.send(paqueteRespuesta);
            logger.info("Respuesta enviada a {}:{}", paquete.getAddress(), paquete.getPort());

        } catch (Exception e) {
            logger.error("Error en el manejador UDP", e);
        }
    }
}
