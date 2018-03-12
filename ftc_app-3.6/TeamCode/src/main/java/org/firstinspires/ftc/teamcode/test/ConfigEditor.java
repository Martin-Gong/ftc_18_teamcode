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

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.util.ButtonHelper;

import static org.firstinspires.ftc.teamcode.util.ButtonHelper.dpad_down;
import static org.firstinspires.ftc.teamcode.util.ButtonHelper.dpad_up;
import static org.firstinspires.ftc.teamcode.util.ButtonHelper.start;
import static org.firstinspires.ftc.teamcode.util.ButtonHelper.dpad_left;
import static org.firstinspires.ftc.teamcode.util.ButtonHelper.dpad_right;
import static org.firstinspires.ftc.teamcode.util.ButtonHelper.a;
import static org.firstinspires.ftc.teamcode.util.ButtonHelper.b;
import static org.firstinspires.ftc.teamcode.util.ButtonHelper.x;
import static org.firstinspires.ftc.teamcode.util.ButtonHelper.y;
import static org.firstinspires.ftc.teamcode.util.ButtonHelper.left_stick_button;
import static org.firstinspires.ftc.teamcode.util.ButtonHelper.right_stick_button;
import static org.firstinspires.ftc.teamcode.util.ButtonHelper.left_bumper;
import static org.firstinspires.ftc.teamcode.util.ButtonHelper.right_bumper;

import org.firstinspires.ftc.teamcode.util.Config;
import org.firstinspires.ftc.teamcode.util.OrderedProperties;
import org.firstinspires.ftc.teamcode.util.telemetry.TelemetryWrapper;

import java.util.ArrayList;

/**
 * {@link ConfigEditor} illustrates various ways in which telemetry can be
 * transmitted from the robot controller to the driver station. The sample illustrates
 * numeric and text data, formatted output, and optimized evaluation of expensive-to-acquire
 * information. The telemetry {@link Telemetry#log() log} is illustrated by scrolling a poem
 * to the driver station.
 *
 * @see Telemetry
 */
@TeleOp(name = "Concept: Config Editor", group = "Concept")
//@Disabled
public class ConfigEditor extends LinearOpMode  {


    private Config config = new Config(Config.configFile);
    private ButtonHelper helper;
    private int RUNNING_STATUS=0;
    private static final int EDITING = 1;
    private static final int READING = 0;

    @Override public void runOpMode() {


        /* we keep track of how long it's been since the OpMode was started, just
         * to have some interesting data to show */
        OrderedProperties prop = config.getProperties();
        int LINES = prop.stringPropertyNames().size();
        TelemetryWrapper.init(telemetry,LINES);
        TelemetryWrapper.setLine(0,"push start to start");
        helper = new ButtonHelper(gamepad1);


        waitForStart();
        ArrayList<String> strArray = new ArrayList<String> ();
        int line=0;
        for (String key : prop.stringPropertyNames()) {
            TelemetryWrapper.setLineNoRender(line, key + "=" + prop.getProperty(key));
            strArray.add(line, key);
            line++;
        }

        line = 0;
        String key = strArray.get(line);
        TelemetryWrapper.setLine(line,">"+key+"="+ prop.getProperty(key));

        while(opModeIsActive()){
            helper.update();
            if(gamepad1.left_trigger>0.5) RUNNING_STATUS = EDITING;
            switch (RUNNING_STATUS){
                case  READING:
                    if(helper.pressing(dpad_up)&&line>0){
                        key = strArray.get(line);
                        TelemetryWrapper.setLineNoRender(line,key+"="+ prop.getProperty(key));
                        line--;
                        key = strArray.get(line);
                        TelemetryWrapper.setLine(line,">"+key+"="+ prop.getProperty(key));
                    }
                    else if(helper.pressing(dpad_down)&&line<strArray.size()-1){
                        key = strArray.get(line);
                        TelemetryWrapper.setLineNoRender(line,key+"="+ prop.getProperty(key));
                        line++;
                        key = strArray.get(line);
                        TelemetryWrapper.setLine(line,">"+key+"="+ prop.getProperty(key));
                    }
                    break;
                case EDITING:
                    key = strArray.get(line);
                    prop.setProperty(key,EditProperties(line,key,prop.getProperty(key)));
                    config.storeProperties(Config.configFile);
                    RUNNING_STATUS = READING;
                    TelemetryWrapper.setLine(line,">"+key+"="+ prop.getProperty(key));
                    break;
            }

        }
    }

    public String EditProperties(int line ,String propertyName, String propertyValue){
        TelemetryWrapper.setLine(line,"***"+ propertyName + "="+propertyValue);
        StringBuffer newValue= new StringBuffer(propertyValue);
        do {
            helper.update();
            if(helper.pressing(start)&&newValue.length()>0)newValue = newValue.deleteCharAt(newValue.length() - 1);
            else if(helper.pressing(left_stick_button)){newValue.append("0");}
            else if(helper.pressing(dpad_down)) newValue.append("1");
            else if(helper.pressing(dpad_left)) newValue.append("2");
            else if(helper.pressing(dpad_up)) newValue.append("3");
            else if(helper.pressing(dpad_right)) newValue.append("4");
            else if(helper.pressing(x)) newValue.append("5");
            else if(helper.pressing(y)) newValue.append("6");
            else if(helper.pressing(b)) newValue.append("7");
            else if(helper.pressing(a)) newValue.append("8");
            else if(helper.pressing(right_stick_button)) newValue.append("9");
            else if(helper.pressing(left_bumper)) newValue.append("-");
            else if(helper.pressing(right_bumper)) newValue.append(".");
            TelemetryWrapper.setLine(line, "***"+ propertyName + "="+ newValue);
        }while(gamepad1.right_trigger<0.5);
        TelemetryWrapper.setLine(line,  propertyName + "="+ newValue);
        return newValue.toString();
    }
}
