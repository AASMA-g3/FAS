package pt.ulisboa.aasma.fas.jade.agents;

import pt.ulisboa.aasma.fas.jade.game.Ball;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class StrikerAgent extends PlayerAgent {
	
	private static final long serialVersionUID = 1L;
//	private Boolean moving = false;

	@Override
	protected void setup() {
		super.setup();

		this.addBehaviour(new ReceiveInformBehaviour(this));
	}
	
	
	/**
	 * This Behaviour receives general information about the game flow
	 * @author Fábio
	 *
	 */
	protected class ReceiveInformBehaviour extends CyclicBehaviour{
		private static final long serialVersionUID = 1L;

		public ReceiveInformBehaviour(Agent agent) {
			super(agent);
		}
		
		@Override
		public void action() {
			ACLMessage msg = this.myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
			if(msg == null)
				block();
			else{
				switch (msg.getOntology()) {
					case AgentMessages.START_GAME:
						gameStarted = true;
						addBehaviour(new MainCycle());
						break;
					case AgentMessages.END_GAME:
						System.out.println("vou morrer!");
						doDelete();
						break;
					case AgentMessages.PAUSE_GAME:
						gameStarted = false;
						player.resetCoords();
						break;
					case AgentMessages.RESTART_GAME:
						gameStarted = true;
						break;
					default:
						break;
				}
			}
		}
	}
	
	protected class MainCycle extends CyclicBehaviour {
		private static final long serialVersionUID = 1L;

		@Override
		public void action() {
			
			if (gameStarted){
			Ball ball = match.getBall();
			/*if(moving && (player.getPlayerMovement().getGoalX() - player.x()) < 0.8
					&& (player.getPlayerMovement().getGoalY() - player.y()) < 0.8){
				moving = false;
				
			}else if(!moving){
				player.resetCoords();
				moving = true;
			}*/
			
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
}
