package pt.ulisboa.aasma.fas.j2d;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;


public class Player implements Sprite {
	private static final float SPEED = 200; //Velocidade em 20 pixels / segundo
	private static final int SIZE = 20;
	
	private float x;
	private float y;
	private int number;
	private boolean isATeam;
	
	private float vx = SPEED;
	private float vy = SPEED / 2;

	
	public Player(boolean isATeam, int number, float x, float y) {
		this.x = x;
		this.y = y;
		this.isATeam = isATeam;
		this.number = number;
	}
	
	
	public void update(long time) {

	}
	
	public void draw(Graphics2D g2d) {
		Graphics2D g = (Graphics2D) g2d.create();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.BLACK);
		g.fill(new Ellipse2D.Float(x, y, SIZE + 5, SIZE + 5));
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if(isATeam)
			g.setColor(Color.RED);
		else g.setColor(Color.BLUE);
		g.fill(new Ellipse2D.Float(x + (float) 2.5, y + (float)2.5, SIZE, SIZE));
		
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setColor(Color.WHITE);
		g.drawString(Integer.toString(this.number),x + 9, y + 17);
		g.dispose();
	}
}
