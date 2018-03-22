package org.usfirst.frc.team1277.robot.autosequences;

import org.usfirst.frc.team1277.robot.commands.DeployClaw;
import org.usfirst.frc.team1277.robot.commands.DriveTo;
import org.usfirst.frc.team1277.robot.commands.LiftToBottom;
import org.usfirst.frc.team1277.robot.commands.LiftToScale;
import org.usfirst.frc.team1277.robot.commands.LiftToSwitch;
import org.usfirst.frc.team1277.robot.commands.ReleaseCube;
import org.usfirst.frc.team1277.robot.commands.DriveRotateTo;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ScoreOnScaleRightFromLeft extends CommandGroup {

    public ScoreOnScaleRightFromLeft() {
        
    	addSequential(new DeployClaw());
    	addSequential(new LiftToSwitch());
    	addSequential(new DriveTo(0, 215));
    	addSequential(new DriveRotateTo(90));
    	addSequential(new DriveTo(190, 0));
    	addSequential(new DriveRotateTo(0));
    	addSequential(new LiftToScale());
    	addSequential(new DriveTo(0, 45));
    	addSequential(new DriveRotateTo(-45));
    	addSequential(new ReleaseCube());
    	addSequential(new DriveRotateTo(0));
    	addSequential(new DriveTo(0, -12));
    	addSequential(new LiftToBottom());
    	
    }
}
