package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.Constants.DRIVETRAIN;
import frc.robot.lib.drivers.TalonSRX;
import frc.robot.lib.drivers.VictorSPX;
import frc.robot.lib.drivers.ADIS16470;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.PneumaticsModuleType;

/**
* The Drivetrain class is designed to use the command-based programming model and extends the SubsystemBase class.
* @see {@link edu.wpi.first.wpilibj2.command.SubsystemBase}
*/
public class Drivetrain extends SubsystemBase {

    // Hardware
    private final WPI_TalonSRX mLeftMaster;
    private final WPI_VictorSPX mLeftFollower_1;
    private final WPI_VictorSPX mLeftFollower_2;
    private final WPI_TalonSRX mRightMaster;
    private final WPI_VictorSPX mRightFollower_1;
    private final WPI_VictorSPX mRightFollower_2;
    public final DoubleSolenoid mShifter;
    private final ADIS16470 mIMU;

    // Drive conrol (both open and closed loop)
    public DifferentialDrive mDifferentialDrive;
    private MotorControllerGroup mLeftMotorControllerGroup;
    private MotorControllerGroup mRightMotorControllerGroup;

    // State variables
    private boolean mIsReversedDirection;
    private boolean mIsHighGear;
    private boolean mIsBrakeMode;


    //-----------------------------------------------------------------------------------------------------------------
    /*                                                PUBLIC METHODS                                                 */
    //-----------------------------------------------------------------------------------------------------------------

    /**
    * This method will set determine the direction of the drive motors.
    *
    * @param wantsReversed boolean True if the driver wants the direction reversed, false otherwise
    */ 
    public void SetReversedDirection ( boolean wantsReversedDirection ) {
        if ( wantsReversedDirection != mIsReversedDirection ) {
            mIsReversedDirection = wantsReversedDirection;
            mLeftMaster.setInverted( !mIsReversedDirection );
            mLeftFollower_1.setInverted( !mIsReversedDirection );
            mLeftFollower_2.setInverted( !mIsReversedDirection );
            mRightMaster.setInverted( !mIsReversedDirection) ;
            mRightFollower_1.setInverted( !mIsReversedDirection );
            mRightFollower_2.setInverted( !mIsReversedDirection );
        }
    }

    /**
    * This method will set the gearing of the transmission.
    *
    * @param wantsHighGear boolean True if the driver wants high gear, false for low gear
    */ 
    public void SetHighGear ( boolean wantsHighGear ) {
        if ( wantsHighGear && !mIsHighGear ) {
            mIsHighGear = wantsHighGear;
            mShifter.set( DoubleSolenoid.Value.kForward );

        } else if ( !wantsHighGear && mIsHighGear ) {
            mIsHighGear = wantsHighGear;
            mShifter.set( DoubleSolenoid.Value.kReverse );

        }
    }

    /**
    * This method will set the neutral mode of the motor controllers.
    *
    * @param wantsBrake boolean True if the driver wants brake, false for coast
    */ 
    public void SetBrakeMode ( boolean wantsBrake ) {
        if ( wantsBrake && !mIsBrakeMode ) {
            mIsBrakeMode = wantsBrake;
            mLeftMaster.setNeutralMode( NeutralMode.Brake );
            mLeftFollower_1.setNeutralMode( NeutralMode.Brake );
            mLeftFollower_2.setNeutralMode( NeutralMode.Brake );
            mRightMaster.setNeutralMode( NeutralMode.Brake );
            mRightFollower_1.setNeutralMode( NeutralMode.Brake );
            mRightFollower_2.setNeutralMode( NeutralMode.Brake );

        } else if ( !wantsBrake && mIsBrakeMode ) {
            mIsBrakeMode = wantsBrake;
            mLeftMaster.setNeutralMode( NeutralMode.Coast );
            mLeftFollower_1.setNeutralMode( NeutralMode.Coast );
            mLeftFollower_2.setNeutralMode( NeutralMode.Coast );
            mRightMaster.setNeutralMode( NeutralMode.Coast );
            mRightFollower_1.setNeutralMode( NeutralMode.Coast );
            mRightFollower_2.setNeutralMode( NeutralMode.Coast );

        }
    }

    /**
    * This method will return the state of the reversed mode 
    *
    * @return boolean True if the direction is reversed, false otherwise
    */
    public boolean IsReversedDirection () {
        return mIsReversedDirection;
    }

    /**
    * This method will return the state of the shifting transmission 
    *
    * @return boolean True if the transmission is in high-gear, false for low-gear
    */
    public boolean IsHighGear () {
        return mIsHighGear;
    }

    /**
    * This method will return the state of motor controllers neutral mode 
    *
    * @return boolean True for brake, false for coast
    */
    public boolean IsBrakeMode () {
        return mIsBrakeMode;
    }

