package com.company;

import java.util.ArrayList;

public class Router {

    private ArrayList<Device> connections;
    private Semaphore semaphore;
    private int size;

    public Router(int N){
        size=N;
        semaphore=new Semaphore(N);
    }

    public void occupy(Device device)
    {
        if(connections.size()<size){
            connections.add(device);
        }
        semaphore.P(device);
    }
    public void release(Device device)
    {
        connections.remove(device);
        semaphore.V();
    }
}
