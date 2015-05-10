package pt.ulisboa.aasma.fas.jade.agents;

import jade.core.Agent;
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
						addBehaviour(new MainCycle(this.myAgent));
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
		
		public MainCycle(Agent agent) {
			super(agent);
			
			if (player.getTeam() == Player.TEAM_A)
				player.getPlayerMovement().setGoal(Player.TEAM_A_KEEPER_XPOS, Game.GOAL_Y_MED);
			else
				player.getPlayerMovement().setGoal(Player.TEAM_B_KEEPER_XPOS, Game.GOAL_Y_MED);
		}

		@Override
		public void action() {

			if (gameStarted){
				Ball ball = match.getBall();
				double distance = player.getDistanceToBall(ball);
				
				if(ball.isOnTrajectoryToGoal(player.getTeam()) &&
						(ball.getCurrentMovement().getOriginalIntensity() == Ball.INTENSITY_SHOOT)){
						//The ball has been shot to the goal so we have to catch it!
					
					if(distance < 1.0 &&
							tryCatchBehaviour != PlayerAgent.WAITING_ANSWER &&
							tryCatchBehaviour != PlayerAgent.FAILED){
						//The ball is close enough so let's try to catch it!
						System.out.println("Remate!");
						addBehaviour(new TryCatchBehaviour(this.myAgent));
						tryCatchBehaviour = PlayerAgent.WAITING_ANSWER;
					}else{
						//The ball is to far away or I failed to defend it, so let's get in position!
					}
				}else if(ball.isOnTrajectoryToGoal(player.getTeam()) &&
							(ball.getCurrentMovement().getOriginalIntensity() < Ball.INTENSITY_SHOOT) &&
							(ball.getCurrentMovement().getOriginalIntensity() > Ball.INTENSITY_RUN)){
						//The ball has been passed to me so I have to control it.
				}else if(hasBall) {
					//I have the ball so let's pass it to a open player, if none open just hold.
					Player p1 = player.getNearestAllyOpenPlayer(match.getTeamA(), match.getTeamB());
					System.out.println("vou passar para o :" + p1.getName());
					if(p1 != null)
						addBehaviour(new MoveBallBehaviour(this.myAgent, Ball.INTENSITY_LONG_PASS,
								player.getDirectionToPlayer(p1)));
				}else{
					//Nothing of notice so I'll position myself 
					if (player.getTeam() == Player.TEAM_A){
						player.getPlayerMovement().setGoal(player.x() + 1, Game.GOAL_Y_MED);
					}	
					else{
						player.getPlayerMovement().setGoal(Player.TEAM_B_KEEPER_XPOS, Game.GOAL_Y_MED);
					}
				}
			}
		}
	}
}
