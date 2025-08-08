package org.firstinspires.ftc.teamcode.InfiniteFailsafes.Hardware;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Utils.Timer;

public class ColorSensorWrapper {

    public double red, distance, blue, green, gain;
    public boolean objectIsClose = false;

    public double minimumDetectionDistanceMM = 20;
    private final RevColorSensorV3 sensor;
    private Timer timer;
    private final long updateIntervalMillis;

    public ColorSensorWrapper(HardwareMap hardwareMap, String name) {
        this(hardwareMap, name, 100); // Default to 100 ms interval
    }

    public ColorSensorWrapper(HardwareMap hardwareMap, String name, long updateIntervalMillis) {
        sensor = hardwareMap.get(RevColorSensorV3.class, name);
        timer = new Timer();
        timer.reset();
        this.updateIntervalMillis = updateIntervalMillis;
    }

    public void updateData() {
        if (timer.getTime() >= updateIntervalMillis) {
            red = sensor.red();
            green = sensor.green();
            blue = sensor.blue();
            gain = sensor.getGain();
            distance = sensor.getDistance(DistanceUnit.MM);
            objectIsClose = distance <= minimumDetectionDistanceMM;
            timer.reset();
        }
    }


    public double getMinimumDetectionDistanceMM() {
        return minimumDetectionDistanceMM;
    }

    public void setMinimumDetectionDistanceMM(double minimumDetectionDistanceMM) {
        this.minimumDetectionDistanceMM = minimumDetectionDistanceMM;
    }


    // Debug stuff
    public String getDebugString() {
        String s =
                    "DATA FROM VARIABLES: " + '\n' +
                    "var Red: " + red + '\n' +
                    "var Green: " +  green + '\n' +
                    "var Blue: " +  blue + '\n' +
                    "var Gain: " +  gain + '\n' +
                    "var Distance in MM: " +  distance + '\n' +
                    "var Object Close?: " +  objectIsClose + '\n' +

                    "DATA FROM SENSOR: " + '\n' +
                    "Sensor Red: " +  sensor.red() + '\n' +
                    "Sensor Green: " +  sensor.green() + '\n' +
                    "Sensor Blue: " +  sensor.blue() + '\n' +
                    "Sensor Gain: " +  sensor.getGain() + '\n' +
                    "Sensor Distance in MM: " +  sensor.getDistance(DistanceUnit.MM) + '\n' +
                    "Sensor Object Close?: " +  ((sensor.getDistance(DistanceUnit.MM) <= minimumDetectionDistanceMM) ? "YES" : "NO") ;
        return s;
    }
}