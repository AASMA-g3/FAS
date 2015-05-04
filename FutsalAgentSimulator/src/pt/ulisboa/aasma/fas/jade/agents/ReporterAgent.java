package pt.ulisboa.aasma.fas.jade.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;
import pt.ulisboa.aasma.fas.jade.game.Ball;
import pt.ulisboa.aasma.fas.jade.game.BallMovement;
import pt.ulisboa.aasma.fas.jade.game.Game;
import pt.ulisboa.aasma.fas.jade.game.Player;

public class ReporterAgent extends Agent{
	
	private static final long serialVersionUID = 1L;
	
	private Game match;
	
	private StartGame startGame;
	private RestartBall restartBall;
	private Timer timer;
	private TerminateGame terminateGame;
	
	@Override
	protected void setup() {
		super.setup();
		Object[] args = getArguments();
		if (args == null || args.length == 0){
			System.out.println("Please input correct arguments.");
			return;
		}
		else{
			match = (Game) args[0];
		}
		
		startGame = new StartGame(this, 1000);
		timer = new Timer(this, Game.TICK_TIME);
		terminateGame = new TerminateGame(this);
				
		this.addBehaviour(startGame);
		
	}
	
	protected class StartGame extends WakerBehaviour{
		private static final long serialVersionUID = 1L;

		public StartGame(Agent agent, long timeToStart) {
			super(agent, timeToStart);
		}

		@Override
		protected void handleElapsedTimeout() {
			this.myAgent.addBehaviour(timer);
			
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			for(Player player : match.getTeamA()){
				msg.addReceiver(new AID(player.getName(), AID.ISLOCALNAME));
			}
			for(Player player : match.getTeamB()){
				msg.addReceiver(new AID(player.getName(), AID.ISLOCALNAME));
			}
			msg.setContent(AgentMessages.START_GAME);
			this.myAgent.send(msg);
		}

		
	}
	
	protected class Timer extends TickerBehaviour{
		private static final long serialVersionUID = 1L;
		
		public Timer(Agent agent, long tickTime) {
			super(agent, tickTime);
			agent = (ReporterAgent) agent;
		}

		@Override
		protected void onTick() {
			if(match.getGameTime()== Game.GAME_TIME){
				this.myAgent.addBehaviour(terminateGame);
				this.stop();
			}
			match.setGameTime(match.getGameTime()+Game.TICK_TIME);
			
			if(match.isGoal()){
				double x = match.getBall().x();
				double y = match.getBall().y();
				if(x < 0.0f){
					match.setTeamBScore(match.getTeamBScore()+1);
					restartBall = new RestartBall(this.myAgent, 2000, new BallMovement(Ball.INTENSITY_SHOOT, 180.0f, 20, 10, match.getGameTime()/1000.0f));
				} else {
					match.setTeamAScore(match.getTeamAScore()+1);
					restartBall = new RestartBall(this.myAgent, 2000, new BallMovement(Ball.INTENSITY_SHOOT, 0.0f, 20, 10, match.getGameTime()/1000.0f));
				}
				match.getBall().setCurrentMovement(new BallMovement(0, 0.0f, 20.0f, 10.0f, match.getGameTime()/1000.0f));
				this.myAgent.addBehaviour(restartBall);
			}
			
		}
		
	}
	
	protected class RestartBall extends WakerBehaviour{
		private static final long serialVersionUID = 1L;
		private BallMovement movement;
		public RestartBall(Agent agent, long timeToStart, BallMovement movement) {
			super(agent, timeToStart);
		}

		@Override
		protected void handleElapsedTimeout() {
			//match.getBall().setCurrentMovement(movement);
		}

		
	}
	
	protected class TerminateGame extends OneShotBehaviour{
		private static final long serialVersionUID = 1L;
		
		public TerminateGame(Agent agent) {
			super(agent);
		}
		
		@Override
		public void action() {
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			for(Player player : match.getTeamA()){
				System.out.println(player.getName());
				msg.addReceiver(new AID(player.getName(), AID.ISLOCALNAME));
			}
			for(Player player : match.getTeamB()){
				System.out.println(player.getName());
				msg.addReceiver(new AID(player.getName(), AID.ISLOCALNAME));
			}
			msg.addReceiver(new AID("Ball", AID.ISLOCALNAME));
			msg.setContent(AgentMessages.END_GAME);
			this.myAgent.send(msg);
			
			match.isEnded.set(true);
		
			this.myAgent.doDelete();
		}
		
	}
	
}
