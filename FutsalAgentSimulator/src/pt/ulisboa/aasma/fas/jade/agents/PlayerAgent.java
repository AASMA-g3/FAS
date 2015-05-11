package pt.ulisboa.aasma.fas.jade.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import pt.ulisboa.aasma.fas.exceptions.FutsalAgentSimulatorException;
import pt.ulisboa.aasma.fas.jade.game.Ball;
import pt.ulisboa.aasma.fas.jade.game.Game;
import pt.ulisboa.aasma.fas.jade.game.Player;

/**
 * Super class from which all Agents inherit
 * It's basically responsible to kill the agent when the game ends 
 * And holds the behaviours that interact with the ball since all
 * agents interact with the ball in the same way.
 * @author Fábio
 *
 */
public class PlayerAgent extends Agent {
	
	private static final long serialVersionUID = 1L;
	
	protected static final int NOT_TRYING_BEHAVIOUR = 0;
	protected static final int WAITING_ANSWER = 1;
	protected static final int SUCCEDED = 2;
	protected static final int FAILED = 3;
	
	protected static AID ballAgent = new AID("Ball", AID.ISLOCALNAME);
	protected Game match;
	Player player;
	protected Boolean gameStarted = false;
	
	protected int tryCatchBehaviour = NOT_TRYING_BEHAVIOUR;
	protected int tryReceiveBehaviour = NOT_TRYING_BEHAVIOUR;
	protected int tryTackleBehaviour = NOT_TRYING_BEHAVIOUR;
	protected int tryInterceptBehaviour = NOT_TRYING_BEHAVIOUR;
	protected int moveBallBehaviour = NOT_TRYING_BEHAVIOUR;

	/**
	 * Set's up the agents, checking the existence of the Agent in the game structure
	 * Also adds the behaviour to listen to the END_GAME message
	 */
	@Override
	protected void setup() {
		super.setup();
		
		Object[] args = getArguments();
		if (args == null || args.length == 0){
			System.out.println("Please input correct arguments.");
			return;
		}else{
			match = (Game) args[0];
		}
		
		try{
			player = match.getPlayer(getLocalName());
		} catch (FutsalAgentSimulatorException f){
			System.out.println(f.getMessage());
			return;
		}
		
		addBehaviour(new ReceiveAgreeBehaviour(this));
		addBehaviour(new ReceiveRefuseBehaviour(this));
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
		}
		
