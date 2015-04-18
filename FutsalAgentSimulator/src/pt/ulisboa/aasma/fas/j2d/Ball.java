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


public class Ball implements Sprite {
	private static final float SPEED = 200; //Velocidade em 20 pixels / segundo
	private static final int SIZE = 10;
	private static final float INITIAL_X = (float) 299;
	private static final float INITIAL_Y = 173;
	private  BufferedImage ball_texture = null;
	
	private float x = INITIAL_X;
	private float y = INITIAL_Y;
	
	private float vx = SPEED;
	private float vy = SPEED / 2;
	
	private int screenWidth;
	private int screenHeight;
	
	public Ball(int screenWidth, int screenHeight) {
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
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
	
	private void checkCollision()
	{
		//Testamos se a bola saiu da tela
		//Se sair, recolocamos na tela e invertemos a velocidade do eixo
		//Isso fará a bola "quicar".		
		if (x < 0) { //Lateral esquerda
			vx = -vx;
			x = 0;
		} else if ((x+SIZE) > screenWidth) { //Lateral direita
			vx = -vx;
			x = screenWidth - SIZE;
		}
		
		if (y < 0) { //topo
			vy = -vy;
			y = 0;
		} else if (((y+SIZE) > screenHeight)) { //baixo
			vy = -vy;
			y = screenHeight - SIZE;
		}
	}
	
	
	public void draw(Graphics2D g2d) {
		Graphics2D g = (Graphics2D) g2d.create();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		TexturePaint tp = new TexturePaint(ball_texture, new Rectangle2D.Double(x,y, SIZE, SIZE));
		g.setPaint(tp);
		g.fill(new Ellipse2D.Float(x, y, SIZE, SIZE));
		g.dispose();
	}
}
