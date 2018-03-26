package org.usfirst.frc.team1277.robot.commands;

import org.usfirst.frc.team1277.robot.Robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveToVisionTargetCube extends Command {
	
	private final int FRAME_WIDTH = 640;
	private final double XSPEED_MULTIPLIER = 0.004, YSPEED_MULTIPLIER = 20.0;
	private final double  MAX_SPEED = 0.6, PARALLEL_MOVE_LOWEST_SPEED = 0.1, PERPENDICULAR_MOVE_LOWEST_SPEED = 0.3, ROTATE_LOWEST_SPEED = 0.1, LOWEST_CONTROL = 0.01;
	private int mostSignificantObject;

    public DriveToVisionTargetCube() {
    	requires(Robot.driveTrain);
    }

    protected void initialize() {
    	Robot.driveTrain.setDesiredDirection(Robot.driveTrain.getDirection());
    }

    protected void execute() {
    	NetworkTableInstance instance = NetworkTableInstance.getDefault();
    	NetworkTable cubetarget = instance.getTable("cubetarget");
    	double numberOfObjects = cubetarget.getEntry("objcount").getValue().getDouble();
    	double[] vtargetobjx = cubetarget.getEntry("cubetargetx").getValue().getDoubleArray();
    	//double[] vtargetobjy = cubetarget.getEntry("cubetargety").getValue().getDoubleArray();
    	double[] vtargetobjw = cubetarget.getEntry("cubetargetw").getValue().getDoubleArray();
    	double[] vtargetobjh = cubetarget.getEntry("cubetargeth").getValue().getDoubleArray();
    	
    	SmartDashboard.putNumber("numberOfObjects", numberOfObjects);
    	
    	double moveX, moveY, rotate;
    	
    	//Find Which Object to Track
    	mostSignificantObject = -1;
    	for (int i = 0; i <= numberOfObjects; i++) {
    		if (mostSignificantObject == -1) mostSignificantObject = i;
    		else if ((vtargetobjw[i] * vtargetobjh[i])/*area*/ * Math.abs((FRAME_WIDTH / 2) - (vtargetobjx[i] + (vtargetobjw[i] / 2)))/*position from center*/ > 
    				(vtargetobjw[mostSignificantObject] * vtargetobjh[mostSignificantObject])/*area*/ * 
    				Math.abs((FRAME_WIDTH / 2) - (vtargetobjx[mostSignificantObject] + (vtargetobjw[mostSignificantObject] / 2)))/*position from center*/) {
    			mostSignificantObject = i;
    		}
    	}
    	
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
        return Robot.claw.holdingCube();
    }
    
    protected void end() {
    	Robot.driveTrain.drive(0, 0, 0, true, false);
    }

    protected void interrupted() {
    	Robot.driveTrain.drive(0, 0, 0, true, false);
    }
}