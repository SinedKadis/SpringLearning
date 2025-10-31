package com.voltageg.springmodbus;

import com.voltageg.modbus.threads.AbstractThread;
import com.voltageg.modbus.threads.SlaveThread;

public class SlaveStart {
    public static void main(String[] args) {
        AbstractThread slave = new SlaveThread();
        slave.start();
    }
}
