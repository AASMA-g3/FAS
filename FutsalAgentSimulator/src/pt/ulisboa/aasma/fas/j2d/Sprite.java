package pt.ulisboa.aasma.fas.j2d;

import java.awt.Graphics2D;


public interface Sprite {
	void update(double time);
	void draw(Graphics2D g2d);
}
