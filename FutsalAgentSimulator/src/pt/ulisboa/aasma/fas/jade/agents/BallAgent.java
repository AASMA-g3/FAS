package pt.ulisboa.aasma.fas.jade.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import pt.ulisboa.aasma.fas.jade.game.Ball;
import pt.ulisboa.aasma.fas.jade.game.Game;
import pt.ulisboa.aasma.fas.jade.game.Player;


/**
 * Agent that represents the ball.
 * It responds to requests (to be shot, passed and dribled) and queries (ball from players  
 * @author Fábio
 *
 */
public class BallAgent extends Agent{
	private static final long serialVersionUID = 1L;
	
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
		ball.setOwner(null);
		
		this.addBehaviour(new ReceiveRequestBehaviour(this));
		this.addBehaviour(new EndGameBehaviour());
		double prob = Math.round(Math.random()*100);
		double direction = 0.0f;
		if (prob <= 50) direction = 180.0f;
		ball.updateCurrentMovement(Ball.INTENSITY_SHORT_PASS, direction, 20.0f, 10.0f, match.getGameTime()/1000.0f);
	}
	
	
	/**
	 * Behaviour to listen to requests from the Players, like passing, shooting and dribling the ball.
	 * @author Fábio
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
				String[] words;
				double direction;
				int intensity;
				switch (msg.getOntology()) {
					case AgentMessages.TRY_CATCH:
						this.myAgent.addBehaviour(new TryCatchBehaviour(this.myAgent, 
								match.getPlayer(msg.getSender().getLocalName())));
						break;
					case AgentMessages.TRY_RECEIVE:
						this.myAgent.addBehaviour(new TryReceiveBehaviour(this.myAgent, 
								match.getPlayer(msg.getSender().getLocalName())));
						break;
					case AgentMessages.TRY_TACKLE:
						this.myAgent.addBehaviour(new TryTackleBehaviour(this.myAgent, 
								match.getPlayer(msg.getSender().getLocalName())));
						break;
					case AgentMessages.TRY_INTERCEPT:
						this.myAgent.addBehaviour(new TryInterceptBehaviour(this.myAgent, 
								match.getPlayer(msg.getSender().getLocalName())));
						break;
					case AgentMessages.SHOOT:
						words = msg.getContent().split(" ");  
						direction = Double.parseDouble(words[1]);
						this.myAgent.addBehaviour(new TryShootBehaviour(this.myAgent,
								match.getPlayer(msg.getSender().getLocalName()), direction));
						break;
					case AgentMessages.PASS:
						words = msg.getContent().split(" ");  
						direction = Double.parseDouble(words[1]);
						intensity = Integer.parseInt(words[0]);
						this.myAgent.addBehaviour(new TryPassBehaviour(this.myAgent,
								match.getPlayer(msg.getSender().getLocalName()), intensity, direction));
						break;
					case AgentMessages.DRIBLE:
						words = msg.getContent().split(" ");  
						direction = Double.parseDouble(words[1]);
						this.myAgent.addBehaviour(new TryDribleBehaviour(this.myAgent,
								match.getPlayer(msg.getSender().getLocalName()), direction));
						break;
					default:
						break;
				}
			} else {
				block();
			}
		}
	}

	
	protected class TryDribleBehaviour extends OneShotBehaviour{
		private static final long serialVersionUID = 1L;
		private Player p1;
		private double dir;
		
		public TryDribleBehaviour(Agent ball, Player p1, double dir) {
			super(ball);
			this.p1 = p1;
			this.dir = dir;
		}

		@Override
		public void action() {
			ACLMessage msg;
			if (ball.hasOwner() && ball.getOwner().getName().equals(p1.getName())){
				ball.updateCurrentMovement(Ball.INTENSITY_RUN, dir, p1.x(), p1.y(), match.getGameTime()/1000.0f);
				
				msg = new ACLMessage(ACLMessage.AGREE);
				msg.addReceiver(new AID(p1.getName(), AID.ISLOCALNAME));
				msg.setOntology(AgentMessages.DRIBLE);
				send(msg);
	//			System.out.println(myAgent.getName() + ": Drible!" );
			} else {
				msg = new ACLMessage(ACLMessage.REFUSE);
				msg.addReceiver(new AID(p1.getName(), AID.ISLOCALNAME));
				msg.setOntology(AgentMessages.DRIBLE);
				send(msg);
			}
			return;
		}
	}
	
	protected class TryShootBehaviour extends OneShotBehaviour{
		private static final long serialVersionUID = 1L;
		private Player p1;
		private double dir;
		
		public TryShootBehaviour(Agent ball, Player p1, double dir) {
			super(ball);
			this.p1 = p1;
			this.dir = dir;
		}

		@Override
		public void action() {
			ACLMessage msg;
			if(ball.hasOwner() && ball.getOwner().getName().equals(p1.getName())){
				ball.updateCurrentMovement(Ball.INTENSITY_SHOOT, dir, p1.x(), p1.y(), match.getGameTime()/1000.0f);
				ball.setOwner(null);
				
				msg = new ACLMessage(ACLMessage.AGREE);
				msg.addReceiver(new AID(p1.getName(), AID.ISLOCALNAME));
				msg.setOntology(AgentMessages.SHOOT);
				send(msg);
	//			System.out.println(myAgent.getName() + ": Shoot!" );
			}else {
				msg = new ACLMessage(ACLMessage.REFUSE);
				msg.addReceiver(new AID(p1.getName(), AID.ISLOCALNAME));
				msg.setOntology(AgentMessages.SHOOT);
				send(msg);
			}
			return;
		}
	}
	
	protected class TryPassBehaviour extends OneShotBehaviour{
		private static final long serialVersionUID = 1L;
		private Player p1;
		private int i;
		private double dir;
		
		
		public TryPassBehaviour(Agent agent, Player p1, int i, double dir) {
			super(agent);
			this.p1 = p1;
			this.dir = dir;
			this.i =i;
		}

		@Override
		public void action() {
			ACLMessage msg;
			if(ball.hasOwner() && ball.getOwner().getName().equals(p1.getName())){
				ball.updateCurrentMovement(i, dir, p1.x(), p1.y(), match.getGameTime()/1000.0f);
				ball.setOwner(null);
	
				msg = new ACLMessage(ACLMessage.AGREE);
				msg.addReceiver(new AID(p1.getName(), AID.ISLOCALNAME));
				msg.setOntology(AgentMessages.PASS);
				send(msg);
	//			System.out.println(myAgent.getName() + ": Pass!" );
			} else{
				msg = new ACLMessage(ACLMessage.REFUSE);
				msg.addReceiver(new AID(p1.getName(), AID.ISLOCALNAME));
				msg.setOntology(AgentMessages.PASS);
				send(msg);
			}
			return;
		}
	}
	
	/**
	 * Behaviour that processes the request of catching the ball
	 * It will signal the requester player with a message
	 * @author Fábio
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
			ACLMessage msg;
			if (!ball.hasOwner()){
				int prob = (int)(Math.random()*100);
				if(prob < player.getGoalKeepingRatio()){
					ball.updateCurrentMovement(0, 0.0f, player.x(), player.y(), match.getGameTime()/1000.0f);
					ball.setOwner(player);
					
					msg = new ACLMessage(ACLMessage.AGREE);
					msg.addReceiver(new AID(player.getName(), AID.ISLOCALNAME));
					msg.setOntology(AgentMessages.TRY_CATCH);
					send(msg);
//					System.out.println(myAgent.getName() + ": Catch!" );
					return;
				}
			}else{
				msg = new ACLMessage(ACLMessage.REFUSE);
				msg.addReceiver(new AID(player.getName(), AID.ISLOCALNAME));
				msg.setOntology(AgentMessages.TRY_CATCH);
				send(msg);
			}
			return;
		}
	}

	/**
	 * Behaviour that processes the request of receiving the ball
	 * It will signal the requester player with a message
	 * @author Fábio
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
			ACLMessage msg;
			if (!ball.hasOwner()){
//				int prob = (int)(Math.random()*100);
//				if(prob < player.getPassingRatio()){
					ball.updateCurrentMovement(0, 0.0f, player.x(), player.y(), match.getGameTime()/1000.0f);
					ball.setOwner(player);
					msg = new ACLMessage(ACLMessage.AGREE);
					msg.addReceiver(new AID(player.getName(), AID.ISLOCALNAME));
					msg.setOntology(AgentMessages.TRY_RECEIVE);
					send(msg);
//					System.out.println(myAgent.getName() + ": Receive!" );
//				}
			}else{
				msg = new ACLMessage(ACLMessage.REFUSE);
				msg.addReceiver(new AID(player.getName(), AID.ISLOCALNAME));
				msg.setOntology(AgentMessages.TRY_RECEIVE);
				send(msg);
			}
			
		}
	}

	/**
	 * Behaviour that processes the request of stealing the ball from the current owner
	 * It will signal the requester player with a message
	 * @author Fábio
	 *
	 */
	protected class TryTackleBehaviour extends OneShotBehaviour{
		private static final long serialVersionUID = 1L;
		private Player p1;
		
		public TryTackleBehaviour(Agent agent, Player p1) {
			super(agent);
			this.p1 = p1;
		}

		@Override
		public void action() {
			ACLMessage msg;
//			int prob = (int)(Math.random()*100);
			
			if(ball.hasOwner() && !ball.getOwner().getName().equals(p1)
					&& (ball.getOwner().getDribblingRatio() > p1.getDefendingRatio())){
				
				msg = new ACLMessage(ACLMessage.AGREE);
				msg.addReceiver(new AID(p1.getName(), AID.ISLOCALNAME));
				msg.setOntology(AgentMessages.TRY_TACKLE);
				send(msg);
				
				// Inform the player that lost the ball
				msg = new ACLMessage(ACLMessage.INFORM);
				msg.addReceiver(new AID(ball.getOwner().getName(), AID.ISLOCALNAME));
				msg.setOntology(AgentMessages.LOST_BALL);
				send(msg);
				ball.updateCurrentMovement(0, 0.0f, p1.x(), p1.y(), match.getGameTime()/1000.0f);
				ball.setOwner(p1);
//				System.out.println(myAgent.getName() + ": Tackle!" );
			} else {
				msg = new ACLMessage(ACLMessage.REFUSE);
				msg.addReceiver(new AID(p1.getName(), AID.ISLOCALNAME));
				msg.setOntology(AgentMessages.TRY_TACKLE);
				send(msg);
			}
			return;
		}
	}
	
	/**
	 * 
	 * @author Fábio
	 *
	 */
	protected class TryInterceptBehaviour extends OneShotBehaviour{
		private static final long serialVersionUID = 1L;
		private Player p1;
		
		public TryInterceptBehaviour(Agent agent, Player p1) {
			super(agent);
			this.p1 = p1;
		}

		@Override
		public void action() {
			ACLMessage msg;
			int prob = (int)(Math.random()*100);
			
			if(ball.hasOwner() && (prob > p1.getDefendingRatio())){
				msg = new ACLMessage(ACLMessage.REFUSE);
				msg.addReceiver(new AID(p1.getName(), AID.ISLOCALNAME));
				msg.setOntology(AgentMessages.TRY_INTERCEPT);
				send(msg);
				return;
			}else{
				msg = new ACLMessage(ACLMessage.AGREE);
				msg.addReceiver(new AID(p1.getName(), AID.ISLOCALNAME));
				msg.setOntology(AgentMessages.TRY_INTERCEPT);
				send(msg);
				ball.updateCurrentMovement(0, 0.0f, p1.x(), p1.y(), match.getGameTime()/1000.0f);
				ball.setOwner(p1);
	//			System.out.println(myAgent.getName() + ": Intercept!" );
			}
			return;
		}
	}
	
	/**
	 * Behaviour to listen to the END_GAME message sent by the ReporterAgent that kills
	 * the Agent
	 * @author Fábio
	 *
	 */
	protected class EndGameBehaviour extends CyclicBehaviour {
		private static final long serialVersionUID = 1L;

		@Override
		public void action() {
			ACLMessage msg = receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
			if (msg != null && msg.getOntology().equals(AgentMessages.END_GAME)){
				doDelete();
			} else {
				block();
			}
			
		}
	}
}
