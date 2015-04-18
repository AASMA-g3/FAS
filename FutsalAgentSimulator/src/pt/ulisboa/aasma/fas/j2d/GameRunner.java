package pt.ulisboa.aasma.fas.j2d;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;

public class GameRunner extends JFrame implements LoopSteps {
	/**
	 * @author Ode
	 */

	private MainLoop loop = new MainLoop(this, 60);

	private long previous = System.currentTimeMillis();
	private Scorer scorer;
	private Ball ball;
	private FutsalPitch pitch;

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
		ball = new Ball(getWidth() - getInsets().left - getInsets().right,
				getHeight() - getInsets().top - getInsets().bottom);
		ball.init();


	}

	public void processLogics() {
		long time = System.currentTimeMillis() - previous;

		ball.update(time);
		scorer.update(time);

		previous = System.currentTimeMillis();
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

	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				GameRunner bf = new GameRunner();
				bf.setVisible(true);
				bf.startMainLoop();
			}
		});
	}
}
