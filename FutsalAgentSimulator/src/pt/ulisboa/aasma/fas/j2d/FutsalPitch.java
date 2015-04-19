package pt.ulisboa.aasma.fas.j2d;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class FutsalPitch implements Sprite {
	private BufferedImage background = null;

	public void update(double time) {
	}

	public void init(){
		try {
			  background = ImageIO.read(new File("textures/FutsalPitch.png"));
			 
			} catch (IOException e) {
				System.out.println("Background loading failed");
			}
	}
	
	public void draw(Graphics2D g2d) {
		Graphics2D g = (Graphics2D) g2d.create();	
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.drawImage(background, 0, 50, null);
		g.dispose();
	}

}
