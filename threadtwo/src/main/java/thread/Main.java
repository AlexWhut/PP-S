package thread;

class MiPrimerHilo extends Thread {
    @Override
    public void run() {
        System.out.println("First thread from run method!");
    }
}

public class Main {
    public static void main(String[] args) {
        
        MiPrimerHilo hilo = new MiPrimerHilo();
        hilo.start(); // inicia el hilo y ejecuta run()
    }
}