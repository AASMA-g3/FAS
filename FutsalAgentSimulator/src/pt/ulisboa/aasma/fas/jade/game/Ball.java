package pt.ulisboa.aasma.fas.jade.game;

public class Ball {

	private float xCoord;
	private float yCoord;

	public Ball() {
		super();
		this.xCoord=1;
		this.yCoord=(float) 0.5;
	}
	
	public float getxCoord() {
		return xCoord;
	}
	public void setxCoord(float xCoord) {
		this.xCoord = xCoord;
	}
	public float getyCoord() {
		return yCoord;
	}
	public void setyCoord(float yCoord) {
		this.yCoord = yCoord;
	}
	
	
}
