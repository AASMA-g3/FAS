package pt.ulisboa.aasma.fas.j2d;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;

import pt.ulisboa.aasma.fas.jade.game.Player;


public class PlayerGraphic implements Sprite {
	private static final double SPEED = 200; //Velocidade em 20 pixels / segundo
	private static final double SIZE = Player.PLAYER_SIZE * GameRunner.SCREEN_RATIO_X;
	
	private Player player;
	
	private double vx = SPEED;
	private double vy = SPEED / 2;

	
	public PlayerGraphic(Player player) {
		this.player = player;
	}
	
	
	public void update(double time) {
		this.player.getPlayerMovement().updateT(time/1000.0f);
	}
	
	public void draw(Graphics2D g2d) {
		Graphics2D g = (Graphics2D) g2d.create();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.BLACK);
		g.fill(new Ellipse2D.Double(getDrawableX(), getDrawableY(), SIZE + 5, SIZE + 5));
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if(player.getTeam() == Player.TEAM_A)
			g.setColor(Color.RED);
		else g.setColor(Color.BLUE);
		g.fill(new Ellipse2D.Double(getDrawableX() + (double) 2.5, getDrawableY() + (double) 2.5, SIZE, SIZE));
		
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setColor(Color.WHITE);
		g.drawString(Integer.toString(player.getPlayerNumber()),(int)getDrawableX() + 9, (int)getDrawableY() + 17);
		g.dispose();
	}
	
	public double getDrawableX(){
		return (player.x()*GameRunner.SCREEN_RATIO_X)+GameRunner.SCREEN_OFFSET_X;
	}
	
	public double getDrawableY(){
		return ((20-player.y())*GameRunner.SCREEN_RATIO_Y)+GameRunner.SCREEN_OFFSET_Y;
	}
}
