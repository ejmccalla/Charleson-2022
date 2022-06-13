package frc.robot;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj.PowerDistribution;
import frc.robot.Constants.HARDWARE;
import frc.robot.Constants.DRIVER;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.commands.TeleopDrive;

/**
 * The RobotContainer class contains all of the subsystems, commands, and defines how the users interface with the
 * robot.
 */
public class RobotContainer implements Sendable {

    // Hardware
    public final PowerDistribution mPDP;
    private final Joystick mDriverJoystickThrottle;
    private final JoystickButton mDriverJoystickThrottleButton;
    private final Joystick mDriverJoystickTurn;
    private final JoystickButton mDriverJoystickTurnButton;

    // Subsystems
    public Drivetrain mDrivetrain;
    public Intake mIntake;

    // State
    private double mIntakeDutyCycle = 0.5;


    //-----------------------------------------------------------------------------------------------------------------
    /*                                                PUBLIC METHODS                                                 */
    //-----------------------------------------------------------------------------------------------------------------


    //-----------------------------------------------------------------------------------------------------------------
    /*                                                PRIVATE METHODS                                                */
    //-----------------------------------------------------------------------------------------------------------------

    /**
     * This method will configure the joysticks and buttons. This means commands and their behaviours will be assigned
     * to the driver/operators controls.
     */
    private void ConfigureButtonBindings () {
        mDriverJoystickThrottleButton.whenPressed( 
            new InstantCommand( () -> mDrivetrain.SetHighGear( !mDrivetrain.IsHighGear() ), mDrivetrain ) );
        mDriverJoystickTurnButton.whileHeld( 
            new InstantCommand( () -> mIntake.SetIntakeMotorOutput( mIntakeDutyCycle ), mIntake ) );
        mDriverJoystickTurnButton.whenReleased( 
            new InstantCommand( () -> mIntake.SetIntakeMotorOutput( 0.0 ), mIntake ) );
        }

    /**
     * This method will intialize the RobotContainer class by setting the local state variables, configuring the
     * buttons and joysticks, setting the default subsystem commands, setting up the autonomous chooser, and clearing
     * faults in the PCM and PDP.
     */
    private void Initialize () {
        ConfigureButtonBindings();
        mDrivetrain.setDefaultCommand( new TeleopDrive( mDrivetrain, mDriverJoystickThrottle, mDriverJoystickTurn ) );
    }


    //-----------------------------------------------------------------------------------------------------------------
    /*                                        CLASS CONSTRUCTOR AND OVERRIDES                                        */
    //-----------------------------------------------------------------------------------------------------------------

    /**
     * The constructor for the RobotContainer class.
     */ 
    public RobotContainer () {
        mPDP = new PowerDistribution( HARDWARE.PDP_ID, PowerDistribution.ModuleType.kCTRE );
        mDrivetrain = new Drivetrain();
        mIntake = new Intake();

        mDriverJoystickThrottle = new Joystick( DRIVER.JOYSTICK_THROTTLE );
        mDriverJoystickThrottleButton = new JoystickButton( mDriverJoystickThrottle, 1 );
        mDriverJoystickTurn = new Joystick( DRIVER.JOYSTICK_TURN );
        mDriverJoystickTurnButton = new JoystickButton( mDriverJoystickTurn, 1 );
        Initialize();

        SendableRegistry.addLW( this, "TuningParameters", "TuningParameters" );
    }


    /**
     * This method will set the motor duty cycle and is used by the live window sendable for experimenting and tuning
     * in test mode.
     *
     * @param dutyCycle double Motor duty cycle
     */
    private void SetDutyCycle ( double dutyCycle ) {
        mIntakeDutyCycle = dutyCycle;
    }

    /**
    * This method will get the motor duty cycle and is used by the live window sendable for experimenting and tuning
     * in test mode.
     * 
     * @return double The value of the motor duty cycle
     */
    private double GetDutyCycle () {
        return mIntakeDutyCycle;
    }

    /**
     * We are overriding the initSendable and using it to send information back-and-forth between the robot program and
     * the user PC for exprimenting and tuning purposes.
     * 
     * @param builder SendableBuilder This is inherited from SubsystemBase
     */
    @Override
    public void initSendable ( SendableBuilder builder ) {
        builder.setSmartDashboardType( "TuningParameters" );
        builder.addDoubleProperty( "Intake Duty Cycle", this::GetDutyCycle, this::SetDutyCycle );
    }



}
