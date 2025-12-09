package BookActivity2Five;

import java.util.Random;

public class Worker implements Runnable {
	private final String name;
	private final Random rnd = new Random();

	public Worker(String name) {
		this.name = name;
	}

	@Override
	public void run() {
		while (!Thread.currentThread().isInterrupted()) {
			int seconds = rnd.nextInt(10) + 1; // 1..10 seconds
			try {
				Thread.sleep(seconds * 1000L);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				break;
			}
			System.out.println("[" + name + "] I am working");
		}
		System.out.println("[" + name + "] stopped");
	}
}
