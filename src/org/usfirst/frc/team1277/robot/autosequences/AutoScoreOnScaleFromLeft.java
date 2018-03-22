package org.usfirst.frc.team1277.robot.autosequences;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutoScoreOnScaleFromLeft extends CommandGroup {

    public AutoScoreOnScaleFromLeft() {
        
    	String gameData;
		gameData = DriverStation.getInstance().getGameSpecificMessage();
		
        if(gameData.length() > 1) {
        	if(gameData.charAt(1) == 'L') {
        		addSequential(new ScoreOnScaleLeftFromLeft());
        	} 
        	else {
        		addSequential(new ScoreOnScaleRightFromLeft());
        	}
        }
        else {
        	addSequential(new DriveToAutoLine());
        }
    }
}
