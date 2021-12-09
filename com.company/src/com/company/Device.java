package com.company;

import static java.lang.Thread.sleep;

public class Device implements Runnable {
    private String name;
    private String type;
    private Router router;
    private Semaphore sem;
    int connectionNum;

    public Device(String name,String type,Router router){
        this.name=name;
        this.type=type;
        this.router=router;
        sem=router.getSemaphore();
    }

    @Override
    public void run() {
        sem.P(this);
        connect();
        try {
            sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        performOnlineActivity();
        try {
            sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        disconnect();
        sem.V(this);
    }

    public void connect()
    {
        router.occupy(this);
        connectionNum=router.getConnections().lastIndexOf(this)+1;
        System.out.println("Connection "+connectionNum+": "+name + " login");
    }
    public void performOnlineActivity()
    {
        System.out.println("Connection "+connectionNum+": "+name+" performs online activity");
    }
    public void disconnect()
    {
        router.release(this);
        System.out.println("Connection "+connectionNum+": "+name + " Logged out");
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