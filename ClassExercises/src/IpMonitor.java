import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import javax.swing.JOptionPane;

public class IpMonitor implements Runnable {
    private String lastIp = null;

    @Override
    public void run() {
        try {
            String ip = fetchPublicIp();
            if (ip != null) {
                if (lastIp == null) {
                    lastIp = ip;
                    System.out.println("Initial IP: " + ip);
                } else if (!ip.equals(lastIp)) {
                    System.out.println("IP changed from " + lastIp + " to " + ip);
                    final String prev = lastIp;
                    final String now = ip;
                    javax.swing.SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(null, "IP changed from " + prev + " to " + now,
                                "IP Change Detected", JOptionPane.INFORMATION_MESSAGE);
                    });
                    lastIp = ip;
                } else {
                    System.out.println("IP unchanged: " + ip + " (checked at " + java.time.LocalDateTime.now() + ")");
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
}
