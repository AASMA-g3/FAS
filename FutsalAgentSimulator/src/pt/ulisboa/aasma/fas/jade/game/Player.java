package pt.ulisboa.aasma.fas.jade.game;

import java.util.ArrayList;
import java.util.Random;

import pt.ulisboa.aasma.fas.jade.agents.bdi.DefenderAgentBDI;
import pt.ulisboa.aasma.fas.jade.agents.bdi.KeeperAgentBDI;
import pt.ulisboa.aasma.fas.jade.agents.bdi.StrikerAgentBDI;
import pt.ulisboa.aasma.fas.jade.agents.reactive.DefenderAgentReactive;
import pt.ulisboa.aasma.fas.jade.agents.reactive.KeeperAgentReactive;
import pt.ulisboa.aasma.fas.jade.agents.reactive.StrikerAgentReactive;

public class Player {

	public static final double PLAYER_SIZE = 1.0f;
	public static final double PLAYER_REACH = 0.1f;

	public static final int NEW_GOAL_GENERAL_AREA = 0;
	public static final int NEW_GOAL_POSITION_AREA = 1;
	public static final int NEW_GOAL_INTERCEPT_GOAL = 2;
	public static final int NEW_GOAL_INTERCEPT_PASS = 3;
	public static final int NEW_GOAL_NEW_DIRECTION = 4;
	public static final int NEW_GOAL_CHASE_PLAYER = 5;
	public static final int NEW_GOAL_CHASE_BALL = 6;
	public static final int NEW_GOAL_STRIKER_OFFENSIVE = 7;
	public static final int NEW_GOAL_DEFENDER_DEFENSE = 8;

	public static final String KEEPER = "Keeper";
	public static final String DEFENDER = "Defender";
	public static final String STRIKER = "Striker";
	
	public static final String KEEPER_REACTIVE = KeeperAgentReactive.class.getName();
	public static final String DEFENDER_REACTIVE = DefenderAgentReactive.class.getName();
	public static final String STRIKER_REACTIVE = StrikerAgentReactive.class.getName();
	
	public static final String KEEPER_DELIBERATIVE = KeeperAgentBDI.class.getName();
	public static final String DEFENDER_DELIBERATIVE = DefenderAgentBDI.class.getName();
	public static final String STRIKER_DELIBERATIVE = StrikerAgentBDI.class.getName();
	
	public static final int TEAM_A = 0;
	public static final int TEAM_B = 1;
	
	public static final int KEEPER_SR = -30;
	public static final int KEEPER_DR = -30;
	
	public static final int DEFENDER_SR = -20;
	
	public static final int STRIKER_DR = -20;
	
	public static final int RATIO_MULTIPLIER = 10;
	
	public static final float TEAM_A_KEEPER_XPOS = 0.0f;
	public static final float TEAM_B_KEEPER_XPOS = 38.7f;
	
	public static final boolean KEEPER_INCLUDED = true;
	public static final boolean KEEPER_EXCLUDED = false;
	
	public static final int QUADRANT_FIRST = 1;
	public static final int QUADRANT_SECOND = 2;
	public static final int QUADRANT_THIRD = 3;
	public static final int QUADRANT_FOURTH = 4;
	public static final int QUADRANT_FIRST_AND_FOURTH = 5;
	public static final int QUADRANT_SECOND_AND_THIRD = 6;
	
	private static int PAIR_COUNTING = 1;
	
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
	
	private int quadrant;

