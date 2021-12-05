package com.company;

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
                wait();
            }
            catch (InterruptedException e) {}
        }
    }
    public synchronized void V() // Signal
    {
        value++ ;
        if (value <= 0)
            notify();
    }
}
