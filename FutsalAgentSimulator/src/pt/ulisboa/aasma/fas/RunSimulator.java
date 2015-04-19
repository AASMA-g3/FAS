package pt.ulisboa.aasma.fas;

import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import pt.ulisboa.aasma.fas.bootstrap.RunJade;
import pt.ulisboa.aasma.fas.j2d.GameRunner;
import pt.ulisboa.aasma.fas.j2d.MenuFrame;
import pt.ulisboa.aasma.fas.jade.agents.BallAgent;
import pt.ulisboa.aasma.fas.jade.agents.ReporterAgent;
import pt.ulisboa.aasma.fas.jade.game.Game;
import pt.ulisboa.aasma.fas.jade.game.Player;

public class RunSimulator {
	
	private static RunJade r;
	private static ContainerController home;
	
	public static void main(String[] args) {
		
		 r = new RunJade(true, "30000");

		 home = r.getHome();
		
		 MenuFrame mf = new MenuFrame();
		 GameRunner gr = new GameRunner();
		
			 //WAIT FOR RUN PRESS
			 
		 synchronized (mf.runPressed) {
				try {
					mf.runPressed.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		 
			 Game game = new Game(mf.getSliders());
			 
			 gr.startGame(game);
			 
			 Object[] agentParams = {game};
			 Object[] reporterParams = {game,gr};
			  
			 
			try {
				AgentController a;
				
				a = home.createNewAgent("Reporter", ReporterAgent.class.getName(), reporterParams);
				a.start();
				
				for (Player player : game.getTeamA()){
					a = home.createNewAgent(player.getName(),player.getPosition(),agentParams);
					a.start();
				}
	
				for (Player player : game.getTeamB()){
					a = home.createNewAgent(player.getName(),player.getPosition(),agentParams);
					a.start();
				}
				
				a = home.createNewAgent("Ball", BallAgent.class.getName(), agentParams);
				a.start();
				
				
			} catch (StaleProxyException e) {
				e.printStackTrace();
			}
			
			synchronized (game.isEnded) {
				try {
					game.isEnded.wait();
					game.isEnded.set(false);
					System.out.println("Game has ended");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
		 }	
	}

