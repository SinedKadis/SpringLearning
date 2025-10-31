package com.voltageg.modbus.threads;

import com.voltageg.modbus.sensors.Sensors;
import com.voltageg.modbus.starters.Master;
import com.voltageg.springmodbus.events.SensorEventService;
import lombok.Getter;
import org.springframework.stereotype.Component;


@Getter
@Component
public class MasterThread extends AbstractThread{

    private int temperature = -40;
    private int humidity = 0;

    private final SensorEventService sensorEventService;

    // Конструктор для внедрения зависимости
    public MasterThread(SensorEventService sensorEventService) {
        this.sensorEventService = sensorEventService;
    }


    @Override
    public void init() {
        Master.runRTUOverTCPClient();
    }

    @Override
    public void tickLoop() {
        int[] response = Sensors.TEMPERATURE_HUMIDITY.readData();

        setSensorData(response[0],response[1]);

    }

    public void setTemperature(int temperature) {
        int oldTemp = this.temperature;
        this.temperature = temperature;


        if (oldTemp != temperature) {
            sensorEventService.publishSensorChange(this.temperature, this.humidity);
        }
    }


    public void setHumidity(int humidity) {
        int oldHumidity = this.humidity;
        this.humidity = humidity;


        if (oldHumidity != humidity) {
            sensorEventService.publishSensorChange(this.temperature, this.humidity);
        }
    }


    public void setSensorData(int temperature, int humidity) {
        boolean tempChanged = this.temperature != temperature && temperature != -40;
        boolean humidityChanged = this.humidity != humidity && humidity != 0;

        this.temperature = temperature;
        this.humidity = humidity;


        if (tempChanged || humidityChanged) {
            sensorEventService.publishSensorChange(this.temperature, this.humidity);
        }
    }

    @Override
    public void mainLoop() {

    }
}
