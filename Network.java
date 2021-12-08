import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Network {

    public static void main(String[] args) throws IOException {
        int N,TC;
        String TCLine;
        Scanner read = new Scanner(System.in);
        System.out.println("What is the number of WI-FI Connections?");
        N = read.nextInt();
        read.nextLine();
        Router router=new Router(N);
        ArrayList<Thread> devices = new ArrayList<>();
        System.out.println("What is the number of devices Clients want to connect?");
        TC = read.nextInt();
        read.nextLine();
        for (int i=0;i<TC;i++){
            TCLine = read.nextLine();
            String[] line = TCLine.split(" ");
            Thread device = new Thread(new Device(line[0],line[1],router));
            devices.add(device);
        }

        for (Thread device : devices) {
            device.start();
        }
    }
}