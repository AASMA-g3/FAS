package pt.ulisboa.aasma.fas.jade.game;

public class Ball {

	private Movement currentMovement;
	
	private Boolean shoted = false;
	private Boolean passed = false;
	private Boolean dribled = false;

	public Ball() {
		super();
		this.dribled = false;
		this.passed = false;
		this.shoted = false;
		currentMovement = new Movement(10.0f, 0.0f, 0.0f, 10.0f, 0);
	}
	
	public Movement getCurrentMovement() {
		return currentMovement;
	}



	public void setCurrentMovement(Movement currentMovement) {
		this.currentMovement = currentMovement;
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
	
	public class Movement {
		
		private final float initialTime; //in seconds
		
		private float a;
		
		private float vx0;
		private float vy0;
		
		private float x0;
		private float y0;
		
		private float t;  //in seconds

		public Movement(float vx0, float vy0, float x0, float y0, float initialTime) {
			super();
			this.a = -2.0f;
			this.vx0 = vx0;
			this.vy0 = vy0;
			this.x0 = x0;
			this.y0 = y0;
			this.t = 0;
			this.initialTime = initialTime;
		}

		public float getT() {
			return t;
		}

		public void setT(float t) {
			this.t = t;
		}

		public float getVx0() {
			return vx0;
		}

		public float getVy0() {
			return vy0;
		}

		public float getX0() {
			return x0;
		}

		public float getY0() {
			return y0;
		}
		
		
		public float vx(){
			return vx0 + (a*t);			
		}
		
		public float vy(){
			return vy0 + (a*t);
		}
	
		public float x(){
			if(!((vx0 >= 0) ^ (vx() < 0))){
				float ts = -vx0/a;
				return (a*ts*ts)*(1.0f/2.0f) + vx0*ts + x0;
			}else {		
				return (a*t*t)*(1.0f/2.0f) + vx0*t + x0;
			}
		}
		
		public float y(){
			if(!((vy0 >= 0) ^ (vy() < 0))){
				float ts = -vy0/a;
				return (a*ts*ts)*(1.0f/2.0f) + vy0*ts + y0;
			} else {
				return (a*t*t)*(1.0f/2.0f) + vy0*t + y0;
			}
		}
		
		public void updateT(float t){
			this.t = (t-this.initialTime);
		}
		
	}
	
}
