package org.usfirst.frc.team1277.robot.subsystems;

import org.usfirst.frc.team1277.robot.Constants;
import org.usfirst.frc.team1277.robot.OI;
import org.usfirst.frc.team1277.robot.RobotMap;
import org.usfirst.frc.team1277.robot.commands.Drive;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import utilities.DataCommunicator;
import utilities.ModifiedPIDController;

public class DriveTrain extends Subsystem implements PIDOutput {

    private final TalonSRX fLMotor = RobotMap.driveTrainFrontLeft; 
    private final TalonSRX fRMotor = RobotMap.driveTrainFrontRight;
    private final TalonSRX rLMotor = RobotMap.driveTrainRearLeft;
    private final TalonSRX rRMotor = RobotMap.driveTrainRearRight;
    
    private final double PI = Math.PI, PERPENDICULAR_MOVE_FACTOR = 1.5;
    private double direction = 0, desiredDirection = 0;
    
    private PIDController rotateController;
    private final double ROTATE_P_GAIN = 0.01, ROTATE_I_GAIN = 0.0, ROTATE_D_GAIN = 0.02, ROTATE_F_GAIN = 0.0, TOLLERANCE_DEGREES = 0.5;
    private double robotRotate = 0;
    
    private ModifiedPIDController moveXController, moveYController;
    private double MOVE_P_GAIN = 0.00015, MOVE_I_GAIN = 0.0, MOVE_D_GAIN = 0.001, MOVE_F_GAIN = 0.0, TOLLERANCE_COUNTS = 512; //4096 counts per wheel revolution, final
    private DataCommunicator robotPositionX, robotPositionY;
    private DataCommunicator robotDriveX, robotDriveY;
	private double motorPositionFL = 0, motorPositionFR = 0, motorPositionRL = 0, motorPositionRR = 0,
    		motorPositionChangeFL = 0, motorPositionChangeFR = 0, motorPositionChangeRL = 0, motorPositionChangeRR = 0;
    
    public DriveTrain(){
    	NetworkTableInstance instance = NetworkTableInstance.getDefault();
    	NetworkTable gains = instance.getTable("testing");
    	MOVE_P_GAIN = gains.getEntry("MOVE_P_GAIN").getValue().getDouble();
    	MOVE_I_GAIN = gains.getEntry("MOVE_I_GAIN").getValue().getDouble();
    	MOVE_D_GAIN = gains.getEntry("MOVE_D_GAIN").getValue().getDouble();
    	MOVE_F_GAIN = gains.getEntry("MOVE_F_GAIN").getValue().getDouble();
    	//PID Control for rotate
    	rotateController = new PIDController(ROTATE_P_GAIN, ROTATE_I_GAIN, ROTATE_D_GAIN, ROTATE_F_GAIN, OI.getAhrs(), this);
    	rotateController.setInputRange(-360.0,  360.0);
    	rotateController.setOutputRange(-1.0, 1.0);
        rotateController.setAbsoluteTolerance(TOLLERANCE_DEGREES);
        rotateController.setContinuous(true);
        rotateController.enable(); //may want to use only part time
    	
        //Initialize Data Communicators
        robotPositionX = new DataCommunicator();
        robotPositionY = new DataCommunicator();
        robotDriveX = new DataCommunicator();
        robotDriveY = new DataCommunicator();
        
        //PID Control for rotate
        moveXController = new ModifiedPIDController(MOVE_P_GAIN, MOVE_I_GAIN, MOVE_D_GAIN, MOVE_F_GAIN, robotPositionX, robotDriveX);
        moveYController = new ModifiedPIDController(MOVE_P_GAIN, MOVE_I_GAIN, MOVE_D_GAIN, MOVE_F_GAIN, robotPositionY, robotDriveY);
        moveXController.setInputRange(-4096000, 4096000);
        moveYController.setInputRange(-4096000, 4096000);
        moveXController.setOutputRange(-0.6, 0.6);
        moveYController.setOutputRange(-0.6, 0.6);
        moveXController.setAbsoluteTolerance(TOLLERANCE_COUNTS);
        moveYController.setAbsoluteTolerance(TOLLERANCE_COUNTS);
        moveXController.setContinuous(true);
        moveYController.setContinuous(true);
        moveXController.enable();
        moveYController.enable();
    }
    
