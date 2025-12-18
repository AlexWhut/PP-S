package ProcessesAndThreads;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
	public static void main(String[] args) {
		try {
			String os = System.getProperty("os.name").toLowerCase();
			String[] cmd;
			if (os.contains("win")) {
				cmd = new String[] {"cmd.exe", "/c", "ipconfig", "/all"};
			} else {
				cmd = new String[] {"/bin/sh", "-c", "ifconfig -a; hostname"};
			}

			ProcessBuilder pb = new ProcessBuilder(cmd);
			pb.redirectErrorStream(true);
			Process p = pb.start();

			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			StringBuilder output = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				output.append(line).append(System.lineSeparator());
			}

			int exit = p.waitFor();
			System.out.println("Process finished with exit code: " + exit);
			if (exit == 0) System.out.println("Finished successfully.");
			else System.out.println("Finished with errors.");

			String all = output.toString();
			String hostname = null;
			String ipv4 = null;

			Pattern ipPattern = Pattern.compile("\\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b");
			BufferedReader br = new BufferedReader(new StringReader(all));
			while ((line = br.readLine()) != null) {
				String lower = line.toLowerCase();
				if (hostname == null) {
					if (lower.contains("host name") || lower.startsWith("hostname") || lower.startsWith("host-name")) {
						int idx = line.indexOf(':');
						if (idx >= 0) hostname = line.substring(idx + 1).trim();
						else hostname = line.trim();
					}
				}

				if (ipv4 == null) {
					Matcher m = ipPattern.matcher(line);
					if (m.find()) {
						String candidate = m.group();
						if (!candidate.startsWith("127.") && !candidate.startsWith("169.254") && !candidate.equals("0.0.0.0")) {
							ipv4 = candidate;
						}
					}
				}

				if (hostname != null && ipv4 != null) break;
			}

			if (hostname == null) hostname = "unknown-host";
			if (ipv4 == null) ipv4 = "unknown-ip";

			Path resultDir = Paths.get("result");
			if (!Files.exists(resultDir)) Files.createDirectories(resultDir);
			String safeHost = hostname.replaceAll("[^a-zA-Z0-9._-]", "_");
			Path outFile = resultDir.resolve(safeHost + "_network.txt");
			try (BufferedWriter bw = Files.newBufferedWriter(outFile)) {
				bw.write("Hostname: " + hostname);
				bw.newLine();
				bw.write("IPv4: " + ipv4);
				bw.newLine();
			}

			System.out.println("Wrote network info to " + outFile.toAbsolutePath());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
