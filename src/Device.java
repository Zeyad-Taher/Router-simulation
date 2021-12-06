package com.company;

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
    }

    public void connect()
    {
        router.occupy(this);
        System.out.println("login");
    }
    public void performOnlineActivity()
    {
        System.out.println(this.name+" performs online activity");
    }
    public void disconnect()
    {
        router.release(this);
        System.out.println("Logged out");
    }
}
