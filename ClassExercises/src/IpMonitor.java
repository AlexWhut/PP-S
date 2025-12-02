import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

public class IpMonitor implements Runnable {
    private String lastIp = null;
    private String lastLocalIp = null;

    @Override
    public void run() {
        try {
            String ip = fetchPublicIp();
            String localIp = fetchLocalIp();
            if (ip != null) {
                if (lastIp == null) {
                    lastIp = ip;
                    lastLocalIp = localIp;
                    System.out.println("Initial IP: " + ip);
                    if (localIp != null) System.out.println("Local IP: " + localIp);
                } else if (!ip.equals(lastIp)) {
                    System.out.println("IP changed from " + lastIp + " to " + ip);
                    final String prev = lastIp;
                    final String now = ip;
                    final String localNow = localIp;
                    javax.swing.SwingUtilities.invokeLater(() -> {
                        StringBuilder msg = new StringBuilder();
                        msg.append("IP changed from ").append(prev).append(" to ").append(now);
                        if (localNow != null) msg.append("\nLocal IP: ").append(localNow);
                        JOptionPane.showMessageDialog(null, msg.toString(), "IP Change Detected",
                                JOptionPane.INFORMATION_MESSAGE);
                    });
                    lastIp = ip;
                    lastLocalIp = localIp;
                } else {
                    System.out.println("IP unchanged: " + ip + " (checked at " + java.time.LocalDateTime.now() + ")");
                    if (localIp != null && !localIp.equals(lastLocalIp)) {
                        System.out.println("Local IP changed from " + lastLocalIp + " to " + localIp);
                        final String localPrev = lastLocalIp;
                        final String localNow2 = localIp;
                        javax.swing.SwingUtilities.invokeLater(() -> {
                            JOptionPane.showMessageDialog(null, "Local IP changed from " + localPrev + " to " + localNow2,
                                    "Local IP Change Detected", JOptionPane.INFORMATION_MESSAGE);
                        });
                        lastLocalIp = localIp;
                    }
                }
            } else {
                System.out.println("Could not determine IP (network error).");
            }
        } catch (Exception e) {
            System.err.println("Error checking IP: " + e.getMessage());
        }
    }

    private String fetchPublicIp() {
        BufferedReader in = null;
        try {
            URL url = new URL("https://api.ipify.org");
            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            return in.readLine();
        } catch (IOException e) {
            return null;
        } finally {
            if (in != null) try { in.close(); } catch (IOException ignored) {}
        }
    }

    private String fetchLocalIp() {
        BufferedReader in = null;
        try {
            ProcessBuilder pb = new ProcessBuilder("ipconfig");
            pb.redirectErrorStream(true);
            Process p = pb.start();
            in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            Pattern ipv4 = Pattern.compile("(\\d{1,3}(?:\\.\\d{1,3}){3})");
            while ((line = in.readLine()) != null) {
                Matcher m = ipv4.matcher(line);
                if (m.find()) {
                    String candidate = m.group(1);
                    if (!candidate.startsWith("127.") && !candidate.startsWith("169.254.")) {
                        return candidate;
                    }
                }
            }
            p.waitFor();
        } catch (Exception e) {
            return null;
        } finally {
            if (in != null) try { in.close(); } catch (IOException ignored) {}
        }
        return null;
    }

    public static void main(String[] args) {
        int intervalSeconds = 60;
        if (args.length > 0) {
            try {
                intervalSeconds = Integer.parseInt(args[0]);
            } catch (NumberFormatException ignored) {}
        }

        IpMonitor monitor = new IpMonitor();
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(monitor, 0, intervalSeconds, TimeUnit.SECONDS);

        System.out.println("IpMonitor scheduled every " + intervalSeconds + " seconds.");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down scheduler...");
            scheduler.shutdownNow();
        }));
    }
}
