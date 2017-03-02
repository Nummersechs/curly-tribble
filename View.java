package projektoo;

import acm.program.*;

import java.awt.Color;
import java.awt.event.*;
import acm.graphics.*;

/**
 * Dies ist unsere runnable Class. Diese Klasse extends das GraphicsProgram aus
 * der acm library. Ich bin ein wenig unsicher, wie die funktioniert. Diese
 * Klasse hat einen Konstruktor und zwei Instanzvariablen. Das GraphicsProgram
 * muss beim Ausführen automatisch auch den Konstruktor aufrufen, sonst würde
 * das alles nicht funktionieren, aber es funktionert.
 * 
 * Nun denn. Diese Klasse ist für zwei Sachen zuständig. Zum einen initialisiert
 * sie ein Spiel, indem sie im Konstruktor ein Model erstellt, mit diesem auch
 * einen Controller erstellt, und initNewGame() aufruft. Zum anderen bereitet
 * sie für den Nutzer die View auf mit der true-schleife, und leitet Input
 * Events an den Controller weiter.
 * 
 * @author bsg
 *
 */
public class View extends GraphicsProgram {

	/*
	 * instance variables
	 */
	private Model model;
	private Controller contrl;
	private Timer timer;

	// have more obstacles in order to have more obstacles in the game
	private GRect obstacle0;
	private GRect obstacle1;
	private GRect obstacle2;

	// length between the obstacles (I hope)
	private int obstacleLength;
	private final static int OBSTACLE_LENGTH = Model.OBSTACLE_START_X;
	private final static int OBSTACLE_START = 400;

	// y-coordinate for the obstacles. it determines whether the obstacles are
	// on the top or on the bottom
	private int y0;
	private int y1;
	private int y2;

	// activate each obstacles
	private boolean o0;
	private boolean o1;
	private boolean o2;

	// obstacle counter
	private int obstacleCounter;

	// speed of the obstacle
	private int speed;
	private final static int SPEED_LIMIT = 25;

	// Pointcounter
	private int pointCounter;
	
	/*
	 * constructor
	 */
	public View() {
		this.model = new Model();
		this.contrl = new Controller(model);
		this.timer = new Timer(Model.REFRESH_INTERVALL);
		timer.reset();
	}

	/*
	 * initializes game and adds KeyListener
	 */
	public void init() {
		//
		this.setSize(Model.RESOLUTION_X, Model.RESOLUTION_Y);		
		this.model.initNewGame();


		addKeyListeners();
	}

	/*
	 * contains the loop that refreshes the canvas at given interval
	 */
	public void run() {
		// Create the three obstacle objects here, so that the object wont
		// disappear after 200 points
		// or not... whatever case closed and don't know how T.T
		// But seriously I think creating these objects once is better than
		// creating them over and over again
		
		obstacle0 = makeGRect(this.model.getObstacle0());
		obstacle1 = makeGRect(this.model.getObstacle1());
		obstacle2 = makeGRect(this.model.getObstacle2());

		pointCounter = 0;

		obstacleCounter = 0;
		speed = 10;
		while (true) {
			this.update();
			this.contrl.updateModel(System.currentTimeMillis());
			this.timer.pause();
		}
	}

	/*
	 * interprets keyboard input as in game commands and sends these to the
	 * controller
	 */
	public void keyPressed(KeyEvent e) {
		// Space means to trigger a jump
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			this.contrl.jump();
		}
		
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			this.contrl.duck();
		}
	}
	
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			this.contrl.unduck();
		}
	}

	/*
	 * refreshes the view. first clears the canvas, then takes data from the
	 * model and constructs the view out of that
	 */
	public void update() {
		
		// Pointcounter? Its main purpose however is making obstacles
		pointCounter++;

		// makes the obstacle fastter, the more time has passed
		if (pointCounter % 1000 == 0 && speed <= SPEED_LIMIT) {
			speed++;
		}

		this.removeAll();
		// adds the player rectangle to the view

		GRect background = makeGRect(this.model.getBackground());
		this.add(background);

		GRect ground = makeGRect(this.model.getGround());
		this.add(ground);

		// Obstacles
		if (obstacleLength <= OBSTACLE_START) {

			obstacleLength = OBSTACLE_LENGTH;
			switch (obstacleCounter) {
			case 0:
				this.model.getObstacle0().setY(this.model.obstacleState());
				this.model.getObstacle0().setX(Model.OBSTACLE_START_X);
				o0 = true;
				obstacleCounter++;
				break;
			case 1:
				this.model.getObstacle1().setY(this.model.obstacleState());
				this.model.getObstacle1().setX(Model.OBSTACLE_START_X);
				o1 = true;
				obstacleCounter++;
				break;
			case 2:
				this.model.getObstacle2().setY(this.model.obstacleState());
				this.model.getObstacle2().setX(Model.OBSTACLE_START_X);
				o2 = true;
				obstacleCounter -= 2;
				break;

			}
		}

		if (o0 == true && (this.model.getObstacle0().getX() > -obstacle0.getWidth())) {
			obstacle0.setLocation(this.model.getObstacle0().getNewX(), obstacle0.getY());
			this.model.getObstacle0().setX(this.model.getObstacle0().getNewX());
			this.add(obstacle0);
		} else {
			o0 = false;
		}

		if (o1 == true && (this.model.getObstacle1().getX() > -obstacle1.getWidth())) {
			obstacle1.setLocation(this.model.getObstacle1().getNewX(), obstacle1.getY());
			this.model.getObstacle1().setX(this.model.getObstacle1().getNewX());
			this.add(obstacle1);
		} else {
			o1 = false;
		}

		if (o2 == true && (this.model.getObstacle2().getX() > -obstacle2.getWidth())) {
			obstacle2.setLocation(this.model.getObstacle2().getNewX(), obstacle2.getY());
			this.model.getObstacle2().setX(this.model.getObstacle2().getNewX());
			this.add(obstacle2);
		} else {
			o2 = false;
		}

		// is the last, so it can be the top layer
		GRect player = makeGRect(this.model.getHero());
		this.add(player);

		GLabel points = new GLabel("Points: " + ((int) (pointCounter / 10)), 600, 700);
		points.setFont("SansSerif-36");
		add(points);

		obstacleLength -= speed;
	}
	
	public GRect makeGRect(Rectangle rec) {
		GRect grect = new GRect(rec.getX(), rec.getY(), rec.getWidth(), rec.getHeight());
		if(rec.getColor() != null){
			grect.setFillColor(rec.getColor());
			grect.setFilled(true);
		}
		return grect;
	}

}
