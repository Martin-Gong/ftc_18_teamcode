package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.util.Config;


public class JewelArm {
    //defining the jewel arm
    double MAX_POS_ARM = 0.73;
    double MIN_POS_ARM = 0.10;
    Servo jewelArm;
    HardwareMap hwMap;
    public void init(HardwareMap Map, Config config){
        hwMap = Map;
    //TelemetryWrapper.setLine(1,"Color found: " + foundColor);
        jewelArm = hwMap.get(Servo.class, "color_sensor_arm");
        MAX_POS_ARM = config.getDouble("arm_max", 0.73);
        MIN_POS_ARM = config.getDouble("arm_min", 0.10);
        jewelArm.setPosition(MIN_POS_ARM);
    }

    //method for setting jewel arm to a certain position, parameter is a double which is used to set the position
    public void setJewelArm(double Pos){
        jewelArm.setPosition(Pos);
    }
    public void openJewelArm(){
        jewelArm.setPosition(MAX_POS_ARM);
    }
    public void closeJewelArm(){
        jewelArm.setPosition(MIN_POS_ARM);
    }
    public double getPosition(){
        return jewelArm.getPosition();
    }
}
