package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.util.telemetry.TelemetryWrapper;
import org.firstinspires.ftc.teamcode.util.ButtonHelper;
import org.firstinspires.ftc.teamcode.util.Utils;

import java.util.Map;
import java.util.Set;

import static org.firstinspires.ftc.teamcode.util.ButtonHelper.*;
import static org.firstinspires.ftc.teamcode.util.ButtonHelper.dpad_down;
import static org.firstinspires.ftc.teamcode.util.ButtonHelper.dpad_up;
import static org.firstinspires.ftc.teamcode.util.ButtonHelper.start;

/**
 * ServoPositioner - Tool to move specific servos individually to collect positions
 */

@TeleOp(name="Servo Positioner", group = "Test")
public class ServoPositioner extends OpMode {

    private static final int STATE_CHOOSING = 1;
    private static final int STATE_RUNNING = 2;
    private int state;
    private String servo;
    private String[] servos;
    private Servo s;
    private int chosen;
    private int scroll;
    private double value;
    //private static final int VISIBLE_LINES = 3;
    private static int VISIBLE_LINES = 3;
    private ButtonHelper helper;


    @Override
    public void init() {
        state = STATE_CHOOSING;
        helper = new ButtonHelper(gamepad1);
    }

    @Override
    public void loop() {
        helper.update();
        switch (state) {
            case STATE_CHOOSING:
                if (servos == null) {
                    servos = keys(hardwareMap.servo.entrySet());
                    VISIBLE_LINES = servos.length;
                    TelemetryWrapper.init(telemetry, VISIBLE_LINES+2);
                    TelemetryWrapper.setLine(0, "Choose a servo to move; press START to select");
                    draw();
                } else {

                    if (helper.pressing(dpad_up) && chosen > 0) {
                        TelemetryWrapper.setLine(VISIBLE_LINES+1, "[padup] pressed!");
                        chosen--;
                        if (chosen < scroll) scroll = chosen;
                        draw();
                    }
                    if (helper.pressing(dpad_down) && chosen < servos.length-1) {
                        TelemetryWrapper.setLine(VISIBLE_LINES+1, "[paddown] pressed!");
                        chosen++;
                        if (chosen > scroll + VISIBLE_LINES) scroll = chosen - VISIBLE_LINES;
                        draw();
                    }
                    if (helper.pressing(start)) {
                        TelemetryWrapper.setLine(VISIBLE_LINES+1, "[start] pressed!");
                        servo = servos[chosen];
                        state = STATE_RUNNING;
                    }
                }
                break;
            case STATE_RUNNING:
                if (s == null) {
                    TelemetryWrapper.setLines(2);
                    TelemetryWrapper.setLine(0, "Use the left joystick up/down to control the servo");
                    s = hardwareMap.servo.get(servo);
                    s.setPosition(0);
                }
                value += -0.01 * Utils.constrain(gamepad1.left_stick_y, -1,1);
                value = Utils.constrain(value, 0, 1);
                s.setPosition(value);
                TelemetryWrapper.setLine(1, "Value: " + value);
                break;
            default: break;
        }
    }

    private void draw() {
        for (int i = 0; i < VISIBLE_LINES; i++) {
            String s = servos[scroll+i];
            if (scroll+i == chosen) {
                s = "> " + s;
            }
            TelemetryWrapper.setLine(i+1, s);
        }
    }

    @SuppressWarnings("unchecked")
    private String[] keys(Set<Map.Entry<String, Servo>> set) {
        String[] out = new String[set.size()];
        Object[] entries = set.toArray();
        for (int i = 0; i < set.size(); i++) {
            out[i] = ((Map.Entry<String, ?>)entries[i]).getKey();
        }
        return out;
    }
}
