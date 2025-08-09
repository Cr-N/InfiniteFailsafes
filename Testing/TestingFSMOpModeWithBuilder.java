package org.firstinspires.ftc.teamcode.InfiniteFailsafes.Testing;

import android.util.Log;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.InfiniteFailsafes.Commands.MoveServoCommand;
import org.firstinspires.ftc.teamcode.InfiniteFailsafes.Hardware.Robot;
import org.firstinspires.ftc.teamcode.InfiniteFailsafes.Utils.AutoState;
import org.firstinspires.ftc.teamcode.InfiniteFailsafes.Utils.FailsafeBuilder;
import org.firstinspires.ftc.teamcode.InfiniteFailsafes.Utils.StateManager;

@TeleOp(name = "FSM Testbench (Builder)", group = "DECODE")
public class TestingFSMOpModeWithBuilder extends OpMode {

    private StateManager stateManager;
    private Robot robot;

    enum States {
        MOVE_SERVO_CYCLE,
        FAILSAFE_GO_HOME
    }

    @Override
    public void init() {
        robot = Robot.getInstance(hardwareMap);
        stateManager = StateManager.getInstance();

        AutoState startingState = new FailsafeBuilder(robot)

                .state(States.MOVE_SERVO_CYCLE)
                .onEnterSchedule(s -> { // 's' is the state instance
                    CommandScheduler.getInstance().schedule(
                            new SequentialCommandGroup(
                                    s.trackCommand("MoveTo0.2", new MoveServoCommand(robot.getServo(), 0.2, 1000)),
                                    s.trackCommand("TESTING_FAILSAFE", new WaitCommand(5000)),
                                    s.trackCommand("MoveTo0", new MoveServoCommand(robot.getServo(), 0, 1000))
                            )
                    );
                })
                .update((s, r) -> {
                    if (s.m_cmds.getOrDefault("TESTING_FAILSAFE", AutoState.COMMAND_STATE.FINISHED) == AutoState.COMMAND_STATE.EXECUTING) {
                        if (r.getColorSensor().objectIsClose || r.getMls().detectsMagnet()) {
                            CommandScheduler.getInstance().cancelAll();
                        }
                    }
                })
                .transition(s -> (robot.getColorSensor().objectIsClose || robot.getMls().detectsMagnet()), States.FAILSAFE_GO_HOME)
                .transition(s -> s.m_cmds.getOrDefault("MoveTo0", AutoState.COMMAND_STATE.INITIALIZED) == AutoState.COMMAND_STATE.FINISHED)
                .onEnd(s -> System.out.println("Cycle or Failsafe ended. Cleaning up."))



                .state(States.FAILSAFE_GO_HOME)
                .onEnterSchedule(s -> {
                    s.endState = false;
                    CommandScheduler.getInstance().schedule(
                            new SequentialCommandGroup(
                                    s.trackCommand("MoveTo0.5_FAILSAFE", new MoveServoCommand(robot.getServo(), 0.5, 1000)),
                                    new InstantCommand(() -> s.endState = true)
                            )
                    );
                })
                // FIXED: The lambda now receives 's' so it can access s.endState
                .transition(s -> s.endState)
                .onEnd(s -> Log.d("FAILSAFE", "Failsafe Ended Successfully!"))

                .build();

        stateManager.setCurrentState(startingState);
    }

    @Override
    public void loop() {
        robot.updateRobot();
        stateManager.tick();
    }

    @Override
    public void stop() {
        CommandScheduler.getInstance().reset();
    }
}