package pt.ulisboa.aasma.fas.jade.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;
import pt.ulisboa.aasma.fas.j2d.GameRunner;
import pt.ulisboa.aasma.fas.jade.game.Game;
import pt.ulisboa.aasma.fas.jade.game.Player;

public class ReporterAgent extends Agent{
	
	private static final long serialVersionUID = 1L;
	
	private Game match;
	private GameRunner engine;
	
	private StartGame startGame;
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
			engine = (GameRunner) args[1];
		}
		
		startGame = new StartGame(this, 1000);
		timer = new Timer(this, Game.TICK_TIME);
		terminateGame = new TerminateGame(this);
				
		this.addBehaviour(startGame);
		
	}
	
	protected class StartGame extends WakerBehaviour{
		private static final long serialVersionUID = 1L;
		private ReporterAgent agent;

		public StartGame(Agent agent, long timeToStart) {
			super(agent, timeToStart);
			this.agent = (ReporterAgent) agent;
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
		private ReporterAgent agent;
		
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
		}
		
	}
	
	protected class TerminateGame extends OneShotBehaviour{
		private static final long serialVersionUID = 1L;
		private ReporterAgent agent;
		
		public TerminateGame(Agent agent) {
			super(agent);
			this.agent = (ReporterAgent) agent;
		}
		
		@Override
		public void action() {
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			System.out.println("Vamos acabar o jogo!");
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
			
			synchronized (this.agent.match.isEnded) {
				this.agent.match.isEnded.set(true);
				this.agent.match.isEnded.notify();
			}
			
			this.myAgent.doDelete();
		}
		
	}
	
}
