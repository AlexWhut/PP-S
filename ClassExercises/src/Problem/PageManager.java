package Problem;
import java.util.ArrayList;
import java.util.List;

public class PageManager extends Thread{

	private static final List<String> mylist = new ArrayList<String>();

	@Override
	public void run() {
		while (true) {
			// Sincronizamos todas las operaciones que mutan o iteran la lista
			synchronized (mylist) {
				if (mylist.size() >= 10) {
					mylist.remove(0);
				} else if (mylist.size() < 10) {
					mylist.add(new String("texto"));
				}

				// Si necesitamos procesar los elementos, es preferible copiar a un
				// arreglo para minimizar el tiempo dentro del bloqueo. Aquí el
				// bucle está vacío en el original, así que lo mantengo idéntico.
				for (String string : mylist) {
					// procesamiento opcional
				}
			}

			// Evitar busy-wait; dejar que el scheduler del SO reparta CPU
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				break;
			}
		}
	}

	public static void main(String[] args) {
		synchronized (mylist) {
			for (int i = 0; i < 10; i++) {
				mylist.add(new String("texto"));
			}
		}

		for (int i = 0; i < 100; i++) {
			new PageManager().start();
		}
	}

}
