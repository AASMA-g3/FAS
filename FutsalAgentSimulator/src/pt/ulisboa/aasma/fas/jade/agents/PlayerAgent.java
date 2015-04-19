package pt.ulisboa.aasma.fas.jade.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import pt.ulisboa.aasma.fas.exceptions.FutsalAgentSimulatorException;
import pt.ulisboa.aasma.fas.jade.game.Ball;
import pt.ulisboa.aasma.fas.jade.game.Game;
import pt.ulisboa.aasma.fas.jade.game.Player;

/**
 * Super class from which all Agents inherit
 * It's basically responsible to kill the agent when the game ends 
 * @author Fábio
 *
 */
public class PlayerAgent extends Agent {
	
	private static final long serialVersionUID = 1L;
	static AID ballAgent = new AID("Ball", AID.ISLOCALNAME);
	Game match;
	Player player;
	Boolean gameStarted = false;
	
	protected Boolean hasBall = false;
	
	protected TryCatchBehaviour tryCatchBehaviour;
	protected TryReceiveBehaviour tryReceiveBehaviour;
	protected TryTackleBehaviour tryTackleBehaviour;
	protected TryInterceptBehaviour tryInterceptBehaviour;
	
	protected MoveBallBehaviour moveBallBehaviour;

	
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
		
		// Add a behaviour to all agents to listen to the end of the game 
		EndGameBehaviour b = new EndGameBehaviour();
		this.addBehaviour(b);
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
			if (msg != null){
				if(msg.getContent().equals(AgentMessages.END_GAME)){
					doDelete();
				}
			} else {
				block();
			}
		}
	}
	
	protected class TryCatchBehaviour extends OneShotBehaviour{
		public TryCatchBehaviour(Agent agent) {
			super(agent);
		}
		@Override
		public void action() {
			ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
			msg.addReceiver(new AID("Ball", AID.ISLOCALNAME));
			msg.setOntology(AgentMessages.TRY_CATCH);	
			send(msg);
		}
	}
	
	protected class TryReceiveBehaviour extends OneShotBehaviour{
		public TryReceiveBehaviour(Agent agent) {
			super(agent);
		}
		@Override
		public void action() {
			ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
			msg.addReceiver(new AID("Ball", AID.ISLOCALNAME));
			msg.setOntology(AgentMessages.TRY_RECEIVE);	
			send(msg);
		}
	}
	
	protected class TryTackleBehaviour extends OneShotBehaviour{
		public TryTackleBehaviour(Agent agent) {
			super(agent);
		}
		@Override
		public void action() {
			ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
			msg.addReceiver(new AID("Ball", AID.ISLOCALNAME));
			msg.setOntology(AgentMessages.TRY_TACKLE);	
			send(msg);
		}
	}
	
	protected class TryInterceptBehaviour extends OneShotBehaviour{
		public TryInterceptBehaviour(Agent agent) {
			super(agent);
		}
		@Override
		public void action() {
			ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
			msg.addReceiver(new AID("Ball", AID.ISLOCALNAME));
			msg.setOntology(AgentMessages.TRY_INTERCEPT);	
			send(msg);
		}
	}
	
	protected class ReceiveFailureBehaviour extends CyclicBehaviour{
		private static final long serialVersionUID = 1L;
		public ReceiveFailureBehaviour(Agent agent) {
			super(agent);
		}
		@Override
		public void action() {
			ACLMessage msg = this.myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.FAILURE));
			if (msg != null) {
				switch (msg.getOntology()) {
				case AgentMessages.FAILED_CATCH:
					tryCatchBehaviour = null;
				case AgentMessages.FAILED_RECEIVE:
					tryReceiveBehaviour = null;
				case AgentMessages.FAILED_TACKLE:
					tryTackleBehaviour = null;
				case AgentMessages.FAILED_INTERCEPT:
					tryInterceptBehaviour = null;
				default:
					break;
				}
			} else {
				block();
			}
		}
	}
	
	protected class ReceiveAgreeBehaviour extends CyclicBehaviour{
		private static final long serialVersionUID = 1L;
		public ReceiveAgreeBehaviour(Agent agent) {
			super(agent);
		}
		@Override
		public void action() {
			ACLMessage msg = this.myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.AGREE));
			if (msg != null) {
				switch (msg.getOntology()) {
				case AgentMessages.SUCESS_CATCH:
					hasBall = true;
					tryCatchBehaviour = null;
				case AgentMessages.SUCESS_RECEIVE:
					hasBall = true;
					tryReceiveBehaviour = null;
				case AgentMessages.SUCESS_TACKLE:
					hasBall = true;
					tryTackleBehaviour = null;
				case AgentMessages.SUCESS_INTERCEPT:
					hasBall = true;
					tryInterceptBehaviour = null;
				default:
					break;
				}
			} else {
				block();
			}
		}
	}
	
	protected class MoveBallBehaviour extends OneShotBehaviour{
		private int intensity;
		private double direction;
		public MoveBallBehaviour(Agent agent, int intensity, double direction) {
			super(agent);
			this.intensity = intensity;
			this.direction = direction;
		}

		@Override
		public void action() {
			if(hasBall){
				int prob = (int)(Math.random());
				if (prob == 0) prob = -1;
				
				if(intensity == Ball.INTENSITY_SHOOT){
					direction += ((100-player.getShootingRatio())/10)*prob;
					hasBall = false;
				}
				else if((intensity == Ball.INTENSITY_LONG_PASS) || (intensity == Ball.INTENSITY_MEDIUM_PASS) ||
						(intensity == Ball.INTENSITY_SHORT_PASS)) {
					direction += ((100-player.getPassingRatio())/10)*prob;
					hasBall = false;
				}
				
				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
				msg.addReceiver(new AID("Ball", AID.ISLOCALNAME));
				msg.setOntology(AgentMessages.MOVE_TO);
				msg.setContent(intensity + " " + direction);
				send(msg);
				} 
			moveBallBehaviour = null;
		}
		
	}
}
