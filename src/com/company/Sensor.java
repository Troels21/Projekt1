package com.company;

import jssc.SerialPort;
import jssc.SerialPortException;

public class Sensor {
    double vaerdi;

    public double rigtigSensor() {
        SerialPort port = new SerialPort("COM5"); // find din COM port under enheder i kontrolpanel

        //Opsætning af serialport
        try {
            port.openPort();
            port.setParams(9600, 8, 1, 0);
            port.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
            port.setDTR(true);
        } catch (SerialPortException e) {
            e.printStackTrace();
        }

        //Tjekker byte antal, hvis det ikke er et positivt tal vent
        while (true) {
            try {
                if (port.getInputBufferBytesCount() > 0) {
                    String input = port.readString();
                    double input2 = Double.parseDouble(input.substring(2,6));
                    vaerdi = input2;
                    break;
                } else {
                    Thread.sleep(1000);
                }
            } catch (SerialPortException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // lukning af port
        try {
            port.closePort();
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
        return vaerdi;
    }


        // psuedo sensor som bruger random og omregner til celsius
        public double konverttemperatur(){
        vaerdi = Math.random() * 90 + 130;
        return vaerdi * 4 / 50 + 24;
    }
}


