package org.firstinspires.ftc.teamcode.InfiniteFailsafes.Hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.InfiniteFailsafes.*;

// This is usually supposed to hold all of the subsystems but for this demo, we only have a servo and 2 sensors
public class Robot {

    private static Robot instance = null;

    private MagneticLimitSwitchWrapper mls;
    private ColorSensorWrapper colorSensor;
    private ServoWrapper servo;

    private Robot(HardwareMap hardwareMap){
        mls = new MagneticLimitSwitchWrapper(hardwareMap,"mls");
        colorSensor = new ColorSensorWrapper(hardwareMap,"color");
        servo = new ServoWrapper(hardwareMap,"servo");
    }

    public static synchronized Robot getInstance(HardwareMap hardwareMap) {
        if (instance == null) {
            instance = new Robot(hardwareMap);
        }
        return instance;
    }

    public void updateRobot(){

        colorSensor.updateData();

    }


    public MagneticLimitSwitchWrapper getMls() {
        return mls;
    }

    public ColorSensorWrapper getColorSensor() {
        return colorSensor;
    }

    public ServoWrapper getServo() {
        return servo;
    }
}