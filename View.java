package projektooneu;

import acm.program.*;

import java.awt.Color;
import java.awt.event.*;
import acm.graphics.*;

/**
 * Dies ist unsere runnable Class. Diese Klasse extends das GraphicsProgram aus
 * der acm library. Ich bin ein wenig unsicher, wie die funktioniert. Diese
 * Klasse hat einen Konstruktor und zwei Instanzvariablen. Das GraphicsProgram
 * muss beim AusfÃ¼hren automatisch auch den Konstruktor aufrufen, sonst wÃ¼rde
 * das alles nicht funktionieren, aber es funktionert.
 * 
 * Nun denn. Diese Klasse ist fÃ¼r zwei Sachen zustÃ¤ndig. Zum einen
 * initialisiert sie ein Spiel, indem sie im Konstruktor ein Model erstellt, mit
 * diesem auch einen Controller erstellt, und initNewGame() aufruft. Zum anderen
 * bereitet sie fÃ¼r den Nutzer die View auf mit der true-schleife, und leitet
 * Input Events an den Controller weiter.
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
	private final static int OBSTACLE_START = 600;

	// obstacle counter
	private int obstacleCounter;

	// speed of the obstacle
	int speed;
	private final static int SPEED_LIMIT = 20;

	// Pointcounter
	private int pointCounter;

	// end of the game
	private boolean stop;

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
		speed = this.model.getObstacle0().getSpeed();
		obstacleCounter = 0;
		obstacleLength = OBSTACLE_START;

		stop = true;
		while (stop) {
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

		this.model.updateMass();

		// Pointcounter? Its main purpose however was making obstacles
		pointCounter++;

		// makes the obstacle fastter, the more time has passed
		if (pointCounter % 1000 == 0 && speed <= SPEED_LIMIT) {
			speed++;
			this.model.getObstacle0().setSpeed(speed);
			this.model.getObstacle1().setSpeed(speed);
			this.model.getObstacle2().setSpeed(speed);
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
				obstacleCounter++;
				break;
			case 1:
				this.model.getObstacle1().setY(this.model.obstacleState());
				this.model.getObstacle1().setX(Model.OBSTACLE_START_X);
				obstacleCounter++;
				break;
			case 2:
				this.model.getObstacle2().setY(this.model.obstacleState());
				this.model.getObstacle2().setX(Model.OBSTACLE_START_X);
				obstacleCounter -= 2;
				break;

			}
		}

		obstacle0.setLocation(this.model.getObstacle0().getNewX(), this.model.getObstacle0().getY());
		this.model.getObstacle0().setX(this.model.getObstacle0().getNewX());
		this.add(obstacle0);

		obstacle1.setLocation(this.model.getObstacle1().getNewX(), this.model.getObstacle1().getY());
		this.model.getObstacle1().setX(this.model.getObstacle1().getNewX());
		this.add(obstacle1);

		obstacle2.setLocation(this.model.getObstacle2().getNewX(), this.model.getObstacle2().getY());
		this.model.getObstacle2().setX(this.model.getObstacle2().getNewX());
		this.add(obstacle2);

		// is the last, so it can be the top layer
		GRect player = makeGRect(this.model.getHero());
		this.add(player);

		GLabel points = new GLabel("Points: " + ((int) (pointCounter / 10)), 600, 700);
		points.setFont("SansSerif-36");
		add(points);

		if (obstacle0.getX() + obstacle0.getWidth() > this.model.getHero().getX()
				&& obstacle0.getX() < this.model.getHero().getX() + this.model.getHero().getWidth()) {
			stop = !this.model.o0Crash();
		}

		if (obstacle1.getX() + obstacle1.getWidth() > this.model.getHero().getX()
				&& obstacle1.getX() < this.model.getHero().getX() + this.model.getHero().getWidth()) {
			stop = !this.model.o1Crash();
		}

		if (obstacle2.getX() + obstacle2.getWidth() > this.model.getHero().getX()
				&& obstacle2.getX() < this.model.getHero().getX() + this.model.getHero().getWidth()) {
			stop = !this.model.o2Crash();
		}

		obstacleLength -= speed;
	}

	public GRect makeGRect(Rectangle rec) {
		GRect grect = new GRect(rec.getX(), rec.getY(), rec.getWidth(), rec.getHeight());
		if (rec.getColor() != null) {
			grect.setFillColor(rec.getColor());
			grect.setFilled(true);
		}
		return grect;
	}

}
