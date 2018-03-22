package org.usfirst.frc.team1277.robot.subsystems;

import org.usfirst.frc.team1277.robot.RobotMap;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Claw extends Subsystem {

    private final Spark motor = RobotMap.clawMotor;
    private final Encoder encoder = RobotMap.clawEncoder;
    private final DigitalInput limit = RobotMap.clawLimit;
    
    private double speed = 0, previousSpeed = 0;
    
    private final double MIN_SPEED = 70.0, CLAW_SPEED = 0.7, HOLD_SPEED = 0.15;
    
    public void stopClaw() {
    	motor.set(-HOLD_SPEED);
    }
    
    public void closeClaw() {
    	motor.set(-CLAW_SPEED);
    }
    
    public void openClaw() {
    	motor.set(CLAW_SPEED);
    }
    
    public boolean reachedClawLimit() {
    	return limit.get();
    }
    
    public boolean gotStopped() {
    	speed = encoder.getRate();
    	if (Math.abs(speed) <= MIN_SPEED && Math.abs(previousSpeed) > MIN_SPEED) {
    		previousSpeed = 0;
    		return true;
    	}
    	previousSpeed = speed;
    	return false;
    }
    
    public void initDefaultCommand() {
    	
    }
    
}