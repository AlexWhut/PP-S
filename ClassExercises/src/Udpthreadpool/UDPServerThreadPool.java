package Udpthreadpool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UDPServerThreadPool {
    private static final Logger logger = LogManager.getLogger(UDPServerThreadPool.class);

    private static final int PUERTO = 9876;
    private static final int TAM_POOL = 5;

    public static void main(String[] args) {

        ExecutorService threadPool = Executors.newFixedThreadPool(TAM_POOL);

        try (DatagramSocket socket = new DatagramSocket(PUERTO)) {
            logger.info("Servidor UDP con ThreadPool en puerto {}", PUERTO);

            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket paquete = new DatagramPacket(buffer, buffer.length);

                // Espera mensaje
                socket.receive(paquete);
                logger.info("Paquete recibido de {}:{}", paquete.getAddress(), paquete.getPort());

                // IMPORTANTE: copiar los datos del paquete
                threadPool.execute(new ManejadorUDP(socket, copiarPaquete(paquete)));
            }

        } catch (Exception e) {
            logger.error("Error en el servidor UDP", e);
        } finally {
            threadPool.shutdown();
            logger.info("ThreadPool cerrado");
        }
    }

    private static DatagramPacket copiarPaquete(DatagramPacket paquete) {
        byte[] datos = new byte[paquete.getLength()];
        System.arraycopy(
                paquete.getData(),
                paquete.getOffset(),
                datos,
                0,
                paquete.getLength()
        );

        return new DatagramPacket(
                datos,
                datos.length,
                paquete.getAddress(),
                paquete.getPort()
        );
    }
}
