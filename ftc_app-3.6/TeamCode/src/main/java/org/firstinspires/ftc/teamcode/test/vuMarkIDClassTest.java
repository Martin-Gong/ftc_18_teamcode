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

package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.teamcode.VuMarkID;
import org.firstinspires.ftc.teamcode.util.ButtonHelper;
import org.firstinspires.ftc.teamcode.util.Config;
import org.firstinspires.ftc.teamcode.util.telemetry.TelemetryWrapper;

import static org.firstinspires.ftc.teamcode.util.ButtonHelper.a;
import static org.firstinspires.ftc.teamcode.util.ButtonHelper.b;
import static org.firstinspires.ftc.teamcode.util.ButtonHelper.dpad_down;
import static org.firstinspires.ftc.teamcode.util.ButtonHelper.dpad_left;
import static org.firstinspires.ftc.teamcode.util.ButtonHelper.dpad_right;
import static org.firstinspires.ftc.teamcode.util.ButtonHelper.dpad_up;
import static org.firstinspires.ftc.teamcode.util.ButtonHelper.x;
import static org.firstinspires.ftc.teamcode.util.ButtonHelper.y;

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
@TeleOp(name = "vuMark ID Class Test", group = "Test")
//@Disabled
public class vuMarkIDClassTest extends LinearOpMode {

    static final double INCREMENT   = 0.01;     // amount to slew servo each CYCLE_MS cycle
    static final int    CYCLE_MS    =   50;     // period of each cycle

    static final Config config = new Config(Config.configFile);

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();

    private ButtonHelper helper;


    @Override
    public void runOpMode() {


        VuMarkID vuMarkID = new VuMarkID();


        vuMarkID.init(hardwareMap,config);


        RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.UNKNOWN;


        // Wait for the start button
        telemetry.addData(">", "Press Start to test vuMarkID test." );
        telemetry.update();

        vuMarkID.activate();

        waitForStart();
        runtime.reset();

        TelemetryWrapper.init(telemetry,6);

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
