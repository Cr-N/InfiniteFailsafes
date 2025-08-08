package org.firstinspires.ftc.teamcode.InfiniteFailsafes.Commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.InfiniteFailsafes.Hardware.ServoWrapper;
import org.firstinspires.ftc.teamcode.InfiniteFailsafes.Utils.Timer;

public class MoveServoCommand extends CommandBase {

    ServoWrapper servoWrapper;
    Timer timer;
    long waitTimeMilliseconds;
    double targetServoPosition;

    public MoveServoCommand(ServoWrapper servoWrapper, double targetServoPosition, long waitTimeMilliseconds){
        this.servoWrapper = servoWrapper;
        this.targetServoPosition = targetServoPosition;
        this.waitTimeMilliseconds = waitTimeMilliseconds;
        timer = new Timer();
        timer.reset();
    }

    @Override
    public void initialize() {
        servoWrapper.setPosition(targetServoPosition);
        timer.reset(); // just to be sure :)
    }

    @Override
    public boolean isFinished() {
        return timer.getTime() >= waitTimeMilliseconds;
    }
}