    public void driveTrainInit() {

        //Zero Drive Motor Positions
        fLMotor.setSelectedSensorPosition(0, Constants.PIDLoopIdx, Constants.timeoutMs);
    	fRMotor.setSelectedSensorPosition(0, Constants.PIDLoopIdx, Constants.timeoutMs);
    	rLMotor.setSelectedSensorPosition(0, Constants.PIDLoopIdx, Constants.timeoutMs);
    	rRMotor.setSelectedSensorPosition(0, Constants.PIDLoopIdx, Constants.timeoutMs);
    	restartPositionTracking(true);
    }
    
    @Override
	public void pidWrite(double output) {
		robotRotate = output;
	}
    
    public void drive(double parallelMove, double perpendicularMove, double rotate, boolean fieldOriented) {
    	double greatestControl, greatestSpeed;
    	
    	//Preliminary calculations
    	greatestControl = Math.max(Math.max(Math.abs(parallelMove), Math.abs(perpendicularMove)), Math.abs(rotate));
    	greatestSpeed = Math.max(Math.max(Math.abs(parallelMove - perpendicularMove - rotate), 
    			Math.abs(parallelMove - perpendicularMove - rotate)),
    			Math.max(Math.abs(-parallelMove + perpendicularMove - rotate), 
    			Math.abs(-parallelMove + perpendicularMove - rotate)));
    	//if(greatestSpeed == 0) greatestSpeed = 1;
    	
    	//Dashboard
    	SmartDashboard.putNumber("Foward", parallelMove);
    	SmartDashboard.putNumber("Sideways", perpendicularMove);
    	SmartDashboard.putNumber("Turn", rotate);
    	
    	//Position Tracking
    	updateRobotPosition();
    	SmartDashboard.putNumber("robotPositionX", robotPositionX.getDataPoint());
    	SmartDashboard.putNumber("robotPositionY", robotPositionY.getDataPoint());
    	SmartDashboard.putNumber("direction", direction);
    	
    	//Field Oriented
    	if (fieldOriented) {
    		double moveAngle, maxRadiusFactor, speedMagnitude;
    		moveAngle = Math.atan2(perpendicularMove, parallelMove);
    		speedMagnitude = Math.sqrt((parallelMove * parallelMove) + (perpendicularMove * perpendicularMove));
    		//inverse of distance to edge of initial square
    		if ((moveAngle % (PI / 2) + (PI / 2)) % (PI / 2) <= PI / 4) maxRadiusFactor = Math.cos((moveAngle % (PI / 2) + (PI / 2)) % (PI / 2));
    		else maxRadiusFactor = Math.cos((PI / 2) - ((moveAngle % (PI / 2) + (PI / 2)) % (PI / 2)));
    		moveAngle += direction;
    		//factor between distance to edge of final square and initial square
    		if ((moveAngle % (PI / 2) + (PI / 2)) % (PI / 2) <= PI / 4) maxRadiusFactor /= Math.cos((moveAngle % (PI / 2) + (PI / 2)) % (PI / 2));
    		else maxRadiusFactor /= Math.cos((PI / 2) - ((moveAngle % (PI / 2) + (PI / 2)) % (PI / 2)));
    		parallelMove = speedMagnitude * maxRadiusFactor * Math.cos(moveAngle);
    		perpendicularMove = speedMagnitude * maxRadiusFactor * Math.sin(moveAngle);
    		perpendicularMove *= PERPENDICULAR_MOVE_FACTOR;
    		if (Math.abs(perpendicularMove) > 1.0) {
    			parallelMove /= Math.abs(perpendicularMove);
    			perpendicularMove /= Math.abs(perpendicularMove);
    		}
    	}
    	
    	//Drive the motors
    	fLMotor.set(ControlMode.PercentOutput ,(parallelMove - perpendicularMove + rotate)* greatestControl / greatestSpeed);
    	fRMotor.set(ControlMode.PercentOutput ,(-parallelMove - perpendicularMove + rotate)* greatestControl / greatestSpeed);
    	rLMotor.set(ControlMode.PercentOutput ,(parallelMove + perpendicularMove + rotate)* greatestControl / greatestSpeed);
    	rRMotor.set(ControlMode.PercentOutput ,(-parallelMove + perpendicularMove + rotate)* greatestControl / greatestSpeed);
    }
    /*
    public void rotateTo() {
    	double rotate;
    	direction = OI.getAhrs().getAngle() * PI / 180f;
    	//rotate = Math.atan(2f * (desiredDirection - direction)) * 2 / PI;
    	rotate = robotRotate;
    	if (rotate > 0.005f && rotate < LOWEST_SPEED) rotate = LOWEST_SPEED;
    	else if (rotate < -0.005f && rotate > -LOWEST_SPEED) rotate = -LOWEST_SPEED;
    	drive(0, 0, rotate);
    	SmartDashboard.putNumber("Direction", direction);
    	SmartDashboard.putNumber("Desired Direction", desiredDirection);
    	SmartDashboard.putNumber("Rotate", Math.atan(2f * (desiredDirection - direction)) * 2 / PI);
    }*/
    /*
    public void correctPosition() {
    	direction = OI.getAhrs().getAngle() * PI / 180f;
    	if(Constants.driveSensorPhase == Constants.driveMotorInvert) {
        	fLMotor.setSelectedSensorPosition(fLMotor.getSensorCollection().getPulseWidthPosition(), Constants.PIDLoopIdx, Constants.timeoutMs);
        	fRMotor.setSelectedSensorPosition(fRMotor.getSensorCollection().getPulseWidthPosition(), Constants.PIDLoopIdx, Constants.timeoutMs);
        	rLMotor.setSelectedSensorPosition(rLMotor.getSensorCollection().getPulseWidthPosition(), Constants.PIDLoopIdx, Constants.timeoutMs);
        	rRMotor.setSelectedSensorPosition(rRMotor.getSensorCollection().getPulseWidthPosition(), Constants.PIDLoopIdx, Constants.timeoutMs);
    	}
    	else {
        	fLMotor.setSelectedSensorPosition(fLMotor.getSensorCollection().getPulseWidthPosition() * -1, Constants.PIDLoopIdx, Constants.timeoutMs);
        	fRMotor.setSelectedSensorPosition(fRMotor.getSensorCollection().getPulseWidthPosition() * -1, Constants.PIDLoopIdx, Constants.timeoutMs);
        	rLMotor.setSelectedSensorPosition(rLMotor.getSensorCollection().getPulseWidthPosition() * -1, Constants.PIDLoopIdx, Constants.timeoutMs);
        	rRMotor.setSelectedSensorPosition(rRMotor.getSensorCollection().getPulseWidthPosition() * -1, Constants.PIDLoopIdx, Constants.timeoutMs);
    	}
    }*/
    
