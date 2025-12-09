package CallableAndRunnable;

import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

	public static void main(String[] args) throws Exception {
		Scanner sc = new Scanner(System.in);
		System.out.print("Introduce un entero: ");
		if (!sc.hasNextInt()) {
			System.out.println("No se ha introducido un entero válido.");
			sc.close();
			return;
		}

		final int numero = sc.nextInt();
		sc.close();

		ExecutorService executor = Executors.newFixedThreadPool(2);

		// Runnable: simple announcer that runs in background
		Runnable announcer = () -> System.out.println("Comprobando el número " + numero + " en segundo plano...");

		// Callable: busca el mayor divisor propio (devuelve -1 si es primo o no tiene divisores propios)
		Callable<Integer> mayorDivisor = () -> {
			if (numero <= 1) return -1;
			for (int i = numero - 1; i > 1; i--) {
				if (numero % i == 0) {
					return i; // el mayor divisor propio según el enunciado
				}
			}
			return -1; // primo
		};

		// Enviar tareas al ExecutorService
		Future<?> announceFuture = executor.submit(announcer);
		Future<Integer> resultFuture = executor.submit(mayorDivisor);

		// Esperar resultados
		try {
			announceFuture.get(); // esperamos a que el runnable termine (no devuelve valor útil)
		} catch (Exception ignored) {
		}

		Integer mayor = resultFuture.get();

		if (mayor == null || mayor == -1) {
			System.out.println(numero + " es primo (o no tiene divisores propios mayores que 1).");
		} else {
			System.out.println(numero + " NO es primo. Mayor divisor propio: " + mayor);
		}

		executor.shutdown();
	}

}
