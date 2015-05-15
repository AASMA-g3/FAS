package pt.ulisboa.aasma.fas.jade.agents.bdi;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import pt.ulisboa.aasma.fas.jade.agents.AgentMessages;
import pt.ulisboa.aasma.fas.jade.agents.reactive.DefenderAgentReactive;
import pt.ulisboa.aasma.fas.jade.agents.reactive.KeeperAgentReactive;
import pt.ulisboa.aasma.fas.jade.agents.reactive.StrikerAgentReactive;
import pt.ulisboa.aasma.fas.jade.game.Ball;
import pt.ulisboa.aasma.fas.jade.game.Game;
import pt.ulisboa.aasma.fas.jade.game.Player;
import bdi4jade.core.SingleCapabilityAgent;

/**
 * Super class from which all Agents inherit
 * It's basically responsible to kill the agent when the game ends 
 * And holds the behaviours that interact with the ball since all
 * agents interact with the ball in the same way.
 * @author Fábio
 *
 */
public class PlayerAgentBDI extends SingleCapabilityAgent {
	

	private static final long serialVersionUID = 1L;
	
	protected static final int SMALL_BEHAVIOUR_DELAY = 500;
	protected static final int MEDIUM_BEHAVIOUR_DELAY = 1000;
	protected static final int BIG_BEHAVIOUR_DELAY = 1500;
	
	protected static final int NOT_TRYING_BEHAVIOUR = 0;
	protected static final int WAITING_ANSWER = 1;
	protected static final int SUCCEDED = 2;
	protected static final int FAILED = 3;
	
	protected static AID ballAgent = new AID("Ball", AID.ISLOCALNAME);
	protected Game match;
	Player player;
	protected Boolean gameStarted = false;
	
	protected int tryBehaviour = NOT_TRYING_BEHAVIOUR;
	
	protected boolean lostTheBall = false;

	/**
	 * Set's up the agents, checking the existence of the Agent in the game structure
	 * Also adds the behaviour to listen to the END_GAME message
	 */
	public PlayerAgentBDI(Game game, Player player) {
		super.setup();
		
		this.match = game;
		this.player = player;
		
		addBehaviour(new ReceiveAgreeBehaviour(this));
		addBehaviour(new ReceiveRefuseBehaviour(this));
		addBehaviour(new ReceiveInformBehaviour(this));
		
	}
	
	/**
	 * Try to catch the ball by sending a message to the ball
	 * @author Fábio
	 *
	 */
	protected class TryCatchBehaviour extends OneShotBehaviour{
		private static final long serialVersionUID = 1L;
		
		public TryCatchBehaviour(Agent agent) {
			super(agent);
//			tryCatchBehaviour = PlayerAgent.WAITING_ANSWER;
			tryBehaviour = WAITING_ANSWER;
		}
		
		@Override
		public void action() {
				ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
				msg.addReceiver(ballAgent);
				msg.setOntology(AgentMessages.TRY_CATCH);	
				send(msg);
//				System.out.println(myAgent.getName() + ": Try Catch!");
		}
	}
	
	/**
	 * Try to dominate the ball by sending a message to the ball
	 * @author Fábio
	 *
	 */
	protected class TryReceiveBehaviour extends OneShotBehaviour{
		private static final long serialVersionUID = 1L;
		public TryReceiveBehaviour(Agent agent) {
			super(agent);
//			tryReceiveBehaviour = PlayerAgent.WAITING_ANSWER;
			tryBehaviour = WAITING_ANSWER;
		}
		@Override
		public void action() {
				ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
				msg.addReceiver(ballAgent);
				msg.setOntology(AgentMessages.TRY_RECEIVE);	
				send(msg);
//				System.out.println(myAgent.getName() + ": Try Receive");
		}
	}
	
	/**
	 * Try to steal the ball from other player by sending a message to the ball
	 * @author Fábio
	 *
	 */
	protected class TryTackleBehaviour extends OneShotBehaviour{
		private static final long serialVersionUID = 1L;
		public TryTackleBehaviour(Agent agent) {
			super(agent);
//			tryTackleBehaviour = PlayerAgent.WAITING_ANSWER;
			tryBehaviour = WAITING_ANSWER;
		}
		@Override
		public void action() {
				ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
				msg.addReceiver(ballAgent);
				msg.setOntology(AgentMessages.TRY_TACKLE);	
				send(msg);
//				System.out.println(myAgent.getName() + ": Try Tackle");
		}
	}

