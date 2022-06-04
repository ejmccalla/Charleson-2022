package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj.PowerDistribution;
import frc.robot.Constants.HARDWARE;
import frc.robot.Constants.DRIVER;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.commands.TeleopDrive;

public class RobotContainer {

    private static RobotContainer mRobotContainer = new RobotContainer();

    // Hardware
    public final PowerDistribution mPDP;
    private final Joystick mDriverJoystickThrottle;
    private final JoystickButton mDriverJoystickThrottleButton;
    private final Joystick mDriverJoystickTurn;
    private final JoystickButton mDriverJoystickTurnButton;

    
    // Subsystems
    public Drivetrain mDrivetrain;
    public Intake mIntake;


    //-----------------------------------------------------------------------------------------------------------------
    /*                                                PUBLIC METHODS                                                 */
    //-----------------------------------------------------------------------------------------------------------------
	public static RobotContainer GetInstance() {
		return mRobotContainer;
	}


    //-----------------------------------------------------------------------------------------------------------------
    /*                                                PRIVATE METHODS                                                */
    //-----------------------------------------------------------------------------------------------------------------

    /**
    * This method will configure the joysticks and buttons. This means commands and their behaviours will be assigned
    * to the driver/operators controls.
    */
    private void ConfigureButtonBindings () {
        mDriverJoystickThrottleButton.whenPressed( new InstantCommand( () -> mDrivetrain.SetHighGear( !mDrivetrain.IsHighGear() ), mDrivetrain ) );
        //mDriverJoystickTurnButton.whenPressed( new InstantCommand( () -> mDrivetrain.SetReversedDirection( !mDrivetrain.IsReversedDirection() ), mDrivetrain ) );
        mDriverJoystickTurnButton.whenPressed( new InstantCommand( () -> mIntake.EnableIntake(), mIntake ) );
    }

    /**
    * This method will intialize the RobotContainer class by setting the local state variables, configuring the buttons
    * and joysticks, setting the default subsystem commands, setting up the autonomous chooser, and clearing faults in
    * the PCM and PDP.
    */
    private void Initialize () {
        ConfigureButtonBindings();
        mDrivetrain.setDefaultCommand( new TeleopDrive( mDrivetrain, mDriverJoystickThrottle, mDriverJoystickTurn ) );
        mIntake.setDefaultCommand( new InstantCommand( () -> mIntake.DisableIntake(), mIntake ) );
    }


    //-----------------------------------------------------------------------------------------------------------------
    /*                                        CLASS CONSTRUCTOR AND OVERRIDES                                        */
    //-----------------------------------------------------------------------------------------------------------------


    public RobotContainer () {
        mPDP = new PowerDistribution( HARDWARE.PDP_ID, PowerDistribution.ModuleType.kCTRE );
        mDrivetrain = new Drivetrain();
        mIntake = new Intake();

        mDriverJoystickThrottle = new Joystick( DRIVER.JOYSTICK_THROTTLE );
        mDriverJoystickThrottleButton = new JoystickButton( mDriverJoystickThrottle, 1 );
        mDriverJoystickTurn = new Joystick( DRIVER.JOYSTICK_TURN );
        mDriverJoystickTurnButton = new JoystickButton( mDriverJoystickTurn, 1 );
        Initialize();

    
    }

}
