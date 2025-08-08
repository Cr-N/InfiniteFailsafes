package org.firstinspires.ftc.teamcode.DECODE.Servo;

import com.arcrobotics.ftclib.hardware.ServoEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class ServoWrapper {

    private Servo servo;

    public ServoWrapper(HardwareMap hardwareMap, String name){
        servo = hardwareMap.get(Servo.class,name);
    }

    public void setPosition(double pos){
        servo.setPosition(pos);
    }

    public double getPosition(){
        return servo.getPosition();
    }
}
