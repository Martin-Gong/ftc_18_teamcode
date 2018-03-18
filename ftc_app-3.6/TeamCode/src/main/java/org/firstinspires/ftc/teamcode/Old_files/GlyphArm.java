package org.firstinspires.ftc.teamcode.Old_files;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.util.Config;
import com.qualcomm.robotcore.util.Range;


public class GlyphArm {
    private DcMotor verticalMotor;
    private Servo leftHand;
    private Servo rightHand;
    double MAX_POS = 0.75;
    double MIN_POS = 0.15;
    double CONTAINER_SPAN_LEFT = 160;
    double CONTAINER_SPAN_RIGHT = 190;
    double CONTAINER_ERROR_LR = 0;
    double CONTAINER_TURN_SPEED = 1;
    double LIFT_POWER = 0.30;
    double container_position = 0.20;
    HardwareMap hwMap = null;
    public ElapsedTime time = new ElapsedTime();

public void init(HardwareMap Map,Config config) {
    hwMap = Map;
    //verticalMotor = hwMap.get(DcMotor.class, "lift_drive");
    leftHand = hwMap.get(Servo.class, "lift_left");
    rightHand = hwMap.get(Servo.class, "lift_right");

    MAX_POS = config.getDouble("container_max", 0.75);
    MIN_POS = config.getDouble("container_min", 0.15);
    CONTAINER_SPAN_LEFT = config.getDouble("container_span_left",160);
    CONTAINER_SPAN_RIGHT = config.getDouble("container_span_right",185);
    CONTAINER_ERROR_LR = config.getDouble("container_error_left_right",0);
    CONTAINER_TURN_SPEED = config.getDouble("container_turn_speed",1);
    LIFT_POWER = config.getDouble("lift_power", 0.30);

    //verticalMotor.setDirection(DcMotor.Direction.FORWARD);
    //verticalMotor.setPower(0);
    container_position = MIN_POS;
    leftHand.setPosition(container_position);
    rightHand.setPosition(getRightPosFromLeft(container_position));
}

public void getPosition(Telemetry telemetry){
    telemetry.addData("L: " + leftHand.getPosition(), "R: " + rightHand.getPosition());
    telemetry.update();

}

public void moveUpOrDown(double power){
    //verticalMotor.setDirection(DcMotor.Direction.FORWARD);
    //verticalMotor.setPower(power);
}

public void moveUp(){
    //verticalMotor.setDirection(DcMotor.Direction.FORWARD);
    //verticalMotor.setPower(-1.0*LIFT_POWER);
}

public void moveDown(){
       //verticalMotor.setDirection(DcMotor.Direction.FORWARD);
       // verticalMotor.setPower(LIFT_POWER);
    }

public void stop(){
   // verticalMotor.setDirection(DcMotor.Direction.FORWARD);
   // verticalMotor.setPower(0);
}

public void openContainer(){
    leftHand.setPosition(MAX_POS);
    rightHand.setPosition(getRightPosFromLeft(MAX_POS));
}

public void closeContainer(){
    leftHand.setPosition(MIN_POS);
    rightHand.setPosition(getRightPosFromLeft(MIN_POS));

}
public void turnContainer(double speed){
    double pos = container_position;
    pos += speed/180.;
    container_position = Range.clip(pos, MIN_POS, MAX_POS);
    leftHand.setPosition(container_position);
    rightHand.setPosition(getRightPosFromLeft(container_position));
}

public double getRightPosFromLeft(double pos){
    return 1.0 - (pos*CONTAINER_SPAN_LEFT+CONTAINER_ERROR_LR)/CONTAINER_SPAN_RIGHT;
}

public double getLeftPosition(){
    return leftHand.getPosition();
}
    public double getRightPosition(){
        return rightHand.getPosition();
    }
}