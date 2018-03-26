package org.usfirst.frc.team1277.robot.subsystems;

import org.usfirst.frc.team1277.robot.RobotMap;
import org.usfirst.frc.team1277.robot.commands.RotatorToggle;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class ClawRotator extends Subsystem {
	
	private final Spark motor = RobotMap.rotateClawMotor;
    private final Encoder encoder = RobotMap.rotateClawEncoder;
    private final Relay stopper = RobotMap.rotateClawStopper;
    
    public ClawRotator() {
    	encoder.equals(0);
    }
    
    public void rotate(double speed) {
    	motor.set(speed);
    }
    
    public void openStopper() {
    	stopper.set(Relay.Value.kForward);
    }
    
    public void closeStopper() {
    	stopper.set(Relay.Value.kOff);
    }

    public void initDefaultCommand() {
    	setDefaultCommand(new RotatorToggle());
    }
    
    public double getPosition() {
    	return encoder.getDistance();
    }
}

