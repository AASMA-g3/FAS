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
	
	private final double initialTime; //in seconds
	
	private double a = -2.0f;
	
	private double vx0;
	private double vy0;
	
	private double x0;
	private double y0;
	
	private double t;  //in seconds

	public BallMovement(int intensity, double direction, double x0, double y0, double initialTime) {
		super();
		this.vx0 = intensity*Math.cos(Math.toRadians(direction));
		this.vy0 = intensity*Math.sin(Math.toRadians(direction));
		this.x0 = x0;
		this.y0 = y0;
		this.t = 0;
		this.initialTime = initialTime;
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
			ret = vx0 + (a*t);		
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
			ret = vy0 + (a*t);		
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
				double ts = -vx0 / a;
				X = (a * ts * ts) * (1.0f / 2.0f) + vx0 * ts
						+ x0;
			
			} else {
				X = (a * t * t) * (1.0f / 2.0f) + vx0 * t + x0;
			}
		} finally {
			ballPosLock.unlock();
			ballVelLock.unlock();
			ballTimerLock.unlock();
		}
		
		if (X < 0.0f)
			X = 0.0f;
		if (X > Game.LIMIT_X)
			X = Game.LIMIT_X;

		return X;

	}
	
	public double y(){
		double Y;
		
		ballPosLock.lock();
		ballVelLock.lock();
		ballTimerLock.lock();
		try{
			if (!((vy0 >= 0) ^ (vy() < 0))) {
				double ts = -vy0 / a;
				Y = (a * ts * ts) * (1.0f / 2.0f) + vy0 * ts
						+ y0;
			} else {
				Y = (a * t * t) * (1.0f / 2.0f) + vy0 * t + y0;
			}
		} finally {
			ballPosLock.unlock();
			ballVelLock.unlock();
			ballTimerLock.unlock();
		}	
		
		if (Y < 0.0f)
			Y = 0.0f;
		if (Y > Game.LIMIT_Y)
			Y = Game.LIMIT_Y;

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
	
}