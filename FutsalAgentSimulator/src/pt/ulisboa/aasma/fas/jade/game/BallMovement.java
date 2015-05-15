package pt.ulisboa.aasma.fas.jade.game;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BallMovement {
	
	/**
	 * 
	 */
	
	private final Lock ballVelLock = new ReentrantLock();
	private final Lock ballPosLock = new ReentrantLock();
	private final Lock ballTimerLock = new ReentrantLock();
	
	private final double A = -2.0f; 
	
	private double initialTime; //in seconds
	
	private double originalIntensity;
	private double ax;
	private double ay;
	
	private double vx0;
	private double vy0;
	
	private double x0;
	private double y0;
	
	private double direction;
	
	private double t;  //in seconds

	public BallMovement(int intensity, double direction, double x0, double y0, double initialTime) {
		super();
		this.direction = direction;
		this.ax = A*Math.cos(Math.toRadians(direction));
		this.ay = A*Math.sin(Math.toRadians(direction));
		this.vx0 = intensity*Math.cos(Math.toRadians(direction));
		this.vy0 = intensity*Math.sin(Math.toRadians(direction));
		this.x0 = x0;
		this.y0 = y0;
		this.t = 0;
		this.initialTime = initialTime;
		this.originalIntensity = intensity;
	}

	public double getOriginalIntensity() {
		return originalIntensity;
	}

	
	
	public double getDirection() {
		return direction;
	}

	public void setDirection(double direction) {
		this.direction = direction;
	}

	public double getT() {
		ballTimerLock.lock();
		double ret;
		try{
			ret = t;
		} finally{
			ballTimerLock.unlock();
		}
		return ret;
	}

	public double getVx0() {
		ballVelLock.lock();
		double ret;
		try{
			ret = vx0;
		} finally{
			ballVelLock.unlock();
		}
		return ret;
	}

	public double getVy0() {
		ballVelLock.lock();
		double ret;
		try{
			ret = vy0;
		} finally{
			ballVelLock.unlock();
		}
		return ret;
	}

	public double getX0() {
		ballPosLock.lock();
		double ret;
		try{
			ret = x0;
		} finally{
			ballPosLock.unlock();
		}
		return ret;
	}

	public double getY0() {
		ballPosLock.lock();
		double ret;
		try{
			ret = y0;
		} finally{
			ballPosLock.unlock();
		}
		return ret;
	}
	
	public double vx(){
		ballVelLock.lock();
		ballTimerLock.lock();
		double ret;
		try{
			ret = vx0 + (ax*t);		
		} finally{
			ballVelLock.unlock();
			ballTimerLock.unlock();
		}
		return ret;
	}
	
	public double vy(){
		ballVelLock.lock();
		ballTimerLock.lock();
		double ret;
		try{
			ret = vy0 + (ay*t);		
		} finally{
			ballVelLock.unlock();
			ballTimerLock.unlock();
		}
		return ret;
	}

	public double x(){
		double X;
		
		ballPosLock.lock();
		ballVelLock.lock();
		ballTimerLock.lock();
		try{
			if (!((vx0 >= 0) ^ (vx() < 0))) {
				double ts = -vx0 / ax;
				X = (ax * ts * ts) * (1.0f / 2.0f) + vx0 * ts
						+ x0;
			
			} else {
				X = (ax * t * t) * (1.0f / 2.0f) + vx0 * t + x0;
			}
		} finally {
			ballPosLock.unlock();
			ballVelLock.unlock();
			ballTimerLock.unlock();
		}
		
		
		if (X < 0.0f){
			if(!((X > -1.0f) && (y() > Game.GOAL_Y_MIN) && (y() < Game.GOAL_Y_MAX))){
				X = 0.0f;
				vx0 = -vx0;
			}
		}
		if (X > Game.LIMIT_X){
			if(!((X > (Game.LIMIT_X + 1.0f)) && (y() > Game.GOAL_Y_MIN) && (y() < Game.GOAL_Y_MAX))){
				X = Game.LIMIT_X;
				vx0 = -vx0;
			}
		}
		return X;

	}
	
	public double y(){
		double Y;
		
		ballPosLock.lock();
		ballVelLock.lock();
		ballTimerLock.lock();
		
		
		try{
			if (!((vy0 >= 0) ^ (vy() < 0))) {
				double ts = -vy0 / ay;
				Y = (ay * ts * ts) * (1.0f / 2.0f) + vy0 * ts
						+ y0;
			} else {
				Y = (ay * t * t) * (1.0f / 2.0f) + vy0 * t + y0;
			}
		} finally {
			ballPosLock.unlock();
			ballVelLock.unlock();
			ballTimerLock.unlock();
		}	
			
		if (Y < 0.0f){
			Y = 0.0f;
			vy0 = -vy0;
		}
		if (Y > Game.LIMIT_Y){
			Y = Game.LIMIT_Y;
			vy0 = -vy0;
		}

		return Y;
		
	}
	
	public void updateT(double t){
		ballTimerLock.lock();
		try{
			this.t = (t-this.initialTime);
		} finally {
			ballTimerLock.unlock();
		}
	}
	
	public double getCurrentIntensity(){
		ballVelLock.lock();
		double ret;
		try{
			ret = Math.sqrt((vx()*vx()) + (vy()*vy()));
		} finally {
			ballVelLock.unlock();
		}
		return ret;
	}
	
	
	
	public double getInitialTime() {
		return initialTime;
	}
	
	public void updateCurrentMovement(int intensity, double direction, double x0, double y0, double initialTime) {
		ballPosLock.lock();
		ballVelLock.lock();
		ballTimerLock.lock();
		try{
			this.direction = direction;
			this.ax = A*Math.cos(Math.toRadians(direction));
			this.ay = A*Math.sin(Math.toRadians(direction));
			this.vx0 = intensity*Math.cos(Math.toRadians(direction));
			this.vy0 = intensity*Math.sin(Math.toRadians(direction));
			this.x0 = x0;
			this.y0 = y0;
			this.t = 0;
			this.initialTime = initialTime;
			this.originalIntensity = intensity;
		} finally {
			ballPosLock.unlock();
			ballVelLock.unlock();
			ballTimerLock.unlock();
		}	
	}
	
}
