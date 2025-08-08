package org.firstinspires.ftc.teamcode.DECODE.AutoStateMachine.Testing;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.DECODE.AutoStateMachine.AutoState;
import org.firstinspires.ftc.teamcode.DECODE.Robot;
import org.firstinspires.ftc.teamcode.DECODE.Servo.MoveServoCommand;

public class MoveServoAroundState extends AutoState {

    HardwareMap hardwareMap;
    public MoveServoAroundState(HardwareMap hardwareMap){
        this.hardwareMap = hardwareMap;
    }
    boolean finishedCycle = false;
    boolean failsafeTriggered = false;

    @Override
    public void onEnter() {

        finishedCycle = false;
        CommandScheduler.getInstance().schedule(
                new SequentialCommandGroup(
                        trackCommand("MoveTo0.2",new MoveServoCommand(Robot.getInstance(hardwareMap).getServo(),0.2,1000),true),
                        trackCommand("MoveTo0.4",new MoveServoCommand(Robot.getInstance(hardwareMap).getServo(),0.4,1000),true),
                        trackCommand("MoveTo0.6",new MoveServoCommand(Robot.getInstance(hardwareMap).getServo(),0.6,1000),true),
                        trackCommand("MoveTo0.8",new MoveServoCommand(Robot.getInstance(hardwareMap).getServo(),0.8,1000),true),
                        trackCommand("MoveTo1",new MoveServoCommand(Robot.getInstance(hardwareMap).getServo(),1,1000),true),
                        trackCommand("TESTING_FAILSAFE",new WaitCommand(15000),true),
                        trackCommand("MoveTo0",new MoveServoCommand(Robot.getInstance(hardwareMap).getServo(),0,1000),true),
                        trackCommand("Finished Cycle, Cleanup", new InstantCommand(()->finishedCycle = true),true)
                )
        );

    }

    @Override
    public void update() {

        if(m_cmds.containsKey("TESTING_FAILSAFE")){
            if(m_cmds.get("TESTING_FAILSAFE") == COMMAND_STATE.EXECUTING){
                if(Robot.getInstance(hardwareMap).getColorSensor().objectIsClose || Robot.getInstance(hardwareMap).getMls().detectsMagnet()) {
                    CommandScheduler.getInstance().cancelAll();
                    this.failsafeTriggered = true;
                }
            }
        }
    }

    @Override
    public boolean isFinished() {
        return failsafeTriggered || finishedCycle;
    }

    @Override
    public void end() {
        super.end();
    }

    @Override
    public AutoState getNextState() {
        if (failsafeTriggered) {
            return new GoToHomeFailsafe(hardwareMap);
        }
        if (finishedCycle) {
            return new MoveServoAroundState(hardwareMap); // repeat the cycle
        }
        return null;
    }
}
