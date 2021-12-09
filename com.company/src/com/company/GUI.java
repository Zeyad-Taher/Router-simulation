import com.company.Device;
import com.company.Router;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GUI implements ActionListener {
    JFrame mainFrame;
    JPanel simulationFrame;
    JTextField numOfConnectionsField;
    JTextField numOfClientsField;
    JTextArea devicesInfoField;
    ArrayList<JButton> connectionsButtons;
    Router router;
    int numDevices;
    public GUI() {
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
        mainFrame.setSize(600, 400);
        mainFrame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        connectionsButtons = new ArrayList<>();
        simulationFrame = new JPanel();
        simulationFrame.setSize(300, 300);
        simulationFrame.setLocation(20, 320);
        int numConnections = Integer.parseInt(numOfConnectionsField.getText());
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
        }
        for (int i=0; i<numConnections; i++) {
            JButton deviceButton = new JButton("Free");
            deviceButton.setBackground(Color.GREEN);
            deviceButton.setSize(100, 100);

            connectionsButtons.add(deviceButton);
            simulationFrame.add(deviceButton);
        }

        simulationFrame.setLayout(new FlowLayout());
        mainFrame.add(simulationFrame);
    }

    public static void main(String[] args) {
        GUI gui = new GUI();
    }
}
