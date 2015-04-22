package io.github.nickelme.Physics_Sandbox;

import com.badlogic.gdx.controllers.*;

/** Button and axis indices for the Sony Playstation 4 Controller (OEM).
 * The default being for Windows based systems. (Only tested on Windows 8.1)
 * DS4Linux used on linux based systems. (Only tested on Ubuntu)
 * TODO Add OSX and Android indices
 * @author Joelo4 */

public class DS4 {
    public static final int BUTTON_CROSS = 1;
    public static final int BUTTON_SQUARE = 0;
    public static final int BUTTON_TRIANGLE = 3;
    public static final int BUTTON_CIRCLE = 2;
    public static final int BUTTON_PS = 12;
    public static final int BUTTON_OPTIONS = 9;
    public static final int BUTTON_SHARE = 8;
    public static final int BUTTON_TOUCHPAD = 13;
    public static final PovDirection BUTTON_DPAD_UP = PovDirection.north;
    public static final PovDirection BUTTON_DPAD_DOWN = PovDirection.south;
    public static final PovDirection BUTTON_DPAD_RIGHT = PovDirection.east;
    public static final PovDirection BUTTON_DPAD_LEFT = PovDirection.west;
    public static final int BUTTON_L1 = 4;
    public static final int BUTTON_L2 = 6;
    public static final int BUTTON_L3 = 10;
    public static final int BUTTON_R1 = 5;
    public static final int BUTTON_R2 = 7;
    public static final int BUTTON_R3 = 11;
    public static final int AXIS_LEFT_X = 3;// -1 is left, 1 is right
    public static final int AXIS_LEFT_Y = 2;// -1 is up, 1 is down
    public static final int AXIS_R2 = 4; // -1 is released, 1 is pressed
    public static final int AXIS_RIGHT_X = 1;// -1 is left, 1 is right
    public static final int AXIS_RIGHT_Y = 0;// -1 is up, 1 is down
    public static final int AXIS_L2 = 5; // -1 is released, 1 is pressed
}

class DS4Linux {
	public static final int BUTTON_CROSS = 1;
    public static final int BUTTON_SQUARE = 0;
    public static final int BUTTON_TRIANGLE = 3;
    public static final int BUTTON_CIRCLE = 2;
    public static final int BUTTON_PS = 12;
    public static final int BUTTON_OPTIONS = 9;
    public static final int BUTTON_SHARE = 8;
    public static final int BUTTON_TOUCHPAD = 13;
    public static final PovDirection BUTTON_DPAD_UP = PovDirection.north;
    public static final PovDirection BUTTON_DPAD_DOWN = PovDirection.south;
    public static final PovDirection BUTTON_DPAD_RIGHT = PovDirection.east;
    public static final PovDirection BUTTON_DPAD_LEFT = PovDirection.west;
    public static final int BUTTON_L1 = 4;
    public static final int BUTTON_L2 = 6;
    public static final int BUTTON_L3 = 10;
    public static final int BUTTON_R1 = 5;
    public static final int BUTTON_R2 = 7;
    public static final int BUTTON_R3 = 11;
    public static final int AXIS_LEFT_X = 0;// -1 is left, 1 is right
    public static final int AXIS_LEFT_Y = 1;// -1 is up, 1 is down
    public static final int AXIS_R2 = 4; // -1 is released, 1 is pressed
    public static final int AXIS_RIGHT_X = 2;// -1 is left, 1 is right
    public static final int AXIS_RIGHT_Y = 5;// -1 is up, 1 is down
    public static final int AXIS_L2 = 3; // -1 is released, 1 is pressed
}
