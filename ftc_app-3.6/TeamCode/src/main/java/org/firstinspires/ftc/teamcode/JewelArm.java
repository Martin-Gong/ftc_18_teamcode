package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.util.Config;


public class JewelArm {
    //defining the jewel arm
    double MAX_POS_ARM = 0.73;
    double MIN_POS_ARM = 0.10;
    double MAX_POS_ARM1 = 0.73;
    double MIN_POS_ARM1 = 0.10;
    Servo jewelArm;
    Servo jewelArm1;
    HardwareMap hwMap;
    public void init(HardwareMap Map, Config config){
        hwMap = Map;
        jewelArm = hwMap.get(Servo.class, "color_sensor_arm");
        jewelArm1 = hwMap.get(Servo.class, "color_sensor_arm1");
        MAX_POS_ARM = config.getDouble("arm_max", 0.73);
        MIN_POS_ARM = config.getDouble("arm_min", 0.10);
        MAX_POS_ARM1 = config.getDouble("arm1_max", 0.73);
        MIN_POS_ARM1 = config.getDouble("arm1_min", 0.10);
        jewelArm.setPosition(MIN_POS_ARM);
        jewelArm1.setPosition(MIN_POS_ARM1);
    }
    //method for setting jewel arm to a certain position, parameter is a double which is used to set the position
    public void setJewelArm(double Pos){
        jewelArm.setPosition(Pos);
    }
    public void openJewelArm(){
        ElapsedTime runtime = new ElapsedTime();
        runtime.reset();
        while (runtime.seconds() < 0.5) {
            jewelArm1.setPosition(MAX_POS_ARM1);
        }
        jewelArm.setPosition(MAX_POS_ARM);
    }
    public void closeJewelArm(){
        ElapsedTime runtime = new ElapsedTime();
        runtime.reset();
        while (runtime.seconds() < 0.5) {
            jewelArm.setPosition(MIN_POS_ARM);
        }
        jewelArm1.setPosition(MIN_POS_ARM1);
    }
    public void openJewelArm1(){
        jewelArm.setPosition(MAX_POS_ARM1);
    }
    public void closeJewelArm1(){
        jewelArm.setPosition(MIN_POS_ARM1);
    }
    public void openJewelArm0(){
        jewelArm.setPosition(MAX_POS_ARM);
    }
    public void closeJewelArm0(){
        jewelArm.setPosition(MIN_POS_ARM);
    }

    public double getPosition(){
        return jewelArm.getPosition();
    }
    public double getPosition1(){
        return jewelArm1.getPosition();
    }
}
