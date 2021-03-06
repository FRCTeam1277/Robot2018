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

    private boolean highGear;
    
    private final double LOW_GEAR = 0.35, HIGH_GEAR = 0.65, MAX_HEIGHT = 36000;
    
    private final double HIGH_GEAR_HOLD_SPEED = 0.3, HIGH_GEAR_MAX_UPWARD_SPEED = 1.0, HIGH_GEAR_MAX_DOWNWARD_SPEED = -0.2;
    private final double LOW_GEAR_HOLD_SPEED = 0.1, LOW_GEAR_MAX_UPWARD_SPEED = 1.0, LOW_GEAR_MAX_DOWNWARD_SPEED = -0.6;
    
    private final double HIGH_GEAR_P_GAIN = 0.6, HIGH_GEAR_I_GAIN = 0.0, HIGH_GEAR_D_GAIN = 0.5, HIGH_GEAR_F_GAIN = 0.0;
    private final double LOW_GEAR_P_GAIN = 0.6, LOW_GEAR_I_GAIN = 0.0, LOW_GEAR_D_GAIN = 0.1, LOW_GEAR_F_GAIN = 0.0;
    
    public Lift() {
    	
    	//Set Starting Gear
    	highGear = false;
    	if (highGear) gearShifter.set(HIGH_GEAR);
    	else gearShifter.set(LOW_GEAR);
    	if (highGear) SmartDashboard.putString("Gear", "High");
    	else SmartDashboard.putString("Gear", "Low");
    }

    public void lift(double speed) {
    	
    	//Stop Lift at Bottom
    	if (!lowerLimit.get() && speed < 0) speed = 0;
    	
    	trackHeight();
    	
    	//Lift
    	liftMotor.set(ControlMode.PercentOutput, speed);
    }
    
    public void liftHold() {
    	if (highGear) lift(HIGH_GEAR_HOLD_SPEED);
    	else lift(LOW_GEAR_HOLD_SPEED);
    }
    
    public void liftToPosition(double position) {
    	
    	trackHeight();
    	
    	liftMotor.set(ControlMode.Position, position);
    }
    
    public void positionCorrections() {
    	
    	//Zero at Bottom
    	if (!lowerLimit.get()) liftMotor.setSelectedSensorPosition(0, Constants.PIDLoopIdx, Constants.timeoutMs);
    }
    
    public void trackHeight() {
    	positionCorrections();
    	SmartDashboard.putNumber("Height", liftMotor.getSelectedSensorPosition(0));
    }
    
    public void shift() {

    	//Switch Gear
    	highGear = !highGear;
    	if (highGear) gearShifter.set(HIGH_GEAR);
    	else gearShifter.set(LOW_GEAR);
    	if (highGear) SmartDashboard.putString("Gear", "High");
    	else SmartDashboard.putString("Gear", "Low");
    	
    	//Update PID
    	setPID(highGear);
    }
    
    public void setPID(boolean highGear) {
    	if (highGear) {
    		liftMotor.config_kP(Constants.PIDLoopIdx, HIGH_GEAR_P_GAIN, Constants.timeoutMs);
        	liftMotor.config_kI(Constants.PIDLoopIdx, HIGH_GEAR_I_GAIN, Constants.timeoutMs);
        	liftMotor.config_kD(Constants.PIDLoopIdx, HIGH_GEAR_D_GAIN, Constants.timeoutMs);
        	liftMotor.config_kF(Constants.PIDLoopIdx, HIGH_GEAR_F_GAIN, Constants.timeoutMs);
        	liftMotor.configNominalOutputForward(HIGH_GEAR_HOLD_SPEED, Constants.timeoutMs);
        	liftMotor.configNominalOutputReverse(HIGH_GEAR_HOLD_SPEED, Constants.timeoutMs);
        	liftMotor.configPeakOutputForward(HIGH_GEAR_MAX_UPWARD_SPEED, Constants.timeoutMs);
        	liftMotor.configPeakOutputReverse(HIGH_GEAR_MAX_DOWNWARD_SPEED, Constants.timeoutMs);
    	}
    	else {
    		liftMotor.config_kP(Constants.PIDLoopIdx, LOW_GEAR_P_GAIN, Constants.timeoutMs);
        	liftMotor.config_kI(Constants.PIDLoopIdx, LOW_GEAR_I_GAIN, Constants.timeoutMs);
        	liftMotor.config_kD(Constants.PIDLoopIdx, LOW_GEAR_D_GAIN, Constants.timeoutMs);
        	liftMotor.config_kF(Constants.PIDLoopIdx, LOW_GEAR_F_GAIN, Constants.timeoutMs);
        	liftMotor.configNominalOutputForward(LOW_GEAR_HOLD_SPEED, Constants.timeoutMs);
        	liftMotor.configNominalOutputReverse(LOW_GEAR_HOLD_SPEED, Constants.timeoutMs);
        	liftMotor.configPeakOutputForward(LOW_GEAR_MAX_UPWARD_SPEED, Constants.timeoutMs);
        	liftMotor.configPeakOutputReverse(LOW_GEAR_MAX_DOWNWARD_SPEED, Constants.timeoutMs);
    	}
    }
    
    public void lock() {
    	liftLocker.set(Relay.Value.kForward);
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