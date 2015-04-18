package pt.ulisboa.aasma.fas.jade.game;

import pt.ulisboa.aasma.fas.jade.agents.DefenderAgent;
import pt.ulisboa.aasma.fas.jade.agents.KeeperAgent;
import pt.ulisboa.aasma.fas.jade.agents.StrikerAgent;

public class Player {
	
	public static final String KEEPER = KeeperAgent.class.getName();
	public static final String DEFENDER = DefenderAgent.class.getName();
	public static final String STRIKER = StrikerAgent.class.getName();
	
	public static final int TEAM_A = 0;
	public static final int TEAM_B = 1;
	
	public static final int KEEPER_SR = -30;
	public static final int KEEPER_DR = -30;
	
	public static final int DEFENDER_SR = -20;
	
	public static final int STRIKER_DR = -20;
	
	private int team;
	
	private int stamina;

	private int shootingRatio;
	private int defendingRatio;
	private int goalKeepingRatio;
	
	private int passingRatio;
	private int dribblingRatio;
	
	private int xCoord;
	private int yCoord;
	
	private String name;
	
	private String position;
	private int playerNumber;

	public Player(String name, int stamina, int shootingRatio, int defendingRatio,
			int goalKeepingRatio, int passingRatio, int dribblingRatio, String position, int team, int playerNumber) {
		super();
		this.stamina = stamina;
		this.passingRatio = passingRatio;
		this.dribblingRatio = dribblingRatio;
		
		this.name = name;
		this.position = position;
		
		this.team = team;
		this.playerNumber = playerNumber;
		
		if(position.equals(KEEPER)){
			this.shootingRatio = shootingRatio + (shootingRatio*KEEPER_SR)/100;
			this.defendingRatio = defendingRatio + (defendingRatio*KEEPER_DR)/100;
			this.goalKeepingRatio = goalKeepingRatio;
			
			this.xCoord = getRandomXCoord();
			this.yCoord = getRandomYCoord();
			
		} else if (position.equals(DEFENDER)){
			this.shootingRatio = shootingRatio + (shootingRatio*DEFENDER_SR)/100;
			this.defendingRatio = defendingRatio;
			this.goalKeepingRatio = 0;
			
			this.xCoord = getRandomXCoord();
			this.yCoord = getRandomYCoord();
			
		} else if (position.equals(STRIKER)) {
			this.shootingRatio = shootingRatio;
			this.defendingRatio = defendingRatio + (defendingRatio*STRIKER_DR)/100;
			this.goalKeepingRatio = 0;
			
			this.xCoord = getRandomXCoord();
			this.yCoord = getRandomYCoord();
		}
			
	}
	
	public int getRandomXCoord(){
		return 0;
	}
	
	public int getRandomYCoord(){
		return 0;
	}
	
	public int getStamina() {
		return stamina;
	}

	public void setStamina(int stamina) {
		this.stamina = stamina;
	}

	public int getShootingRatio() {
		return shootingRatio;
	}

	public void setShootingRatio(int shootingRatio) {
		this.shootingRatio = shootingRatio;
	}

	public int getxCoord() {
		return xCoord;
	}

	public void setxCoord(int xCoord) {
		this.xCoord = xCoord;
	}

	public int getyCoord() {
		return yCoord;
	}

	public void setyCoord(int yCoord) {
		this.yCoord = yCoord;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public int getTeam() {
		return team;
	}

	public void setTeam(int team) {
		this.team = team;
	}

	public int getDefendingRatio() {
		return defendingRatio;
	}

	public void setDefendingRatio(int defendingRatio) {
		this.defendingRatio = defendingRatio;
	}

	public int getGoalKeepingRatio() {
		return goalKeepingRatio;
	}

	public void setGoalKeepingRatio(
			int goalKeepingRatio) {
		this.goalKeepingRatio = goalKeepingRatio;
	}

	public int getPassingRatio() {
		return passingRatio;
	}

	public void setPassingRatio(int passingRatio) {
		this.passingRatio = passingRatio;
	}

	public int getDribblingRatio() {
		return dribblingRatio;
	}

	public void setDribblingRatio(int dribblingRatio) {
		this.dribblingRatio = dribblingRatio;
	}

	public int getPlayerNumber() {
		return playerNumber;
	}

	public void setPlayerNumber(int playerNumber) {
		this.playerNumber = playerNumber;
	}
	
	
	
}
