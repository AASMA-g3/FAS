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
		
		currentMovement = new BallMovement(0, direction, 20.0f, 10.0f, 0.0f);
	}
	
	public BallMovement getCurrentMovement() {
		return currentMovement;
	}

	public void setCurrentMovement(BallMovement currentMovement) {
		this.currentMovement = currentMovement;
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
		if (team == Player.TEAM_A){
			return Math.toDegrees(Math.atan((Game.GOAL_Y_MED - this.y())
					/ (0 - this.x())));
		}else{
			return Math.toDegrees(Math.atan((Game.GOAL_Y_MED - this.y())
					/ (Game.LIMIT_X - this.x())));
		}
	}
	
	public double getDirectionToPlayer(Player player){
		return Math.toDegrees(Math.atan((player.y() - this.y())
					/ (player.x() - this.x())));
	}
	
}