    /**
    * This method will set the output based on the driver inputs and the reversed direction state.
    */
    public void SetOpenLoopOutput ( double throttle, double turn, boolean quickTurn ) {
        if ( mIsReversedDirection ) {
            mDifferentialDrive.curvatureDrive( throttle, -turn, quickTurn );
        } else {
            mDifferentialDrive.curvatureDrive( throttle, turn, quickTurn );
        }
    }

    /**
    * This method will set the output based on a motor voltage.
    */
    public void SetOpenLoopOutput( double leftVolts, double rightVolts ) {
        mLeftMotorControllerGroup.setVoltage( leftVolts );
        mRightMotorControllerGroup.setVoltage( -rightVolts );
        mDifferentialDrive.feed();
      }


    //-----------------------------------------------------------------------------------------------------------------
    /*                                                PRIVATE METHODS                                                */
    //-----------------------------------------------------------------------------------------------------------------

    /**
    * This method will initialize the Drivetrain subsystem.
    */
    private void Initialize () {
        ResetMotorControllers();
        ResetSensors();
        ResetState();
    }

    /**
    * This method will reset senor and vision controller information.
    */ 
    private void ResetMotorControllers () {
        TalonSRX.ConfigureTalonSRX( mLeftMaster );
        TalonSRX.CTREMagEncoderConfig( mLeftMaster );
        // mLeftMaster.setSensorPhase();
        // mLeftMaster.configSelectedFeedbackCoefficient(coefficient)
        VictorSPX.ConfigureVictorSPX( mLeftFollower_1 );
        VictorSPX.ConfigureVictorSPX( mLeftFollower_2 );
        TalonSRX.ConfigureTalonSRX( mRightMaster );
        TalonSRX.CTREMagEncoderConfig( mRightMaster );
        // mRightMaster.setSensorPhase();
        // mRightMaster.configSelectedFeedbackCoefficient(coefficient)
        VictorSPX.ConfigureVictorSPX( mRightFollower_1 );
        VictorSPX.ConfigureVictorSPX( mRightFollower_2 );
    }

    /**
    * This method will reset senor and vision controller information.
    */ 
    private void ResetSensors () {
        mLeftMaster.setSelectedSensorPosition( 0.0 );
        mRightMaster.setSelectedSensorPosition( 0.0 );
    }


    /**
    * This method will reset all of the internal states.
    */ 
    private void ResetState () {
        mIsHighGear = false;
        SetHighGear( true );
        mIsBrakeMode = false;
        SetBrakeMode( true );
        mIsReversedDirection = true;
        SetReversedDirection( false );
    }


    //-----------------------------------------------------------------------------------------------------------------
    /*                                        CLASS CONSTRUCTOR AND OVERRIDES                                        */
    //-----------------------------------------------------------------------------------------------------------------


    /**
    * The constructor for the Drivetrain class.
    */ 
    public Drivetrain () {
        mLeftMaster = new WPI_TalonSRX( DRIVETRAIN.LEFT_MASTER_ID );
        mLeftFollower_1 = new WPI_VictorSPX( DRIVETRAIN.LEFT_FOLLOWER_1_ID );
        mLeftFollower_2 = new WPI_VictorSPX( DRIVETRAIN.LEFT_FOLLOWER_2_ID );
        mRightMaster = new WPI_TalonSRX( DRIVETRAIN.RIGHT_MASTER_ID );
        mRightFollower_1 = new WPI_VictorSPX( DRIVETRAIN.RIGHT_FOLLOWER_1_ID );
        mRightFollower_2 = new WPI_VictorSPX( DRIVETRAIN.RIGHT_FOLLOWER_2_ID );
        mShifter = new DoubleSolenoid( PneumaticsModuleType.CTREPCM, DRIVETRAIN.HIGH_GEAR_SOLENOID_ID, 
                                                     DRIVETRAIN.LOW_GEAR_SOLENOID_ID );
        mIMU = new ADIS16470(); 


        mLeftMotorControllerGroup = new MotorControllerGroup( mLeftMaster, mLeftFollower_1, mLeftFollower_2 );
        mRightMotorControllerGroup = new MotorControllerGroup( mRightMaster, mRightFollower_1, mRightFollower_2 );
        mDifferentialDrive = new DifferentialDrive( mLeftMotorControllerGroup, mRightMotorControllerGroup );
        Initialize();
    }


    /**
    * The subsystem periodic method gets called by the CommandScheduler at the very beginning of each robot loop. All
    * sensor readings should be updated in this method.
    * @see {@link edu.wpi.first.wpilibj2.command.CommandScheduler#run}
    */ 
    @Override
    public void periodic () {
    
    }

}
