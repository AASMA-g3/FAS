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
		long prob = Math.round(Math.random());
		double direction = 0.0f;
		if (prob == 0) direction = 180.0f;
		
		currentMovement = new BallMovement(0, 0.0f, 20.0f, 10.0f, 0.0f);
	}
	
	public BallMovement getCurrentMovement() {
		return currentMovement;
	}
	
	public double x(){
		return currentMovement.x();
	}
	
	public double y(){
		return currentMovement.y();
	}
	
	public double getOriginalIntensity() {
		return currentMovement.getOriginalIntensity();
	}
	
	public boolean isOnTrajectoryToGoal(int team){
			double ballDirection = getCurrentMovement().getDirection();
			double ballToGoalDirection = getDirectionToGoal(team);
			
			if(Math.round(ballDirection) == Math.round(ballToGoalDirection)){
				return true;
			}	
			return false;
	}
	
	public boolean isOnTrajectoryToPlayer(Player player){
		double ballDirection = getCurrentMovement().getDirection();
		double ballToPlayerDirection = getDirectionToPlayer(player);
		
		if(Math.round(ballDirection) == Math.round(ballToPlayerDirection)){
			return true;
		}	
		return false;
	}

	
	public double getDirectionToGoal(int team){
		double angle;
		if (team == Player.TEAM_A){
			angle= Math.toDegrees(Math.atan2(Game.GOAL_Y_MED - this.y(),0 - this.x()));
		}else{
			angle= Math.toDegrees(Math.atan2(Game.GOAL_Y_MED - this.y(), Game.LIMIT_X - this.x()));
		}
		
		if(angle < 0.0f){
	        angle += 360.0f;
	    }

	    return angle;
	}
	
	public double getDirectionToPlayer(Player player){
		double angle;
		angle= Math.toDegrees(Math.atan2(player.y() - this.y(), player.x() - this.x()));
		if(angle < 0.0f){
	        angle += 360.0f;
	    }
	    return angle;
	}

	public void updateCurrentMovement(int intensity, double direction, double x0, double y0, double initialTime) {
		this.currentMovement.updateCurrentMovement(intensity, direction, x0, y0, initialTime);
	}
	
}
