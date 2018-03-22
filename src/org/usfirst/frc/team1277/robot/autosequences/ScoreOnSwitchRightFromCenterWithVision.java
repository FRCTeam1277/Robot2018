package org.usfirst.frc.team1277.robot.autosequences;

import org.usfirst.frc.team1277.robot.commands.ClawPushCubeOut;
import org.usfirst.frc.team1277.robot.commands.DriveTo;
import org.usfirst.frc.team1277.robot.commands.DriveToVisionTargetRight;
import org.usfirst.frc.team1277.robot.commands.LiftToSwitch;
import org.usfirst.frc.team1277.robot.commands.RotatorRetract;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ScoreOnSwitchRightFromCenterWithVision extends CommandGroup {

    public ScoreOnSwitchRightFromCenterWithVision() {
        
    	addSequential(new RotatorRetract());
    	addSequential(new LiftToSwitch());
    	addSequential(new DriveTo(0, 6));
    	addSequential(new DriveToVisionTargetRight());
    	addSequential(new ClawPushCubeOut());
    	
    }
}
