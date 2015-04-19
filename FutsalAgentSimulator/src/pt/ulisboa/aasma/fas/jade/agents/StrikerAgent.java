package pt.ulisboa.aasma.fas.jade.agents;

import pt.ulisboa.aasma.fas.jade.game.Ball;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class StrikerAgent extends PlayerAgent {
	
	private static final long serialVersionUID = 1L;

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

		@Override
		public void action() {
			Ball ball = match.getBall();
			player.getPlayerMovement().setGoal(ball.x(), ball.y());
			/*Ball ball = match.getBall();

			double distance = ball.getDistanceToBall(player);
		
			if((distance < 1.0f) && !(hasBall) && (tryCatchBehaviour == null)){
					tryCatchBehaviour = new TryCatchBehaviour(this.myAgent);
					this.myAgent.addBehaviour(tryCatchBehaviour);
			} else {
				player.getPlayerMovement().setGoal(ball.x(), ball.y());
			}
			*/
		}
	}
}
