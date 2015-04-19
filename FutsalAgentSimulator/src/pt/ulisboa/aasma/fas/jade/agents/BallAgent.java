package pt.ulisboa.aasma.fas.jade.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import pt.ulisboa.aasma.fas.jade.game.Ball;
import pt.ulisboa.aasma.fas.jade.game.BallMovement;
import pt.ulisboa.aasma.fas.jade.game.Game;
import pt.ulisboa.aasma.fas.jade.game.Player;


/**
 * Agent that represents the ball.
 * It responds to requests (to be shot, passed and dribled) and queries (ball from players  
 * @author F�bio
 *
 */
public class BallAgent extends Agent{
	private static final long serialVersionUID = 1L;
	
	private String owner;
	
	private Game match;
	private Ball ball;
	
	@Override
	protected void setup() {
		super.setup();
		Object[] args = getArguments();
		if (args == null || args.length == 0){
			System.out.println("Please input correct arguments.");
			return;
		} else{
			match = (Game) args[0];
		}
		
		ball = match.getBall();
		owner = "";
		
		this.addBehaviour(new ReceiveRequestBehaviour(this));
		this.addBehaviour(new EndGameBehaviour());
		ball.setCurrentMovement(new BallMovement(10, 180.0f, 20, 10, match.getGameTime()/1000.0f));
	}
	
	
	/**
	 * Behaviour to listen to requests from the Players, like passing, shooting and dribling the ball.
	 * @author F�bio
	 *
	 */
	protected class ReceiveRequestBehaviour extends CyclicBehaviour{
		private static final long serialVersionUID = 1L;
		
		public ReceiveRequestBehaviour(Agent agent) {
			super(agent);
		}

		@Override
		public void action() {
			ACLMessage msg = this.myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
			if (msg != null) {

				switch (msg.getOntology()) {
				case AgentMessages.TRY_CATCH:
					this.myAgent.addBehaviour(new TryCatchBehaviour(this.myAgent, match.getPlayer(msg.getSender().getLocalName())));
					break;
				case AgentMessages.TRY_RECEIVE:
					this.myAgent.addBehaviour(new TryReceiveBehaviour(this.myAgent, match.getPlayer(msg.getSender().getLocalName())));
					break;
				case AgentMessages.TRY_TACKLE:
					this.myAgent.addBehaviour(new TryTackleBehaviour(this.myAgent, match.getPlayer(msg.getSender().getLocalName())));
					break;
				case AgentMessages.TRY_INTERCEPT:
					this.myAgent.addBehaviour(new TryInterceptBehaviour(this.myAgent, match.getPlayer(msg.getSender().getLocalName())));
					break;
				case AgentMessages.MOVE_TO:
					System.out.println(msg.getContent());
					String[] words = msg.getContent().split(" ");  
					int intensity = Integer.parseInt(words[0]);
					double direction = Double.parseDouble(words[1]);
					Player player = match.getPlayer(msg.getSender().getLocalName());
					ball.setCurrentMovement(new BallMovement(intensity, direction, player.x(), player.y(), match.getGameTime()/1000.0f));
					if(intensity > Ball.INTENSITY_RUN) owner = "";
					break;
				default:
					break;
				}
			} else {
				block();
			}
		}
	}
	
	/**
	 * Behaviour that processes the request of catching the ball
	 * It will signal the requester player with a message
	 * @author F�bio
	 *
	 */
	protected class TryCatchBehaviour extends OneShotBehaviour{
		private static final long serialVersionUID = 1L;
		private Player player;
		
		public TryCatchBehaviour(Agent agent, Player player) {
			super(agent);
			this.player = player;
		}

		@Override
		public void action() {
			if (owner.equals("")){
				int prob = (int)(Math.random()*100);
				if(prob < player.getGoalKeepingRatio()){
					ball.setCurrentMovement(new BallMovement(0, 0.0f, player.x(), player.y(), match.getGameTime()/1000.0f));
					owner = player.getName();
					ACLMessage msg = new ACLMessage(ACLMessage.AGREE);
					msg.addReceiver(new AID(player.getName(), AID.ISLOCALNAME));
					msg.setOntology(AgentMessages.TRY_CATCH);
					send(msg);
					return;
				}
			}
			ACLMessage msg = new ACLMessage(ACLMessage.REFUSE);
			msg.addReceiver(new AID(player.getName(), AID.ISLOCALNAME));
			msg.setOntology(AgentMessages.TRY_CATCH);
			send(msg);
		}
	}

