import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

class Semaphore {
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

    protected Semaphore() {
        value = 0;
    }
    protected Semaphore(int initial) {
        value = initial;
        // Empty file if not empty
        try {
            FileWriter file = new FileWriter("output.txt");
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public synchronized void P(Device device) // wait
    {
        value--;
        if (value < 0) {
            try
            {
                System.out.println(device + " arrived and waiting");
                appendStrToFile(device + " arrived and waiting");
                wait();
            }
            catch (InterruptedException ignored) {}
        }
        else {
            System.out.println(device + " arrived");
            appendStrToFile(device + " arrived");

        }
    }
    public synchronized void V() // Signal
    {
        value++ ;
        if (value <= 0) {
            notify();
        }
    }
}

class Device implements Runnable {
    private String name;
    private String type;
    private Router router;
    private Semaphore sem;
    int connectionNum;
    enum State {
        WAITING,
        LOGGED_IN,
        PERFORMS_ACTIVITY,
        LOGGING_OUT
    }
    private State currentState = State.WAITING;
    static GUI gui;

    public Device(String name,String type,Router router) {
        this.name=name;
        this.type=type;
        this.router=router;
        sem=router.getSemaphore();
    }

    @Override
    public void run() {
        sem.P(this);
        try {
            sleep(500);
            connect();
            sleep(500);
            performOnlineActivity();
            sleep(500);
            disconnect();
            sleep(500);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        sem.V();
    }

    public void connect() throws IOException {
        router.occupy(this);
        connectionNum=router.getConnections().lastIndexOf(this)+1;
        currentState = State.LOGGED_IN;
        gui.update();
        System.out.println("Connection "+connectionNum+": "+name + " login");
        sem.appendStrToFile("Connection "+connectionNum+": "+name + " login");


    }
    public void performOnlineActivity() throws IOException {
        currentState = State.PERFORMS_ACTIVITY;
        gui.update();
        System.out.println("Connection "+connectionNum+": "+name+" performs online activity");
        sem.appendStrToFile("Connection "+connectionNum+": "+name+" performs online activity");
    }
    public void disconnect() throws IOException {
        router.release(this);
        currentState = State.LOGGING_OUT;
        gui.update();
        System.out.println("Connection "+connectionNum+": "+name + " Logged out");
        sem.appendStrToFile("Connection "+connectionNum+": "+name + " Logged out");
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setConnectionNum(int n){
        connectionNum=n;
    }

    @Override
    public String toString() {
        return "(" + name + ") (" + type + ")";
    }
}

class Router {

    private ArrayList<Device> connections;
    private Semaphore semaphore;
    private int N;

    public Router(int N) {
        this.N=N;
        semaphore=new Semaphore(N);
        connections = new ArrayList<>();
    }

    public void occupy(Device device)
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

class GUI implements ActionListener {
    JFrame mainFrame;
    JPanel simulationFrame;
    JTextField numOfConnectionsField;
    JTextField numOfClientsField;
    JTextArea devicesInfoField;
    int numConnections;
    ArrayList<JButton> connectionsButtons;
    Router router;
    int numDevices;
    public GUI() {
        Thread t = Thread.currentThread();
        t.setPriority(Thread.MAX_PRIORITY);
        Device.gui = this;
        mainFrame = new JFrame();
        JLabel numOfConnectionsLabel = new JLabel("Number of WI-FI Connections");
        numOfConnectionsLabel.setSize(170, 10);
        numOfConnectionsLabel.setLocation(50, 60);
        mainFrame.add(numOfConnectionsLabel);

        numOfConnectionsField = new JTextField();
        numOfConnectionsField.setSize(60, 30);
        numOfConnectionsField.setLocation(240, 50);
        mainFrame.add(numOfConnectionsField);

        JLabel numOfClientsLabel = new JLabel("Number of devices to connect");
        numOfClientsLabel.setSize(170, 10);
        numOfClientsLabel.setLocation(50, 100);
        mainFrame.add(numOfClientsLabel);

        numOfClientsField = new JTextField();
        numOfClientsField.setSize(60, 30);
        numOfClientsField.setLocation(240, 90);
        mainFrame.add(numOfClientsField);

        JLabel devicesInfoLabel = new JLabel("Enter devices in separate lines");
        devicesInfoLabel.setSize(180, 15);
        devicesInfoLabel.setLocation(50, 190);
        mainFrame.add(devicesInfoLabel);

        devicesInfoField = new JTextArea();
        devicesInfoField.setSize(200, 100);
        devicesInfoField.setLocation(240, 140);
        mainFrame.add(devicesInfoField);

        JButton startButton = new JButton("Start");
        startButton.setSize(80, 40);
        startButton.setLocation(300, 260);
        startButton.addActionListener(this);
        mainFrame.add(startButton);

        mainFrame.setLayout(null);
        mainFrame.setSize(600, 600);
        mainFrame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (simulationFrame != null) {
            mainFrame.remove(simulationFrame);
        }
        connectionsButtons = new ArrayList<>();
        simulationFrame = new JPanel();
        simulationFrame.setSize(400, 200);
        simulationFrame.setLocation(20, 320);
        numConnections = Integer.parseInt(numOfConnectionsField.getText());
        numDevices = Integer.parseInt(numOfClientsField.getText());
        router = new Router(numConnections);
        String[] devicesLines = devicesInfoField.getText().split("\n");
        ArrayList<Thread> devices = new ArrayList<>();
        for (int i=0; i<numDevices; i++) {
            String[] line = devicesLines[i].split(" ");
            Thread device = new Thread(new Device(line[0],line[1],router));
            devices.add(device);
        }

        for (Thread device : devices) {
            device.start();
            try {
                Thread.sleep(50);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }
        for (int i=0; i<numConnections; i++) {
            JButton deviceButton = new JButton("Free");
            deviceButton.setBackground(Color.GREEN);
            connectionsButtons.add(deviceButton);
            simulationFrame.add(deviceButton);
        }

        int dimension = (int) Math.ceil(Math.sqrt(numConnections));
        simulationFrame.setLayout(new GridLayout(dimension, dimension, 10, 10));
        mainFrame.add(simulationFrame);
    }

    public void update() {
        for (int i=0; i<numConnections; i++) {
            JButton changed = connectionsButtons.get(i);
            try {
                Device toCheck = router.getConnections().get(i);
                if(toCheck.getCurrentState() == Device.State.LOGGED_IN){
                    changed.setBackground(Color.red);
                    changed.setText(toCheck + " log in");
                }
                else if(toCheck.getCurrentState() == Device.State.PERFORMS_ACTIVITY) {
                    changed.setBackground(Color.cyan);
                    changed.setText(toCheck + " performs online activity");
                }
                else if(toCheck.getCurrentState() == Device.State.LOGGING_OUT) {
                    changed.setBackground(Color.yellow);
                    changed.setText(toCheck + " logging out");
                }
            }
            catch(IndexOutOfBoundsException e) {
                changed.setBackground(Color.GREEN);
                changed.setText("Free");
                break;
            }
        }
    }

    public static void main(String[] args) {
        GUI gui = new GUI();
    }
}

//class Network {
//
//    public static void main(String[] args) throws IOException {
//        int N,TC;
//        String TCLine;
//        Scanner read = new Scanner(System.in);
//        System.out.println("What is the number of WI-FI Connections?");
//        N = read.nextInt();
//        read.nextLine();
//        Router router=new Router(N);
//        ArrayList<Thread> devices = new ArrayList<>();
//        System.out.println("What is the number of devices Clients want to connect?");
//        TC = read.nextInt();
//        read.nextLine();
//        for (int i=0;i<TC;i++){
//            TCLine = read.nextLine();
//            String[] line = TCLine.split(" ");
//            Thread device = new Thread(new Device(line[0],line[1],router));
//            devices.add(device);
//        }
//
//        for (Thread device : devices) {
//            device.start();
//        }
//    }
//}
