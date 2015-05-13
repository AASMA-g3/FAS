package pt.ulisboa.aasma.fas.jade.agents.reactive;

import pt.ulisboa.aasma.fas.jade.agents.AgentMessages;
import pt.ulisboa.aasma.fas.jade.agents.reactive.PlayerAgent.MoveBallBehaviour;
import pt.ulisboa.aasma.fas.jade.game.Ball;
import pt.ulisboa.aasma.fas.jade.game.Game;
import pt.ulisboa.aasma.fas.jade.game.Player;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class DefenderAgent extends PlayerAgent {
	
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
				Ball ball = match.getBall();
				Player p1 = player.getNearestAllyOpenPlayer(match.getTeamA(), match.getTeamB());
				
				if(player.hasBall() && p1 != null && moveBallBehaviour == PlayerAgent.NOT_TRYING_BEHAVIOUR){
					System.out.println("vou passar para o :" + p1.getName());
					addBehaviour(new MoveBallBehaviour(
									this.myAgent,
									Ball.INTENSITY_LONG_PASS,
									player.getDirectionToPlayer(p1)));
				}else if(player.hasBall() && p1 == null){
					//advance
					player.setGoal(player.x() + 1.0f , player.y());
//					addBehaviour(new DribleBehaviour(this, player.x() + 1.0f , player.y()));
				}
				
			
			
			}
			
		}
	}
}
