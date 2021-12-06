import java.util.Scanner;

public class Network {
    public static void main(String[] args) {
        int N,TC;
        String TCLine;
        Scanner read = new Scanner(System.in);
        System.out.println("What is the number of WI-FI Connections?");
        N = read.nextInt();
        Router router=new Router(N);
        System.out.print("What is the number of devices Clients want to connect?");
        TC = read.nextInt();
        for (int i=0;i<TC;i++){
            TCLine = read.nextLine();
            String[] line = TCLine.split(" ");
            Device d = new Device(line[0],line[1],router);
        }
    }
}
