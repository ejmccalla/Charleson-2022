package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.REVLibError;
import frc.robot.Constants.INTAKE;

/**
 * The Intake class is designed to use the command-based programming model and extends the SubsystemBase class.
 * 
 * @see {@link edu.wpi.first.wpilibj2.command.SubsystemBase}
 */
public class Intake extends SubsystemBase {

    // Hardware
    private final CANSparkMax mNeo550;
    private RelativeEncoder mEncoder;

    //-----------------------------------------------------------------------------------------------------------------
    /*                                                PUBLIC METHODS                                                 */
    //-----------------------------------------------------------------------------------------------------------------


    /**
     * This method will set the motor controller output using the given duty cycle.
     * 
     * @param dutyCycle double Motor duty cycle
     */
    public void SetIntakeMotorOutput ( double dutyCycle ) {
        mNeo550.set( dutyCycle );
    }


    //-----------------------------------------------------------------------------------------------------------------
    /*                                                PRIVATE METHODS                                                */
    //-----------------------------------------------------------------------------------------------------------------


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
        error = mNeo550.setSmartCurrentLimit( 20, 20 );

    }

    /**
     * Get the linear velocity of the intake
     */
    private double GetIntakeLinearVelocity () {
        return mEncoder.getVelocity();
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
        SetIntakeMotorOutput( 0.0 );
        mEncoder = mNeo550.getEncoder();
        mEncoder.setVelocityConversionFactor( Math.PI * INTAKE.DIAMETER_FT / 60.0 / INTAKE.GEARING_REDUCTION ); // Rev per Minute to Feet per Second
    }

    /**
    * The subsystem periodic method gets called by the CommandScheduler at the very beginning of each robot loop.  All
    * sensor readings should be updated in this method.
    * @see {@link edu.wpi.first.wpilibj2.command.CommandScheduler#run}
    */ 
    @Override
    public void periodic () {
        SmartDashboard.putNumber("Intake Velocity (FPS)", GetIntakeLinearVelocity() );
    }


}
