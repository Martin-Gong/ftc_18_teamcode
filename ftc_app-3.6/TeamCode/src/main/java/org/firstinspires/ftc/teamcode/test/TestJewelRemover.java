package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.*;

import org.firstinspires.ftc.teamcode.util.telemetry.TelemetryWrapper;

/**
 * Created by Martin on 22/2/18.
 */

@TeleOp(name="ColorSense_test_presents", group = "Test")
public class TestJewelRemover extends OpMode{

    ColorSensor colorSensor;
    final String teamColor = "blue"; //The team color that we are on

    @Override
    public void start () {

    }

    @Override
    public void init () {
        colorSensor = hardwareMap.colorSensor.get("color"); //THE COLOR SENSOR IS NAMED "color"
        TelemetryWrapper.init(telemetry,7); //Static constructor + methods, no instance needed
    }

    @Override
    public void loop () {
//        int redTimes = 0, blueTimes = 0;
        String foundColor = ""; //Variable that saves the result color
        if (colorSensor.red() > colorSensor.blue())
        {
            foundColor = "red";
//            redTimes++;
        }
        else if (colorSensor.blue() > colorSensor.red())
        {
            foundColor = "blue";
//            blueTimes++;
        }
        else
        {
            foundColor = "equal value of blue and red";
        }
        TelemetryWrapper.setLine(1,"Color found: " + foundColor);
    }

    @Override
    public void stop () {

    }
}
