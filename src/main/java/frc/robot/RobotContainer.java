package frc.robot;

import frc.robot.Constants.HARDWARE;
import edu.wpi.first.wpilibj.PowerDistribution;
import frc.robot.lib.drivers.ADIS16470;


public class RobotContainer {

    // Hardware
    public final PowerDistribution mPDP;
    public final ADIS16470 mIMU;
    
    // Subsystems
    


    //-----------------------------------------------------------------------------------------------------------------
    /*                                                PUBLIC METHODS                                                 */
    //-----------------------------------------------------------------------------------------------------------------



    //-----------------------------------------------------------------------------------------------------------------
    /*                                                PRIVATE METHODS                                                */
    //-----------------------------------------------------------------------------------------------------------------



    //-----------------------------------------------------------------------------------------------------------------
    /*                                        CLASS CONSTRUCTOR AND OVERRIDES                                        */
    //-----------------------------------------------------------------------------------------------------------------


    public RobotContainer () {
        mPDP = new PowerDistribution( HARDWARE.PDP_ID, PowerDistribution.ModuleType.kCTRE );
        mIMU = new ADIS16470(); // This will take ~35 seconds to initialize AND THE ROBOT CANNOT BE MOVED

    }


}
