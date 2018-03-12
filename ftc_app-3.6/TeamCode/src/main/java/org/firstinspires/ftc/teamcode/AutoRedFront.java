package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.util.Config;
import org.firstinspires.ftc.teamcode.util.telemetry.TelemetryWrapper;

import javax.microedition.khronos.opengles.GL;


@Autonomous(name = "AutoRedFront")
public class AutoRedFront extends LinearOpMode {
    //define the parts that will be moving

    private ElapsedTime runtime = new ElapsedTime();
    //color of the team
    String teamColor = "red";
    String foundColor = null;
    ColorSensor colorSensor;
    private Config config = new Config(Config.configFile);

    @Override
    public void runOpMode() {
        JewelArm jewelArm = new JewelArm();
        GlyphArm glyphArm = new GlyphArm();
        DriveTrain driveTrain = new DriveTrain();
        VuMarkID vuMarkID = new VuMarkID();

        //Initializing and performing init methods
        jewelArm.init(hardwareMap,config);
        glyphArm.init(hardwareMap,config);
        driveTrain.init(hardwareMap,config);
        vuMarkID.init(hardwareMap,config);
        colorSensor = hardwareMap.colorSensor.get("color");
        /*
            red_front_kick_color_time1
            red_front_kick_color_power1
            red_front_kick_color_time2
            red_front_kick_color_power2
            red_front_time_to_run_left
            red_front_time_to_run_right
            red_front_time_to_run_center
            red_front_time_to_run_power
            red_front_turn_time
            red_front_turn_power
            red_front_approach_time
            red_front_approach_power
            red_front_adjust_pos_time
            red_front_adjust_pos_power
         */
        double kickColorTime1 = config.getDouble("red_front_kick_color_time1",1);
        double kickColorPower1 = config.getDouble("red_front_kick_color_power1",0.45);
        double kickColorTime2 = config.getDouble("red_front_kick_color_time2",1);
        double kickColorPower2 = config.getDouble("red_front_kick_color_power2",0.45);
        double timeToRunLeft = config.getDouble("red_front_time_to_run_left",4);
        double timeToRunCenter = config.getDouble("red_front_time_to_run_center",3.5);
        double timeToRunRight = config.getDouble("red_front_time_to_run_right",3);
        double timeToRunPower = config.getDouble("red_front_time_to_run_power",-0.2);
        double turnTime = config.getDouble("red_front_turn_time",3);
        double turnPower = config.getDouble("red_front_turn_power",-0.2);
        double approachTime = config.getDouble("red_front_approach_time",1);
        double approachPower = config.getDouble("red_front_approach_power",0.2);
        double adjustPosTime = config.getDouble("red_front_adjust_pos_time",1);
        double adjustPosPower = config.getDouble("red_front_adjust_pos_power",0.2);

        RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.UNKNOWN;

        vuMarkID.activate();

        //waiting for user to press start
        waitForStart();

        TelemetryWrapper.init(telemetry,7);

        jewelArm.openJewelArm();

        runtime.reset();

        while (opModeIsActive() && (runtime.seconds() < 10)) {
            vuMark = vuMarkID.getVuMark();
            TelemetryWrapper.setLine(0,"Elapsed "+runtime.seconds());
            if(vuMark!=RelicRecoveryVuMark.UNKNOWN){
                TelemetryWrapper.setLine(1,"vuMark ID is "+vuMark +""+ vuMarkID.format(vuMarkID.pose));
                vuMarkID.deactivate();
                break;
            }
            else
                TelemetryWrapper.setLine(1,"vuMark ID is invisible, wait for recognizing ....");

        }
        runtime.reset();

        //turn on color sensor light
        colorSensor.enableLed(true);
        //drop jewel arm so color sensor can detect
        runtime.reset();
        while (opModeIsActive() && runtime.seconds() < 2) {
            jewelArm.openJewelArm();
            //returns which color the jewel is
            if (colorSensor.red() > colorSensor.blue()) {
                foundColor = "red";
            } else if (colorSensor.blue() > colorSensor.red()) {
                foundColor = "blue";
            } else {
                foundColor = "red";
            }
        }

        //checks if the color the jewel it detects is same as team color
        if (teamColor == foundColor) {
            while (opModeIsActive() && runtime.seconds() < kickColorTime1) {
                jewelArm.openJewelArm();
                //moves and pushes the other jewel
                driveTrain.move(0,kickColorPower1, 0);
            }
            driveTrain.stop();
            runtime.reset();
            while (opModeIsActive() && runtime.seconds() < kickColorTime2) {
                jewelArm.closeJewelArm();
                driveTrain.move(0,-1*kickColorPower2, 0);
            }
            // if the color of the jewel is not the same as the color of the team
        } else {
            while (opModeIsActive() && runtime.seconds() < kickColorTime1) {
                jewelArm.openJewelArm();
                //moves and pushes off the same jewel it detects because colors dont match
                driveTrain.move(0,-1*kickColorPower1, 0);
            }
            driveTrain.stop();
            //move to regain back to the same position
            runtime.reset();
            while (opModeIsActive() && runtime.seconds() < kickColorTime2) {
                jewelArm.closeJewelArm();
                driveTrain.move(0,kickColorPower2, 0);
            }

        }
        driveTrain.stop();
        /////////////
        //move to crypto
        double timeToRun = timeToRunCenter;
        if(vuMark == RelicRecoveryVuMark.LEFT) timeToRun = timeToRunLeft;
        else  if(vuMark == RelicRecoveryVuMark.RIGHT) timeToRun = timeToRunRight;
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < timeToRun)) {
            driveTrain.move(0,timeToRunPower, 0);
        }
        //turn to crypto box
        runtime.reset();
        while (opModeIsActive() && runtime.seconds() < turnTime) {
            driveTrain.move(0,0.0, turnPower);
        }
        driveTrain.stop();
        //move close to crypt box
        runtime.reset();
        while (opModeIsActive() && runtime.seconds() < approachTime) {
            driveTrain.move(0,approachPower, 0);
        }
        driveTrain.stop();

        //open container
        runtime.reset();
        while (opModeIsActive() && runtime.seconds() < 1) {
            glyphArm.openContainer();
        }
        glyphArm.closeContainer();
        //move in all the way
        while (opModeIsActive() && runtime.seconds() < adjustPosTime) {
            driveTrain.move(0,adjustPosPower, 0.0);
        }
        driveTrain.stop();
        //To stop the drive train
        runtime.reset();
        while (opModeIsActive() && runtime.seconds() < 1) {
            driveTrain.stop();
        }

        driveTrain.stop();
    }
}