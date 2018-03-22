package org.usfirst.frc.team1277.robot.autosequences;

import org.usfirst.frc.team1277.robot.commands.DeployClaw;
import org.usfirst.frc.team1277.robot.commands.DriveTo;
import org.usfirst.frc.team1277.robot.commands.LiftToSwitch;
import org.usfirst.frc.team1277.robot.commands.ReleaseCube;
import org.usfirst.frc.team1277.robot.commands.DriveRotateTo;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ScoreOnSwitchRightFromRight extends CommandGroup {

    public ScoreOnSwitchRightFromRight() {
        
    	addSequential(new DeployClaw());
    	addSequential(new LiftToSwitch());
    	addSequential(new DriveTo(0, 148));
    	addSequential(new DriveRotateTo(-90));
    	addSequential(new DriveTo(-24, 0));
    	addSequential(new ReleaseCube());
    	
    }
}
