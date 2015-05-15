package pt.ulisboa.aasma.fas.jade.agents.bdi;

import bdi4jade.annotation.GoalOwner;
import bdi4jade.goal.Goal;

public class PlayerGoals {
	
	@bdi4jade.annotation.GoalOwner(capability = PlayFutsalCapability.class)
	public class AvoidGoalsGoal implements Goal{
}
}