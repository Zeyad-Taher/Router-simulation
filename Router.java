import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Router {

    private ArrayList<Device> connections;
    private Semaphore semaphore;
    private int N;





    public Router(int N) throws IOException {
        this.N=N;
        semaphore=new Semaphore(N);
        connections = new ArrayList<>();
    }

    public void occupy(Device device) throws IOException
    {
        if(connections.size()<N){
            connections.add(device);
        }
        int connectionNum=connections.lastIndexOf(device)+1;
        device.setConnectionNum(connectionNum);
        System.out.println("Connection "+connectionNum+": "+device.getName()+" Occupied");
        semaphore.appendStrToFile("Connection "+connectionNum+": "+device.getName()+" Occupied");

    }
    public void release(Device device)
    {
        connections.remove(device);
    }
    public Semaphore getSemaphore()
    {
        return semaphore;
    }
    public ArrayList<Device> getConnections(){
        return connections;
    }
}