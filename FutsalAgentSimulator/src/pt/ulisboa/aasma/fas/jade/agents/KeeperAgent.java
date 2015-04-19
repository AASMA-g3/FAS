package pt.ulisboa.aasma.fas.jade.agents;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import pt.ulisboa.aasma.fas.jade.game.Ball;
import pt.ulisboa.aasma.fas.jade.game.Game;
import pt.ulisboa.aasma.fas.jade.game.Player;

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
//			Ball ball = match.getBall();
//			double distance = ball.getDistanceToBall(player);
		
//			if (hasBall){
//				//pass
//			}else if (ball.getCurrentMovement().getOriginalIntensity() == Ball.INTENSITY_SHOOT){
//				//defend
//			}else {
				double my_pos_x = 0;
				double my_pos_y = Game.GOAL_Y_MED;
				double ball_pos_x = match.getBall().x();
				double ball_pox_y = match.getBall().y();
				double inclination;
				if(player.getTeam() == Player.TEAM_A)
					my_pos_x = 0;
				else
					my_pos_y = Game.LIMIT_X;
				
				inclination = (ball_pox_y - my_pos_y) / (ball_pos_x - my_pos_x);
				
				double my_new_y = my_pos_x * inclination;
//				System.out.println("inclination: " + inclination);
//				System.out.println("Move to x: " + my_pos_x);
//				System.out.println("Move to y: " + my_new_y);
//				player.set()
						
						
			}
				
				
				
//			if((distance < 1.0f) && !(hasBall) && (tryCatchBehaviour == null)){
//					tryCatchBehaviour = new TryCatchBehaviour(this.myAgent);
//					this.myAgent.addBehaviour(tryCatchBehaviour);
//			} else {
//				player.getPlayerMovement().setGoal(ball.x(), ball.y());
//			}
//		}
			
		
	}
}
