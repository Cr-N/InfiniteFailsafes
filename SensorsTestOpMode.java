package org.firstinspires.ftc.teamcode.DECODE.Sensors;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedroPathing.constants.FConstants;
import org.firstinspires.ftc.teamcode.pedroPathing.constants.LConstants;

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
