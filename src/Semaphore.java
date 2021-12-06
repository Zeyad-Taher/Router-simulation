public class Semaphore {
    protected int value = 0;
    protected Semaphore()
    {
        value = 0;
    }
    protected Semaphore(int initial)
    {
        value = initial;
    }
    public synchronized void P(Device device) // wait
    {
        value--;
        if (value < 0) {
            try
            {
                System.out.println(device + " arrived and waiting");
                wait();
            }
            catch (InterruptedException e) {}
        }
        else {
            System.out.println(device + " arrived");
        }
    }
    public synchronized void V(Device device) // Signal
    {
        System.out.println(device.getName() + ": Logged out");
        value++ ;
        if (value <= 0) {
            notify();
        }
    }
}