    public void updateRobotPosition() {
    	double previousDirection = direction;

    	//Get gyroscope reading
    	updateDirection();
    	
    	//Find motor position changes
    	motorPositionChangeFL = fLMotor.getSelectedSensorPosition(0) - motorPositionFL;
    	motorPositionChangeFR = fRMotor.getSelectedSensorPosition(0) - motorPositionFR;
    	motorPositionChangeRL = rLMotor.getSelectedSensorPosition(0) - motorPositionRL;
    	motorPositionChangeRR = rRMotor.getSelectedSensorPosition(0) - motorPositionRR;
    	
    	//Calculate robot position
    	robotPositionX.setDataPoint(robotPositionX.getDataPoint() 
    			+ (-motorPositionChangeFL - motorPositionChangeFR + motorPositionChangeRL + motorPositionChangeRR) * Math.cos((previousDirection + direction) / 2) / PERPENDICULAR_MOVE_FACTOR
    			+ (motorPositionChangeFL - motorPositionChangeFR + motorPositionChangeRL - motorPositionChangeRR) * Math.sin((previousDirection + direction) / 2));
    	robotPositionY.setDataPoint(robotPositionY.getDataPoint() 
    			+ (motorPositionChangeFL - motorPositionChangeFR + motorPositionChangeRL - motorPositionChangeRR) * Math.cos((previousDirection + direction) / 2)
    			+ (-motorPositionChangeFL - motorPositionChangeFR + motorPositionChangeRL + motorPositionChangeRR) * Math.sin((previousDirection + direction) / 2) / PERPENDICULAR_MOVE_FACTOR);
    	
    	//Update motor positions
    	motorPositionFL += motorPositionChangeFL;
    	motorPositionFR += motorPositionChangeFR;
    	motorPositionRL += motorPositionChangeRL;
    	motorPositionRR += motorPositionChangeRR;
    	
    	SmartDashboard.putNumber("motorPositionChangeFL", motorPositionChangeFL);
    	SmartDashboard.putNumber("fLMotor", fLMotor.getSelectedSensorPosition(0));
    	SmartDashboard.putNumber("fRMotor", fRMotor.getSelectedSensorPosition(0));
    	SmartDashboard.putNumber("rLMotor", rLMotor.getSelectedSensorPosition(0));
    	SmartDashboard.putNumber("rRMotor", rRMotor.getSelectedSensorPosition(0));
    }
    
