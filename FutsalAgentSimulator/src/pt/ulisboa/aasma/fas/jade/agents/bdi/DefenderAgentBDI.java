package pt.ulisboa.aasma.fas.jade.agents.bdi;

import pt.ulisboa.aasma.fas.jade.agents.AgentMessages;
import pt.ulisboa.aasma.fas.jade.game.Ball;
import pt.ulisboa.aasma.fas.jade.game.Game;
import pt.ulisboa.aasma.fas.jade.game.Player;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class DefenderAgentBDI extends PlayerAgentBDI {
	
	private static final long serialVersionUID = 1L;
	
	public DefenderAgentBDI(Game game) {
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
//				Ball ball = match.getBall();
//				Player p1 = player.getNearestAllyOpenPlayer(match.getTeamA(), match.getTeamB());
//				
//				if(player.hasBall() && p1 != null && moveBallBehaviour == PlayerAgent.NOT_TRYING_BEHAVIOUR){
//					System.out.println("vou passar para o :" + p1.getName());
//					addBehaviour(new MoveBallBehaviour(
//									this.myAgent,
//									Ball.INTENSITY_LONG_PASS,
//									player.getDirectionToPlayer(p1)));
//				}else if(player.hasBall() && p1 == null){
//					//advance
//					player.setGoal(player.x() + 1.0f , player.y());
////					addBehaviour(new DribleBehaviour(this, player.x() + 1.0f , player.y()));
//				}
				
//				public void action() {
//				
//				if (gameStarted  && !lostTheBall){
//					Ball ball = match.getBall();
//					ArrayList<Player> myTeam = (player.getTeam() == Player.TEAM_A)? match.getTeamA() : match.getTeamB();
//					
					
//					if(ball.isOnTrajectoryToPlayer(player) && (Ball.INTENSITY_SHORT_PASS <= ball.getOriginalIntensity())
//							&& (ball.getOriginalIntensity() <= Ball.INTENSITY_LONG_PASS) && player.isAroundBall(ball) &&
//							tryReceiveBehaviour == PlayerAgent.NOT_TRYING_BEHAVIOUR){
//						// Ball has been passed to me and I'm close enough, so catch it
//						addBehaviour(new TryReceiveBehaviour(this.myAgent));
//						tryReceiveBehaviour = PlayerAgent.WAITING_ANSWER;
//						
//					}else if(ball.isOnTrajectoryToPlayer(player) && (Ball.INTENSITY_SHORT_PASS <= ball.getOriginalIntensity())
//							&& (ball.getOriginalIntensity() <= Ball.INTENSITY_LONG_PASS) && !player.isAroundBall(ball) &&
//							tryReceiveBehaviour == PlayerAgent.NOT_TRYING_BEHAVIOUR){
//						// They passed the ball to me so let's go to the ball
//						player.setNewGoal(Player.NEW_GOAL_NEW_DIRECTION, player.getDirectionToBall(ball));
//						
//					}else if(ball.isOnTrajectoryToGoal(player.getTeam()) &&	(ball.getOriginalIntensity() == Ball.INTENSITY_SHOOT)
//							&& player.isAroundBall(ball) && tryInterceptBehaviour == PlayerAgent.NOT_TRYING_BEHAVIOUR){
//						// ball was shooted and i'm in the intercept line! Defend it
//						addBehaviour(new TryInterceptBehaviour(this.myAgent));
//						tryInterceptBehaviour = PlayerAgent.WAITING_ANSWER;
//						
//					}else if(ball.isOnTrajectoryToGoal(player.getTeam()) && (ball.getOriginalIntensity() == Ball.INTENSITY_SHOOT)
//							&& !player.isAroundBall(ball) && tryInterceptBehaviour == PlayerAgent.NOT_TRYING_BEHAVIOUR){
//						// ball was shooted and i'm trying to intercept it
//						player.setNewGoal(Player.NEW_GOAL_INTERCEPT_GOAL, ball);
//						
//					}else if(ball.hasOwner() && ball.getOwner().getTeam() != player.getTeam() && 
//							player.isAroundPlayer(ball.getOwner()) && tryTackleBehaviour == PlayerAgent.NOT_TRYING_BEHAVIOUR){
//						// The ball is in possession of the other team and I'm the last man and close enough to tackle
//						myAgent.addBehaviour(new TryTackleBehaviour(myAgent));
//						tryTackleBehaviour = PlayerAgent.WAITING_ANSWER;
						
//					}else if(ball.hasOwner() && ball.getOwner().getTeam() != player.getTeam() && player.isLastDefender(myTeam)
//							&& !player.isAroundPlayer(ball.getOwner()) && tryTackleBehaviour == PlayerAgent.NOT_TRYING_BEHAVIOUR){
//						// The ball is in possession of the other team and I'm the last man, chase the ball
//						player.setGoal(ball.x(), ball.y());
//						
//					}else if(player.hasBall() && ){
//						// 
//						
//					}
//					}else if(ball.hasOwner() && 
//							ball.getOwner().getTeam() != player.getTeam() &&
//							player.isNearestToBall(myTeam, ball, false)){
//						System.out.println("ball is in possession of the other team but I'm not the last man"); 
//						//The ball is in possession of the other team but I'm not the last man
//						if(player.isAroundPlayer(ball.getOwner()) && tryTackleBehaviour == PlayerAgent.NOT_TRYING_BEHAVIOUR){
//							System.out.println("tackle");
//							myAgent.addBehaviour(new TryTackleBehaviour(myAgent));
//							tryTackleBehaviour = PlayerAgent.WAITING_ANSWER;
//						}else{
//							System.out.println("after ball!");
//							player.getPlayerMovement().setGoal(ball.x(), ball.y());
//						}
//					}else if(player.hasBall()){
//							Player p1 = player.getNearestAllyOpenPlayer(match.getTeamA(), match.getTeamB());
//							if(p1 != null && moveBallBehaviour == PlayerAgent.NOT_TRYING_BEHAVIOUR){
//								addBehaviour(new MoveBallBehaviour(this.myAgent, Ball.INTENSITY_LONG_PASS,
//										player.getDirectionToPlayer(p1)));
//								moveBallBehaviour = PlayerAgent.WAITING_ANSWER;
//								System.out.println("pass to "+p1.getName());
//							}else{
//								///Nobody open so I have to move around with the ball
//							}
//					}else if(ball.hasOwner() && 
//							ball.getOwner().getTeam() != player.getTeam()){
//						
//					}else if(player.isLastDefender(myTeam) && player.isBehindMidfield()){
//						if (player.getTeam() == Player.TEAM_A){
//							player.setNewGoal(Player.NEW_GOAL_NEW_DIRECTION, .0f);
//						}	
//						else{
//							player.setNewGoal(Player.NEW_GOAL_NEW_DIRECTION, 180.0f);
//						}
//					}else{
//						if (player.getTeam() == Player.TEAM_A){
//							player.setNewGoal(Player.NEW_GOAL_NEW_DIRECTION, 180.0f);
//						}	
//						else{
//							player.setNewGoal(Player.NEW_GOAL_NEW_DIRECTION, .0f);
//						}
//					}
					
//					if(player.hasBall() && p1 != null && moveBallBehaviour == PlayerAgent.NOT_TRYING_BEHAVIOUR){
//						System.out.println("vou passar para o :" + p1.getName());
//						addBehaviour(new MoveBallBehaviour(
//										this.myAgent,
//										Ball.INTENSITY_LONG_PASS,
//										player.getDirectionToPlayer(p1)));
//					}else if(player.hasBall() && p1 == null){
//						//advance
//						player.setGoal(player.x() + 1.0f , player.y());
//						addBehaviour(new DribleBehaviour(this, player.x() + 1.0f , player.y()));
//					}
					
				
//					Player p1 = player.getNearestAllyOpenPlayer(match.getTeamA(), match.getTeamB());
						
						
//					if(ball.hasOwner()){
//						if(ball.getOwner().getTeam() == player.getTeam()){ //My team has the ball
//							if(player.hasBall()){// I have the ball
//								if(player.isBehindMidfield()){ 
//									Player p1 = player.getNearestAllyOpenPlayer(match.getTeamA(), match.getTeamB());
//									if( p1 != null){ //I have an open team mate so pass the ball
//										addBehaviour(new PassBallBehaviour(this.myAgent, p1));
//									}else if (player){
//										
//									}
//								}else if( ){
//									
//								}
//							}else{
//								
//							}
//						}else { // The other team has the ball
//							
//						}
//					}else{ // Nobody has the ball
//						
//					}
			
			}
			
		}
	}
}
