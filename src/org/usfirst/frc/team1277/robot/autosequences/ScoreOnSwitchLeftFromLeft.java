package org.usfirst.frc.team1277.robot.autosequences;

import org.usfirst.frc.team1277.robot.commands.ClawPushCubeOut;
import org.usfirst.frc.team1277.robot.commands.DriveRotateTo;
import org.usfirst.frc.team1277.robot.commands.DriveTo;
import org.usfirst.frc.team1277.robot.commands.LiftToSwitch;
import org.usfirst.frc.team1277.robot.commands.RotatorRetract;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ScoreOnSwitchLeftFromLeft extends CommandGroup {

    public ScoreOnSwitchLeftFromLeft() {
       
    	addSequential(new RotatorRetract());
    	addSequential(new LiftToSwitch());
    	addSequential(new DriveTo(0, 148));
    	addSequential(new DriveRotateTo(90));
    	addSequential(new DriveTo(24, 0));
    	addSequential(new ClawPushCubeOut());
    	
    }
}
