package pt.ulisboa.aasma.fas.exceptions;

public class PlayerDoesNotExistException extends
		FutsalAgentSimulatorException {
	
	private String playerName;
	
	
	
	public PlayerDoesNotExistException(String playerName) {
		super();
		this.playerName = playerName;
	}

	@Override
	public String getMessage() {
		return "Player " + playerName + " does not exist.";
	}

}
