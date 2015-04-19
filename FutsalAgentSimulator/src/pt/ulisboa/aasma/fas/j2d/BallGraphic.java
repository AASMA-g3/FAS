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

	private static final int SIZE = 10;
	private  BufferedImage ball_texture = null;
	
	private Ball ball;
	
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
	
	public void update(double time) {
		ball.getCurrentMovement().updateT(time/1000.0f);
	}
	
	
	public void draw(Graphics2D g2d) {
		Graphics2D g = (Graphics2D) g2d.create();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		TexturePaint tp = new TexturePaint(ball_texture, new Rectangle2D.Double(getDrawableX(),getDrawableX(), SIZE, SIZE));
		g.setPaint(tp);
		g.fill(new Ellipse2D.Double(getDrawableX(), getDrawableY(), SIZE, SIZE));
		g.dispose();
	}

	public double getDrawableX() {
		return (ball.getCurrentMovement().x()*GameRunner.SCREEN_RATIO_X)+GameRunner.SCREEN_OFFSET_X;
	}

	public double getDrawableY() {
		return (ball.getCurrentMovement().y()*GameRunner.SCREEN_RATIO_Y)+GameRunner.SCREEN_OFFSET_Y;
	}
	
	
}
