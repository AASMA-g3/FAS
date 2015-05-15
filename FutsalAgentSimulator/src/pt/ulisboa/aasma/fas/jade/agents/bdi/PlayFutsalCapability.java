package pt.ulisboa.aasma.fas.jade.agents.bdi;

import pt.ulisboa.aasma.fas.jade.agents.bdi.PlayerGoals.AvoidGoalsGoal;
import pt.ulisboa.aasma.fas.jade.agents.bdi.plans.DefendGoalPlan;
import pt.ulisboa.aasma.fas.jade.agents.bdi.plans.plansbody.DefendGoalPlanBody;
import bdi4jade.core.Capability;
import bdi4jade.examples.ping.PingPlanBody;
import bdi4jade.examples.ping.PingPongCapability.PingGoal;
import bdi4jade.plan.DefaultPlan;

@bdi4jade.annotation.GoalOwner(capability = PlayFutsalCapability.class)
public class PlayFutsalCapability extends Capability {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@bdi4jade.annotation.Plan
	private DefendGoalPlan dgP = new DefendGoalPlan();

	public PlayFutsalCapability() {
		super();
	}
	
	
}
