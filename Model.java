package projektooneu;

import java.awt.Color;
import java.util.List;

import acm.util.RandomGenerator;

/**
 * Im Model sammeln sich Konstanten und Objekte und ein paar Methoden. Ein paar
 * Konstanten mÃ¼ssen nicht unbedingt Konstanten bleiben.
 * 
 * @author bsg
 * @param <E>
 *
 */
public class Model<E> {

	/*
	 * constants
	 */

	/*
	 * i guess it is von vorteil, die supportete resolution im model zu
	 * behandeln. die view kann dann hochskalieren oder herunterskalieren. die
	 * werte hier sind praktischerweise ein vielfaches der auflÃ¶sung des
	 * hochhauses. das sollte alles durch 50 teilbar sein. nah, man darf sich
	 * doch auch mehr genauigkeit erlauben. wenn man dann eine low-res version
	 * auf einem high-res bildschirm spielenmÃ¶chte, dann skaliert man auf der
	 * view erst herunter und verliert genauigkeit und skaliert dann wieder
	 * hoch. dann hat mans pixelig. das model liefert daten in seiner prÃ¤zision
	 * und das view baut sich daraus eine niedrigauflÃ¶sende sicht. Ã¤hnlich wird
	 * man dann fÃ¼r dass pixelarray die graphischen objekte, die man auf das
	 * canvas setzen wÃ¼rde wie man es auf unseren konventionellen views getan
	 * hat, Ã¼berprÃ¼fen, wo im niedrigauflÃ¶senden raster sie ausreichend sind, um
	 * sie als "daseiend" bewerten zu kÃ¶nnen. ich finde, das klingt umsetzbar.
	 * (dies sind selbstgesprÃ¤che, nevermind)
	 */
	public static final int RESOLUTION_X = 1400;
	public static final int RESOLUTION_Y = 700;

	// wie lange die true-schleife am ende pausiert
	public static final double REFRESH_INTERVALL = 17;

	// constants for the creation of the player rectangle
	public static final int HERO_HEIGHT = 200;
	public static final int HERO_WIDTH = 50;
	public static final int HERO_START_X = 200;
	public static final int HERO_START_Y = 400;
	
	public static final int GROUND_Y = HERO_HEIGHT + HERO_START_Y;
	public static final int GROUND_HEIGHT = RESOLUTION_Y - GROUND_Y;

	// constants for the physics. that's a cool word "physics". we implemented
	// physics. heard that? physics.
	// the start_speed:gravition 20:1 feels like a reasonable relation for now.
	public static final int START_SPEED = 1600;
	public static final int GRAVITATION = 8;
	// adjusts stuff so it fits into the screen. increment one halves
	// everything, decrement doubles it.
	// higher value makes greater precision in the other values possible
	public static final int ADJUSTMENT_RIGHTSHIFT = 10;
	
	// Color constants
	public static final Color BACKGROUND_COLOR = Color.GRAY;
	
	//Obastacle variables
	public static final int OBSTACLE_HEIGHT = 150;
	public static final int OBSTACLE_WIDTH = 50;
	public static final int OBSTACLE_START_X = 1400;
	public static final int OBSTACLE_START_Y_ABOVE = 300;
	public static final int OBSTACLE_START_Y_BELOW = 450;

	private int obstacleY = obstacleState();

	static RandomGenerator rgen = new RandomGenerator();
	int random;
	
	int i = 0;

	/*
	 * class variables
	 */

	/*
	 * instance variables
	 */
	private long oldTime;
	private Hero hero;
	private Background background;
	private Ground ground;
	private Obstacle obstacle0;
	private Obstacle obstacle1;
	private Obstacle obstacle2;
	
	private Mass heroMass;
	private Mass obstacle0Mass;
	private Mass obstacle1Mass;
	private Mass obstacle2Mass;

	/*
	 * constructor
	 */
	public Model() {
	}

	/*
	 * does stuff actually, if one was consequent, this method should be in the
	 * controller, but it feels like it belongs here.
	 */
	public void initNewGame() {
		this.oldTime = System.currentTimeMillis();
		this.hero = new Hero(HERO_START_X, HERO_START_Y, HERO_WIDTH, HERO_HEIGHT, Color.YELLOW);
		this.background = new Background(BACKGROUND_COLOR);
		this.ground = new Ground(GROUND_Y, GROUND_HEIGHT, Color.GREEN);
		this.obstacle0 = new Obstacle(OBSTACLE_START_X, obstacleY, OBSTACLE_WIDTH, OBSTACLE_HEIGHT, Color.RED);
		this.obstacle1 = new Obstacle(OBSTACLE_START_X, obstacleY, OBSTACLE_WIDTH, OBSTACLE_HEIGHT, Color.RED);
		this.obstacle2 = new Obstacle(OBSTACLE_START_X, obstacleY, OBSTACLE_WIDTH, OBSTACLE_HEIGHT, Color.RED);
		this.heroMass = new Mass(this.hero);
		this.obstacle0Mass = new Mass(this.obstacle0);
		this.obstacle1Mass = new Mass(this.obstacle1);
		this.obstacle2Mass = new Mass(this.obstacle2);
	}
	
	
	
	public void setObstacleY(int y){
		this.obstacleY = y;
	}
	
	public void updateMass(){
		this.heroMass = new Mass(this.hero);
		this.obstacle0Mass = new Mass(this.obstacle0);
		this.obstacle1Mass = new Mass(this.obstacle1);
		this.obstacle2Mass = new Mass(this.obstacle2);
	}
	
	public int obstacleState() {
		random = rgen.nextInt(1, 2);
		System.out.println(random);
		if(i == 1) {
			i= random;
			return OBSTACLE_START_Y_ABOVE;
		}else{
			i= random;
			return OBSTACLE_START_Y_BELOW;
		}
	}

	public boolean o0Crash(){
		return this.heroMass.collide(obstacle0Mass);
	}
	
	public boolean o1Crash(){
		return this.heroMass.collide(obstacle1Mass);
	}
	
	public boolean o2Crash(){
		return this.heroMass.collide(obstacle2Mass);
	}

	/*
	 * getters and setters
	 */
	public Hero getHero() {
		return hero;
	}

	public Ground getGround() {
		return ground;
	}

	public Background getBackground() {
		return background;
	}

	public void setOldTime(long oldTime) {
		this.oldTime = oldTime;
	}

	public long getOldTime() {
		return oldTime;
	}

	public Obstacle getObstacle0() {
		return obstacle0;
	}	
	
	public Obstacle getObstacle1() {
		return obstacle1;
	}	
	
	public Obstacle getObstacle2() {
		return obstacle2;
	}
	
	public Mass getObstacle0Mass(){
		return this.obstacle0Mass;
	}
	
	public Mass getObstacle1Mass(){
		return this.obstacle1Mass;
	}
	
	public Mass getObstacle2Mass(){
		return this.obstacle2Mass;
	}
}
