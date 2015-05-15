package pt.ulisboa.aasma.fas.jade.agents.bdi;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import pt.ulisboa.aasma.fas.jade.agents.AgentMessages;
import pt.ulisboa.aasma.fas.jade.game.Ball;
import pt.ulisboa.aasma.fas.jade.game.Game;
import pt.ulisboa.aasma.fas.jade.game.Player;

public class KeeperAgentBDI extends PlayerAgentBDI {
	
	private static final long serialVersionUID = 1L;
	
	public KeeperAgentBDI(Game game){
		super(game);

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
						doDelete();
						break;
					case AgentMessages.PAUSE_GAME:
						gameStarted = false;
						if (player.getTeam() == Player.TEAM_A)
							player.setGoal(Player.TEAM_A_KEEPER_XPOS + 2, Game.GOAL_Y_MED);
						else
							player.getPlayerMovement().setGoal(Player.TEAM_B_KEEPER_XPOS - 2, Game.GOAL_Y_MED);;
						break;
					case AgentMessages.RESTART_GAME:
						tryCatchBehaviour = NOT_TRYING_BEHAVIOUR;
						tryReceiveBehaviour = NOT_TRYING_BEHAVIOUR;
						tryTackleBehaviour = NOT_TRYING_BEHAVIOUR;
						tryInterceptBehaviour = NOT_TRYING_BEHAVIOUR;
						tryPassBehaviour = NOT_TRYING_BEHAVIOUR;
						tryShootBehaviour = NOT_TRYING_BEHAVIOUR;
						gameStarted = true;
						lostTheBall = false;
						break;
					case AgentMessages.LOST_BALL:
						lostTheBall = true;
						myAgent.addBehaviour(new CleanLostBallBehaviour(myAgent));
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
				player.setGoal(Player.TEAM_A_KEEPER_XPOS, Game.GOAL_Y_MED);
			else
				player.setGoal(Player.TEAM_B_KEEPER_XPOS, Game.GOAL_Y_MED);
		}

		@Override
		public void action() {

			if (gameStarted && !lostTheBall){
				Ball ball = match.getBall();
				double distance = player.getDistanceToBall(ball);
				
				if(ball.isOnTrajectoryToGoal(player.getTeam()) &&
						(ball.getCurrentMovement().getOriginalIntensity() == Ball.INTENSITY_SHOOT)){
						//The ball has been shot to the goal so we have to catch it!

					if(distance < 1.0 &&
							tryCatchBehaviour == PlayerAgentBDI.NOT_TRYING_BEHAVIOUR){
						//The ball is close enough so let's try to catch it!
						addBehaviour(new TryCatchBehaviour(this.myAgent));
					}
				}else if(ball.isOnTrajectoryToGoal(player.getTeam()) &&
							(ball.getCurrentMovement().getOriginalIntensity() < Ball.INTENSITY_SHOOT) &&
							(ball.getCurrentMovement().getOriginalIntensity() > Ball.INTENSITY_RUN)){
					if(distance < 1.0 &&
							tryCatchBehaviour == PlayerAgentBDI.NOT_TRYING_BEHAVIOUR){
						//The ball has been passed to me so I have to control it.
						addBehaviour(new TryReceiveBehaviour(this.myAgent));
					}
				}else if(player.hasBall() && tryPassBehaviour == PlayerAgentBDI.NOT_TRYING_BEHAVIOUR) {
					//I have the ball so let's pass it to a open player, if none open just hold.
					Player p1 = player.getNearestAllyOpenPlayer(match.getTeamA(), match.getTeamB());
					if(p1 != null){
						addBehaviour(new PassBallBehaviour(this.myAgent, p1));
						player.getPlayerMovement().setGoal(ball.x(), ball.y());
					}
				}else{
					//Nothing of notice so I'll position myself 
					if (player.getTeam() == Player.TEAM_A){
						player.getPlayerMovement().setGoal(Player.TEAM_A_KEEPER_XPOS + 1, Game.GOAL_Y_MED);
					}	
					else{
						player.getPlayerMovement().setGoal(Player.TEAM_B_KEEPER_XPOS - 1, Game.GOAL_Y_MED);
					}
				}
			}
		}
	}
}
