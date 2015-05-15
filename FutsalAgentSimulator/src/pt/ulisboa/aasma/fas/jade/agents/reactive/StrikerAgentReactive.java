package pt.ulisboa.aasma.fas.jade.agents.reactive;

import pt.ulisboa.aasma.fas.jade.agents.AgentMessages;
import pt.ulisboa.aasma.fas.jade.agents.reactive.PlayerAgentReactive.CleanLostBallBehaviour;
import pt.ulisboa.aasma.fas.jade.agents.reactive.PlayerAgentReactive.TryReceiveBehaviour;
import pt.ulisboa.aasma.fas.jade.agents.reactive.PlayerAgentReactive.TryTackleBehaviour;
import pt.ulisboa.aasma.fas.jade.game.Ball;
import pt.ulisboa.aasma.fas.jade.game.Player;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class StrikerAgentReactive extends PlayerAgentReactive {
	
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
						doDelete();
						break;
					case AgentMessages.PAUSE_GAME:
						gameStarted = false;
						player.setNewGoal(Player.NEW_GOAL_POSITION_AREA);
						break;
					case AgentMessages.RESTART_GAME:
//						tryCatchBehaviour = NOT_TRYING_BEHAVIOUR;
//						tryReceiveBehaviour = NOT_TRYING_BEHAVIOUR;
//						tryTackleBehaviour = NOT_TRYING_BEHAVIOUR;
//						tryInterceptBehaviour = NOT_TRYING_BEHAVIOUR;
//						tryPassBehaviour = NOT_TRYING_BEHAVIOUR;
//						tryShootBehaviour = NOT_TRYING_BEHAVIOUR;
						tryBehaviour = PlayerAgentReactive.NOT_TRYING_BEHAVIOUR;
						gameStarted = true;
						lostTheBall = false;
						break;
					case AgentMessages.LOST_BALL:
						lostTheBall = true;
						myAgent.addBehaviour(new CleanLostBallBehaviour(myAgent));
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
			
			if (gameStarted && !lostTheBall && tryBehaviour == PlayerAgentReactive.NOT_TRYING_BEHAVIOUR){
				Ball ball = match.getBall();
				Player p1 = player.getNearestAllyOpenPlayer(match.getTeamA(), match.getTeamB());
				double dir = player.getDirectionToEnemyGoal();
				
				if(player.hasBall() && player.isNearEnemyGoal() 
//						&& tryBehaviour == PlayerAgent.NOT_TRYING_BEHAVIOUR 
//						&& tryTackleBehaviour == PlayerAgent.NOT_TRYING_BEHAVIOUR
//						&& tryPassBehaviour == PlayerAgent.NOT_TRYING_BEHAVIOUR
//						&& tryShootBehaviour == PlayerAgent.NOT_TRYING_BEHAVIOUR
						){
					myAgent.addBehaviour(new ShootBallBehaviour(this.myAgent));
				}else if(player.hasBall() && player.hasClearShot(
						(player.getTeam() == Player.TEAM_A)? match.getTeamB() : match.getTeamB(), ball)){
					
					myAgent.addBehaviour(new DribleBehaviour(myAgent, dir));
				}else if(player.hasBall() && p1 != null
//						&& tryBehaviour == PlayerAgent.NOT_TRYING_BEHAVIOUR
//						&& tryTackleBehaviour == PlayerAgent.NOT_TRYING_BEHAVIOUR
//						&& tryPassBehaviour == PlayerAgent.NOT_TRYING_BEHAVIOUR
//						&& tryShootBehaviour == PlayerAgent.NOT_TRYING_BEHAVIOUR
						){
					myAgent.addBehaviour(new PassBallBehaviour(this.myAgent, p1));
				}else if(player.hasBall()){
					//advance
					myAgent.addBehaviour(new DribleBehaviour(myAgent, dir));
				}else if(ball.isOnQuadrant(player.getQuadrant()) 
						&& ball.enemyHasBall(player.getTeam()) 
						&& ball.getOwner().getTeam() == player.getTeam()){
					
					player.setNewGoal(Player.NEW_GOAL_STRIKER_OFFENSIVE);
				}else if(ball.isOnQuadrant(player.getQuadrant()) && 
						ball.enemyHasBall(player.getTeam())){
					if(player.isAroundBall(ball)
//							&& tryBehaviour == PlayerAgent.NOT_TRYING_BEHAVIOUR
//							&& tryTackleBehaviour == PlayerAgent.NOT_TRYING_BEHAVIOUR
//							&& tryPassBehaviour == PlayerAgent.NOT_TRYING_BEHAVIOUR
//							&& tryShootBehaviour == PlayerAgent.NOT_TRYING_BEHAVIOUR
							){
						myAgent.addBehaviour(new TryTackleBehaviour(myAgent));
					}else{
						player.getPlayerMovement().setGoal(ball.x(), ball.y());
					}
				}else if(ball.isOnQuadrant(player.getQuadrant()) && 
						!ball.enemyHasBall(player.getTeam())){
					//chase and receive
					if(player.isAroundBall(ball) 
//							&& tryBehaviour == PlayerAgent.NOT_TRYING_BEHAVIOUR
//							&& tryReceiveBehaviour == PlayerAgent.NOT_TRYING_BEHAVIOUR
//							&& tryPassBehaviour == PlayerAgent.NOT_TRYING_BEHAVIOUR
//							&& tryShootBehaviour == PlayerAgent.NOT_TRYING_BEHAVIOUR
							){
						myAgent.addBehaviour(new TryReceiveBehaviour(myAgent));
					}else{
						player.getPlayerMovement().setGoal(ball.x(), ball.y());
					}
				}else{
					//open line
					if(player.isOnGoal()){
						player.setNewGoal(Player.NEW_GOAL_STRIKER_OFFENSIVE);
//						System.out.println(player.getName() +player.getDistanceToBall(ball));

//						System.out.println(player.getName() + "x: " + player.getPlayerMovement().getGoalX()
//								+ " y: " + player.getPlayerMovement().getGoalY());
//						System.out.println("ball x: " + ball.x() + " ball y: " + ball.y());
					}
				}
				
//				System.out.println(player.getName() + " " + tryBehaviour);
			}
			
			
		}
	}
}