	/**
	 * Behaviour that processes the request of receiving the ball
	 * It will signal the requester player with a message
	 * @author F�bio
	 *
	 */
	protected class TryReceiveBehaviour extends OneShotBehaviour{
		private static final long serialVersionUID = 1L;
		private Player player;
		
		public TryReceiveBehaviour(Agent agent, Player player) {
			super(agent);
			this.player = player;
		}

		@Override
		public void action() {
			if (owner.equals("")){
				int prob = (int)(Math.random()*100);
				if(prob < player.getPassingRatio()){
					ball.setCurrentMovement(new BallMovement(0, 0.0f, player.x(), player.y(), match.getGameTime()/1000.0f));
					owner = player.getName();
					ACLMessage msg = new ACLMessage(ACLMessage.AGREE);
					msg.addReceiver(new AID(player.getName(), AID.ISLOCALNAME));
					msg.setOntology(AgentMessages.TRY_RECEIVE);
					send(msg);
					return;
				}
			}
			ACLMessage msg = new ACLMessage(ACLMessage.REFUSE);
			msg.addReceiver(new AID(player.getName(), AID.ISLOCALNAME));
			msg.setOntology(AgentMessages.TRY_RECEIVE);
			send(msg);
		}
	}

	/**
	 * Behaviour that processes the request of stealing the ball from the current owner
	 * It will signal the requester player with a message
	 * @author F�bio
	 *
	 */
	protected class TryTackleBehaviour extends OneShotBehaviour{
		private static final long serialVersionUID = 1L;
		private Player player;
		
		public TryTackleBehaviour(Agent agent, Player player) {
			super(agent);
			this.player = player;
		}

		@Override
		public void action() {
			int prob = (int)(Math.random()*100);
			if(prob > player.getDribblingRatio()){
				ACLMessage msg = new ACLMessage(ACLMessage.REFUSE);
				msg.addReceiver(new AID(player.getName(), AID.ISLOCALNAME));
				msg.setOntology(AgentMessages.TRY_TACKLE);
				send(msg);
				return;
			} else {
				ball.setCurrentMovement(new BallMovement(0, 0.0f, player.x(), player.y(), match.getGameTime()/1000.0f));
				owner = player.getName();
				ACLMessage msg = new ACLMessage(ACLMessage.AGREE);
				msg.addReceiver(new AID(player.getName(), AID.ISLOCALNAME));
				msg.setOntology(AgentMessages.TRY_TACKLE);
				send(msg);
				return;
			}
		}
	}
	
	/**
	 * 
	 * @author F�bio
	 *
	 */
	protected class TryInterceptBehaviour extends OneShotBehaviour{
		private static final long serialVersionUID = 1L;
		private Player player;
		
		public TryInterceptBehaviour(Agent agent, Player player) {
			super(agent);
			this.player = player;
		}

		@Override
		public void action() {
			int prob = (int)(Math.random()*100);
			if(prob > player.getDefendingRatio()){
				ACLMessage msg = new ACLMessage(ACLMessage.REFUSE);
				msg.addReceiver(new AID(player.getName(), AID.ISLOCALNAME));
				msg.setOntology(AgentMessages.TRY_INTERCEPT);
				send(msg);
				return;
			} else {
				ball.setCurrentMovement(new BallMovement(0, 0.0f, player.x(), player.y(), match.getGameTime()/1000.0f));
				owner = player.getName();
				ACLMessage msg = new ACLMessage(ACLMessage.AGREE);
				msg.addReceiver(new AID(player.getName(), AID.ISLOCALNAME));
				msg.setOntology(AgentMessages.TRY_INTERCEPT);
				send(msg);
				return;
			}
		}
	}
	
	/**
	 * Behaviour to listen to the END_GAME message sent by the ReporterAgent that kills
	 * the Agent
	 * @author F�bio
	 *
	 */
	protected class EndGameBehaviour extends CyclicBehaviour {
		private static final long serialVersionUID = 1L;

		@Override
		public void action() {
			ACLMessage msg = receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
			if (msg != null && msg.getContent().equals(AgentMessages.END_GAME)){
				doDelete();
			} else {
				block();
			}
			
		}
	}
}