		@Override
		public void action() {
			ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
			msg.addReceiver(ballAgent);
			msg.setOntology(AgentMessages.TRY_CATCH);	
			send(msg);
			tryCatchBehaviour = PlayerAgent.WAITING_ANSWER;
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
		}
		@Override
		public void action() {
			ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
			msg.addReceiver(ballAgent);
			msg.setOntology(AgentMessages.TRY_RECEIVE);	
			send(msg);
			tryReceiveBehaviour = PlayerAgent.WAITING_ANSWER;
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
		}
		@Override
		public void action() {
			ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
			msg.addReceiver(ballAgent);
			msg.setOntology(AgentMessages.TRY_TACKLE);	
			send(msg);
			tryTackleBehaviour = PlayerAgent.WAITING_ANSWER;
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
		}
		@Override
		public void action() {
			ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
			msg.addReceiver(ballAgent);
			msg.setOntology(AgentMessages.TRY_INTERCEPT);	
			send(msg);
			tryInterceptBehaviour = PlayerAgent.WAITING_ANSWER;
		}
	}
	
	/**
	 * Behaviour to manipulate the ball
	 * @author Fábio
	 *
	 */
	protected class MoveBallBehaviour extends WakerBehaviour{
		private static final long serialVersionUID = 1L;
		
		private int intensity;
		private double direction;
		
		public MoveBallBehaviour(Agent agent, int intensity, double direction) {
			super(agent, 500);
			this.intensity = intensity;
			this.direction = direction;
		}

		
		public void onWake() {
			if(player.hasBall()){
				int prob = (int)(Math.random());
				if (prob == 0) prob = -1;
				
				if(intensity == Ball.INTENSITY_SHOOT){
					direction += ((100-player.getShootingRatio())/10)*prob;
					player.setHasBall(false);
				}
				else if((intensity == Ball.INTENSITY_LONG_PASS) || (intensity == Ball.INTENSITY_MEDIUM_PASS) ||
						(intensity == Ball.INTENSITY_SHORT_PASS)) {
					direction += ((100-player.getPassingRatio())/10)*prob;
					player.setHasBall(false);
				}
				
				ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
				msg.addReceiver(ballAgent);
				msg.setOntology(AgentMessages.MOVE_TO);
				msg.setContent(intensity + " " + direction);
				send(msg);
				moveBallBehaviour = PlayerAgent.WAITING_ANSWER;
			} else {
				moveBallBehaviour = PlayerAgent.FAILED;
			}
		}
	}

	protected class DribleBehaviour extends OneShotBehaviour{
		private static final long serialVersionUID = 1L;
		
		private float x;
		private float direction;
		
		public DribleBehaviour(Agent agent, float direction) {
			super(agent);
			this.x = x;
			this.direction = direction;
		}
		
		@Override
		public void action() {
			if(player.hasBall()){
				ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
				msg.addReceiver(ballAgent);
				msg.setOntology(AgentMessages.MOVE_TO);
				msg.setContent(Ball.INTENSITY_RUN + " " + direction);
				send(msg);
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
						tryCatchBehaviour = PlayerAgent.SUCCEDED;
						myAgent.addBehaviour(new CleanCatchBehaviour(myAgent));
						break;
					case AgentMessages.TRY_RECEIVE:
						player.setHasBall(true);
						tryReceiveBehaviour = PlayerAgent.SUCCEDED;
						myAgent.addBehaviour(new CleanReceiveBehaviour(myAgent));
						break;
					case AgentMessages.TRY_TACKLE:
						player.setHasBall(true);
						myAgent.addBehaviour(new CleanTackleBehaviour(myAgent));
						break;
					case AgentMessages.TRY_INTERCEPT:
						player.setHasBall(true);
						myAgent.addBehaviour(new CleanInterceptBehaviour(myAgent));
						break;
					case AgentMessages.MOVE_TO:
						moveBallBehaviour = PlayerAgent.SUCCEDED;
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
					tryCatchBehaviour = PlayerAgent.FAILED;
					myAgent.addBehaviour(new CleanCatchBehaviour(myAgent));
					break;
				case AgentMessages.TRY_RECEIVE:
					tryReceiveBehaviour = PlayerAgent.FAILED;
					myAgent.addBehaviour(new CleanReceiveBehaviour(myAgent));
					break;
				case AgentMessages.TRY_TACKLE:
					tryTackleBehaviour = PlayerAgent.FAILED;
					myAgent.addBehaviour(new CleanTackleBehaviour(myAgent));
					break;
				case AgentMessages.TRY_INTERCEPT:
					tryInterceptBehaviour = PlayerAgent.FAILED;
					myAgent.addBehaviour(new CleanInterceptBehaviour(myAgent));
					break;
				case AgentMessages.MOVE_TO:
					moveBallBehaviour = PlayerAgent.FAILED;
					break;
				default:
					break;
				}
			}
		}
	}
	
	protected class CleanCatchBehaviour extends WakerBehaviour{
		private static final long serialVersionUID = 1L;

		public CleanCatchBehaviour(Agent arg0) {
			super(arg0, 100);
		}

		@Override
		protected void onWake() {
			super.onWake();
			tryCatchBehaviour = NOT_TRYING_BEHAVIOUR;
		}
	}
	
	protected class CleanInterceptBehaviour extends WakerBehaviour{
		private static final long serialVersionUID = 1L;

		public CleanInterceptBehaviour(Agent arg0) {
			super(arg0, 100);
		}

		@Override
		protected void onWake() {
			super.onWake();
			tryInterceptBehaviour = NOT_TRYING_BEHAVIOUR;
		}
	}
	
	protected class CleanReceiveBehaviour extends WakerBehaviour{
		private static final long serialVersionUID = 1L;

		public CleanReceiveBehaviour(Agent arg0) {
			super(arg0, 100);
		}

		@Override
		protected void onWake() {
			super.onWake();
			tryReceiveBehaviour = NOT_TRYING_BEHAVIOUR;
		}
	}
	
	protected class CleanTackleBehaviour extends WakerBehaviour{
		private static final long serialVersionUID = 1L;

		public CleanTackleBehaviour(Agent arg0) {
			super(arg0, 100);
		}

		@Override
		protected void onWake() {
			super.onWake();
			tryTackleBehaviour = NOT_TRYING_BEHAVIOUR;
		}
	}
}
