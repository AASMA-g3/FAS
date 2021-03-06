package pt.ulisboa.aasma.fas.j2d;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pt.ulisboa.aasma.fas.jade.game.Game;
import pt.ulisboa.aasma.fas.jade.game.Player;

public class GameRunner extends JFrame implements LoopSteps {
	/**
	 * @author Ode
	 */
	private static final long serialVersionUID = 1L;
	public static final double SCREEN_RATIO_X = 14.35f;
	public static final double SCREEN_RATIO_Y = 14.0f;
	
	public static final double SCREEN_OFFSET_X = 8.0f;
	public static final double SCREEN_OFFSET_Y = 57.0f; 
	
	private MainLoop loop = new MainLoop(this, 60);

	private Scorer scorer;
	private BallGraphic ball;
	private FutsalPitch pitch;
	private Game game;
	private ArrayList<PlayerGraphic> playerList;
	
	private boolean gameInProgress = false;
	
	public GameRunner() {
		super("FutsalAgentSimulator");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		setSize(606, 385);
		setResizable(false);

		//SLIDERS E BOT�ES TODO
		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				// Se carregar no x, paramos o loop.
				loop.stop();
			}
		});
		setVisible(true);
		
	
		// TODO: SETUP DE TODOS OS AGENTES + BOLA
		pitch = new FutsalPitch();
		
		scorer = new Scorer();
		
		startMainLoop();

	}

	public void startMainLoop() {
		// Iniciamos o main loop
		new Thread(loop, "Main loop").start();
	}
	
	@Override
	public void setup() {
		// TODO Auto-generated method stub
		createBufferStrategy(2);
		pitch.init();
	}

	
	public void startGame(Game game){
		this.game = game;
		
		ball = new BallGraphic(game.getBall());
		ball.init();
		
		playerList = new ArrayList<PlayerGraphic>();
		
		for(Player player : game.getTeamA()){
			PlayerGraphic playerGraphic = new PlayerGraphic(player);
			playerList.add(playerGraphic);
		}
		for(Player player : game.getTeamB()){
			PlayerGraphic playerGraphic = new PlayerGraphic(player);
			playerList.add(playerGraphic);
		}
		gameInProgress = true;
	}

	public void processLogics() {
		if (gameInProgress){
			
			double time = game.getGameTime();
			ball.update(time);
			
			scorer.setScoreTeamA(game.getTeamAScore());
			scorer.setScoreTeamB(game.getTeamBScore());
			
			scorer.update(time);
			
			for (PlayerGraphic pl : playerList)
				pl.update(time);
			
		}
		
	}

	public void renderGraphics() {
		Graphics g = getBufferStrategy().getDrawGraphics();

		// Criamos um contexto gr�fico que n�o leva em conta as bordas
		Graphics g2 = g.create(getInsets().right, getInsets().top, getWidth()
				- getInsets().left, getHeight() - getInsets().bottom);
		// Limpamos o ecra
		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, 606, 357);
		
		scorer.draw((Graphics2D) g2);
		pitch.draw((Graphics2D) g2);

		if (gameInProgress){
		
			for(PlayerGraphic pl : playerList)
				pl.draw((Graphics2D) g2);
			
			ball.draw((Graphics2D) g2);
			
		}
		g.dispose();
		g2.dispose();
	}
	
	@Override
	public void paint(Graphics g) {
		g = g.create(getInsets().right, getInsets().top, getWidth()
				- getInsets().left,  getInsets().bottom);

		g.fillRect(0, 0, 606, 357);

		scorer.draw((Graphics2D) g);
		pitch.draw((Graphics2D) g);
		
		if (gameInProgress){
			
			for(PlayerGraphic pl : playerList)
				pl.draw((Graphics2D) g);
			
			ball.draw((Graphics2D) g);
		
		}
		g.dispose();
	}

	public void paintScreen() {
		if (!getBufferStrategy().contentsLost())
			getBufferStrategy().show();
	}

	public void tearDown() {
		pitch = null;
		ball = null;	
	}

	public MainLoop getLoop() {
		return loop;
	}

	public void setLoop(MainLoop loop) {
		this.loop = loop;
	}

	public Scorer getScorer() {
		return scorer;
	}

	public void setScorer(Scorer scorer) {
		this.scorer = scorer;
	}

	public BallGraphic getBall() {
		return ball;
	}

	public void setBall(BallGraphic ball) {
		this.ball = ball;
	}

	public FutsalPitch getPitch() {
		return pitch;
	}

	public void setPitch(FutsalPitch pitch) {
		this.pitch = pitch;
	}

	public boolean isGameInProgress() {
		return gameInProgress;
	}

	public void setGameInProgress(boolean gameInProgress) {
		this.gameInProgress = gameInProgress;
	}



}
