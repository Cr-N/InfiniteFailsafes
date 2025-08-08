package org.firstinspires.ftc.teamcode.InfiniteFailsafes.Testing;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.InfiniteFailsafes.Hardware.ColorSensorWrapper;
import org.firstinspires.ftc.teamcode.InfiniteFailsafes.Hardware.MagneticLimitSwitchWrapper;

@TeleOp
public class SensorsTestOpMode extends CommandOpMode {

    MagneticLimitSwitchWrapper mls;
    ColorSensorWrapper color;

    @Override
    public void initialize() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        mls = new MagneticLimitSwitchWrapper(hardwareMap,"mls");
        color = new ColorSensorWrapper(hardwareMap,"color",100);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        initialize();
        waitForStart();
        while (opModeIsActive()){
            color.updateData();

            telemetry.addLine(color.getDebugString());
            telemetry.addLine(mls.getDebugString());
            telemetry.update();
        }
    }
}
