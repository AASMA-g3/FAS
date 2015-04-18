package pt.ulisboa.aasma.fas;

import java.awt.EventQueue;

import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import pt.ulisboa.aasma.fas.bootstrap.RunJade;
import pt.ulisboa.aasma.fas.j2d.GameRunner;
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
		 

			GameRunner bf = new GameRunner();
			bf.setVisible(true);
			bf.startMainLoop();
		 
		 //WAIT FOR RUN PRESS
		 
		 Game game = new Game(70, 50, 90, 70, 70);
		 bf.startGame(game);
		 
		 Object[] agentParams = {game};
		 Object[] reporterParams = {game,bf};
		  
		 
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


	}
}
