package pt.ulisboa.aasma.fas.jade.agents.reactive;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import pt.ulisboa.aasma.fas.jade.game.Ball;
import pt.ulisboa.aasma.fas.jade.game.Game;
import pt.ulisboa.aasma.fas.jade.game.Player;

public class KeeperAgentReactive extends PlayerAgentReactive {
	
	private static final long serialVersionUID = 1L;

	public KeeperAgentReactive(Game game, Player player) {
		super(game, player);
	}
	
	@Override
	protected void setup() {
		super.setup();
	}

	protected class MainCycle extends CyclicBehaviour {
		private static final long serialVersionUID = 1L;
		
		public MainCycle(Agent a) {
			super(a);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void action() {

			if (gameStarted && !lostTheBall){
				Ball ball = match.getBall();
				
				if(ball.isOnTrajectoryToGoal(player.getTeam()) 
						&& ball.getOriginalIntensity() == Ball.INTENSITY_SHOOT){
						//The ball has been shot to the goal so we have to catch it!
					if(player.isAroundBall(ball) && tryBehaviour == PlayerAgentReactive.NOT_TRYING_BEHAVIOUR)
						addBehaviour(new TryCatchBehaviour(this.myAgent));
					else
						player.getPlayerMovement().setGoal(ball.x(), ball.y());
				}else if(player.hasBall() && tryBehaviour == PlayerAgentReactive.NOT_TRYING_BEHAVIOUR) {
					//I have the ball so let's pass it to a open player, if none open just hold.
					Player p1 = player.getFurthestAllyOpenPlayer(match.getTeamA(),
							match.getTeamB(), Player.DEFENDER);
					if(p1 != null){
						addBehaviour(new PassBallBehaviour(this.myAgent, p1));
						player.getPlayerMovement().setGoal(ball.x(), ball.y());
					}
					p1 = player.getFurthestAllyOpenPlayer(match.getTeamA(),
							match.getTeamB(), Player.STRIKER);
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
