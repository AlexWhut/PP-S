import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        try {
            String path;
            if (args.length > 0) {
                path = args.length == 1 ? args[0] : String.join(" ", args);
            } else {
                Scanner sc = new Scanner(System.in);
                System.out.println("Introduce la ruta del archivo .txt (o pulsa Enter para abrir Notepad vacío):");
                path = sc.nextLine().trim();
                sc.close();
            }

            if (path == null || path.isEmpty()) {
                System.out.println("Abriendo Notepad sin archivo...");
                Process p = launchNotepad();
                System.out.println("Bloc de notas iniciado (pid aprox.): " + p.pid());
            } else {
                File f = new File(path);
                System.out.println((f.exists() ? "Abriendo" : "Creando/Abriendo") + " archivo: " + path);
                Process p = launchNotepadWithFile(path);
                System.out.println("Bloc de notas iniciado (pid aprox.): " + p.pid());
            }
        } catch (IOException e) {
            System.err.println("Error al lanzar Notepad: " + e.getMessage());
            e.printStackTrace();
        }
        //C:\Users\DAM2_Diurno\Desktop\testtest.txt
    }

    public static Process launchNotepad() throws IOException {
        String[] cmd = new String[] { "notepad.exe" };
        return Runtime.getRuntime().exec(cmd);
    }

    public static Process launchNotepadWithFile(String path) throws IOException {
        String[] cmd = new String[] { "notepad.exe", path };
        return Runtime.getRuntime().exec(cmd);
    }
}