	public Player(String name, int shootingRatio, int defendingRatio,
			int goalKeepingRatio, int passingRatio, int dribblingRatio, String position, int team, int playerNumber) {
		super();
		
		this.passingRatio = passingRatio*RATIO_MULTIPLIER;
		this.dribblingRatio = dribblingRatio*RATIO_MULTIPLIER;
		
		this.name = name;
		this.position = position;
		
		this.team = team;
		this.playerNumber = playerNumber;
		
		if (PAIR_COUNTING == 6)
			PAIR_COUNTING = 1;
		
		switch (PAIR_COUNTING) {
		case 1:
			if (this.team == TEAM_A){
				quadrant = QUADRANT_SECOND_AND_THIRD;
			} else {
				quadrant = QUADRANT_FIRST_AND_FOURTH;
			}
			break;
		case 2:
			if (this.team == TEAM_A){
				quadrant = QUADRANT_THIRD;
			} else {
				quadrant = QUADRANT_FOURTH;
			}
			break;
		case 3:
			if (this.team == TEAM_A){
				quadrant = QUADRANT_SECOND;
			} else {
				quadrant = QUADRANT_FIRST;
			}
			break;
		case 4:
			if (this.team == TEAM_A){
				quadrant = QUADRANT_FOURTH;
			} else {
				quadrant = QUADRANT_THIRD;
			}
			break;
		case 5:
			if (this.team == TEAM_A){
				quadrant = QUADRANT_FIRST;
			} else {
				quadrant = QUADRANT_SECOND;
			}
			break;
		default:
			break;
		}

		double x = getNewXCoord();
		double y = getNewYCoord();
		
		if (this.team == TEAM_B){
			x += (20.0f - x)*2.0f;
		}
		PAIR_COUNTING++;
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
	
	public Player getClosestPlayer(Player playerA, Player playerB){
		if(this.getDistanceToPlayer(playerA) > this.getDistanceToPlayer(playerB))
			return playerB;
		else
			return playerA;
	}
	
	public int getDefendingRatio() {
		return defendingRatio;
	}
	
	public double getDirectionToAllyGoal(){
		double angle;
		if(this.getTeam() == Player.TEAM_B){
			angle= Math.toDegrees(Math.atan2(Game.GOAL_Y_MED - this.y(), Game.LIMIT_X - this.x()));
		} else {
			angle= Math.toDegrees(Math.atan2(Game.GOAL_Y_MED - this.y(), 0 - this.x()));
		}
		if(angle < 0.0f){
	        angle += 360.0f;
	    }
	    return angle;
	}
	
	public double getDirectionToBall(Ball ball){
		double angle;
		angle= Math.toDegrees(Math.atan2(ball.y() - this.y(), ball.x() - this.x()));
		if(angle < 0.0f){
	        angle += 360.0f;
	    }
	    return angle;
	}
	
	public double getDirectionToEnemyGoal(){
		double angle;
		if(this.getTeam() == Player.TEAM_A){
			angle= Math.toDegrees(Math.atan2(Game.GOAL_Y_MED - this.y(), Game.LIMIT_X - this.x()));
		} else {
			angle= Math.toDegrees(Math.atan2(Game.GOAL_Y_MED - this.y(), 0 - this.x()));
		}
		if(angle < 0.0f){
	        angle += 360.0f;
	    }
	    return angle;
	}

	public double getDirectionToGoal(int team){
		double angle;
		if(team == Player.TEAM_B){
			angle= Math.toDegrees(Math.atan2(Game.GOAL_Y_MED - this.y(), Game.LIMIT_X - this.x()));
		} else {
			angle= Math.toDegrees(Math.atan2(Game.GOAL_Y_MED - this.y(), 0 - this.x()));
		}
		if(angle < 0.0f){
	        angle += 360.0f;
	    }
	    return angle;
	}
	
	public double getDirectionToPlayer(Player player){
		double angle;
		angle= Math.toDegrees(Math.atan2(player.y() - this.y(), player.x() - this.x()));
		if(angle < 0.0f){
	        angle += 360.0f;
	    }
	    return angle;
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

	public double getDistanceToBall(Ball ball){
		return Math.sqrt(Math.pow((y() - ball.y()), 2) +
				Math.pow((x() - ball.x()), 2));
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
	
	public double getDistanceToPlayer(Player player){
		return Math.sqrt(Math.pow((y() - player.y()), 2) +
				Math.pow((x() - player.x()), 2));
	}

	public int getDribblingRatio() {
		return dribblingRatio;
	}

	public ArrayList<Player> getEnemyPlayersOnQuadrant(ArrayList<Player> teamA, ArrayList<Player> teamB){
		ArrayList<Player> enemiesOnQuadrant = new ArrayList<Player>();
		if (this.getTeam() == TEAM_B){
			for (Player player : teamA) {
				if(player.position.equals(Player.KEEPER)) continue;
				if(player.isOnQuadrant(this.quadrant)){
					enemiesOnQuadrant.add(player);
				}
			}	
		}else{
			for (Player player : teamB) {
				if(player.position.equals(Player.KEEPER)) continue;
				if(player.isOnQuadrant(this.quadrant)){
					enemiesOnQuadrant.add(player);
				}
			}	
		}
		return enemiesOnQuadrant;
	}

	public Player getFurthestAllyOpenPlayer(ArrayList<Player> teamA, ArrayList<Player> teamB){
		ArrayList<Player> openPlayers = this.getOpenAllyPlayers(teamA, teamB);
		if(openPlayers.isEmpty()){
			return null;
		}
		
		Player furthestPlayer = null;
		for (Player player : openPlayers) {
			if(player.position.equals(Player.KEEPER)) continue;
			if(furthestPlayer == null){
				furthestPlayer = player;
				continue;
			}
			furthestPlayer = getFurthestPlayer(furthestPlayer, player);	
		}
		return furthestPlayer;
	}

	public Player getFurthestAllyPlayer(ArrayList<Player> teamA, ArrayList<Player> teamB){
		
		Player furthestPlayer = null;
		if(this.team == TEAM_A){
			for (Player player : teamA) {
				if(player.position.equals(Player.KEEPER)) continue;
				if(furthestPlayer == null){
					furthestPlayer = player;
					continue;
				}
				furthestPlayer = getClosestPlayer(furthestPlayer, player);	
			}	
		} else {
			for (Player player : teamB) {
				if(player.position.equals(Player.KEEPER)) continue;
				if(furthestPlayer == null){
					furthestPlayer = player;
					continue;
				}
				furthestPlayer = getFurthestPlayer(furthestPlayer, player);	
			}	
		}
		
		return furthestPlayer;
	}

	public Player getFurthestEnemyPlayer(ArrayList<Player> teamA, ArrayList<Player> teamB){
		
		Player furthestPlayer = null;
		if(this.team == TEAM_B){
			for (Player player : teamA) {
				if(player.position.equals(Player.KEEPER)) continue;
				if(furthestPlayer == null){
					furthestPlayer = player;
					continue;
				}
				furthestPlayer = getFurthestPlayer(furthestPlayer, player);	
			}	
		} else {
			for (Player player : teamB) {
				if(player.position.equals(Player.KEEPER)) continue;
				if(furthestPlayer == null){
					furthestPlayer = player;
					continue;
				}
				furthestPlayer = getFurthestPlayer(furthestPlayer, player);	
			}	
		}
		
		return furthestPlayer;
	}

	public Player getFurthestPlayer(Player playerA, Player playerB){
		if(this.getDistanceToPlayer(playerA) < this.getDistanceToPlayer(playerB))
			return playerB;
		else
			return playerA;
	}

	public int getGoalKeepingRatio() {
		return goalKeepingRatio;
	}

	public String getName() {
		return name;
	}

	public Player getNearestAllyOpenPlayer(ArrayList<Player> teamA, ArrayList<Player> teamB){
		ArrayList<Player> openPlayers = this.getOpenAllyPlayers(teamA, teamB);
		if(openPlayers.isEmpty()){
			return null;
		}
		
		Player nearestPlayer = null;
		for (Player player : openPlayers) {
			if(player.position.equals(Player.KEEPER)) continue;
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
				if(player.position.equals(Player.KEEPER)) continue;
				if(nearestPlayer == null){
					nearestPlayer = player;
					continue;
				}
				nearestPlayer = getClosestPlayer(nearestPlayer, player);	
			}	
		} else {
			for (Player player : teamB) {
				if(player.position.equals(Player.KEEPER)) continue;
				if(nearestPlayer == null){
					nearestPlayer = player;
					continue;
				}
				nearestPlayer = getClosestPlayer(nearestPlayer, player);	
			}	
		}
		
		return nearestPlayer;
	}
	
	public Player getNearestEnemyPlayer(ArrayList<Player> teamA, ArrayList<Player> teamB){
		
		Player nearestPlayer = null;
		if(this.team == TEAM_B){
			for (Player player : teamA) {
				if(player.position.equals(Player.KEEPER)) continue;
				if(nearestPlayer == null){
					nearestPlayer = player;
					continue;
				}
				nearestPlayer = getClosestPlayer(nearestPlayer, player);	
			}	
		} else {
			for (Player player : teamB) {
				if(player.position.equals(Player.KEEPER)) continue;
				if(nearestPlayer == null){
					nearestPlayer = player;
					continue;
				}
				nearestPlayer = getClosestPlayer(nearestPlayer, player);	
			}	
		}
		
		return nearestPlayer;
	}
	
	public Player getNearestEnemyPlayerOnQuadrant(ArrayList<Player> teamA, ArrayList<Player> teamB){
		ArrayList<Player> enemiesOnQuadrant = this.getEnemyPlayersOnQuadrant(teamA, teamB);
		if(enemiesOnQuadrant.isEmpty()){
			return null;
		}
		
		Player nearestPlayer = null;
		for (Player player : enemiesOnQuadrant) {
			if(player.position.equals(Player.KEEPER)) continue;
			if(nearestPlayer == null){
				nearestPlayer = player;
				continue;
			}
			nearestPlayer = getClosestPlayer(nearestPlayer, player);	
		}
		return nearestPlayer;
	}
	
	private double getNewXCoord(){
		if(position.equals(KEEPER)){
			double minX = 0.0f;
			double maxX = 2.0f;
			Random rand = new Random();
			return rand.nextDouble() * (maxX - minX) + minX;
		} else if (position.equals(DEFENDER)){
			double minX = 10.0f;
			double maxX = 11.0f;
			Random rand = new Random();
			return rand.nextDouble() * (maxX - minX) + minX;
		} else if (position.equals(STRIKER)) {
			double minX = 17.0f;
			double maxX = 19.0f;
			Random rand = new Random();
			return rand.nextDouble() * (maxX - minX) + minX;
		}
		return -1;
	}
	
	private double getNewXZoneCoord(){
			double minX = 5.0f;
			double maxX = 19.0f;
			Random rand = new Random();
			return rand.nextDouble() * (maxX - minX) + minX;
	}

	private double getNewYCoord(){
		if(position.equals(KEEPER)){
			double minY = 9.0f;
			double maxY = 11.0f;
			Random rand = new Random();
			return rand.nextDouble() * (maxY - minY) + minY;
		} else if (position.equals(DEFENDER)){
			if((quadrant == QUADRANT_THIRD) ||
					(quadrant == QUADRANT_FOURTH)){
				double minY = 5.0f;
				double maxY = 7.0f;
				Random rand = new Random();
				return rand.nextDouble() * (maxY - minY) + minY;
			} else {
				double minY = 13.0f;
				double maxY = 15.0f;
				Random rand = new Random();
				return rand.nextDouble() * (maxY - minY) + minY;
			}
		} else if (position.equals(STRIKER)) {
			if((quadrant == QUADRANT_THIRD) ||
					(quadrant == QUADRANT_FOURTH)){
				double minY = 5.0f;
				double maxY = 7.0f;
				Random rand = new Random();
				return rand.nextDouble() * (maxY - minY) + minY;
			} else {
				double minY = 13.0f;
				double maxY = 15.0f;
				Random rand = new Random();
				return rand.nextDouble() * (maxY - minY) + minY;
			}
		}
		return -1;
	}
	
	private double getNewYZoneCoord(){
			if((quadrant == QUADRANT_THIRD) ||
					(quadrant == QUADRANT_FOURTH)){
				double minY = 1.0f;
				double maxY = 9.0f;
				Random rand = new Random();
				return rand.nextDouble() * (maxY - minY) + minY;
			} else {
				double minY = 11.0f;
				double maxY = 19.0f;
				Random rand = new Random();
				return rand.nextDouble() * (maxY - minY) + minY;
			}
	}
	
	public ArrayList<Player> getOpenAllyPlayers(ArrayList<Player> teamA, ArrayList<Player> teamB){
		ArrayList<Player> openPlayers = new ArrayList<Player>();
		if (this.getTeam() == TEAM_A){
			for (Player player : teamA) {
				if(player != this && !player.isBlocked(this, teamB)){
					openPlayers.add(player);
				}
			}	
		}else{
			for (Player player : teamB) {
				if(player != this && !player.isBlocked(this, teamA)){
					openPlayers.add(player);
				}
			}	
		}
		return openPlayers;
	}
	
	public int getPassingRatio() {
		return passingRatio;
	}

	public PlayerMovement getPlayerMovement() {
		return playerMovement;
	}
	
	public int getPlayerNumber() {
		return playerNumber;
	}

	public String getPosition() {
		return position;
	}
	
	public int getQuadrant() {
		return quadrant;
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
	
	public int getTeam() {
		return team;
	}
	
	public boolean hasBall() {
		return hasBall;
	}
	
	public boolean hasClearShot(ArrayList<Player> enemyTeam, Ball ball){
		
		for (Player enemyPlayer : enemyTeam) {
			if(!(enemyPlayer.position.equals(Player.KEEPER)) && enemyPlayer.isInBetweenBallAndGoal(ball, this.team))
				return false;
		}
		return true;
		
	}

	public boolean isAroundBall(Ball ball){
		return this.getDistanceToBall(ball) < Player.PLAYER_SIZE; 
	}
	
	public boolean isAroundPlayer(Player allyPlayer){
		Player enemyPlayer = this;
		
		if (enemyPlayer.getDistanceToPlayer(allyPlayer) <= Player.PLAYER_SIZE)
			return true;
		else
			return false;

	}
	
	public boolean isBehindMidfield(){
		if(team == TEAM_A)
			return this.x() < 20;
		else
			return this.x() >= 20;
	}
	
	public boolean isBlocked(Player senderPlayer, ArrayList<Player> enemyTeam){
			for (Player enemyPlayer : enemyTeam) {
				if (enemyPlayer.isInBetweenTwoPlayers(senderPlayer, this)){
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
	
	public boolean isGoalOnQuadrant(){
		if((this.playerMovement.getGoalX() >= 20.0f) && (this.playerMovement.getGoalX() <= 40.0f)){
			//FIRST AND FOURTH
			if(this.quadrant == QUADRANT_FIRST_AND_FOURTH)
				return true;
			
			if((this.playerMovement.getGoalY() >= 10.0f) && (this.playerMovement.getGoalY() <= 20.0f)){
				//FIRST
				if(this.quadrant == QUADRANT_FIRST)
					return true;
			} 
			
			if((this.playerMovement.getGoalY() >= 0.0f) && (this.playerMovement.getGoalY() <= 10.0f)){
				//FOURTH
				if(this.quadrant == QUADRANT_FOURTH)
					return true;
			} 
		}
		if ((this.playerMovement.getGoalX() >= 0.0f) && (this.playerMovement.getGoalX() <= 20.0f)){
			//SECOND AND THIRD
			if(this.quadrant == QUADRANT_SECOND_AND_THIRD)
				return true;
			
			if((this.playerMovement.getGoalY() >= 10.0f) && (this.playerMovement.getGoalY() <= 20.0f)){
				//SECOND
				if(this.quadrant == QUADRANT_SECOND)
					return true;
			} 
			
			if((this.playerMovement.getGoalY() >= 0.0f) && (this.playerMovement.getGoalY() <= 10.0f)){
				//THIRD
				if(this.quadrant == QUADRANT_THIRD)
					return true;
			} 
		} 
		return false;
	}

	public boolean isInBetweenBallAndGoal(Ball ball, int team){
		Player me = this;
		//Goal is the Target, ball is the Origin
		double ballToGoalDirection = ball.getDirectionToGoal(team);
		double meToGoalDirection = me.getDirectionToGoal(team);			
		
		if(Math.round(ballToGoalDirection) == Math.round(meToGoalDirection)){
			return true;
		}
		return false;
	}
	
	public boolean isInBetweenTwoPlayers(Player originPlayer, Player targetPlayer){
		Player me = this;
		double originToTargetDirection = originPlayer.getDirectionToPlayer(targetPlayer);
		double meToTargetDirection = me.getDirectionToPlayer(targetPlayer);
		
		if(Math.round(originToTargetDirection) == Math.round(meToTargetDirection)){
			return true;
		}
		return false;
	}
	
	public boolean isLastDefender(ArrayList<Player> allyTeam){
		
		Player nearestPlayer = this;
		
		for (Player ally : allyTeam) {
				if (!ally.position.equals(Player.KEEPER) && 
						(nearestPlayer.getDistanceToAllyGoal() < ally.getDistanceToAllyGoal()));
					nearestPlayer = ally;
		}
		
		if(nearestPlayer == this)
			return true;
		else
			return false;
	}
	
	public boolean isNearAllyGoal(){
		return this.getDistanceToAllyGoal() < 7.0f; 
	}
	
	public boolean isNearEnemyGoal(){
		return this.getDistanceToEnemyGoal() < 7.0f; 
	}
	
	public boolean isNearestToBall(ArrayList<Player> team, Ball ball, boolean keeperIncluded){
		Player nearestPlayer = this;
		
		if(keeperIncluded == KEEPER_INCLUDED){
			for (Player player : team) {
					if (!(player.position.equals(Player.KEEPER)) &&
							(nearestPlayer.getDistanceToBall(ball)< player.getDistanceToBall(ball)));
						nearestPlayer = player;
			}
		} else{
			for (Player player : team) {
				if (nearestPlayer.getDistanceToBall(ball)< player.getDistanceToBall(ball));
					nearestPlayer = player;
			}
		}
	
		
		if(nearestPlayer == this)
			return true;
		else
			return false;
	}
	
	public boolean isOnGoal(){
		return this.playerMovement.isOnGoal();
	}
	
	public boolean isOnHisQuadrant(){
		if((this.x() >= 20.0f) && (this.x() <= 40.0f)){
			//FIRST AND FOURTH
			if(this.quadrant == QUADRANT_FIRST_AND_FOURTH)
				return true;
			
			if((this.y() >= 10.0f) && (this.y() <= 20.0f)){
				//FIRST
				if(this.quadrant == QUADRANT_FIRST)
					return true;
			} 
			
			if((this.y() >= 0.0f) && (this.y() <= 10.0f)){
				//FOURTH
				if(this.quadrant == QUADRANT_FOURTH)
					return true;
			} 
		}
		if ((this.x() >= 0.0f) && (this.x() <= 20.0f)){
			//SECOND AND THIRD
			if(this.quadrant == QUADRANT_SECOND_AND_THIRD)
				return true;
			
			if((this.y() >= 10.0f) && (this.y() <= 20.0f)){
				//SECOND
				if(this.quadrant == QUADRANT_SECOND)
					return true;
			} 
			
			if((this.y() >= 0.0f) && (this.y() <= 10.0f)){
				//THIRD
				if(this.quadrant == QUADRANT_THIRD)
					return true;
			} 
		} 
		return false;
	}
	
	public boolean isOnQuadrant(int quadrant){
		if((this.x() >= 20.0f) && (this.x() <= 40.0f)){
			//FIRST AND FOURTH
			if(quadrant == QUADRANT_FIRST_AND_FOURTH)
				return true;
			
			if((this.y() >= 10.0f) && (this.y() <= 20.0f)){
				//FIRST
				if(quadrant == QUADRANT_FIRST)
					return true;
			} 
			
			if((this.y() >= 0.0f) && (this.y() <= 10.0f)){
				//FOURTH
				if(quadrant == QUADRANT_FOURTH)
					return true;
			} 
		}
		if ((this.x() >= 0.0f) && (this.x() <= 20.0f)){
			//SECOND AND THIRD
			if(quadrant == QUADRANT_SECOND_AND_THIRD)
				return true;
			
			if((this.y() >= 10.0f) && (this.y() <= 20.0f)){
				//SECOND
				if(quadrant == QUADRANT_SECOND)
					return true;
			} 
			
			if((this.y() >= 0.0f) && (this.y() <= 10.0f)){
				//THIRD
				if(quadrant == QUADRANT_THIRD)
					return true;
			} 
		} 
		return false;
	}
	
	public boolean isPointOnQuadrant(double x, double y){
		if((x >= 20.0f) && (x <= 40.0f)){
			//FIRST AND FOURTH
			if(this.quadrant == QUADRANT_FIRST_AND_FOURTH)
				return true;
			
			if((y >= 10.0f) && (y <= 20.0f)){
				//FIRST
				if(this.quadrant == QUADRANT_FIRST)
					return true;
			} 
			
			if((y >= 0.0f) && (y <= 10.0f)){
				//FOURTH
				if(this.quadrant == QUADRANT_FOURTH)
					return true;
			} 
		}
		if ((x >= 0.0f) && (x <= 20.0f)){
			//SECOND AND THIRD
			if(this.quadrant == QUADRANT_SECOND_AND_THIRD)
				return true;
			
			if((y >= 10.0f) && (y <= 20.0f)){
				//SECOND
				if(this.quadrant == QUADRANT_SECOND)
					return true;
			} 
			
			if((y >= 0.0f) && (y <= 10.0f)){
				//THIRD
				if(this.quadrant == QUADRANT_THIRD)
					return true;
			} 
		} 
		return false;
	}
	
	private void resetNewCoords(){
		double x = getNewXCoord();
		double y = getNewYCoord();
		if (this.team == TEAM_B){
			x += (20.0f - x)*2.0f;
		}
		this.setGoal(x, y);
	}
	
	private void resetRandomCoords(){
		double x = getRandomXCoord();
		double y = getRandomYCoord();
		if (this.team == TEAM_B){
			x += (20.0f - x)*2.0f;
		}
		this.setGoal(x, y);
	}

	private void setCoordsDefensive(){
		double x = getNewXZoneCoord();
		double y = getNewXZoneCoord();
		if (this.team == TEAM_B){
			x += (20.0f - x)*2.0f;
		}
		this.setGoal(x, y);
	}
	
	private void setCoordsOffensive(){
		double x = getNewXZoneCoord();
		double y = getNewYZoneCoord();
		if (this.team == TEAM_A){
			x += (20.0f - x)*2.0f;
		}
		this.setGoal(x, y);
	}
	
	public void setGoal(double x, double y) {
		this.playerMovement.setGoal(x, y);
	}
	
	public void setHasBall(boolean hasBall) {
		this.hasBall = hasBall;
	}
	
	private void setInterceptGoal(Ball ball){
		double mFU, mBG, thetaBG, bFU, bBG, myX, myY, goalX, goalY, ballX, ballY;
		
		thetaBG = ball.getDirectionToGoal(this.team);
		mBG = Math.tan(Math.toRadians(thetaBG));
		mFU = Math.tan(Math.toRadians(thetaBG + 90.0f));
				
		myX = this.x();
		myY = this.y();
		
		ballX = ball.x();
		ballY= ball.y();
		
		bBG = ballY - (mBG * ballX);
		bFU = myY- (mFU * myX);
		
		goalX = (bBG - bFU) / (mFU - mBG);
		goalY = mFU * goalX + bFU;
		
		setGoal(goalX, goalY);
		
	}
	
	private void setInterceptPass(Ball ball, Player enemyPlayer){
		double mFU, mBG, thetaBG, bFU, bBG, myX, myY, goalX, goalY, ballX, ballY;
		
		thetaBG = ball.getDirectionToPlayer(enemyPlayer);
		mBG = Math.tan(Math.toRadians(thetaBG));
		mFU = Math.tan(Math.toRadians(thetaBG + 90.0f));
				
		myX = this.x();
		myY = this.y();
		
		ballX = ball.x();
		ballY= ball.y();
		
		bBG = ballY - (mBG * ballX);
		bFU = myY- (mFU * myX);
		
		goalX = (bBG - bFU) / (mFU - mBG);
		goalY = mFU * goalX + bFU;
		
		setGoal(goalX, goalY);
		
	}
	
	private void setNewDirection(double direction, double distance){
		double sin, cos, goalX, goalY;
		
		sin = Math.sin(Math.toRadians(direction));
		cos = Math.cos(Math.toRadians(direction));
		
		goalX = x() + (distance * cos);
		goalY = y() + (distance * sin);
		
		setGoal(goalX, goalY);
		
	}

	public void setNewGoal(int option){
		switch (option) {
		case NEW_GOAL_GENERAL_AREA:
			resetRandomCoords();
			break;
		case NEW_GOAL_POSITION_AREA:
			resetNewCoords();
			break;
		case NEW_GOAL_STRIKER_OFFENSIVE:
			setCoordsOffensive();
			break;
		case NEW_GOAL_DEFENDER_DEFENSE:
			setCoordsDefensive();
			break;
		default:
			break;
		}
	}

	public void setNewGoal(int option, Ball ball){
		switch (option) {
		case NEW_GOAL_INTERCEPT_GOAL:
			setInterceptGoal(ball);
			break;
		case NEW_GOAL_CHASE_BALL:
			this.setGoal(ball.x(), ball.y());
			break;
		default:
			break;
		}
	}

	public void setNewGoal(int option, Ball ball, Player player){
		switch (option) {
		case NEW_GOAL_INTERCEPT_PASS:
			setInterceptPass(ball, player);
			break;
		default:
			break;
		}
	}

	public void setNewGoal(int option, double direction){
		switch (option) {
		case NEW_GOAL_NEW_DIRECTION:
			setNewDirection(direction, Player.PLAYER_SIZE);
			break;
		default:
			break;
		}
	}
	
	public void setNewGoal(int option, double direction, double distance){
		switch (option) {
		case NEW_GOAL_NEW_DIRECTION:
			setNewDirection(direction, distance);
			break;
		default:
			break;
		}
	}
	
	public void setNewGoal(int option, Player player){
		switch (option) {
		case NEW_GOAL_CHASE_PLAYER:
			this.setGoal(player.x(), player.y());
			break;
		default:
			break;
		}
	}
	
	public double x(){
		return playerMovement.x();
	}
	
	public double y(){
		return playerMovement.y();
	}
}
