package pt.ulisboa.aasma.fas.jade.game;

public class Ball {

	private int xCoord;
	private int yCoord;

	public Ball() {
		super();
		this.xCoord=0;
		this.yCoord=0;
	}
	
	public int getxCoord() {
		return xCoord;
	}
	public void setxCoord(int xCoord) {
		this.xCoord = xCoord;
	}
	public int getyCoord() {
		return yCoord;
	}
	public void setyCoord(int yCoord) {
		this.yCoord = yCoord;
	}
	
	
}
