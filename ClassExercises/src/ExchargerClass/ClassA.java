package ExchargerClass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClassA {
    private String ip;
    private String receivedMac;

    public String fetchIpFromCmd() {
        List<String> cmd = new ArrayList<>();
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            cmd.add("cmd"); cmd.add("/c"); cmd.add("ipconfig");
        } else {
            // try ip, then ifconfig
            cmd.add("/bin/sh"); cmd.add("-c"); cmd.add("ip addr || ifconfig -a");
        }
        List<String> out = executeCommand(cmd);
        Pattern ipv4 = Pattern.compile("(\\d{1,3}(?:\\.\\d{1,3}){3})");
        for (String line : out) {
            Matcher m = ipv4.matcher(line);
            while (m.find()) {
                String candidate = m.group(1);
                if (!candidate.startsWith("127.") && !candidate.startsWith("169.254.")) {
                    this.ip = candidate;
                    return candidate;
                }
            }
        }
        return null;
    }

    public void receiveMac(String mac) {
        this.receivedMac = mac;
        System.out.println("ClassA received MAC: " + mac);
    }

    public String getIp() { return ip; }
    public String getReceivedMac() { return receivedMac; }

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

