/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode.Old_files;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


import org.firstinspires.ftc.teamcode.util.ButtonHelper;
import org.firstinspires.ftc.teamcode.util.Config;
import org.firstinspires.ftc.teamcode.JewelArm;
import org.firstinspires.ftc.teamcode.GlyphArm;
import org.firstinspires.ftc.teamcode.DriveTrain;

import org.firstinspires.ftc.teamcode.util.ButtonHelper;
import static org.firstinspires.ftc.teamcode.util.ButtonHelper.dpad_down;
import static org.firstinspires.ftc.teamcode.util.ButtonHelper.dpad_up;
import static org.firstinspires.ftc.teamcode.util.ButtonHelper.dpad_left;
import static org.firstinspires.ftc.teamcode.util.ButtonHelper.dpad_right;
import static org.firstinspires.ftc.teamcode.util.ButtonHelper.a;
import static org.firstinspires.ftc.teamcode.util.ButtonHelper.b;
import static org.firstinspires.ftc.teamcode.util.ButtonHelper.x;
import static org.firstinspires.ftc.teamcode.util.ButtonHelper.y;
import static org.firstinspires.ftc.teamcode.util.ButtonHelper.start;




import org.firstinspires.ftc.robotcontroller.external.samples.ConceptVuforiaNavigation;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuMarkInstanceId;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;


import org.firstinspires.ftc.teamcode.util.telemetry.TelemetryWrapper;

/**
 * This OpMode scans a single servo back and forwards until Stop is pressed.
 * The code is structured as a LinearOpMode
 * INCREMENT sets how much to increase/decrease the servo position each cycle
 * CYCLE_MS sets the update period.
 *
 * This code assumes a Servo configured with the name "left claw" as is found on a pushbot.
 *
 * NOTE: When any servo position is set, ALL attached servos are activated, so ensure that any other
 * connected servos are able to move freely before running this test.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */
@TeleOp(name = "TeleOp Mode Test",group = "Test")
//@Disabled
public class TeleOpTest extends LinearOpMode {

    static final double INCREMENT   = 0.01;     // amount to slew servo each CYCLE_MS cycle
    static final int    CYCLE_MS    =   50;     // period of each cycle

    private Config config = new Config(Config.configFile);

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();

    private ButtonHelper helper;


    VuforiaLocalizer vuforia;


    @Override
    public void runOpMode() {

        helper = new ButtonHelper(gamepad1);

        JewelArm jewelArm = new JewelArm();
        GlyphArm glyphArm = new GlyphArm();
        DriveTrain driveTrain = new DriveTrain();
        VuMarkID vuMarkID = new VuMarkID();

        jewelArm.init(hardwareMap,config);
        glyphArm.init(hardwareMap,config);
        driveTrain.init(hardwareMap,config);
        vuMarkID.init(hardwareMap,config);


        RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.UNKNOWN;


        // Wait for the start button
        telemetry.addData(">", "Press Start to Servo test for arm and container." );
        telemetry.update();
        vuMarkID.activate();

        waitForStart();
        runtime.reset();

        TelemetryWrapper.init(telemetry,8);
        TelemetryWrapper.setLine(6, "arm_min="+jewelArm.MIN_POS_ARM+"arm_max="+jewelArm.MAX_POS_ARM);
        TelemetryWrapper.setLine(7, "container: min="+glyphArm.MIN_POS+" max="+glyphArm.MAX_POS
                +" span_left="+glyphArm.CONTAINER_SPAN_LEFT+" span_right="+glyphArm.CONTAINER_SPAN_RIGHT);

        // Scan servo till stop pressed.
        while(opModeIsActive()){

            if(vuMark == RelicRecoveryVuMark.UNKNOWN) {
                vuMark = vuMarkID.getVuMark();
                if (vuMark != RelicRecoveryVuMark.UNKNOWN) {
                    TelemetryWrapper.setLine(0,"VuMark: " + vuMark + "  visible; Pose"+ format(vuMarkID.pose));
                    vuMarkID.deactivate();
                } else {
                    TelemetryWrapper.setLine(0,"VuMark not visible");
                }
            }

            helper.update();

            // control for container up and down
            if(helper.pressed(dpad_up)){
                glyphArm.moveUp();
                TelemetryWrapper.setLine(1,"Container Move Down at speed: "+ -1*glyphArm.LIFT_POWER);
            } else if(helper.pressed(dpad_down)){
                glyphArm.moveDown();
                TelemetryWrapper.setLine(1,"Container Move Down at speed: "+ glyphArm.LIFT_POWER);
            } else {
                glyphArm.stop();
                TelemetryWrapper.setLine(1,"Container Stopped!");
            }


            //control for container turn back and forward
            if(helper.pressed(dpad_left)){
                glyphArm.turnContainer(-1*glyphArm.CONTAINER_TURN_SPEED);
                TelemetryWrapper.setLine(2,"Container Turn Back at speed "+ -1*glyphArm.CONTAINER_TURN_SPEED
                        + "Position: left=" + glyphArm.getLeftPosition() + "right="+ glyphArm.getRightPosition());
            } else if(helper.pressed(dpad_right)){
                glyphArm.turnContainer(glyphArm.CONTAINER_TURN_SPEED);
                TelemetryWrapper.setLine(2,"Container Turn Forward at speed "+ glyphArm.CONTAINER_TURN_SPEED
                        + "Position: left=" + glyphArm.getLeftPosition() + "right="+ glyphArm.getRightPosition());
            }

            if(helper.pressed(x)) glyphArm.closeContainer();
            else if(helper.pressed(y)) glyphArm.openContainer();

            //control for arm
            if(helper.pressed(a)){
                jewelArm.closeJewelArm();
                TelemetryWrapper.setLine(3,"Servo Color Arm Closed, at position"+jewelArm.MIN_POS_ARM);
            }
            else if(helper.pressed(b)){
                jewelArm.openJewelArm();
                TelemetryWrapper.setLine(3,"Servo Color Arm Opened, at position"+jewelArm.MAX_POS_ARM);
            }


            // Choose to drive using either Tank Mode, or POV Mode
            // Comment out the method that's not used.  The default below is POV.

            // POV Mode uses left stick to go forward, and right stick to turn.
            // - This uses basic math to combine motions and is easier to drive straight.
            double drivey =  gamepad1.left_stick_y;
            double drivex =  gamepad1.left_stick_x;
            double turn  =  -gamepad1.right_stick_x;
            driveTrain.move(drivex,drivey,turn);

            // Show the elapsed game time and wheel power.
            TelemetryWrapper.setLine(4,"Motors in drivex: " + drivex +"drivey: " + drivey+" turn: "+turn);


            TelemetryWrapper.setLine(5, "Press Stop to end test." );


            if(vuMark != RelicRecoveryVuMark.UNKNOWN) sleep(CYCLE_MS);
            idle();
        }

        // Signal done;
        telemetry.addData(">", "Done");
        telemetry.update();
    }

    String format(OpenGLMatrix transformationMatrix) {
        return (transformationMatrix != null) ? transformationMatrix.formatAsTransform() : "null";
    }
}
