package org.usfirst.frc.team1277.robot.commands;

import org.usfirst.frc.team1277.robot.Robot;
import edu.wpi.first.networktables.*;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveToVisionTargetLeft extends Command {
	
	private final int FRAME_WIDTH = 640, FINAL_WIDTH = 180;
	private final double XSPEED_MULTIPLIER = 0.004, YSPEED_MULTIPLIER = 20.0;
	private final double  MAX_SPEED = 0.6, PARALLEL_MOVE_LOWEST_SPEED = 0.1, PERPENDICULAR_MOVE_LOWEST_SPEED = 0.3, ROTATE_LOWEST_SPEED = 0.1, LOWEST_CONTROL = 0.01;
	private int mostSignificantObject;
	private double objectWidth;

    public DriveToVisionTargetLeft() {
    	requires(Robot.driveTrain);
    }

    protected void initialize() {
    	Robot.driveTrain.setDesiredDirection(Robot.driveTrain.getDirection());
    	objectWidth = 0;
    }

    protected void execute() {
    	NetworkTableInstance instance = NetworkTableInstance.getDefault();
    	NetworkTable objects = instance.getTable("vtargetobj");
    	double numberOfObjects = objects.getEntry("objcount").getValue().getDouble();
    	double[] vtargetobjx = objects.getEntry("vtargetobjx").getValue().getDoubleArray();
    	//double[] vtargetobjy = objects.getEntry("vtargetobjy").getValue().getDoubleArray();
    	double[] vtargetobjw = objects.getEntry("vtargetobjw").getValue().getDoubleArray();
    	double[] vtargetobjh = objects.getEntry("vtargetobjh").getValue().getDoubleArray();
    	
    	double moveX, moveY, rotate;
    	
    	SmartDashboard.putNumber("numberOfObjects", numberOfObjects);

    	//Find Which Object to Track
    	mostSignificantObject = -1;
    	for (int i = 0; i < numberOfObjects; i++) {
    		if (mostSignificantObject == -1) mostSignificantObject = i;
    		else if ((vtargetobjw[i] * vtargetobjh[i])/*area*/ * (FRAME_WIDTH - (vtargetobjx[i] + (vtargetobjw[i] / 2)))/*position from right*/ > 
    				(vtargetobjw[mostSignificantObject] * vtargetobjh[mostSignificantObject])/*area*/ * 
    				(FRAME_WIDTH - (vtargetobjx[mostSignificantObject] + (vtargetobjw[mostSignificantObject] / 2)))/*position from right*/) {
    			mostSignificantObject = i;
    		}
    	}
    	
    	objectWidth = vtargetobjw[mostSignificantObject];
    	
    	//Calculate Speeds
    	if (mostSignificantObject == -1) {
    		moveX = 0;
    		moveY = 0;
    	}
    	else {
    		moveX = (vtargetobjx[mostSignificantObject]+vtargetobjw[mostSignificantObject]/2)/*position*/ - (FRAME_WIDTH / 2);
    		moveY = 1 / Math.abs(vtargetobjx[mostSignificantObject]+vtargetobjw[mostSignificantObject]/2/*position*/ - (FRAME_WIDTH / 2));
    	}
    	
    	//Adjust Speeds
    	moveX *= XSPEED_MULTIPLIER;
    	moveY *= YSPEED_MULTIPLIER;
    	if (moveX > MAX_SPEED) moveX = MAX_SPEED;
    	else if (moveX < -MAX_SPEED) moveX = MAX_SPEED;
    	if (moveY > MAX_SPEED) moveY = MAX_SPEED;
    	else if (moveY < -MAX_SPEED) moveY = -MAX_SPEED;
    	if (moveX > LOWEST_CONTROL && moveX < PARALLEL_MOVE_LOWEST_SPEED) moveX = PARALLEL_MOVE_LOWEST_SPEED;
    	else if (moveX < -LOWEST_CONTROL && moveX > -PARALLEL_MOVE_LOWEST_SPEED) moveX = -PARALLEL_MOVE_LOWEST_SPEED;
    	if (moveY > LOWEST_CONTROL && moveY < PERPENDICULAR_MOVE_LOWEST_SPEED) moveY = PERPENDICULAR_MOVE_LOWEST_SPEED;
    	else if (moveY < -LOWEST_CONTROL && moveY > -PERPENDICULAR_MOVE_LOWEST_SPEED) moveY = -PERPENDICULAR_MOVE_LOWEST_SPEED;
    	
    	//Rotate Corrections
    	rotate = Robot.driveTrain.getRobotRotate();
    	if (rotate > LOWEST_CONTROL && rotate < ROTATE_LOWEST_SPEED) rotate = ROTATE_LOWEST_SPEED;
    	else if (rotate < -LOWEST_CONTROL && rotate > -ROTATE_LOWEST_SPEED) rotate = -ROTATE_LOWEST_SPEED;
    	
    	//Drive
    	Robot.driveTrain.drive(moveY, -moveX, rotate, false, false); //Look into why it is negative
    }

    protected boolean isFinished() {
        return (objectWidth >= FINAL_WIDTH);
    }
    
    protected void end() {
    	Robot.driveTrain.drive(0, 0, 0, true, false);
    }

    protected void interrupted() {
    	Robot.driveTrain.drive(0, 0, 0, true, false);
    }
}
