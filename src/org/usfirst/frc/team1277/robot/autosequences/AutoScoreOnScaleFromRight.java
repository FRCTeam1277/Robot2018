package org.usfirst.frc.team1277.robot.autosequences;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoScoreOnScaleFromRight extends CommandGroup {

    public AutoScoreOnScaleFromRight() {

    	String gameData;
		gameData = DriverStation.getInstance().getGameSpecificMessage();
		
        if(gameData.length() > 1) {
        	if(gameData.charAt(1) == 'L') {
        		addSequential(new ScoreOnScaleLeftFromRight());
        	} 
        	else {
        		addSequential(new ScoreOnScaleRightFromRight());
        	}
        }
        else {
        	addSequential(new DriveToAutoLine());
        }
    }
}
