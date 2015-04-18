package pt.ulisboa.aasma.fas.jade.agents;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import pt.ulisboa.aasma.fas.jade.agents.StrikerAgent.MainCycle;
import pt.ulisboa.aasma.fas.jade.game.Ball;
import pt.ulisboa.aasma.fas.jade.game.Game;

public class BallAgent extends Agent{
	
	private Game match;
	private Ball ball;
	
	private MainCycle mainCycle;
	
	@Override
	protected void setup() {
		// TODO Auto-generated method stub
		super.setup();
		Object[] args = getArguments();
		if (args == null || args.length == 0){
			System.out.println("Please input correct arguments.");
			return;
		} else{
			match = (Game) args[0];
		}
		
		ball = match.getBall();	
		
		mainCycle = new MainCycle(this);
		
		this.addBehaviour(mainCycle);
	}
	
	protected class MainCycle extends CyclicBehaviour{
		private BallAgent agent;
		
		public MainCycle(Agent agent) {
			super(agent);
			this.agent = (BallAgent) agent;
		}

		@Override
		public void action() {
			ACLMessage msg = this.myAgent.receive();
			if (msg != null) {
				 
			} else 
				block();
		}
		
	}
	
}
