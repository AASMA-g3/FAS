package pt.ulisboa.aasma.fas.jade.game;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import pt.ulisboa.aasma.fas.exceptions.PlayerDoesNotExistException;

public class Game {

	public static final double GAME_TIME = 500000.0f;
	
	public static final int LIMIT_X = 40;
	public static final int LIMIT_Y = 20;
	
	public static final long TICK_TIME = 10;
	public static final int PLAYERS_PER_TEAM = 5;
	
	public static final int GOAL_Y_MIN = 9;
	public static final int GOAL_Y_MAX = 11;
	
	public static final int GOAL_Y_MED = 10;
	
	private ArrayList<Player> teamA;
	private ArrayList<Player> teamB;
	
	private Ball ball;
	
	private int playerNumberCounter = 1;
	
	private double gameTime = 0;
	
	public final AtomicBoolean isEnded = new AtomicBoolean(false);
	
	public Game(ArrayList<Integer> ratios) {

		this.ball = new Ball();
		this.teamA = new ArrayList<Player>();
		this.teamB = new ArrayList<Player>();
		
		ArrayList<String> names = getRandomNames();
		
		this.teamA.add(new Player(names.get(0), ratios
				.get(4), ratios.get(2), ratios.get(0),
				ratios.get(8), ratios.get(6),
				Player.KEEPER, Player.TEAM_A,
				playerNumberCounter));
		playerNumberCounter++;
		this.teamA.add(new Player(names.get(1), ratios
				.get(4), ratios.get(2), ratios.get(0),
				ratios.get(8), ratios.get(6),
				Player.DEFENDER, Player.TEAM_A,
				playerNumberCounter));
		playerNumberCounter++;
		this.teamA.add(new Player(names.get(2), ratios
				.get(4), ratios.get(2), ratios.get(0),
				ratios.get(8), ratios.get(6),
				Player.DEFENDER, Player.TEAM_A,
				playerNumberCounter));
		playerNumberCounter++;
		this.teamA.add(new Player(names.get(3),ratios
				.get(4), ratios.get(2), ratios.get(0),
				ratios.get(8), ratios.get(6),
				Player.STRIKER, Player.TEAM_A,
				playerNumberCounter));
		playerNumberCounter++;
		this.teamA.add(new Player(names.get(4),  ratios
				.get(4), ratios.get(2), ratios.get(0),
				ratios.get(8), ratios.get(6),
				Player.STRIKER, Player.TEAM_A,
				playerNumberCounter));
		playerNumberCounter = 1;
		this.teamB.add(new Player(names.get(5), ratios
				.get(5), ratios.get(3), ratios.get(1),
				ratios.get(9), ratios.get(7),
				Player.KEEPER, Player.TEAM_B,
				playerNumberCounter));
		playerNumberCounter++;
		this.teamB.add(new Player(names.get(6),  ratios
				.get(5), ratios.get(3), ratios.get(1),
				ratios.get(9), ratios.get(7),
				Player.DEFENDER, Player.TEAM_B,
				playerNumberCounter));
		playerNumberCounter++;
		this.teamB.add(new Player(names.get(7),  ratios
				.get(5), ratios.get(3), ratios.get(1),
				ratios.get(9), ratios.get(7),
				Player.DEFENDER, Player.TEAM_B,
				playerNumberCounter));
		playerNumberCounter++;
		this.teamB.add(new Player(names.get(8), ratios
				.get(5), ratios.get(3), ratios.get(1),
				ratios.get(9), ratios.get(7),
				Player.STRIKER, Player.TEAM_B,
				playerNumberCounter));
		playerNumberCounter++;
		this.teamB.add(new Player(names.get(9),  ratios
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

	public double getGameTime() {
		return gameTime;
	}

	public void setGameTime(double gameTime) {
		this.gameTime = gameTime;
	}
	
}
