package pt.ulisboa.aasma.fas.jade.game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

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
	
	public static final int RATIO_MULTIPLIER = 20;
	
	public static final float TEAM_A_KEEPER_XPOS = 0.0f;
	public static final float TEAM_B_KEEPER_XPOS = 38.7f;
	
	private int team;

	private int shootingRatio;
	private int defendingRatio;
	private int goalKeepingRatio;
	
	private int passingRatio;
	private int dribblingRatio;
	
	private String name;
	
	private PlayerMovement playerMovement;
	
	private String position;
	private int playerNumber;
	
	private boolean hasBall;

	public Player(String name, int shootingRatio, int defendingRatio,
			int goalKeepingRatio, int passingRatio, int dribblingRatio, String position, int team, int playerNumber) {
		super();
		
		this.passingRatio = passingRatio*RATIO_MULTIPLIER;
		this.dribblingRatio = dribblingRatio*RATIO_MULTIPLIER;
		
		this.name = name;
		this.position = position;
		
		this.team = team;
		this.playerNumber = playerNumber;
		
		
		double x = getRandomXCoord();
		double y = getRandomYCoord();
		if (this.team == TEAM_B){
			x += (20.0f - x)*2.0f;
		}
		this.playerMovement = new PlayerMovement(x, y);
		
		shootingRatio = shootingRatio*RATIO_MULTIPLIER;
		defendingRatio = defendingRatio*RATIO_MULTIPLIER;
		goalKeepingRatio = goalKeepingRatio*RATIO_MULTIPLIER;
		
		if(position.equals(KEEPER)){
			this.shootingRatio = shootingRatio + (shootingRatio*KEEPER_SR)/100;
			this.defendingRatio = defendingRatio + (defendingRatio*KEEPER_DR)/100;
			this.goalKeepingRatio = goalKeepingRatio;
		} else if (position.equals(DEFENDER)){
			this.shootingRatio = shootingRatio + (shootingRatio*DEFENDER_SR)/100;
			this.defendingRatio = defendingRatio;
			this.goalKeepingRatio = 0;
		} else if (position.equals(STRIKER)) {
			this.shootingRatio = shootingRatio;
			this.defendingRatio = defendingRatio + (defendingRatio*STRIKER_DR)/100;
			this.goalKeepingRatio = 0;
		}
		
		this.hasBall = false;
	}
	
	private double getRandomXCoord(){
		if(position.equals(KEEPER)){
			double minX = 0.0f;
			double maxX = 5.0f;
			Random rand = new Random();
			return rand.nextDouble() * (maxX - minX) + minX;
		} else if (position.equals(DEFENDER)){
			double minX = 9.0f;
			double maxX = 11.0f;
			Random rand = new Random();
			return rand.nextDouble() * (maxX - minX) + minX;
		} else if (position.equals(STRIKER)) {
			double minX = 13.0f;
			double maxX = 17.0f;
			Random rand = new Random();
			return rand.nextDouble() * (maxX - minX) + minX;
		}
		return -1;
	}
	
	public void resetCoords(){
		double x = getRandomXCoord();
		double y = getRandomYCoord();
		if (this.team == TEAM_B){
			x += (20.0f - x)*2.0f;
		}
		this.playerMovement.setGoal(x, y);
	}
	
	private double getRandomYCoord(){
		if(position.equals(KEEPER)){
			double minY = 8.0f;
			double maxY = 12.0f;
			Random rand = new Random();
			return rand.nextDouble() * (maxY - minY) + minY;
		} else if (position.equals(DEFENDER)){
			double minY = 1.0f;
			double maxY = 19.0f;
			Random rand = new Random();
			return rand.nextDouble() * (maxY - minY) + minY;
		} else if (position.equals(STRIKER)) {
			double minY = 1.0f;
			double maxY = 19.0f;
			Random rand = new Random();
			return rand.nextDouble() * (maxY - minY) + minY;
		}
		return -1;
	}

	public int getShootingRatio() {
		return shootingRatio;
	}

	public String getName() {
		return name;
	}

	public String getPosition() {
		return position;
	}

	public int getTeam() {
		return team;
	}

	public int getDefendingRatio() {
		return defendingRatio;
	}

	public int getGoalKeepingRatio() {
		return goalKeepingRatio;
	}

	public int getPassingRatio() {
		return passingRatio;
	}

	public int getDribblingRatio() {
		return dribblingRatio;
	}

	public int getPlayerNumber() {
		return playerNumber;
	}

	public double getDirectionToPlayer(Player player){
		return  Math.toDegrees(Math.atan(Math.abs(player.y()- y()) /
				Math.abs(player.x()- x())));
	}
	
	public double getDirectionToBall(Ball ball){
		return  Math.toDegrees(Math.atan(Math.abs(ball.y()- y()) /
				Math.abs(ball.x()- x())));
	}
	
	public double getDirectionToAllyGoal(){
		if(this.getTeam() == Player.TEAM_A){
			return  Math.toDegrees(Math.atan(Math.abs(Game.GOAL_Y_MED- y()) /
					Math.abs(Game.LIMIT_X- x())));
		} else {
			return  Math.toDegrees(Math.atan(Math.abs(Game.GOAL_Y_MED- y()) /
					Math.abs(0- x())));
		}
	}
	
	public double getDirectionToEnemyGoal(){
		if(this.getTeam() == Player.TEAM_B){
			return  Math.toDegrees(Math.atan(Math.abs(Game.GOAL_Y_MED- y()) /
					Math.abs(Game.LIMIT_X- x())));
		} else {
			return  Math.toDegrees(Math.atan(Math.abs(Game.GOAL_Y_MED- y()) /
					Math.abs(0- x())));
		}
	}
	
	public double getDistanceToPlayer(Player player){
		return Math.sqrt(Math.pow((y() - player.y()), 2) +
				Math.pow((x() - player.x()), 2));
	}

	public double getDistanceToBall(Ball ball){
		return Math.sqrt(Math.pow((y() - ball.y()), 2) +
				Math.pow((x() - ball.x()), 2));
	}
	
	public double getDistanceToAllyGoal(){
		double xCoord, yCoord;
		if(this.getTeam() == Player.TEAM_B)
			xCoord=Game.LIMIT_X;
		 else
			xCoord=0;
		yCoord = Game.GOAL_Y_MED;
		return Math.sqrt(Math.pow((y() - yCoord), 2) +
				Math.pow((x() - xCoord), 2));
	}
	
	public double getDistanceToEnemyGoal(){
		double xCoord, yCoord;
		if(this.getTeam() == Player.TEAM_A)
			xCoord=Game.LIMIT_X;
		 else
			xCoord=0;
		yCoord = Game.GOAL_Y_MED;
		return Math.sqrt(Math.pow((y() - yCoord), 2) +
				Math.pow((x() - xCoord), 2));
	}
	
	public PlayerMovement getPlayerMovement() {
		return playerMovement;
	}

	public double x(){
		return playerMovement.x();
	}
	
	public double y(){
		return playerMovement.y();
	}

	public void setPlayerMovement(PlayerMovement playerMovement) {
		this.playerMovement = playerMovement;
	}

	public boolean isOnTrajectoryToPlayer(Player senderPlayer, Player allyPlayer){
		Player enemyPlayer = this;
		double senderToAllyDirection = senderPlayer.getDirectionToPlayer(allyPlayer);
		double enemyToAllyDirection = enemyPlayer.getDirectionToPlayer(allyPlayer);
		
		if(Math.round(senderToAllyDirection) == Math.round(enemyToAllyDirection)){
			return true;
		}
		return false;
	}
	
	public boolean isAroundPlayer(Player allyPlayer){
		Player enemyPlayer = this;
		
		if (enemyPlayer.getDistanceToPlayer(allyPlayer) <= 1.0f)
			return true;
		else
			return false;

	}
	
	public boolean hasBall() {
		return hasBall;
	}

	public void setHasBall(boolean hasBall) {
		this.hasBall = hasBall;
	}
	
	public ArrayList<Player> getOpenAllyPlayers(ArrayList<Player> teamA, ArrayList<Player> teamB){
		ArrayList<Player> openPlayers = new ArrayList<Player>();
		if (this.getTeam() == TEAM_A){
			for (Player player : teamA) {
				if(!player.isPlayerBlocked(this, teamB)){
					openPlayers.add(player);
				}
			}	
		}else{
			for (Player player : teamB) {
				if(!player.isPlayerBlocked(this, teamA)){
					openPlayers.add(player);
				}
			}	
		}
		return openPlayers;
	}
	
	public Player getNearestAllyOpenPlayer(ArrayList<Player> teamA, ArrayList<Player> teamB){
		ArrayList<Player> openPlayers = getOpenAllyPlayers(teamA, teamB);
		if(openPlayers.isEmpty()){
			return null;
		}
		
		Player nearestPlayer = null;
		for (Player player : openPlayers) {
			if(nearestPlayer == null){
				nearestPlayer = player;
				continue;
			}
			nearestPlayer = getClosestPlayer(nearestPlayer, player);	
		}
		return nearestPlayer;
	}
	
	public Player getNearestAllyPlayer(ArrayList<Player> teamA, ArrayList<Player> teamB){
		
		Player nearestPlayer = null;
		if(this.team == TEAM_A){
			for (Player player : teamA) {
				if(nearestPlayer == null){
					nearestPlayer = player;
					continue;
				}
				nearestPlayer = getClosestPlayer(nearestPlayer, player);	
			}	
		} else {
			for (Player player : teamB) {
				if(nearestPlayer == null){
					nearestPlayer = player;
					continue;
				}
				nearestPlayer = getClosestPlayer(nearestPlayer, player);	
			}	
		}
		
		return nearestPlayer;
	}
	
	public Player getClosestPlayer(Player playerA, Player playerB){
		if(this.getDistanceToPlayer(playerA) > this.getDistanceToPlayer(playerB))
			return playerB;
		else
			return playerA;
	}
	
	public boolean isPlayerBlocked(Player senderPlayer, ArrayList<Player> enemyTeam){
			for (Player enemyPlayer : enemyTeam) {
				if (enemyPlayer.isOnTrajectoryToPlayer(senderPlayer, this)){
					return true;
				} else {
					if (enemyPlayer.isAroundPlayer(this)){
						return true;
					} else {
						continue;
					}
				}
			}
			return false;
	}
	
}
