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
					addBehaviour(new UpdatePerceptions());
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
	
	protected class UpdatePerceptions extends CyclicBehaviour {
		private static final long serialVersionUID = 1L;
		
		@Override
		public void action() {
			// Await messages from the world with the updated perceptions
			ACLMessage msg = receive(MessageTemplate.MatchPerformative(ACLMessage.CONFIRM));
			if (msg != null) {
				switch (msg.getContent()) {
				case AgentMessages.BALL_PASSED:
					ballPassed = true;
					ballShoted = false;
					break;
				case AgentMessages.BALL_SHOTED:
					ballShoted = true;
					ballPassed = false;
					break;
				default:
					break;
				}
			} else {
				block();
			}
		}
	}
	
	protected class AskPerceptions extends TickerBehaviour {
		private static final long serialVersionUID = 1L;

		public AskPerceptions(Agent agent, long timer) {
			super(agent, timer);
		}

		@Override
		protected void onTick() {
			ACLMessage msg = new ACLMessage(ACLMessage.QUERY_IF);
			msg.addReceiver(ballAgent);
			msg.setContent(AgentMessages.BALL_PASSED);
			send(msg);
			msg.setContent(AgentMessages.BALL_SHOTED);
			send(msg);
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
