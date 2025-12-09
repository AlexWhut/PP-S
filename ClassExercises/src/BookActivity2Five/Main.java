package BookActivity2Five;

import java.util.Random;

public class Main {

	public static void main(String[] args) {
		final int THREAD_COUNT = 5;
		Thread[] workers = new Thread[THREAD_COUNT];

		// Create and start worker threads
		for (int i = 0; i < THREAD_COUNT; i++) {
			String name = "Worker-" + (i + 1);
			Thread t = new Thread(new Worker(name), name);
			workers[i] = t;
			t.start();
		}

		// Add shutdown hook to interrupt workers on JVM termination
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			System.out.println("Shutdown requested: interrupting workers...");
			for (Thread t : workers) {
				if (t != null) t.interrupt();
			}
			for (Thread t : workers) {
				if (t != null) {
					try {
						t.join(2000);
					} catch (InterruptedException ignored) {
					}
				}
			}
			System.out.println("Workers interrupted. Exiting shutdown hook.");
		}));

		// Keep main thread alive indefinitely so worker threads continue running
		try {
			Thread.currentThread().join();
		} catch (InterruptedException e) {
			// If main is interrupted, exit
		}
	}

	
}

