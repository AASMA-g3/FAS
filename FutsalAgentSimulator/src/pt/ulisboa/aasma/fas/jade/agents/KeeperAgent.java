package pt.ulisboa.aasma.fas.jade.agents;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class KeeperAgent extends PlayerAgent {
	
	private static final long serialVersionUID = 1L;
	
	private Boolean ballShoted = false;
	private Boolean ballPassed = false;
	private Boolean hasBall = false;
	
	
	
	@Override
	protected void setup() {
		super.setup();

		this.addBehaviour(new StartGameBehaviour());
	}
	
	/**
	 * This Behaviour is responsible to listen to the game start 
	 * @author Fábio
	 *
	 */
	protected class StartGameBehaviour extends Behaviour {
		private static final long serialVersionUID = 1L;
		
		@Override
		public void action() {
			ACLMessage msg = receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
			if (msg != null){
				switch (msg.getContent()) {
				case AgentMessages.START_GAME:
					gameStarted = true;
					addBehaviour(new MainCycle());
					addBehaviour(new AskPerceptions(this.myAgent, 300));
					break;
				default:
					break;
				}
			}
		}

		@Override
		public boolean done() {
			return gameStarted;
		}
	}

	
	protected class AskPerceptions extends TickerBehaviour {
		private static final long serialVersionUID = 1L;

		public AskPerceptions(Agent agent, long timer) {
			super(agent, timer);
		}

		@Override
		protected void onTick() {
			// Get the world perceptions
		}
	}
	
	protected class MainCycle extends CyclicBehaviour {
		private static final long serialVersionUID = 1L;

		@Override
		public void action() {
//			if(ballShoted){
//				
//			}
		}
	}
}
