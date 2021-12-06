import static java.lang.Thread.sleep;

public class Device implements Runnable {
    private String name;
    private String type;
    private Router router;

    public Device(String name,String type,Router router){
        this.name=name;
        this.type=type;
        this.router=router;
    }

    @Override
    public void run() {
        try {
            connect();
            System.out.println(name + " logged in1");
            sleep(100);
            performOnlineActivity();
            sleep(100);
            disconnect();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void connect()
    {
        router.occupy(this);
    }
    public void performOnlineActivity()
    {
        System.out.println(this.name+" performs online activity");
    }
    public void disconnect()
    {
        router.release(this);
        System.out.println(name + " Logged out1");
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return name + " (" + type + ")";
    }
}
