package pt.ulisboa.aasma.fas.jade.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import pt.ulisboa.aasma.fas.jade.game.Ball;

public class KeeperAgent extends PlayerAgent {
	
	private static final long serialVersionUID = 1L;
	
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
					addBehaviour(new MainCycle(this.myAgent));
					addBehaviour(new ReceiveFailureBehaviour(this.myAgent));
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
	

	
	protected class MainCycle extends CyclicBehaviour {
		private static final long serialVersionUID = 1L;
		
		public MainCycle(Agent agent) {
			super(agent);
		}

		@Override
		public void action() {
			Ball ball = match.getBall();

			double distance = ball.getDistanceToBall(player);
		
			if((distance < 1.0f) && !(hasBall) && (tryCatchBehaviour == null)){
					tryCatchBehaviour = new TryCatchBehaviour(this.myAgent);
					this.myAgent.addBehaviour(tryCatchBehaviour);
			} else {
				player.getPlayerMovement().setGoal(ball.x(), ball.y());
			}
		}
			
		
	}
}
