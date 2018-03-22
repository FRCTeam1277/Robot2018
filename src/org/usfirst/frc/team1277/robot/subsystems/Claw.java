package org.usfirst.frc.team1277.robot.subsystems;

import org.usfirst.frc.team1277.robot.RobotMap;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Claw extends Subsystem {
	
    private final DigitalInput limit = RobotMap.clawLimit;
    private final Spark lMotor = RobotMap.clawMotorLeft;
	private final Spark rMotor = RobotMap.clawMotorRight;
	
	public void dirveWheels(double leftSpeed, double rightSpeed) {
		lMotor.set(leftSpeed);
		rMotor.set(-rightSpeed);
	}
    
    public void initDefaultCommand() {
    	
    }
    
    public boolean holdingCube() {
    	return limit.get();
    }
}