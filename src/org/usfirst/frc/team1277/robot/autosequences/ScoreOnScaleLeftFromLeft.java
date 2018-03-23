package org.usfirst.frc.team1277.robot.autosequences;

import org.usfirst.frc.team1277.robot.commands.ClawThrowCubeOut;
import org.usfirst.frc.team1277.robot.commands.DriveRotateTo;
import org.usfirst.frc.team1277.robot.commands.DriveTo;
import org.usfirst.frc.team1277.robot.commands.LiftToBottom;
import org.usfirst.frc.team1277.robot.commands.LiftToScale;
import org.usfirst.frc.team1277.robot.commands.LiftToSwitch;
import org.usfirst.frc.team1277.robot.commands.RotatorRetract;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ScoreOnScaleLeftFromLeft extends CommandGroup {

    public ScoreOnScaleLeftFromLeft() {
        
    	addSequential(new RotatorRetract());
    	addSequential(new LiftToSwitch());
    	addSequential(new DriveTo(0, 260));
    	addSequential(new LiftToScale());
    	addSequential(new DriveTo(24, 0));
    	addSequential(new DriveRotateTo(45));
    	addSequential(new ClawThrowCubeOut());
    	addSequential(new DriveRotateTo(0));
    	addSequential(new DriveTo(0, -12));
    	addSequential(new LiftToBottom());
    	
    }
}
