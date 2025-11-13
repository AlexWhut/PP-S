package multiThreads;

import java.util.concurrent.atomic.AtomicLong;

// OPCIÓN 2: Implementando Runnable (para 10 hilos)
class ContadorParesRunnable implements Runnable {
    private long inicio;
    private long fin;
    private AtomicLong contadorGlobal;
    private int numeroHilo;
    
    public ContadorParesRunnable(long inicio, long fin, AtomicLong contadorGlobal, int numeroHilo) {
        this.inicio = inicio;
        this.fin = fin;
        this.contadorGlobal = contadorGlobal;
        this.numeroHilo = numeroHilo;
    }
    
    @Override
    public void run() {
        long contadorLocal = 0;
        System.out.println("RUNNABLE-Hilo " + numeroHilo + " iniciado - Rango: " + inicio + " a " + fin);
        
        for (long i = inicio; i <= fin; i++) {
            if (i % 2 == 0) {
                contadorLocal++;
            }
        }
        
        contadorGlobal.addAndGet(contadorLocal);
        System.out.println("RUNNABLE-Hilo " + numeroHilo + " completado - Pares encontrados: " + contadorLocal);
    }
}