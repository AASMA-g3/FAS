package pt.ulisboa.aasma.fas.jade.game;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import pt.ulisboa.aasma.fas.exceptions.PlayerDoesNotExistException;

public class Game {

	public static final int GAME_TIME = 120000;
	
	public static final int LIMIT_X = 40;
	public static final int LIMIT_Y = 20;
	
	public static final int TICK_TIME = 10;
	public static final int PLAYERS_PER_TEAM = 5;
	
	public static final int GOAL_Y_MIN = 9;
	public static final int GOAL_Y_MAX = 11;
	
	public static final int GOAL_Y_MED = 10;
	
	private int TeamAScore;
	private int TeamBScore;
	
	private ArrayList<Player> teamA;
	private ArrayList<Player> teamB;
	
	private Ball ball;
	
	private int playerNumberCounter = 1;
	
	private int gameTime = 0;
	
	public final AtomicBoolean isEnded = new AtomicBoolean(false);
	
	public Game(ArrayList<Integer> ratios) {

		this.ball = new Ball();
		this.teamA = new ArrayList<Player>();
		this.teamB = new ArrayList<Player>();
		this.TeamAScore = 0;
		this.TeamBScore = 0;
		
	//	ArrayList<String> names = getRandomNames();
		
		this.teamA.add(new Player("Red1", ratios
				.get(4), ratios.get(2), ratios.get(0),
				ratios.get(8), ratios.get(6),
				Player.KEEPER, Player.TEAM_A,
				playerNumberCounter));
		playerNumberCounter++;
		this.teamA.add(new Player("Red2", ratios
				.get(4), ratios.get(2), ratios.get(0),
				ratios.get(8), ratios.get(6),
				Player.DEFENDER, Player.TEAM_A,
				playerNumberCounter));
		playerNumberCounter++;
		this.teamA.add(new Player("Red3", ratios
				.get(4), ratios.get(2), ratios.get(0),
				ratios.get(8), ratios.get(6),
				Player.DEFENDER, Player.TEAM_A,
				playerNumberCounter));
		playerNumberCounter++;
		this.teamA.add(new Player("Red4",ratios
				.get(4), ratios.get(2), ratios.get(0),
				ratios.get(8), ratios.get(6),
				Player.STRIKER, Player.TEAM_A,
				playerNumberCounter));
		playerNumberCounter++;
		this.teamA.add(new Player("Red5",  ratios
				.get(4), ratios.get(2), ratios.get(0),
				ratios.get(8), ratios.get(6),
				Player.STRIKER, Player.TEAM_A,
				playerNumberCounter));
		playerNumberCounter = 1;
		this.teamB.add(new Player("Blue1", ratios
				.get(5), ratios.get(3), ratios.get(1),
				ratios.get(9), ratios.get(7),
				Player.KEEPER, Player.TEAM_B,
				playerNumberCounter));
		playerNumberCounter++;
		this.teamB.add(new Player("Blue2",  ratios
				.get(5), ratios.get(3), ratios.get(1),
				ratios.get(9), ratios.get(7),
				Player.DEFENDER, Player.TEAM_B,
				playerNumberCounter));
		playerNumberCounter++;
		this.teamB.add(new Player("Blue3",  ratios
				.get(5), ratios.get(3), ratios.get(1),
				ratios.get(9), ratios.get(7),
				Player.DEFENDER, Player.TEAM_B,
				playerNumberCounter));
		playerNumberCounter++;
		this.teamB.add(new Player("Blue4", ratios
				.get(5), ratios.get(3), ratios.get(1),
				ratios.get(9), ratios.get(7),
				Player.STRIKER, Player.TEAM_B,
				playerNumberCounter));
		playerNumberCounter++;
		this.teamB.add(new Player("Blue5",  ratios
				.get(5), ratios.get(3), ratios.get(1),
				ratios.get(9), ratios.get(7),
				Player.STRIKER, Player.TEAM_B,
				playerNumberCounter));
		
	}

	public static ArrayList<String> getRandomNames(){
		ArrayList<String> randomNames = new ArrayList<String>();
		
		String[] names = {"Ronaldo", "Messi", "Robben", "Neuer", "Ebenzema", "Tripho", "Bale"};
        String[] names2 = {"Von", "Mig", "Sin", "Tri", "Son", "Khan", "Tro"};
        String[] names3 = {"Tripas", "Maria", "Couves", "Rodrigues", "Cenas", "Smoke", "Blaze"};

        for (int i = 0; i < 10; i++){
        	int random = (int) (Math.random()*names.length);
            int random2 = (int) (Math.random()*names2.length);
            int random3 = (int) (Math.random()*names3.length);

            String name = names[random] + names2[random2] + names3[random3];
            
            boolean repeated = false;
            for (String s : randomNames){
            	if(name.equals(s)){
            		repeated = true;
            		i--;
            		break;
            	}
            }
        	if (repeated)
        		continue;
        	else
        		randomNames.add(name);
        }
        return randomNames;
	}
	
	
	public ArrayList<Player> getTeamA() {
		return teamA;
	}

	public void setTeamA(ArrayList<Player> teamA) {
		this.teamA = teamA;
	}

	public ArrayList<Player> getTeamB() {
		return teamB;
	}

	public void setTeamB(ArrayList<Player> teamB) {
		this.teamB = teamB;
	}

	public void addPlayerTeamA(Player player) {
		this.teamA.add(player);
	}

	public void addPlayerTeamB(Player player) {
		this.teamB.add(player);
	}

	public int getPlayerNumberCounter() {
		return playerNumberCounter;
	}

	public void setPlayerNumberCounter(
			int playerNumberCounter) {
		this.playerNumberCounter = playerNumberCounter;
	}

	public Ball getBall() {
		return ball;
	}

	public void setBall(Ball ball) {
		this.ball = ball;
	}
	
	public Player getPlayer(String playerName){
		for (Player player : teamA){
			if(player.getName().equals(playerName))
				return player;
		}
		for (Player player : teamB){
			if(player.getName().equals(playerName))
				return player;
		}
		throw new PlayerDoesNotExistException(playerName);
	}

	public int getGameTime() {
		return gameTime;
	}

	public void setGameTime(int gameTime) {
		this.gameTime = gameTime;
	}

	public int getTeamAScore() {
		return TeamAScore;
	}

	public void setTeamAScore(int teamAScore) {
		TeamAScore = teamAScore;
	}

	public int getTeamBScore() {
		return TeamBScore;
	}

	public void setTeamBScore(int teamBScore) {
		TeamBScore = teamBScore;
	}
	
	public boolean isGoal(){
		double X = ball.x();
		double Y = ball.y();
		
		if (X < 0.0f){
			if(((X > -1.0f) && (Y > Game.GOAL_Y_MIN) && (Y < Game.GOAL_Y_MAX))){
				return true;
			}
		}
		if (X > Game.LIMIT_X){
			if(!((X < (Game.LIMIT_X + 1.0f)) && (Y > Game.GOAL_Y_MIN) && (Y < Game.GOAL_Y_MAX))){
				return true;
			}
		}
		return false;
		
	}
	
}
