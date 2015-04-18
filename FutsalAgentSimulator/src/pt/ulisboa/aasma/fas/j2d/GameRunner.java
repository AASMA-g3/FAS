package pt.ulisboa.aasma.fas.j2d;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JFrame;

import pt.ulisboa.aasma.fas.jade.game.Game;
import pt.ulisboa.aasma.fas.jade.game.Player;

public class GameRunner extends JFrame implements LoopSteps {
	/**
	 * @author Ode
	 */
	private static final long serialVersionUID = 1L;
	public static final long SCREEN_RATIO_X = 287;
	public static final long SCREEN_RATIO_Y = 280;
	
	public static final long SCREEN_OFFSET_X = 8;
	public static final long SCREEN_OFFSET_Y = 57; 
	
	private MainLoop loop = new MainLoop(this, 60);

	private Scorer scorer;
	private BallGraphic ball;
	private FutsalPitch pitch;
	private Game game;
	private ArrayList<PlayerGraphic> playerList;
	
	public GameRunner() {
		super("FutsalAgentSimulator");

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(606, 600);
		setResizable(false);
		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				// Se carregar no x, paramos o loop.
				loop.stop();
			}
		});

	}

	public void startMainLoop() {
		// Iniciamos o main loop
		new Thread(loop, "Main loop").start();
	}

	public void setup() {
		createBufferStrategy(2);
		// TODO: SETUP DE TODOS OS AGENTES + BOLA
		pitch = new FutsalPitch();
		scorer = new Scorer();
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
		
	}

	public void processLogics() {
		long time = game.getGameTime();

		ball.update(time);
		scorer.update(time);
		for (PlayerGraphic pl : playerList){
			pl.update(time);
		}

	}

	public void renderGraphics() {
		Graphics g = getBufferStrategy().getDrawGraphics();

		// Criamos um contexto gráfico que não leva em conta as bordas
		Graphics g2 = g.create(getInsets().left, getInsets().top, getWidth()
				- getInsets().right, getHeight() - getInsets().bottom);
		// Limpamos o ecra
		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, 606, 357);

		scorer.draw((Graphics2D) g2);

		if (pitch != null)
			pitch.draw((Graphics2D) g2);

		if (ball != null)
			ball.draw((Graphics2D) g2);
		
		for(PlayerGraphic pl : playerList){
			pl.draw((Graphics2D) g2);
		}

		g.dispose();
		g2.dispose();
	}

	public void paint(Graphics g) {

		g = g.create(getInsets().right, getInsets().top, getWidth()
				- getInsets().left, getHeight() - getInsets().bottom);

		g.fillRect(0, 0, 606, 357);

		if (scorer != null)
			scorer.draw((Graphics2D) g);

		if (pitch != null)
			pitch.draw((Graphics2D) g);

		if (ball != null)
			ball.draw((Graphics2D) g);

		for(PlayerGraphic pl : playerList){
			pl.draw((Graphics2D) g);
		}

		g.dispose();
	}

	public void paintScreen() {
		if (!getBufferStrategy().contentsLost())
			getBufferStrategy().show();
	}

	public void tearDown() {
		// Não é realmente necessário, pois o jogo acaba.
		// Mas se fosse um fim de fase, seria.
		pitch = null;
		ball = null;
		scorer = null;
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


}
