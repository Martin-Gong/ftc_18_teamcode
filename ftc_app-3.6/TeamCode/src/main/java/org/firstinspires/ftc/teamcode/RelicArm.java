package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@Disabled
public class RelicArm {
    public DcMotor relicSlide   = null;
    public Servo relicClaw = null;
    public Servo relicArm1 = null;
    public final double relicArm1_ST = 1;
    public final double relicClaw_ST = 0;

    HardwareMap hwMap = null;

    public void init(HardwareMap Map) {
        hwMap = Map;
        relicSlide = hwMap.get(DcMotor.class, "relic_slide");
        relicClaw = hwMap.get(Servo.class, "relic_claw");
        relicArm1 = hwMap.get(Servo.class, "relic_arm");

        relicSlide.setDirection(DcMotor.Direction.FORWARD);
        relicSlide.setPower(0);
        relicSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        relicClaw.setPosition(relicClaw_ST);
        relicArm1.setPosition(relicArm1_ST);


    }
    public void RelicExt(double power){

        relicSlide.setPower(power);
    }
    public void ClawMove(double PosC){
        Range.clip(PosC,-1,1);
        relicClaw.setPosition(PosC);
    }
    public void ArmMove(double PosA){
        Range.clip(PosA,-1,1);
        relicArm1.setPosition(PosA);
    }
}