package ExchargerClass;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClassB {
    private String mac;
    private String receivedIp;

    public String fetchMacFromCmd() {
        List<String> cmd = new ArrayList<>();
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            cmd.add("cmd"); cmd.add("/c"); cmd.add("getmac");
        } else {
            cmd.add("/bin/sh"); cmd.add("-c"); cmd.add("ip link || ifconfig -a");
        }
        List<String> out = executeCommand(cmd);
        Pattern macPat = Pattern.compile("([0-9A-Fa-f]{2}([:-])){5}[0-9A-Fa-f]{2}");
        for (String line : out) {
            Matcher m = macPat.matcher(line);
            if (m.find()) {
                String candidate = m.group(0);
                this.mac = candidate;
                return candidate;
            }
        }
        return null;
    }

    public void receiveIp(String ip) {
        this.receivedIp = ip;
        System.out.println("ClassB received IP: " + ip);
    }

    public String getMac() { return mac; }
    public String getReceivedIp() { return receivedIp; }

    private List<String> executeCommand(List<String> command) {
        List<String> lines = new ArrayList<>();
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);
        Process p = null;
        try {
            p = pb.start();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                String l;
                while ((l = in.readLine()) != null) lines.add(l);
            }
            p.waitFor();
        } catch (IOException | InterruptedException e) {
            // ignore and return any collected lines
        } finally {
            if (p != null) p.destroy();
        }
        return lines;
    }
}
