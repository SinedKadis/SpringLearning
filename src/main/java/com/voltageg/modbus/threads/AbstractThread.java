package com.voltageg.modbus.threads;

import java.util.function.Supplier;

public abstract class AbstractThread extends Thread{
    public static boolean stop = false;
    public static final Supplier<Long> tick = System::currentTimeMillis;
    public static final int tickRate = 50;
    static boolean ticked = false;

    @Override
    public void run() {
        init();
        while (!stop) {
            if (tick.get() % tickRate == 0) {
                if (!ticked) {
                    tickLoop();
                    ticked = true;
                }
            } else ticked = false;
            mainLoop();
        }
    }

    public abstract void init();
    public abstract void tickLoop();
    public abstract void mainLoop();
}
