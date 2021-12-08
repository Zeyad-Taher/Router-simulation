import java.io.*;

public class Semaphore {
    protected int value = 0;

    public static void appendStrToFile(String str)
    {
        try {
            BufferedWriter out = new BufferedWriter(
                    new FileWriter("output.txt", true));

            out.write(str + "\n");
            out.close();
        }

        catch (IOException e) {
            System.out.println("exception occurred" + e);
        }
    }

    protected Semaphore() throws IOException {
        value = 0;
    }
    protected Semaphore(int initial) throws IOException {
        value = initial;
    }
    public synchronized void P(Device device) throws IOException // wait
    {
        value--;
        if (value < 0) {
            try
            {
                System.out.println(device + " arrived and waiting");
                appendStrToFile(device + " arrived and waiting");
                wait();
            }
            catch (InterruptedException e) {}
        }
        else {
            System.out.println(device + " arrived");
            appendStrToFile(device + " arrived");

        }
    }
    public synchronized void V(Device device) // Signal
    {
        value++ ;
        if (value <= 0) {
            notify();
        }
    }
}