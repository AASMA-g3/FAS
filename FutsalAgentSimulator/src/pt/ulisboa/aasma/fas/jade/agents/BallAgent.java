package pt.ulisboa.aasma.fas.jade.agents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import pt.ulisboa.aasma.fas.jade.game.Ball;
import pt.ulisboa.aasma.fas.jade.game.Game;

public class BallAgent extends Agent{
	
	private static final long serialVersionUID = 1L;
	private Game match;
	private Ball ball;
	
	private AnswerQueryBehaviour answerBehaviour;
	private RequestBehaviour requestBehaviour;
	
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
		answerBehaviour = new AnswerQueryBehaviour(this);
		requestBehaviour = new RequestBehaviour(this);
		
		this.addBehaviour(answerBehaviour);
		this.addBehaviour(requestBehaviour);
	}
	
	protected class AnswerQueryBehaviour extends CyclicBehaviour{
		private static final long serialVersionUID = 1L;
		private BallAgent agent;
		
		public AnswerQueryBehaviour(Agent agent) {
			super(agent);
			this.agent = (BallAgent) agent;
		}

		@Override
		public void action() {
			ACLMessage msg = this.myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.QUERY_IF));
			ACLMessage res = new ACLMessage(ACLMessage.CONFIRM);
			if (msg != null) {
				switch (msg.getContent()) {
				case AgentMessages.BALL_PASSED:
					res.addReceiver(msg.getSender());
					res.setContent(AgentMessages.BALL_PASSED);
					send(res);
//					System.out.println(AgentMessages.BALL_PASSED);
					break;
				case AgentMessages.BALL_DRIBLED:
					res.addReceiver(msg.getSender());
					res.setContent(AgentMessages.BALL_DRIBLED);
					send(res);
//					System.out.println(AgentMessages.BALL_DRIBLED);
					break;
				case AgentMessages.BALL_SHOTED:
					res.addReceiver(msg.getSender());
					res.setContent(AgentMessages.BALL_SHOTED);
					send(res);
//					System.out.println(AgentMessages.BALL_SHOTED);
					break;
				default:
					break;
				}
			} else {
				block();
			}
		}
	}
	
	protected class RequestBehaviour extends CyclicBehaviour{
		private static final long serialVersionUID = 1L;
		private BallAgent agent;
		
		public RequestBehaviour(BallAgent agent) {
			super();
			this.agent = (BallAgent) agent;
		}

		@Override
		public void action() {
			ACLMessage msg = this.myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
			ACLMessage res = new ACLMessage(ACLMessage.CONFIRM);
			if (msg != null) {
				switch (msg.getContent()) {
				case AgentMessages.PASS_BALL:
					res.addReceiver(msg.getSender());
					res.setContent(AgentMessages.PASS_BALL);
					send(res);
					//TODO actualizar a posição da bola
					break;
				case AgentMessages.DRIBLE_BALL:
					res.addReceiver(msg.getSender());
					res.setContent(AgentMessages.DRIBLE_BALL);
					send(res);
					// TODO actualizar a posição da bola
					break;
				case AgentMessages.SHOOT_BALL:
					res.addReceiver(msg.getSender());
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
}
