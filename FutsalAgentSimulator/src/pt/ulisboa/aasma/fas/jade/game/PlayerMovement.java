package pt.ulisboa.aasma.fas.jade.game;

public class PlayerMovement {

	public static final double STANDARD_VELOCITY = 1.0f;
	public static final double STAMINA_DECAY = 0.2f;

	private double goalX;
	private double goalY;

	private double stamina;

	private double lastX;
	private double lastY;

	private double lastT; // in seconds
	private double t;

	public PlayerMovement(double x, double y) {
		super();
		this.stamina = 100.0f;
		this.goalX = x;
		this.lastX = x;
		this.goalY = y;
		this.lastY = y;
		this.lastT = 0;
		this.t = 0;
	}

	public void updateT(double time) {
		this.t = time - this.lastT;
		this.lastT = time;
		updateStamina();
//		if (lastX != goalX && lastY != goalY)
			// System.out.println(lastX + "  " + lastY);
//			System.out
//					.println((stamina / 100.0f)
//							+ "  "
//							+ lastX
//							+ "  "
//							+ lastY
//							+ "  "
//							+ t
//							+ "  "
//							+ Math.cos(Math
//									.toRadians(getDirectionToGoal()))
//							+ "  "
//							+ Math.sin(Math
//									.toRadians(getDirectionToGoal()))
//							+ "  "
//							+ Math.cos(Math
//									.toRadians(getDirectionToGoal()))*t*(stamina / 100.0f)		
//							+ "  "
//							+ (Double.sum(Math.cos(Math
//									.toRadians(getDirectionToGoal()))*t*(stamina / 100.0f),lastX))	
//							);
	}

	public double getGoalX() {
		return goalX;
	}

	public double getGoalY() {
		return this.goalY;
	}

	public void setGoal(double x, double y) {
		this.goalX = x;
		this.goalY = y;
	}

	public double getLastX() {
		return this.lastX;
	}

	public double getLastY() {
		return this.lastY;
	}

	private void updateStamina() {
		this.stamina -= STAMINA_DECAY * t;
		if (this.stamina <= 0.0f)
			this.stamina = 0.0f;
	}

	private double getDirectionToGoal() {
		return Math.toDegrees(Math.atan(Math.abs(this.goalY
				- this.lastY)
				/ Math.abs(this.goalX - this.lastX)));
	}

	public double x() {
		if (this.lastX == this.goalX)
			return this.lastX;
		this.lastX = (Double.sum((double)((this.stamina / 100.0f) * STANDARD_VELOCITY
				* t * Math.cos(Math
				.toRadians(getDirectionToGoal()))), lastX));
		if (this.lastX < 0.0f)
			this.lastX = 0.0f;
		if (this.lastX > Game.LIMIT_X)
			this.lastX = Game.LIMIT_X;

		return this.lastX;
	}

	public double y() {
		if (this.lastY == this.goalY)
			return this.lastY;
		this.lastY = (Double.sum((double)((this.stamina / 100.0f) * STANDARD_VELOCITY
				* t * Math.sin(Math
				.toRadians(getDirectionToGoal()))), lastY));
		if (this.lastY < 0.0f)
			this.lastY = 0.0f;
		if (this.lastY > Game.LIMIT_Y)
			this.lastY = Game.LIMIT_Y;

		return this.lastY;
	}

	public double getLastT() {
		return this.lastT;
	}

}