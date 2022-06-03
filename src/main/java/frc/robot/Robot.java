// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.util.datalog.DataLog;
import edu.wpi.first.util.datalog.DoubleLogEntry;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DriverStation;


/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

    private static final RobotContainer mRobotContainer = new RobotContainer();
    private static Timer timer = new Timer();
    DoubleLogEntry imuHeading;
    DoubleLogEntry imuTemp;

    public Robot() {
        // Set the loop rate to 5ms since the IMU data will be sampled every 5ms by the 
        // robot pose estimator.
        super(0.005); 
    }

    @Override
    public void robotInit() {
        LiveWindow.disableAllTelemetry(); // This only serves to chew up unnecessary CPU cylcles, disable it

        // Use the new data logger to log the IMU heading and temperature. Be sure to always use a USB thumb drive for
        // logging. To do this, be sure to format the thumb drive with a RoboRIO compatible FS (like FAT32) and simply
        // plug into one of the 2 USB ports on the RoboRIO.
        // See https://docs.wpilib.org/en/stable/docs/software/telemetry/datalog.html
        DataLogManager.start();
        DataLogManager.logNetworkTables( false );
        DataLog log = DataLogManager.getLog();
        imuHeading = new DoubleLogEntry(log, "/sensors/heading");
        imuTemp = new DoubleLogEntry(log, "/sensors/temp");
        timer.start();
    }

    @Override
    public void robotPeriodic() {}

    @Override
    public void autonomousInit() {}

    @Override
    public void autonomousPeriodic() {}

    @Override
    public void teleopInit() {}

    @Override
    public void teleopPeriodic() {}

    @Override
    public void disabledInit() {}

    @Override
    public void disabledPeriodic() {
        // For competition code, the IMU calibration routine should be continuously called here to use the "latest and
        // greatest" data from the IMU's CBE. For debug and understanding, here we use a timer to simulate the amount
        // of time spent disabled before moving to the autoInit state without actually having to enable
        // autonomous/teleop from the DS.
        if ( timer.hasElapsed( 120 ) ) {
            mRobotContainer.mIMU.calibrate();
            mRobotContainer.mIMU.reset();
            DriverStation.reportWarning( "ADIS16470 IMU recalibrated!", false );
            timer.stop();
            timer.reset();
        }
        imuHeading.append( mRobotContainer.mIMU.getAngle() );
        imuTemp.append( mRobotContainer.mIMU.getTemp() );
    }

    @Override
    public void testInit() {}

    @Override
    public void testPeriodic() {}

    @Override
    public void simulationInit() {}

    @Override
    public void simulationPeriodic() {}

}