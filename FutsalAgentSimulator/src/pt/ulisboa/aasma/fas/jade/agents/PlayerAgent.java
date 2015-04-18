package pt.ulisboa.aasma.fas.jade.agents;

import jade.core.Agent;
import pt.ulisboa.aasma.fas.exceptions.FutsalAgentSimulatorException;
import pt.ulisboa.aasma.fas.jade.game.Game;
import pt.ulisboa.aasma.fas.jade.game.Player;

public class PlayerAgent extends Agent {
	
	private Game match;
	private Player player;
	
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

	}


}
