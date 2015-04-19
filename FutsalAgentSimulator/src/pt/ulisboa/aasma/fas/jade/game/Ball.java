package pt.ulisboa.aasma.fas.jade.game;

public class Ball {
	
	public static final int INTENSITY_SHOOT = 10;
	public static final int INTENSITY_LONG_PASS = 8;
	public static final int INTENSITY_MEDIUM_PASS = 6;
	public static final int INTENSITY_SHORT_PASS = 4;
	public static final int INTENSITY_RUN = 2;

	private BallMovement currentMovement;

	public Ball() {
		super();
		int prob = (int)(Math.random());
		double direction = 0.0f;
		if (prob == 0) direction = 180.0f;
		
		currentMovement = new BallMovement(Ball.INTENSITY_MEDIUM_PASS, direction, 20.0f, 10.0f, 0.0f);
	}
	
	public BallMovement getCurrentMovement() {
		return currentMovement;
	}

	public void setCurrentMovement(BallMovement currentMovement) {
		this.currentMovement = currentMovement;
	}
	
	public double getDistanceToBall(Player player){
		return Math.sqrt(Math.pow((player.y() - y()), 2) +
				Math.pow((player.x() - x()), 2));
	}
	
	public double x(){
		return currentMovement.x();
	}
	
	public double y(){
		return currentMovement.y();
	}
	
	public int getOriginalIntensity() {
		return currentMovement.getOriginalIntensity();
	}
}
