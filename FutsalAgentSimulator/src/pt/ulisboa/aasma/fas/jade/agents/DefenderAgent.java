package pt.ulisboa.aasma.fas.jade.agents;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import pt.ulisboa.aasma.fas.jade.agents.StrikerAgent.MainCycle;
import pt.ulisboa.aasma.fas.jade.agents.StrikerAgent.StartGame;
import pt.ulisboa.aasma.fas.jade.game.Game;

public class DefenderAgent extends PlayerAgent {
	
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
		private DefenderAgent agent;
		private ACLMessage msg;
		
		public StartGame(Agent agent) {
			super(agent);
			this.agent = (DefenderAgent) agent;
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
		private DefenderAgent agent;
		
		public MainCycle(Agent agent) {
			super(agent);
			this.agent = (DefenderAgent) agent;
		}

		@Override
		public void action() {
	
		}
	}
}
