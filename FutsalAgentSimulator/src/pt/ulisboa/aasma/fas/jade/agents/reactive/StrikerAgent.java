package pt.ulisboa.aasma.fas.jade.agents.reactive;

import pt.ulisboa.aasma.fas.jade.agents.AgentMessages;
import pt.ulisboa.aasma.fas.jade.agents.reactive.PlayerAgent.CleanLostBallBehaviour;
import pt.ulisboa.aasma.fas.jade.agents.reactive.PlayerAgent.TryReceiveBehaviour;
import pt.ulisboa.aasma.fas.jade.agents.reactive.PlayerAgent.TryTackleBehaviour;
import pt.ulisboa.aasma.fas.jade.game.Ball;
import pt.ulisboa.aasma.fas.jade.game.Player;
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
						doDelete();
						break;
					case AgentMessages.PAUSE_GAME:
						gameStarted = false;
						player.setNewGoal(Player.NEW_GOAL_POSITION_AREA);
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
			
			if (gameStarted && !lostTheBall){
				Ball ball = match.getBall();
				Player p1 = player.getNearestAllyOpenPlayer(match.getTeamA(), match.getTeamB());
				
				if(player.hasBall() && player.isNearEnemyGoal()){
					addBehaviour(new ShootBallBehaviour(this.myAgent));
				}else if(player.hasBall() && p1 != null){
					addBehaviour(new PassBallBehaviour(this.myAgent, p1));
				}else if(player.hasBall()){
					//advance
				}else if(ball.isOnQuadrant(player.getQuadrant()) && 
						ball.enemyHasBall(player.getTeam())){
					if(player.isAroundBall(ball) && tryTackleBehaviour == PlayerAgent.NOT_TRYING_BEHAVIOUR){
						myAgent.addBehaviour(new TryTackleBehaviour(myAgent));
						System.out.println("Tackle!");
					}else{
						player.getPlayerMovement().setGoal(ball.x(), ball.y());
					}
				}else if(ball.isOnQuadrant(player.getQuadrant()) && 
						!ball.enemyHasBall(player.getTeam())){
					//chase and receive
					if(player.isAroundBall(ball) && tryReceiveBehaviour == PlayerAgent.NOT_TRYING_BEHAVIOUR){
						myAgent.addBehaviour(new TryReceiveBehaviour(myAgent));
						System.out.println("Receive Ball!");
					}else{
						player.getPlayerMovement().setGoal(ball.x(), ball.y());
					}
				}else{
					//open line
					if(player.isOnGoal())
						player.setNewGoal(Player.NEW_GOAL_STRIKER_OFFENSIVE);
				}
			}
		}
	}
}
