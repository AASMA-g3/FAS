package pt.ulisboa.aasma.fas.jade.agents;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class DefenderAgent extends PlayerAgent {
	
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void setup() {
		super.setup();
		StartGame startGame = new StartGame();
		
		this.addBehaviour(startGame);
	}
	
	protected class StartGame extends Behaviour {
		private static final long serialVersionUID = 1L;
		private ACLMessage msg;
		
		@Override
		public void action() {
			msg = this.myAgent.receive();
			if (msg != null && msg.getContent().equals(AgentMessages.START_GAME)){
				MainCycle b = new MainCycle();
				 this.myAgent.addBehaviour(b);
			}
				
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

		@Override
		public void action() {
			
		}
	}
}
