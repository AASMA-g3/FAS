package pt.ulisboa.aasma.fas.jade.game;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import pt.ulisboa.aasma.fas.j2d.PlayerGraphic;

public class PlayerMovement {

	public static final double STANDARD_VELOCITY = 5.0f;
	public static final double STAMINA_DECAY = 0.2f;

	private final Lock playerVelLock = new ReentrantLock();
	private final Lock playerPosLock = new ReentrantLock();
	private final Lock playerTimerLock = new ReentrantLock();
	
	private double goalX;
	private double goalY;

	private double stamina;

	private double myX;
	private double myY;

	private double lastTick; // in seconds

	public PlayerMovement(double x, double y) {
		super();
		this.stamina = 100.0f;
		this.goalX = x;
		this.myX = x;
		this.goalY = y;
		this.myY = y;
		this.lastTick = 0;
	}

	public PlayerMovement(double xpos, double ypos, double xgoal, double ygoal, double stamina) {
		super();
		this.stamina = stamina;
		this.goalX = xgoal;
		this.myX = xpos;
		this.goalY = ygoal;
		this.myY = ypos;
		this.lastTick = 0;
	}

	
	public double getStamina() {
		playerTimerLock.lock();
		double ret;
		try{
			ret = stamina;
		} finally{
			playerTimerLock.unlock();
		}
		return ret;
	}

	public void updateT(double currentTick) {
		playerTimerLock.lock();
		try{
			double t = currentTick - lastTick;
			this.stamina -= STAMINA_DECAY * t;
			if (this.stamina <= 0.0f)
				this.stamina = 0.0f;
			
			setX(currentTick);
			setY(currentTick);
			this.lastTick = currentTick;
		} finally {
			playerTimerLock.unlock();
		}
	}

	public double getGoalX() {
		double ret;
		playerPosLock.lock();
		try{
			ret = this.goalX;
		} finally{
			playerPosLock.unlock();
		}
		return ret;
	}

	public double getGoalY() {
		double ret;
		playerPosLock.lock();
		try{
			ret = this.goalY;
		} finally{
			playerPosLock.unlock();
		}
		return ret;
	}

	public void setGoal(double x, double y) {
		playerPosLock.lock();
		try{
			this.goalX = x;
			this.goalY = y;
		} finally{
			playerPosLock.unlock();
		}
	}

	public double x() {
		double ret;
		playerPosLock.lock();
		try{
			ret = this.myX;
		} finally{
			playerPosLock.unlock();
		}
		return ret;
	}

	public double y() {
		double ret;
		playerPosLock.lock();
		try{
			ret = this.myY;
		} finally{
			playerPosLock.unlock();
		}
		return ret;
	}

	private double getDirectionToGoal() {
					return Math.toDegrees(Math.atan((this.goalY	- this.myY)
				/ (this.goalX - this.myX)));
	}

	private void setX(double currentTick) {	
		playerPosLock.lock();
		playerTimerLock.lock();
		try{
		if (Math.round(this.myX) != Math.round(this.goalX)){
			
			double theta = getDirectionToGoal();
			double cos =  Math.cos(Math.toRadians(theta));
			if (((Math.round(theta)>= 179.0f) && (Math.round(theta)<=181.0f))
				|| ((Math.round(theta)>= 0.0f) && (Math.round(theta)<=1.0f)))
				cos = 1;
			if (((Math.round(theta)>= 89.0f) && (Math.round(theta)<=91.0f))
					|| ((Math.round(theta)>= 269.0f) && (Math.round(theta)<=271.0f)))
					cos = 0;
			
			double velocity = (this.stamina / 100.0f) * STANDARD_VELOCITY * cos;
			double time = currentTick - this.lastTick;
		//	System.out.println(cos + "  " + velocity + "  " + time + "  " + this.myX + "  " + (this.myX+(velocity*time)));
			
	
				if (myX > goalX)
					velocity = -velocity;
				 else
					velocity = Math.abs(velocity);
			
			this.myX += velocity*time;
		}
		
		if (this.myX < 0.0f)
			this.myX = 0.0f;
		if (this.myX > Game.LIMIT_X)
			this.myX = Game.LIMIT_X;
	
		} finally {
			playerPosLock.unlock();
			playerTimerLock.unlock();
		}
	}

	private void setY(double currentTick) {
		playerPosLock.lock();
		playerTimerLock.lock();
		try{
			if (Math.round(this.myY) != Math.round(this.goalY)){
			
			double theta = getDirectionToGoal();
			double sin =  Math.sin(Math.toRadians(theta));
			if (((Math.round(theta)>= 179.0f) && (Math.round(theta)<=181.0f))
					|| ((Math.round(theta)>= 0.0f) && (Math.round(theta)<=1.0f)))
					sin = 0;
				if (((Math.round(theta)>= 89.0f) && (Math.round(theta)<=91.0f))
						|| ((Math.round(theta)>= 269.0f) && (Math.round(theta)<=271.0f)))
						sin = 1;
			double velocity = (this.stamina / 100.0f) * STANDARD_VELOCITY * sin;
			double time = currentTick - this.lastTick;
			
			if(myY > goalY)
					velocity = -velocity;
					else
						velocity = Math.abs(velocity);
		
			this.myY += velocity*time;
		}
		
		if (this.myY < 0.0f)
			this.myY = 0.0f;
		if (this.myY > Game.LIMIT_Y)
			this.myY = Game.LIMIT_Y;
	
		} finally {
			playerPosLock.unlock();
			playerTimerLock.unlock();
		}
	}

}