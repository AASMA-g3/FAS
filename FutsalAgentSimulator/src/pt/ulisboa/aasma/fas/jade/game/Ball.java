package pt.ulisboa.aasma.fas.jade.game;

public class Ball {

	private float xCoord;
	private float yCoord;
	
	private Boolean shoted = false;
	private Boolean passed = false;
	private Boolean dribled = false;

	public Ball() {
		super();
		this.xCoord=1;
		this.yCoord=(float) 0.5;
		this.dribled = false;
		this.passed = false;
		this.shoted = false;
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

	public Boolean getShoted() {
		return shoted;
	}

	public void setShoted(Boolean shoted) {
		this.shoted = shoted;
	}

	public Boolean getPassed() {
		return passed;
	}

	public void setPassed(Boolean passed) {
		this.passed = passed;
	}

	public Boolean getDribled() {
		return dribled;
	}
	
	public void setDribled(Boolean dribled) {
		this.dribled = dribled;
	}
	
	
}
