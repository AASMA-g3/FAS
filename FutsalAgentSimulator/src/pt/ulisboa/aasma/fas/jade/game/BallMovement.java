package pt.ulisboa.aasma.fas.jade.game;

public class BallMovement {
	
	/**
	 * 
	 */

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
		return t;
	}

	public double getVx0() {
		return vx0;
	}

	public double getVy0() {
		return vy0;
	}

	public double getX0() {
		return x0;
	}

	public double getY0() {
		return y0;
	}
	
	public double vx(){
		return vx0 + (a*t);			
	}
	
	public double vy(){
		return vy0 + (a*t);
	}

	public double x(){
		double X;
		if(!((vx0 >= 0) ^ (vx() < 0))){
			double ts = -vx0/a;
			X = (a*ts*ts)*(1.0f/2.0f) + vx0*ts + x0;
		}else {		
			X = (a*t*t)*(1.0f/2.0f) + vx0*t + x0;
		}
		
		if(X<0.0f)X=0.0f;
		if(X>Game.LIMIT_X)X=Game.LIMIT_X;
		
		return X;
	}
	
	public double y(){
		double Y;
		if(!((vy0 >= 0) ^ (vy() < 0))){
			double ts = -vy0/a;
			Y = (a*ts*ts)*(1.0f/2.0f) + vy0*ts + y0;
		} else {
			Y = (a*t*t)*(1.0f/2.0f) + vy0*t + y0;
		}
		if(Y<0.0f)Y=0.0f;
		if(Y>Game.LIMIT_Y)Y=Game.LIMIT_Y;
		
		return Y;
	}
	
	public void updateT(double t){
		this.t = (t-this.initialTime);
	}
	
	public double getCurrentIntensity(){
		return Math.sqrt((vx()*vx()) + (vy()*vy()));
	}
	
}