import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class App {
    public static void main(String[] args) throws Exception {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        IpMonitor monitor = new IpMonitor();
        // schedule at fixed rate: initial delay 0, period 2 minutes
        scheduler.scheduleAtFixedRate(monitor, 0, 2, TimeUnit.MINUTES);

        System.out.println("IP monitor started — checking every 2 minutes. Press Enter to stop.");

        // Wait for user to press Enter to exit
        System.in.read();

        System.out.println("Shutting down IP monitor...");
        scheduler.shutdown();
        scheduler.awaitTermination(10, TimeUnit.SECONDS);
    }
}
