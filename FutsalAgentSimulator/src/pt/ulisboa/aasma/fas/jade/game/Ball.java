package pt.ulisboa.aasma.fas.jade.game;

public class Ball {
	
	public static final int INTENSITY_SHOOT = 18;
	public static final int INTENSITY_LONG_PASS = 11;
	public static final int INTENSITY_MEDIUM_PASS = 8;
	public static final int INTENSITY_SHORT_PASS = 5;
	public static final int INTENSITY_RUN = 2;

	public static final Player NO_OWNER = null;
	
	private Player owner;
	
	private BallMovement currentMovement;

	public Ball() {
		super();
		this.owner = NO_OWNER;
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

	public boolean hasOwner() {
		if(this.owner == null)
			return false;
		return true;
	}
	
	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}
	
	public void cleanOwner(){
		this.owner = null;
	}
	
	public boolean isOnQuadrant(int quadrant){
		if((this.x() >= 20.0f) && (this.x() <= 40.0f)){
			//FIRST AND FOURTH
			if(quadrant == Player.QUADRANT_FIRST_AND_FOURTH)
				return true;
			
			if((this.y() >= 10.0f) && (this.y() <= 20.0f)){
				//FIRST
				if(quadrant == Player.QUADRANT_FIRST)
					return true;
			} 
			
			if((this.y() >= 0.0f) && (this.y() <= 10.0f)){
				//FOURTH
				if(quadrant == Player.QUADRANT_FOURTH)
					return true;
			} 
		}
		if ((this.x() >= 0.0f) && (this.x() <= 20.0f)){
			//SECOND AND THIRD
			if(quadrant == Player.QUADRANT_SECOND_AND_THIRD)
				return true;
			
			if((this.y() >= 10.0f) && (this.y() <= 20.0f)){
				//SECOND
				if(quadrant == Player.QUADRANT_SECOND)
					return true;
			} 
			
			if((this.y() >= 0.0f) && (this.y() <= 10.0f)){
				//THIRD
				if(quadrant == Player.QUADRANT_THIRD)
					return true;
			} 
		} 
		return false;
	}
	
	public boolean enemyHasBall(int team){
		if(this.owner == NO_OWNER)
			return false;
		if (this.owner.getTeam() == team)
			return false;
		return true;
	}
	
}
