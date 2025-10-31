package com.voltageg.modbus.threads;

import com.voltageg.modbus.sensors.Sensors;
import com.voltageg.modbus.starters.Slave;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@NoArgsConstructor
public class SlaveThread extends AbstractThread{
    @Override
    public void init() {
        Slave.startRTUOverTCPSlave();
        //Sensors.TEMPERATURE_HUMIDITY.writeData(20,40);
    }

    @Override
    public void tickLoop() {
        Random random = new Random();

        int temperature = -100;
        if (random.nextFloat() < 0.05f) temperature = random.nextInt(-10, 60);

        int humidity = -100;
        if (random.nextFloat() < 0.05f) humidity = random.nextInt(70);


        if (temperature != -100 && humidity != -100)
            Sensors.TEMPERATURE_HUMIDITY.writeData(temperature,humidity);
    }

    @Override
    public void mainLoop() {

    }
}
