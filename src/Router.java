import java.util.ArrayList;

public class Router {

    private ArrayList<Device> connections;
    private Semaphore semaphore;
    private int N;

    public Router(int N){
        this.N=N;
        semaphore=new Semaphore(N);
        connections = new ArrayList<>();
    }

    public void occupy(Device device)
    {
        if(connections.size()<N){
            connections.add(device);
        }
        semaphore.P(device);
    }
    public void release(Device device)
    {
        connections.remove(device);
        semaphore.V(device);
    }
}
