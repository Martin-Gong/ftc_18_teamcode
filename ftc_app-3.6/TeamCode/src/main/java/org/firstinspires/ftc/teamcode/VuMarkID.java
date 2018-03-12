package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Hardware;

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

import org.firstinspires.ftc.teamcode.util.Config;
import org.firstinspires.ftc.teamcode.util.telemetry.TelemetryWrapper;

/**
 * Created by thomas on 2018-2-18.
 */

public class VuMarkID {
    public  RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.UNKNOWN;
    private VuforiaLocalizer vuforia;
    private VuforiaTrackable relicTemplate;
    private VuforiaTrackables relicTrackables;
    public  OpenGLMatrix pose;

    public void init(HardwareMap hardwareMap, Config config){
        vuMark = RelicRecoveryVuMark.UNKNOWN;

        // start of VuMarkID
        /*
         * To start up Vuforia, tell it the view that we wish to use for camera monitor (on the RC phone);
         * If no camera monitor is desired, use the parameterless constructor instead (commented out below).
         */
        VuforiaLocalizer.Parameters parameters;
        boolean isDisplayView = config.getBoolean("is_display_camera_view",false);
        if(isDisplayView) {
            int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
            parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        }
        else  parameters = new VuforiaLocalizer.Parameters();  // no camera displayed

        // OR...  Do Not Activate the Camera Monitor View, to save power
        // VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        /*
         * IMPORTANT: You need to obtain your own license key to use Vuforia. The string below with which
         * 'parameters.vuforiaLicenseKey' is initialized is for illustration only, and will not function.
         * A Vuforia 'Development' license key, can be obtained free of charge from the Vuforia developer
         * web site at https://developer.vuforia.com/license-manager.
         *
         * Vuforia license keys are always 380 characters long, and look as if they contain mostly
         * random data. As an example, here is a example of a fragment of a valid key:
         *      ... yIgIzTqZ4mWjk9wd3cZO9T1axEqzuhxoGlfOOI2dRzKS4T0hQ8kT ...
         * Once you've obtained a license key, copy the string from the Vuforia web site
         * and paste it in to your code onthe next line, between the double quotes.
         */
        parameters.vuforiaLicenseKey = "Ac4SO4P/////AAAAmYo+Dd1E4komrpVteq5yhwyKezLLi2tGgobkZ33Cw+FfGBDlxL282Ow6UJycv6OKKGKtALv6scAq+4cHivE+XPOu6008QHCI0P6yx8X9vb8IKrLWM7dC2ZaWp1Em6rVZFS9q/SnAWVjU1J2oZFNKK5t2jsfpcFV+vN+ZCyNXT+kBsk8mLKwesanwvrCoja1i4Ycs/8FJt7G7EVL2H+wQtGH1Q2sy/AGhJRXAiOyZHM97UBhOptoY9trn6omnmlO3/z8Gr+ntJEqXA/GdyHbJkRcI3bG+vxU3fhUsX3W5Gm7dUs3dX2po7Kz1Q38ABtrLuwpJd1abPHZvSt1vrKe8p5JJtk9ABMZcgPXBBL7eOUr6";

        /*
         * We also indicate which camera on the RC that we wish to use.
         * Here we chose the back (HiRes) camera (for greater range), but
         * for a competition robot, the front camera might be more convenient.
         */

        boolean isFrontCamera = config.getBoolean("is_using_front_camera",false);
        if(isFrontCamera) parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
        else parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);
        /**
         * Load the data set containing the VuMarks for Relic Recovery. There's only one trackable
         * in this data set: all three of the VuMarks in the game were created from this one template,
         * but differ in their instance id information.
         * @see VuMarkInstanceId
         */
        relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary

        // end of VuMarkId initialization
    }

    public RelicRecoveryVuMark getVuMark()
    {
        vuMark = RelicRecoveryVuMark.from(relicTemplate);
        if (vuMark != RelicRecoveryVuMark.UNKNOWN) {

            /* Found an instance of the template. In the actual game, you will probably
             * loop until this condition occurs, then move on to act accordingly depending
             * on which VuMark was visible. */

            /* For fun, we also exhibit the navigational pose. In the Relic Recovery game,
             * it is perhaps unlikely that you will actually need to act on this pose information, but
             * we illustrate it nevertheless, for completeness. */
            pose = ((VuforiaTrackableDefaultListener) relicTemplate.getListener()).getPose();
            //TelemetryWrapper.setLine(0, "VuMark: " + vuMark + "  visible; Pose" + format(pose));
        }
        return vuMark;
    }

    public void activate(){
        relicTrackables.activate();
    }

    public void deactivate(){
        relicTrackables.deactivate();
    }

    public String format(OpenGLMatrix transformationMatrix) {
        return (transformationMatrix != null) ? transformationMatrix.formatAsTransform() : "null";
    }
}
