package pt.ulisboa.aasma.fas.jade.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import pt.ulisboa.aasma.fas.exceptions.FutsalAgentSimulatorException;
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
					System.out.println("Mataram-me!");
					doDelete();
				}
			} else {
				block();
			}
		}
	}
}
