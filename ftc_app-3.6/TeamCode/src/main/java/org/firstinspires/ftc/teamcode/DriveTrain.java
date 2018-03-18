package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.util.Config;
import com.qualcomm.robotcore.util.Range;


public class DriveTrain {
    DcMotor leftFront   = null;
    DcMotor rightFront   = null;
    DcMotor leftBack  = null;
    DcMotor rightBack  = null;
    double speed;
    double offset;
    HardwareMap hwMap = null;

    public void init(HardwareMap Map, Config config) {
        hwMap = Map;
// not use the front drive temporary
//        leftFront = hwMap.get(DcMotor.class, "fl_drive");
//        rightFront = hwMap.get(DcMotor.class, "fr_drive");
//        leftFront.setDirection(DcMotor.Direction.FORWARD);
//        rightFront.setDirection(DcMotor.Direction.REVERSE);
//        leftFront.setPower(0);
//        rightFront.setPower(0);


        leftBack = hwMap.get(DcMotor.class, "rl_drive");
        rightBack = hwMap.get(DcMotor.class, "rr_drive");
        leftBack.setDirection(DcMotor.Direction.FORWARD);
        rightBack.setDirection(DcMotor.Direction.REVERSE);
        leftBack.setPower(0);
        rightBack.setPower(0);


    }
    public void move(double power, double dif){
        speed = power;
        offset = dif;
//        leftFront.setPower(speed-offset);
//        rightFront.setPower(speed+offset);
        leftBack.setPower(Range.clip(speed+offset,-1,1));
        rightBack.setPower(Range.clip(speed-offset,-1,1));
    }
    public void stop(){
        move(0,0);
    }
}