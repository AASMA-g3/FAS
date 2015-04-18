package pt.ulisboa.aasma.fas.j2d;


import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.TexturePaint;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import pt.ulisboa.aasma.fas.jade.game.Ball;


public class BallGraphic implements Sprite {
	private static final float SPEED = 200; //Velocidade em 20 pixels / segundo
	private static final int SIZE = 10;
	private static final float INITIAL_X = (float) 299;
	private static final float INITIAL_Y = 173;
	private  BufferedImage ball_texture = null;
	
	private Ball ball;
	
	private float vx = SPEED;
	private float vy = SPEED / 2;
	
	private int screenWidth;
	private int screenHeight;
	
	public BallGraphic(Ball ball) {
		this.ball = ball;
	}
	
	public void init(){
		try {
			  ball_texture = ImageIO.read(new File("textures/ballTexture.jpg"));
			 
			} catch (IOException e) {
				System.out.println("Background loading failed");
			}
	}
	
	public void update(long time) {
		//O tempo é em milis. Para obter em segundos, precisamos dividi-lo por 1000.
		//TODO: Comportamento da bola -> físicas
		/*x += (time * vx) / 1000;
		y += (time * vy) / 1000;
		checkCollision();*/
	}
	
	
	public void draw(Graphics2D g2d) {
		Graphics2D g = (Graphics2D) g2d.create();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		TexturePaint tp = new TexturePaint(ball_texture, new Rectangle2D.Double(getDrawableX(),getDrawableX(), SIZE, SIZE));
		g.setPaint(tp);
		g.fill(new Ellipse2D.Float(getDrawableX(), getDrawableY(), SIZE, SIZE));
		g.dispose();
	}

	public float getDrawableY() {
		return (ball.getxCoord()*GameRunner.SCREEN_RATIO_X)+GameRunner.SCREEN_OFFSET_Y;
	}

	public float getDrawableX() {
		return (ball.getyCoord()*GameRunner.SCREEN_RATIO_Y)+GameRunner.SCREEN_OFFSET_Y;
	}
	
	
}
