package ExchargerClass;

public class Interexchanger {
    public static void main(String[] args) {
        ClassA a = new ClassA();
        ClassB b = new ClassB();

        System.out.println("Fetching IP from command line (ClassA)...");
        String ip = a.fetchIpFromCmd();
        System.out.println("IP found: " + ip);

        System.out.println("Fetching MAC from command line (ClassB)...");
        String mac = b.fetchMacFromCmd();
        System.out.println("MAC found: " + mac);

        // Exchange values
        System.out.println("Exchanging values: sending IP -> ClassB and MAC -> ClassA");
        b.receiveIp(ip);
        a.receiveMac(mac);

        System.out.println("After exchange:");
        System.out.println("ClassA IP: " + a.getIp());
        System.out.println("ClassA received MAC: " + a.getReceivedMac());
        System.out.println("ClassB MAC: " + b.getMac());
        System.out.println("ClassB received IP: " + b.getReceivedIp());
    }
}