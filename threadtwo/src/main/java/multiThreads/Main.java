package multiThreads;

// AtomicLong para contador global seguro entre hilos
import java.util.concurrent.atomic.AtomicLong;

class ContadorPares extends Thread {
    private long inicio;
    private long fin;
    private AtomicLong contadorGlobal;
    private int numeroHilo;
    
   
    // constructor
    public ContadorPares(long inicio, long fin, AtomicLong contadorGlobal, int numeroHilo) {
        this.inicio = inicio;
        this.fin = fin;
        this.contadorGlobal = contadorGlobal;
        this.numeroHilo = numeroHilo;
    }
    
    @Override
    public void run() {
        long contadorLocal = 0;
        System.out.println("Hilo " + numeroHilo + " iniciado - Rango: " + inicio + " a " + fin);
        
        for (long i = inicio; i <= fin; i++) {
            if (i % 2 == 0) {
                contadorLocal++;
            }
        }
        
        contadorGlobal.addAndGet(contadorLocal);
        System.out.println("Hilo " + numeroHilo + " completado - Pares encontrados: " + contadorLocal);
    }
}

public class Main {
    public static void main(String[] args) {
        final long LIMITE = 100_000_000L; // 100 millones en long
        final int NUM_HILOS = 7;
        
        System.out.println("Contador de pares con " + NUM_HILOS + " Threads");
        System.out.println("pares del 1 al " + LIMITE);
        System.out.println();
        
        // Contador compartido entre todos los hilos
        AtomicLong contadorPares = new AtomicLong(0);
        
        // Array para almacenar los hilos
        ContadorPares[] hilos = new ContadorPares[NUM_HILOS];
        
        // Calcular el rango de cada hilo
        long rangoHilo = LIMITE / NUM_HILOS;
        
        // Medir tiempo de inicio
        long inicio = System.nanoTime();
        
        // Crear e iniciar los hilos
        for (int i = 0; i < NUM_HILOS; i++) {
            long inicioRango = (i * rangoHilo) + 1;
            // Ternario para el rango final del hilo
            long finRango = (i == NUM_HILOS - 1) ? LIMITE : (i + 1) * rangoHilo;
            
            hilos[i] = new ContadorPares(inicioRango, finRango, contadorPares, i + 1);
            hilos[i].start();
        }
        
        // Esperar a que todos los hilos terminen
        try {
            for (ContadorPares hilo : hilos) {
                hilo.join();
            }
        } catch (InterruptedException e) {
            System.err.println("Error esperando hilos: " + e.getMessage());
        }
        
        // Medir tiempo final
        long fin = System.nanoTime();
        long duracion = (fin - inicio) / 1_000_000; // convertir a ms
        
        // Mostrar resultados
        System.out.println();
        System.out.println("Resultado final");
        System.out.println("Total de pares encontrados: " + contadorPares.get());
        System.out.println("Milisegundos: " + duracion + " ms (" + (duracion / 1000.0) + " segundos)");
        
        // Verificacion matematica
        long paresEsperados = LIMITE / 2;
        System.out.println("Pares esperados (verificación): " + paresEsperados);
        System.out.println("Resultado correcto: " + (contadorPares.get() == paresEsperados ? "SI" : "NO")); // 
        
        // Comparacion con version secuencial
        System.out.println();
        System.out.println("Comparacion con ver secuencial");
        compararConSecuencial(LIMITE);
    }
    
    private static void compararConSecuencial(long limite) {
        System.out.println("Ejecutando versión secuencial para comparar...");
        
        long tiempoInicio = System.currentTimeMillis();
        long contador = 0;
        
        for (long i = 1; i <= limite; i++) {
            if (i % 2 == 0) {
                contador++;
            }
        }
        
        long tiempoFin = System.currentTimeMillis();
        long tiempoSecuencial = tiempoFin - tiempoInicio;
        
        System.out.println("Version secuencial - Tiempo: " + tiempoSecuencial + " ms" + " (" + (tiempoSecuencial / 1000.0) + " segundos)");
        System.out.println("Version secuencial - Pares: " + contador);
        System.out.println("Mejora aproximada: La versión con hilos debería ser más rápida");
    }
}
