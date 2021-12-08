import java.io.FileWriter;
import java.io.IOException;

import static java.lang.Thread.sleep;

public class Device implements Runnable {
    private String name;
    private String type;
    private Router router;
    private Semaphore sem;
    int connectionNum;

    public Device(String name,String type,Router router) throws IOException{
        this.name=name;
        this.type=type;
        this.router=router;
        sem=router.getSemaphore();
    }

    @Override
    public void run() {
        try {
            sem.P(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            performOnlineActivity();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sem.V(this);
    }

    public void connect() throws IOException {
        router.occupy(this);
        connectionNum=router.getConnections().lastIndexOf(this)+1;
        System.out.println("Connection "+connectionNum+": "+name + " login");
        sem.appendStrToFile("Connection "+connectionNum+": "+name + " login");


    }
    public void performOnlineActivity() throws IOException {
        System.out.println("Connection "+connectionNum+": "+name+" performs online activity");
        sem.appendStrToFile("Connection "+connectionNum+": "+name+" performs online activity");



    }
    public void disconnect() throws IOException {
        router.release(this);
        System.out.println("Connection "+connectionNum+": "+name + " Logged out");
        sem.appendStrToFile("Connection "+connectionNum+": "+name + " Logged out");


    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public void setConnectionNum(int n){
        connectionNum=n;
    }

    @Override
    public String toString() {
        return "(" + name + ") (" + type + ")";
    }
}