	/**
	 * Try to intercept the ball by sending a message to the ball
	 * @author Fábio
	 *
	 */
	protected class TryInterceptBehaviour extends OneShotBehaviour{
		private static final long serialVersionUID = 1L;
		public TryInterceptBehaviour(Agent agent) {
			super(agent);
//			tryInterceptBehaviour = PlayerAgent.WAITING_ANSWER;
			tryBehaviour = WAITING_ANSWER;
		}
		@Override
		public void action() {
				ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
				msg.addReceiver(ballAgent);
				msg.setOntology(AgentMessages.TRY_INTERCEPT);	
				send(msg);
//				System.out.println(myAgent.getName() + ": Try Intercept");
		}
	}
	
	/**
	 * Behaviour to pass the ball
	 * @author Fábio
	 *
	 */
	protected class PassBallBehaviour extends OneShotBehaviour{
		private static final long serialVersionUID = 1L;
		
		private int intensity;
		private double direction;
		
		public PassBallBehaviour(Agent agent, Player p1) {
			super(agent);
//			tryPassBehaviour = PlayerAgent.WAITING_ANSWER;
			tryBehaviour = WAITING_ANSWER;
			
			double dist = player.getDistanceToBall(match.getBall());
			
			if (dist < 3)
				this.intensity = Ball.INTENSITY_SHORT_PASS;
			else if (dist >= 3 && dist <= 7)
				this.intensity = Ball.INTENSITY_MEDIUM_PASS;
			else
				this.intensity = Ball.INTENSITY_LONG_PASS;
			
			
			this.direction = player.getDirectionToPlayer(p1);
			
		}

		@Override
		public void action() {
			if(player.hasBall()){
				int prob = (int)(Math.random()*100);
				if (prob <= 50) 
					prob = -1;
				else
					prob = 1;
				
				direction += ((100-player.getPassingRatio())/10)*prob;
				
				ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
				msg.addReceiver(ballAgent);
				msg.setOntology(AgentMessages.PASS);
				msg.setContent(intensity + " " + direction);
				send(msg);
//				System.out.println(myAgent.getName() + ": Try Pass");
			} else {
//				tryPassBehaviour = PlayerAgent.FAILED;
				tryBehaviour = FAILED;
				myAgent.addBehaviour(new CleanPassBehaviour(myAgent, SMALL_BEHAVIOUR_DELAY));
			}
		}
	}
	
	protected class ShootBallBehaviour extends OneShotBehaviour{
		private static final long serialVersionUID = 1L;
		
		private double direction;
		
		public ShootBallBehaviour(Agent agent) {
			super(agent);
//			tryShootBehaviour = PlayerAgent.WAITING_ANSWER;
			tryBehaviour = WAITING_ANSWER;
			
			this.direction = player.getDirectionToEnemyGoal();
		}
		
		@Override
		public void action() {
			if(player.hasBall()){
				int prob = (int)(Math.random());
				if (prob == 0) prob = -1;
				direction += ((100-player.getShootingRatio())/10)*prob;
				
				ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
				msg.addReceiver(ballAgent);
				msg.setOntology(AgentMessages.SHOOT);
				msg.setContent(Ball.INTENSITY_SHOOT + " " + direction);
				send(msg);
//				System.out.println(myAgent.getName() + ": Try Shoot");
			} else {
//				tryShootBehaviour = PlayerAgent.FAILED;
				tryBehaviour = FAILED;
				myAgent.addBehaviour(new CleanShootBehaviour(myAgent, SMALL_BEHAVIOUR_DELAY));
			}
		}
	}

	protected class DribleBehaviour extends OneShotBehaviour{
		private static final long serialVersionUID = 1L;
		
		private double direction;
		
		public DribleBehaviour(Agent agent, double direction) {
			super(agent);
			this.direction = direction;
		}
		
