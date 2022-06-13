package frc.robot;

public final class Constants {

    public static final class HARDWARE {
        public static final int CTRE_CAN_TIMEOUT_MS = 10;                       // CTRE CAN normal CAN timeout for blocking calls
        public static final int CTRE_CAN_LONG_TIMEOUT_MS = 100;                 // CTRE CAN long CAN timeout for blocking calls
        public static final int PCM_ID = 0;                                     // PCM CAN ID
        public static final int PDP_ID = 0;                                     // PDP CAN ID
    }

    /**
    * These are the constants which are used to map the hardware and define the working bahavior of the driver and
    * operator controls.
    * @see {@link frc.robot.RobotContainer#ConfigureButtonBindings}
    */      
    public static final class DRIVER {
        public static final int JOYSTICK_TURN = 0;                              // Turn joystick port number
        public static final int JOYSTICK_THROTTLE = 1;                          // Throttle joystick port number
        public static final int DRIVER_BUTTON_BOARD = 2;                        // Drive button controller port number
        public static final double QUICKTURN_THRESHOLD = 0.05;                  // Threshold for curvature drive where the quickturn feature is enabled
    }

    /**
    * These are the constants which are used to map the hardware and define the working bahavior of the driver and
    * operator controls.
    * @see {@link frc.robot.RobotContainer#ConfigureButtonBindings}
    */      
    public static final class INTAKE {
        public static final int CAN_ID = 12;                                    // Motor controller CAN ID AND PDP Port number
        public static final double DIAMETER_FT = 0.088;                         // Diameter of the intake roller in feet
        public static final double GEARING_REDUCTION = 5.0;                     // Gearing reduction
    }



    /**
    * These are the constants which are used to map the hardware and define the working bahavior of the the drivetrain
    * subsystem. The Limelight 2 has a horizontal FOV of -29.8 to 29.8 degrees, which means the max error is within
    * this range as well. To drive a mechanism at 25% at max Limelight error, set the P-gain to 0.25 / 29.8.
    * @see {@link frc.robot.subsystems.Drivetrain}
    * @see {@link https://phoenix-documentation.readthedocs.io/en/latest/ch16_ClosedLoop.html#}
    * @see {@link https://docs.limelightvision.io/en/latest/networktables_api.html}
    * @see {@link https://docs.limelightvision.io/en/latest/cs_estimating_distance.html}
    */
    public static final class DRIVETRAIN {
        public static final int LEFT_MASTER_ID = 15;                            // Motor controller CAN ID AND PDP Port number
        public static final int LEFT_FOLLOWER_1_ID = 14;                        // Motor controller CAN ID AND PDP Port number
        public static final int LEFT_FOLLOWER_2_ID = 13;                        // Motor controller CAN ID AND PDP Port number
        public static final int RIGHT_MASTER_ID = 0;                            // Motor controller CAN ID AND PDP Port number
        public static final int RIGHT_FOLLOWER_1_ID = 1;                        // Motor controller CAN ID AND PDP Port number
        public static final int RIGHT_FOLLOWER_2_ID = 2;                        // Motor controller CAN ID AND PDP Port number
        public static final int LOW_GEAR_SOLENOID_ID = 0;                       // PCM port number for low gear shifting
        public static final int HIGH_GEAR_SOLENOID_ID = 1;                      // PCM port number for high gear shifting
    }




}
