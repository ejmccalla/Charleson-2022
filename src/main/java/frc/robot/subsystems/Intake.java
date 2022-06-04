package frc.robot.subsystems;

import edu.wpi.first.networktables.NTSendable;
import edu.wpi.first.networktables.NTSendableBuilder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.REVLibError;
import frc.robot.Constants.INTAKE;


/**
* The Drivetrain class is designed to use the command-based programming model and extends the SubsystemBase class.
* @see {@link edu.wpi.first.wpilibj2.command.SubsystemBase}
*/
public class Intake extends SubsystemBase implements NTSendable {

    // Hardware
    private final CANSparkMax mNeo550;

    private double mMotorDutyCycle;


    //-----------------------------------------------------------------------------------------------------------------
    /*                                                PUBLIC METHODS                                                 */
    //-----------------------------------------------------------------------------------------------------------------


    /**
     * 
     */
    public void EnableIntake () {
        mNeo550.set( mMotorDutyCycle );
    }

    /**
     * 
     */
    public void DisableIntake () {
        mNeo550.set( 0.0 );
    }


    //-----------------------------------------------------------------------------------------------------------------
    /*                                                PRIVATE METHODS                                                */
    //-----------------------------------------------------------------------------------------------------------------


    /**
    * This method will set the motor duty cycle and is used by the live window sendable.
    *
    * @param dc double Motor duty cycle
    */
    private void SetMotorDutyCycle ( double dc ) {
        mMotorDutyCycle = dc;
    }

    /**
    * This method will get the motor duty cycle and is used by the live window sendable.
    * 
    * @return double The value of the motor duty cycle
    */
    private double GetMotorDutyCycle () {
        return mMotorDutyCycle;
    }

       
    /**
    * This method will intialize the motor controller.
    */ 
    private void ResetMotorController () {
        REVLibError error;
        short faults;

        // Log any sticky faults
        faults = mNeo550.getStickyFaults();
        if ( faults != 0 ) {
            //mLogger.warn( "Clearing SparkMax [{}] sticky faults: [{}]", sparkMax.getDeviceId(), faults );
            error = mNeo550.clearFaults();
            if ( error != REVLibError.kOk ) {
                //mLogger.error( "Could not clear sticky faults due to EC: [{}]", canError.toString() );
            }  
        }

        // Restore factory defaults
        error = mNeo550.restoreFactoryDefaults();
        if ( error != REVLibError.kOk ) {
            //mLogger.error( "Could not factory reset SparkMax [{}] due to EC: [{}]", sparkMax.getDeviceId(), canError.toString() );
        }  
        // canError = sparkMax.enableVoltageCompensation( 12.0 );
        // if ( canError != REVLibError.kOk ) {
        //     //mLogger.error( "Could not set SparkMax [{}] voltage compensation due to EC: [{}]", sparkMax.getDeviceId(), canError.toString() );
        // }

        // Set idle to coast
        error = mNeo550.setIdleMode( CANSparkMax.IdleMode.kCoast );
        if ( error != REVLibError.kOk ) {
            //mLogger.error( "Could not set SparkMax [{}] idle mode due to EC: [{}]", sparkMax.getDeviceId(), canError.toString() );
        }        

        // Set 10A stall and free speed current limit
        error = mNeo550.setSmartCurrentLimit( 10, 10 );

    }


    //-----------------------------------------------------------------------------------------------------------------
    /*                                        CLASS CONSTRUCTOR AND OVERRIDES                                        */
    //-----------------------------------------------------------------------------------------------------------------
    /**
    * The constructor for the Intake class.
    */ 
    public Intake () {
        mNeo550 = new CANSparkMax( INTAKE.CAN_ID, MotorType.kBrushless );
        ResetMotorController();
    }


    /**
    * We are overriding the initSendable and using it to send information back-and-forth between the robot program and
    * the user PC for live PID tuning purposes.
    * 
    * @param builder NTSendableBuilder This is inherited from SubsystemBase
    */
    @Override
    public void initSendable ( NTSendableBuilder builder ) {
        builder.setSmartDashboardType( "Intake Tuning" );
        builder.setActuator( true );
        builder.setSafeState( this::DisableIntake );
        builder.addDoubleProperty( "Duty Cycle", this::GetMotorDutyCycle, this::SetMotorDutyCycle );
    }

}
