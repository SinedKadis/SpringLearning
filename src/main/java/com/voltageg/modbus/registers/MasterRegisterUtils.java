package com.voltageg.modbus.registers;

import com.digitalpetri.modbus.exceptions.ModbusExecutionException;
import com.digitalpetri.modbus.exceptions.ModbusResponseException;
import com.digitalpetri.modbus.exceptions.ModbusTimeoutException;
import com.digitalpetri.modbus.pdu.ReadHoldingRegistersRequest;
import com.digitalpetri.modbus.pdu.ReadHoldingRegistersResponse;
import com.digitalpetri.modbus.pdu.WriteMultipleRegistersRequest;
import com.digitalpetri.modbus.pdu.WriteMultipleRegistersResponse;
import com.voltageg.modbus.starters.Master;

import static com.voltageg.modbus.starters.Master.client;


public class MasterRegisterUtils {

    public static void writeRegistries(int unitID,int quantity,int[] data) {
        byte[] toWrite = new byte[quantity*2];
        for (int i = 0,i1 = 0; i < quantity*2; i=i+2,i1++) {
            int b = data[i1];
            int v = b/256;
            int v1 = b%256;
            toWrite[i] = (byte) (v-256);
            toWrite[i+1] = (byte) (v1-256);
        }
        WriteMultipleRegistersResponse response = null;
        while (response == null) {
            try {
                response = client.writeMultipleRegisters(
                        unitID,
                        new WriteMultipleRegistersRequest(0, quantity, toWrite));
            } catch (ModbusExecutionException |ModbusResponseException e) {
                throw new RuntimeException(e);
            } catch (ModbusTimeoutException e) {
                Master.reconnect();
            }
        }
    }

    public static int[] readRegisters(int unitID,int quantity) {
        ReadHoldingRegistersResponse response = null;
        while (response == null){

                try {
                    response = client.readHoldingRegisters(
                            unitID,
                            new ReadHoldingRegistersRequest(0, quantity)
                    );
                } catch (ModbusResponseException e) {
                    throw new RuntimeException(e);
                } catch (ModbusExecutionException |ModbusTimeoutException e) {
                    Master.reconnect();
                }

        }
        byte[] registers = response.registers();
        int[] toReturn = new int[quantity];
        for (int i = 0,i1 = 0; i < registers.length; i=i+2,i1++) {
            int b = registers[i];
            int b1 = registers[i+1];
            if (b<0) b = 256+b;
            if (b1<0) b1 = 256+b1;
            toReturn[i1] = (b<<8)+b1;
        }
        return toReturn;
    }
}
