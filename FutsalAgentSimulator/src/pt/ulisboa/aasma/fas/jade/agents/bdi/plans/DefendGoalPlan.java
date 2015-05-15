package pt.ulisboa.aasma.fas.jade.agents.bdi.plans;

import pt.ulisboa.aasma.fas.jade.agents.bdi.PlayFutsalCapability;
import pt.ulisboa.aasma.fas.jade.agents.bdi.PlayerGoals.AvoidGoalsGoal;
import pt.ulisboa.aasma.fas.jade.agents.bdi.plans.plansbody.DefendGoalPlanBody;
import bdi4jade.annotation.GoalOwner;
import bdi4jade.goal.Goal;
import bdi4jade.plan.DefaultPlan;
import bdi4jade.plan.planbody.PlanBody;

@bdi4jade.annotation.GoalOwner(capability = PlayFutsalCapability.class)
public class DefendGoalPlan extends DefaultPlan{
	
	public DefendGoalPlan(){
		super(AvoidGoalsGoal.class, DefendGoalPlanBody.class);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
