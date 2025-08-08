package org.firstinspires.ftc.teamcode.DECODE.AutoStateMachine.Testing;

import android.util.Log;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.DECODE.AutoStateMachine.AutoState;
import org.firstinspires.ftc.teamcode.DECODE.Robot;
import org.firstinspires.ftc.teamcode.DECODE.Servo.MoveServoCommand;

public class GoToHomeFailsafe extends AutoState {

    HardwareMap hardwareMap;

    public GoToHomeFailsafe(HardwareMap hardwareMap){
        this.hardwareMap = hardwareMap;
    }

    @Override
    public void onEnter() {
        this.endState = false;
        CommandScheduler.getInstance().schedule(
                new SequentialCommandGroup(
                        trackCommand("MoveTo0.5_FAILSAFE",new MoveServoCommand(Robot.getInstance(hardwareMap).getServo(),0.5,1000),true),
                        trackCommand("endStateForFailsafe",new InstantCommand(()->this.endState = true),true)
                )
        );
    }

    @Override
    public boolean isFinished() {
        return endState;
    }

    @Override
    public void end() {
        Log.d("FAILSAFE","Failsafe Ended Successfully!");
    }

    @Override
    public AutoState getNextState() {
        return null;
    }
}

