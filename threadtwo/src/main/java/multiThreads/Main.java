package multiThreads;

import java.util.concurrent.atomic.AtomicLong;

public class Main {
    public static void main(String[] args) {
        final long LIMITE = 100_000_000L; // 100 millones
        final int TOTAL_HILOS = 7;
        final int HILOS_THREAD = 4;  // hilos heredando Thread
        final int HILOS_RUNNABLE = 3; // hilos con Runnable
        
        System.out.println("=== CONTADOR DE PARES CON HILOS MIXTOS ===");
        System.out.println("Total hilos: " + TOTAL_HILOS + " (" + HILOS_THREAD + " Thread + " + HILOS_RUNNABLE + " Runnable)");
        System.out.println("Contando pares del 1 al " + LIMITE);
        System.out.println();
        
        // Contador compartido entre todos los hilos
        AtomicLong contadorPares = new AtomicLong(0);
        
        // Arrays para almacenar los diferentes tipos de hilos
        ContadorParesThread[] hilosThread = new ContadorParesThread[HILOS_THREAD];
        Thread[] hilosRunnable = new Thread[HILOS_RUNNABLE];
        
        // Calcular el rango de cada hilo
        long rangoHilo = LIMITE / TOTAL_HILOS;
        
        // Medir tiempo de inicio
        long inicio = System.nanoTime();
        
        // CREAR 10 HILOS CON HERENCIA DE THREAD
        for (int i = 0; i < HILOS_THREAD; i++) {
            long inicioRango = (i * rangoHilo) + 1;
            long finRango = (i + 1) * rangoHilo;
            
            hilosThread[i] = new ContadorParesThread(inicioRango, finRango, contadorPares, i + 1);
            hilosThread[i].start();
        }
        
        // CREAR 10 HILOS CON RUNNABLE
        for (int i = 0; i < HILOS_RUNNABLE; i++) {
            long inicioRango = ((i + HILOS_THREAD) * rangoHilo) + 1;
            long finRango = (i == HILOS_RUNNABLE - 1) ? LIMITE : ((i + HILOS_THREAD + 1) * rangoHilo);
            
            ContadorParesRunnable runnable = new ContadorParesRunnable(inicioRango, finRango, contadorPares, i + HILOS_THREAD + 1);
            hilosRunnable[i] = new Thread(runnable);
            hilosRunnable[i].start();
        }
        
        // Esperar a que todos los hilos Thread terminen
        try {
            for (ContadorParesThread hilo : hilosThread) {
                hilo.join();
            }
            // Esperar a que todos los hilos Runnable terminen
            for (Thread hilo : hilosRunnable) {
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
        System.out.println("=== RESULTADOS FINALES ===");
        System.out.println("Hilos Thread (herencia): " + HILOS_THREAD);
        System.out.println("Hilos Runnable (interfaz): " + HILOS_RUNNABLE);
        System.out.println("Total de pares encontrados: " + contadorPares.get());
        System.out.println("Tiempo paralelo: " + duracion + " ms (" + (duracion / 1000.0) + " segundos)");
        
        // Verificacion matematica
        long paresEsperados = LIMITE / 2;
        System.out.println("Pares esperados (verificación): " + paresEsperados);
        System.out.println("Resultado correcto: " + (contadorPares.get() == paresEsperados ? "SI" : "NO"));
        
        // Comparacion con version secuencial
        System.out.println();
        System.out.println("=== COMPARACIÓN CON VERSIÓN SECUENCIAL ===");
        compararConSecuencial(LIMITE, duracion);
    }
    
    private static void compararConSecuencial(long limite, long tiempoParalelo) {
        System.out.println("Ejecutando versión secuencial para comparar...");
        
        long tiempoInicio = System.nanoTime();
        long contador = 0;
        
        for (long i = 1; i <= limite; i++) {
            if (i % 2 == 0) {
                contador++;
            }
        }
        
        long tiempoFin = System.nanoTime();
        long tiempoSecuencial = (tiempoFin - tiempoInicio) / 1_000_000; // convertir a ms
        
        System.out.println("Versión secuencial - Tiempo: " + tiempoSecuencial + " ms (" + (tiempoSecuencial / 1000.0) + " segundos)");
        System.out.println("Versión secuencial - Pares: " + contador);
        System.out.println();
        
        // Calcular mejora de rendimiento
        if (tiempoParalelo < tiempoSecuencial) {
            double mejora = (double) tiempoSecuencial / tiempoParalelo;
            System.out.println("HILOS MÁS RÁPIDOS: " + String.format("%.2fx", mejora) + " veces más rápido");
        } else if (tiempoSecuencial < tiempoParalelo) {
            double perdida = (double) tiempoParalelo / tiempoSecuencial;
            System.out.println("SECUENCIAL MÁS RÁPIDO: " + String.format("%.2fx", perdida) + " veces");
            System.out.println("   Razón: Overhead de hilos > beneficio del paralelismo");
        } else {
            System.out.println("RENDIMIENTO SIMILAR");
        }
        
        System.out.println();
        System.out.println("DIFERENCIAS ENTRE THREAD Y RUNNABLE");
        System.out.println("Thread (herencia): Más directo, pero menos flexible");
        System.out.println("Runnable (interfaz): Más flexible, permite herencia múltiple");
        System.out.println("Rendimiento: Ambos tienen el mismo rendimiento en ejecución");
    }
}
