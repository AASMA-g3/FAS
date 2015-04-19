package pt.ulisboa.aasma.fas.jade.agents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import pt.ulisboa.aasma.fas.jade.game.Ball;
import pt.ulisboa.aasma.fas.jade.game.Game;


/**
 * Agent that represents the ball.
 * It responds to requests (to be shot, passed and dribled) and queries (ball from players  
 * @author Fábio
 *
 */
public class BallAgent extends Agent{
	private static final long serialVersionUID = 1L;
	
	//Constants that represent the state of the ball
	public static final int BALL_ABANDONED = 0;
	public static final int BALL_SHOTED = 1;
	public static final int BALL_PASSED = 2;
	public static final int BALL_DRIBLED = 3;
	
	private Game match;
	private Ball ball;
	private int state = BallAgent.BALL_ABANDONED;
	
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
		
		this.addBehaviour(new AnswerQueryBehaviour(this));
		this.addBehaviour(new AnswerRequestBehaviour(this));
		this.addBehaviour(new EndGameBehaviour());
	}
	
	
	/**
	 * Behaviour to answer queries from players about the state of the ball.
	 * The ball can be abandoned, shoted, passed or being dribled.
	 * @author Fábio
	 *
	 */
	protected class AnswerQueryBehaviour extends CyclicBehaviour{
		private static final long serialVersionUID = 1L;
		
		public AnswerQueryBehaviour(Agent agent) {
			super(agent);
		}

		@Override
		public void action() {
			ACLMessage msg = this.myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.QUERY_IF));
			if (msg != null) {
				switch (msg.getContent()) {
				case AgentMessages.BALL_PASSED:	
					if (state == BallAgent.BALL_PASSED){
						ACLMessage res = new ACLMessage(ACLMessage.CONFIRM);
						res.addReceiver(msg.getSender());
						res.setContent(AgentMessages.BALL_PASSED);
						send(res);
					} else{
						ACLMessage res = new ACLMessage(ACLMessage.DISCONFIRM);
						res.addReceiver(msg.getSender());
						res.setContent(AgentMessages.BALL_PASSED);
						send(res);
					}
					break;
				case AgentMessages.BALL_DRIBLED:
					if (state == BallAgent.BALL_DRIBLED){
						ACLMessage res = new ACLMessage(ACLMessage.CONFIRM);
						res.addReceiver(msg.getSender());
						res.setContent(AgentMessages.BALL_DRIBLED);
						send(res);
					} else{
						ACLMessage res = new ACLMessage(ACLMessage.DISCONFIRM);
						res.addReceiver(msg.getSender());
						res.setContent(AgentMessages.BALL_DRIBLED);
						send(res);
					}
					break;
				case AgentMessages.BALL_SHOTED:
					if (state == BallAgent.BALL_SHOTED){
						ACLMessage res = new ACLMessage(ACLMessage.CONFIRM);
						res.addReceiver(msg.getSender());
						res.setContent(AgentMessages.BALL_SHOTED);
						send(res);
					} else{
						ACLMessage res = new ACLMessage(ACLMessage.DISCONFIRM);
						res.addReceiver(msg.getSender());
						res.setContent(AgentMessages.BALL_SHOTED);
						send(res);
					}
					break;
				case AgentMessages.BALL_ABANDONED:
					if (state == BallAgent.BALL_ABANDONED){
						ACLMessage res = new ACLMessage(ACLMessage.CONFIRM);
						res.addReceiver(msg.getSender());
						res.setContent(AgentMessages.BALL_ABANDONED);
						send(res);
					} else{
						ACLMessage res = new ACLMessage(ACLMessage.DISCONFIRM);
						res.addReceiver(msg.getSender());
						res.setContent(AgentMessages.BALL_ABANDONED);
						send(res);
					}
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
	 * Behaviour to listen to requests from the Players, like passing, shooting and dribling the ball.
	 * @author Fábio
	 *
	 */
	protected class AnswerRequestBehaviour extends CyclicBehaviour{
		private static final long serialVersionUID = 1L;
		
		public AnswerRequestBehaviour(BallAgent agent) {
			super();
		}

		@Override
		public void action() {
			ACLMessage msg = this.myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
			if (msg != null) {
				ACLMessage res = new ACLMessage(ACLMessage.CONFIRM);
				res.addReceiver(msg.getSender());
				//TODO lógica de tirar a bola e tudo mais
				switch (msg.getContent()) {
				case AgentMessages.PASS_BALL:
					res.setContent(AgentMessages.PASS_BALL);
					send(res);
					//TODO actualizar a posição da bola
					break;
				case AgentMessages.DRIBLE_BALL:
					res.setContent(AgentMessages.DRIBLE_BALL);
					send(res);
					// TODO actualizar a posição da bola
					break;
				case AgentMessages.SHOOT_BALL:
					res.setContent(AgentMessages.SHOOT_BALL);
					send(res);
					//TODO actualizar a posição da bola
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
			if (msg != null && msg.getContent().equals(AgentMessages.END_GAME)){
				doDelete();
			} else {
				block();
			}
			
		}
	}
}