    public void restartPositionTracking(boolean zeroPosition) {
    	motorPositionFL = fLMotor.getSelectedSensorPosition(0);
    	motorPositionFR = fRMotor.getSelectedSensorPosition(0);
    	motorPositionRL = rLMotor.getSelectedSensorPosition(0);
    	motorPositionRR = rRMotor.getSelectedSensorPosition(0);
    	if(zeroPosition) {
    		robotPositionX.setDataPoint(0);
    		robotPositionY.setDataPoint(0);
    	}
    }
    /*
	public void velocityMode() {
    	fLMotor.config_kP(0, 0.90f, 10);
		fLMotor.config_kI(0, 0, 10);
		fLMotor.config_kD(0, 0.72f, 10);
		fLMotor.config_kF(0, 0.36f, 10);
		fRMotor.config_kP(0, 0.90f, 10);
		fRMotor.config_kI(0, 0, 10);
		fRMotor.config_kD(0, 0.72f, 10);
		fRMotor.config_kF(0, 0.36f, 10);
		rLMotor.config_kP(0, 0.90f, 10);
		rLMotor.config_kI(0, 0, 10);
		rLMotor.config_kD(0, 0.72f, 10);
		rLMotor.config_kF(0, 0.36f, 10);
		rRMotor.config_kP(0, 0.90f, 10);
		rRMotor.config_kI(0, 0, 10);
		rRMotor.config_kD(0, 0.72f, 10);
		rRMotor.config_kF(0, 0.36f, 10);
    }
    
    public void positionMode() {
    	fLMotor.config_kP(0, 0.90f, 10);
		fLMotor.config_kI(0, 0, 10);
		fLMotor.config_kD(0, 0.72f, 10);
		fLMotor.config_kF(0, 0, 10);
		fRMotor.config_kP(0, 0.90f, 10);
		fRMotor.config_kI(0, 0, 10);
		fRMotor.config_kD(0, 0.72f, 10);
		fRMotor.config_kF(0, 0, 10);
		rLMotor.config_kP(0, 0.90f, 10);
		rLMotor.config_kI(0, 0, 10);
		rLMotor.config_kD(0, 0.72f, 10);
		rLMotor.config_kF(0, 0, 10);
		rRMotor.config_kP(0, 0.90f, 10);
		rRMotor.config_kI(0, 0, 10);
		rRMotor.config_kD(0, 0.72f, 10);
		rRMotor.config_kF(0, 0, 10);
    }*/
    
    public void initDefaultCommand() {
        setDefaultCommand(new Drive());
    }
    
    public void updateDirection() {
		//direction = ((OI.getAhrs().getAngle() * PI / 180f) % (PI));
    	direction = OI.getAhrs().getAngle();
	}

	public double getDirection() {
		return OI.getAhrs().getAngle() * PI / 180f;
	}

	public double getDesiredDirection() {
		return desiredDirection;
	}

	public void setDesiredDirection(double desiredDirection) {
		this.desiredDirection = desiredDirection;
		rotateController.setSetpoint(desiredDirection);
	}
    
	public double getRobotPositionX() {
		return robotPositionX.getDataPoint();
	}

	public double getRobotPositionY() {
		return robotPositionY.getDataPoint();
	}
	
	public double getDesiredRobotPositionX() {
		return moveXController.getSetpoint();
	}

	public double getDesiredRobotPositionY() {
		return moveYController.getSetpoint();
	}
	
	public double getRobotRotate() {
		return robotRotate;
	}
	
	public double getRobotDriveX() {
		return robotDriveX.getDataPoint();
	}
	
	public double getRobotDriveY() {
		return robotDriveY.getDataPoint();
	}
	
	public void setXPosition(double xPosition) {
		moveXController.setSetpoint(xPosition);
	}
	
	public void setYPosition(double yPosition) {
		moveYController.setSetpoint(yPosition);
	}

}