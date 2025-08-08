package org.firstinspires.ftc.teamcode.DECODE.AutoStateMachine.Testing;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.DECODE.AutoStateMachine.AutoState;
import org.firstinspires.ftc.teamcode.DECODE.Robot;

import java.util.Map;



@TeleOp(name = "FSM Testbench", group = "DECODE")
public class TestingFSMOpMode extends OpMode {

    private Robot robot;
    private StateManager stateManager;

    @Override
    public void init() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        robot = Robot.getInstance(hardwareMap);
        stateManager = StateManager.getInstance();

        stateManager.setCurrentState(new MoveServoAroundState(hardwareMap));

        telemetry.addData("Status", "Initialized. Press Start to run the FSM.");
        telemetry.update();
    }

    @Override
    public void loop() {
        robot.updateRobot();
        stateManager.tick();
        displayTelemetry();
    }

    private void displayTelemetry() {
        AutoState currentState = stateManager.getCurrentState();

        // Verificare esentiala pentru a preveni NullPointerException
        if (currentState != null) {
            telemetry.addData("Current State", currentState.getClass().getSimpleName());

            // Formatare sigura si curata a map-ului de comenzi
            if (currentState.m_cmds.isEmpty()) {
                telemetry.addLine("No commands are being tracked.");
            } else {
                telemetry.addLine("--- Tracked Commands ---");
                for (Map.Entry<String, AutoState.COMMAND_STATE> entry : currentState.m_cmds.entrySet()) {
                    telemetry.addData(entry.getKey(), entry.getValue());
                }
                telemetry.addLine("------------------------");
            }
        } else {
            telemetry.addData("Status", "State Machine Finished. Autonomous sequence is complete. Aka currentState == null");
        }

        telemetry.update();
    }

    @Override
    public void stop() {
        com.arcrobotics.ftclib.command.CommandScheduler.getInstance().reset();
    }
}