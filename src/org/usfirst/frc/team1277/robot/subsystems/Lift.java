package org.usfirst.frc.team1277.robot.subsystems;

import org.usfirst.frc.team1277.robot.Constants;
import org.usfirst.frc.team1277.robot.RobotMap;
import org.usfirst.frc.team1277.robot.commands.LiftHeightTracking;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Lift extends Subsystem {

    private final TalonSRX liftMotor = RobotMap.liftMotor1;
    private final DigitalInput lowerLimit = RobotMap.liftLowerLimit;
    private final Servo gearShifter = RobotMap.liftGearShifter;
    private final Relay liftLocker = RobotMap.liftLocker;

    private final double LOW_GEAR = 0.35, HIGH_GEAR = 0.65, MAX_HEIGHT = 36000, HOLD_SPEED = 0.1, MAX_UPWARD_SPEED = 1.0, MAX_DOWNWARD_SPEED = -0.6;
	private boolean highGear = false;
    private final double P_GAIN = 0.6, I_GAIN = 0.0, D_GAIN = 0.1, F_GAIN = 0.0;
    
    public Lift() {
    	liftMotor.config_kP(Constants.PIDLoopIdx, P_GAIN, Constants.timeoutMs);
    	liftMotor.config_kI(Constants.PIDLoopIdx, I_GAIN, Constants.timeoutMs);
    	liftMotor.config_kD(Constants.PIDLoopIdx, D_GAIN, Constants.timeoutMs);
    	liftMotor.config_kF(Constants.PIDLoopIdx, F_GAIN, Constants.timeoutMs);
    	liftMotor.configNominalOutputForward(HOLD_SPEED, Constants.timeoutMs);
    	liftMotor.configNominalOutputReverse(HOLD_SPEED, Constants.timeoutMs);
    	liftMotor.configPeakOutputForward(MAX_UPWARD_SPEED, Constants.timeoutMs);
    	liftMotor.configPeakOutputReverse(MAX_DOWNWARD_SPEED, Constants.timeoutMs);
    	gearShifter.set(LOW_GEAR);
    	SmartDashboard.putString("Gear", "Low");
    }

    public void lift(double speed) {
    	if (!lowerLimit.get() && speed < 0) speed = 0;
    	if (highGear && speed < MAX_DOWNWARD_SPEED) speed = MAX_DOWNWARD_SPEED;
    	trackHeight();
    	liftMotor.set(ControlMode.PercentOutput, speed);
    }
    
    public void liftHold() {
    	lift(HOLD_SPEED);
    }
    
    public void liftToPosition(double position) {
    	trackHeight();
    	liftMotor.set(ControlMode.Position, position);
    }
    
    public void positionCorrections() {
    	if (!lowerLimit.get()) liftMotor.setSelectedSensorPosition(0, Constants.PIDLoopIdx, Constants.timeoutMs);
    }
    
    public void trackHeight() {
    	positionCorrections();
    	SmartDashboard.putNumber("Height", liftMotor.getSelectedSensorPosition(0));
    }
    
    public void shift() {
    	if (highGear) gearShifter.set(LOW_GEAR);
    	else gearShifter.set(HIGH_GEAR);
    	highGear = !highGear;
    	if (highGear) SmartDashboard.putString("Gear", "High");
    	else SmartDashboard.putString("Gear", "Low");
    }
    
    public void lock() {
    	liftLocker.set(Relay.Value.kForward);
    	//SmartDashboard.putString("Locker", "On");
    }
    
    public void unlock() {
    	liftLocker.set(Relay.Value.kOff);
    }

    public void initDefaultCommand() {
    	setDefaultCommand(new LiftHeightTracking());
    }
    
    public double getHeight() {
    	return liftMotor.getSelectedSensorPosition(0);
    }
    
    public double getMAX_HEIGHT() {
		return MAX_HEIGHT;
	}
    
}