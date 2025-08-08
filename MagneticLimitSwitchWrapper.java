package org.firstinspires.ftc.teamcode.DECODE.Sensors;

import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class MagneticLimitSwitchWrapper {
    RevTouchSensor magneticLS;

    public MagneticLimitSwitchWrapper (HardwareMap hardwareMap,String name){
        this.magneticLS = hardwareMap.get(RevTouchSensor.class,name);
    }

    public boolean detectsMagnet(){
        return magneticLS.isPressed();
    }

    public String getDebugString(){
        String s =  "MAGNETIC LIMIT SWITCH DEBUG INFO (" + magneticLS.getDeviceName() + ")\n" +
                    "detects magnet:  " + ((detectsMagnet()) ? "YES" : "NO") + '\n' +
                    "device name:     " + magneticLS.getDeviceName() + '\n' +
                    "connection info: " + magneticLS.getConnectionInfo()+ '\n' +
                    "manufacturer:    " + magneticLS.getManufacturer()+ '\n' +
                    "version:         " + magneticLS.getVersion() + '\n';
        return s;
    }
}
