package pt.ulisboa.aasma.fas.jade.agents;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
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

			if (hasBall){
				//pass
			}else if (ball.getCurrentMovement().getOriginalIntensity() == Ball.INTENSITY_SHOOT){
				//defend
			}else {
				System.out.println(getName());
				double ref_x;
				double ref_y = Game.GOAL_Y_MED;
				double ball_x = match.getBall().x();
				double ball_y = match.getBall().y();
				
				System.out.println("ball_x " + ball_x);
				System.out.println("ball_y " + ball_y);

				double inclination;
				double my_new_y;
				double my_new_x;
				
				if (player.getTeam() == Player.TEAM_A){
					ref_x = 0;
					my_new_x = 0.2;
					inclination = (ball_y - ref_y) / (ball_x - ref_x);
					my_new_y = my_new_x * inclination + Game.GOAL_Y_MED;
				} else {
					ref_x = Game.LIMIT_X;
					inclination = (ball_y - ref_y) / (ball_x - ref_x);
					my_new_x = 39.8;
					my_new_y = my_new_x * inclination + Game.GOAL_Y_MED;
					
				}
				player.getPlayerMovement().setGoal(my_new_x, my_new_y);
				/*
				if((distance < 1.0f) && !(hasBall) && (tryCatchBehaviour == null)){
						tryCatchBehaviour = new TryCatchBehaviour(this.myAgent);
						this.myAgent.addBehaviour(tryCatchBehaviour);
				} else {
				
				}
				*/
			}
		}
	}
}