		@Override
		public void action() {
			if(player.hasBall()){
				ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
				msg.addReceiver(ballAgent);
				msg.setOntology(AgentMessages.DRIBLE);
				msg.setContent(Ball.INTENSITY_RUN + " " + direction);
				send(msg);
				player.setNewGoal(Player.NEW_GOAL_NEW_DIRECTION, direction);
//				System.out.println(myAgent.getName() + ": Try Drible");
			}
		}
		
	}
	/**
	 * Recieve Agree messages from the ball
	 * @author Fábio
	 *
	 */
	protected class ReceiveAgreeBehaviour extends CyclicBehaviour{
		private static final long serialVersionUID = 1L;

		public ReceiveAgreeBehaviour(Agent agent) {
			super(agent);
		}
		
		@Override
		public void action() {
			ACLMessage msg = this.myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.AGREE));
			if(msg == null)
				block();
			else{
				switch (msg.getOntology()) {
					case AgentMessages.TRY_CATCH:
						player.setHasBall(true);
//						tryCatchBehaviour = PlayerAgent.SUCCEDED;
						tryBehaviour = SUCCEDED;
						myAgent.addBehaviour(new CleanCatchBehaviour(myAgent, BIG_BEHAVIOUR_DELAY));
						break;
					case AgentMessages.TRY_RECEIVE:
						player.setHasBall(true);
//						tryReceiveBehaviour = PlayerAgent.SUCCEDED;
						tryBehaviour = SUCCEDED;
						myAgent.addBehaviour(new CleanReceiveBehaviour(myAgent, SMALL_BEHAVIOUR_DELAY));
						break;
					case AgentMessages.TRY_TACKLE:
						tryBehaviour = SUCCEDED;
						player.setHasBall(true);
						myAgent.addBehaviour(new CleanTackleBehaviour(myAgent, SMALL_BEHAVIOUR_DELAY));
						break;
					case AgentMessages.TRY_INTERCEPT:
						tryBehaviour = SUCCEDED;
						player.setHasBall(true);
						myAgent.addBehaviour(new CleanInterceptBehaviour(myAgent, SMALL_BEHAVIOUR_DELAY));
						break;
					case AgentMessages.PASS:
						player.setHasBall(false);
//						tryPassBehaviour = PlayerAgent.SUCCEDED;
						tryBehaviour = SUCCEDED;
						myAgent.addBehaviour(new CleanPassBehaviour(myAgent, BIG_BEHAVIOUR_DELAY));
						break;
					case AgentMessages.SHOOT:
						player.setHasBall(false);
//						tryShootBehaviour = PlayerAgent.SUCCEDED;
						tryBehaviour = SUCCEDED;
						myAgent.addBehaviour(new CleanShootBehaviour(myAgent, BIG_BEHAVIOUR_DELAY));
						break;
					default:
						break;
				}
			}
		}
	}
	
	/**
	 * Receive the messages of success or refusal from the ball when trying to execute some behaviour
	 * @author Fábio
	 *
	 */
	protected class ReceiveRefuseBehaviour extends CyclicBehaviour{
		private static final long serialVersionUID = 1L;
		
		public ReceiveRefuseBehaviour(Agent agent) {
			super(agent);
		}
		
		@Override
		public void action() {
			ACLMessage msg = this.myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.REFUSE));
			if (msg == null)
				block();
			else{
				switch (msg.getOntology()) {
				case AgentMessages.TRY_CATCH:
//					tryCatchBehaviour = PlayerAgent.FAILED;
					tryBehaviour = FAILED;
					myAgent.addBehaviour(new CleanCatchBehaviour(myAgent, BIG_BEHAVIOUR_DELAY));
					break;
				case AgentMessages.TRY_RECEIVE:
//					tryReceiveBehaviour = PlayerAgent.FAILED;
					tryBehaviour = FAILED;
					myAgent.addBehaviour(new CleanReceiveBehaviour(myAgent, MEDIUM_BEHAVIOUR_DELAY));
					break;
				case AgentMessages.TRY_TACKLE:
//					tryTackleBehaviour = PlayerAgent.FAILED;
					tryBehaviour = FAILED;
					myAgent.addBehaviour(new CleanTackleBehaviour(myAgent, BIG_BEHAVIOUR_DELAY));
					break;
				case AgentMessages.TRY_INTERCEPT:
//					tryInterceptBehaviour = PlayerAgent.FAILED;
					tryBehaviour = FAILED;
					myAgent.addBehaviour(new CleanInterceptBehaviour(myAgent, MEDIUM_BEHAVIOUR_DELAY));
					break;
				case AgentMessages.PASS:
//					tryPassBehaviour = PlayerAgent.FAILED;
					tryBehaviour = FAILED;
					myAgent.addBehaviour(new CleanPassBehaviour(myAgent, MEDIUM_BEHAVIOUR_DELAY));
					break;
				case AgentMessages.SHOOT:
//					tryShootBehaviour = PlayerAgent.FAILED;
					tryBehaviour = FAILED;
					myAgent.addBehaviour(new CleanShootBehaviour(myAgent, BIG_BEHAVIOUR_DELAY));
					break;
				default:
					break;
				}
			}
		}
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
					break;
					case AgentMessages.END_GAME:
						doDelete();
						break;
					case AgentMessages.PAUSE_GAME:
						gameStarted = false;
						player.setNewGoal(Player.NEW_GOAL_POSITION_AREA);
						break;
					case AgentMessages.RESTART_GAME:
						tryBehaviour = NOT_TRYING_BEHAVIOUR;
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
	
	protected class CleanCatchBehaviour extends WakerBehaviour{
		private static final long serialVersionUID = 1L;

		public CleanCatchBehaviour(Agent arg0, int timer) {
			super(arg0, timer);
		}

		@Override
		protected void onWake() {
			super.onWake();
//			tryCatchBehaviour = NOT_TRYING_BEHAVIOUR;
			tryBehaviour = NOT_TRYING_BEHAVIOUR;
		}
	}
	
	protected class CleanInterceptBehaviour extends WakerBehaviour{
		private static final long serialVersionUID = 1L;

		public CleanInterceptBehaviour(Agent arg0, int timer) {
			super(arg0, timer);
		}

		@Override
		protected void onWake() {
			super.onWake();
//			tryInterceptBehaviour = NOT_TRYING_BEHAVIOUR;
			tryBehaviour = NOT_TRYING_BEHAVIOUR;
		}
	}
	
	protected class CleanReceiveBehaviour extends WakerBehaviour{
		private static final long serialVersionUID = 1L;

		public CleanReceiveBehaviour(Agent arg0, int timer) {
			super(arg0, timer);
		}

		@Override
		protected void onWake() {
			super.onWake();
//			tryReceiveBehaviour = NOT_TRYING_BEHAVIOUR;
			tryBehaviour = NOT_TRYING_BEHAVIOUR;
		}
	}
	
	protected class CleanTackleBehaviour extends WakerBehaviour{
		private static final long serialVersionUID = 1L;

		public CleanTackleBehaviour(Agent arg0, int timer) {
			super(arg0, timer);
		}

		@Override
		protected void onWake() {
			super.onWake();
//			tryTackleBehaviour = NOT_TRYING_BEHAVIOUR;
			tryBehaviour = NOT_TRYING_BEHAVIOUR;
		}
	}
	
	protected class CleanPassBehaviour extends WakerBehaviour{
		private static final long serialVersionUID = 1L;

		public CleanPassBehaviour(Agent arg0, int timer) {
			super(arg0, timer);
		}

		@Override
		protected void onWake() {
			super.onWake();
//			tryPassBehaviour = NOT_TRYING_BEHAVIOUR;
			tryBehaviour = NOT_TRYING_BEHAVIOUR;
		}
	}
	
	protected class CleanShootBehaviour extends WakerBehaviour{
		private static final long serialVersionUID = 1L;

		public CleanShootBehaviour(Agent arg0, int timer) {
			super(arg0, timer);
		}

		@Override
		protected void onWake() {
			super.onWake();
//			tryShootBehaviour = NOT_TRYING_BEHAVIOUR;
			tryBehaviour = NOT_TRYING_BEHAVIOUR;
		}
	}
	
	protected class CleanLostBallBehaviour extends WakerBehaviour{
		private static final long serialVersionUID = 1L;

		public CleanLostBallBehaviour(Agent arg0) {
			super(arg0, BIG_BEHAVIOUR_DELAY);
		}

		@Override
		protected void onWake() {
			super.onWake();
			lostTheBall = false;
		}
	}
}
