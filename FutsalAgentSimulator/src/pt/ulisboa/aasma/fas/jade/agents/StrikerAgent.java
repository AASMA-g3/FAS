package pt.ulisboa.aasma.fas.jade.agents;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class StrikerAgent extends PlayerAgent {
	
	private static final long serialVersionUID = 1L;
	private StartGame startGame;
	private MainCycle mainCycle;
	
	@Override
	protected void setup() {
		super.setup();
		startGame = new StartGame(this);
		mainCycle = new MainCycle(this);
		
		this.addBehaviour(startGame);
	}
	
	
	protected class StartGame extends Behaviour {
		private static final long serialVersionUID = 1L;
		private StrikerAgent agent;
		private ACLMessage msg;
		
		public StartGame(Agent agent) {
			super(agent);
			this.agent = (StrikerAgent) agent;
		}
		
		@Override
		public void action() {
			msg = this.myAgent.receive();
			if (msg != null && msg.getContent().equals(AgentMessages.START_GAME))				
				 this.myAgent.addBehaviour(mainCycle);
			else 
				block();
		}

		@Override
		public boolean done() {
			if(msg == null)
				return false;
			else
				return true;			
		}
	}
	
	protected class MainCycle extends CyclicBehaviour {
		private static final long serialVersionUID = 1L;
		private StrikerAgent agent;
		
		public MainCycle(Agent agent) {
			super(agent);
			this.agent = (StrikerAgent) agent;
		}

		@Override
		public void action() {
	
		}
	}
}
