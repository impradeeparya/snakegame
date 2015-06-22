package com.mysnakegame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JPanel;

public class GameBoard extends JPanel implements Runnable {

	private static final long serialVersionUID = 5364147787150299178L;

	private Thread snakeThread;
	private Thread backgroundMusicThread;

	private boolean rightMove;
	private boolean leftMove;
	private boolean upMove;
	private boolean downMove;

	private int targetX;
	private int targetY;
	private List<SnakeBody> snake;

	private int snakeX;
	private int snakeY;

	private int BOX_SIZE;

	private int time = 0;

	private int BOARD_LOWER_LIMIT;
	private int BOARD_UPPER_LIMIT;

	private int BOARD_WIDTH;
	private int BOARD_HEIGHT;

	private static Configurations config = Configurations.getInstance();

	public GameBoard() {

		setBackground(Color.green);

		BOARD_WIDTH = Integer.parseInt(config.getProperty("BOARD_WIDTH"));
		BOARD_HEIGHT = Integer.parseInt(config.getProperty("BOARD_HEIGHT"));

		BOARD_LOWER_LIMIT = Integer.parseInt(config
				.getProperty("BOARD_LOWER_LIMIT"));
		BOARD_UPPER_LIMIT = Integer.parseInt(config
				.getProperty("BOARD_UPPER_LIMIT"));

		snakeX = Integer.parseInt(config.getProperty("SNAKE_X"));
		snakeY = Integer.parseInt(config.getProperty("SNAKE_Y"));

		BOX_SIZE = Integer.parseInt(config.getProperty("BOX_SIZE"));

		setFocusable(true);
		addKeyListener(new SnakeMovements());
		setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
		setDefaultDirection();
		startGame();
	}

	private void setDefaultDirection() {
		rightMove = false;
		leftMove = false;
		upMove = true;
		downMove = false;
	}

	private void startGame() {
		snake = new ArrayList<SnakeBody>();
		setTargetLocation();

		backgroundMusicThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true)
					try {
						Clip backgroundMusic = AudioSystem.getClip();
						AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(
								"backgroundmusic.wav"));
						backgroundMusic.open(inputStream);
						backgroundMusic.start();
						Thread.sleep(10000);
					} catch (LineUnavailableException e) {
						e.printStackTrace();
					} catch (UnsupportedAudioFileException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			}
		}, "Game Background Music");
		backgroundMusicThread.start();
		snakeThread = new Thread(this, "Snake Game");
		snakeThread.start();
	}

	private void setTargetLocation() {
		Random random = new Random();
		targetX = BOX_SIZE
				* random.nextInt((BOARD_UPPER_LIMIT - BOARD_LOWER_LIMIT) + 1)
				+ BOARD_LOWER_LIMIT;
		targetY = BOX_SIZE
				* random.nextInt((BOARD_UPPER_LIMIT - BOARD_LOWER_LIMIT) + 1)
				+ BOARD_LOWER_LIMIT;
	}

	private void moveSnake() {

		if (0 == snake.size()) {
			snake.add(new SnakeBody(snakeX, snakeY, BOX_SIZE, BOX_SIZE));
			snakeY -= (BOX_SIZE);
			snake.add(new SnakeBody(snakeX, snakeY, BOX_SIZE, BOX_SIZE));
		}

		time++;

		if (time > 1000000) {
			checkMovement();
			time = 0;
			snake.add(new SnakeBody(snakeX, snakeY, BOX_SIZE, BOX_SIZE));
			if ((snakeX >= BOARD_WIDTH) || (snakeX < 0)
					|| (snakeY >= BOARD_HEIGHT) || (snakeY < 0)) {
				setTargetLocation();
				snake.removeAll(snake);
				snakeX = Integer.parseInt(config.getProperty("SNAKE_X"));
				snakeY = Integer.parseInt(config.getProperty("SNAKE_Y"));
				setDefaultDirection();
			} else if ((snakeX == targetX) && ((snakeY == targetY))) {
				setTargetLocation();
			} else {
				snake.remove(0);
			}
		}
	}

	private void checkMovement() {
		if (rightMove) {
			snakeX += (BOX_SIZE);
		}
		if (leftMove) {
			snakeX -= (BOX_SIZE);
		}
		if (upMove) {
			snakeY -= (BOX_SIZE);
		}
		if (downMove) {
			snakeY += (BOX_SIZE);
		}
	}

	@Override
	public void paintComponent(Graphics graphic) {
		super.paintComponent(graphic);
		paintBoard(graphic);
	}

	private void paintBoard(Graphics graphic) {
		for (int index = 0; index < snake.size(); index++) {
			if ((snake.size() - 1) == index) {
				graphic.setColor(Color.RED);
			} else {
				graphic.setColor(Color.BLACK);
			}
			SnakeBody snakeBody = snake.get(index);
			snakeBody.drawSnake(graphic);
		}
		graphic.setColor(Color.RED);
		graphic.fill3DRect(targetX, targetY, BOX_SIZE, BOX_SIZE, true);
	}

	@Override
	public void run() {
		while (true) {
			moveSnake();
			repaint();
		}
	}

	private class SnakeMovements implements KeyListener {

		@Override
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			if (KeyEvent.VK_LEFT == key && !rightMove) {
				leftMove = true;
				rightMove = false;
				upMove = false;
				downMove = false;
			}
			if (KeyEvent.VK_RIGHT == key && !leftMove) {
				leftMove = false;
				rightMove = true;
				upMove = false;
				downMove = false;
			}
			if (KeyEvent.VK_UP == key && !downMove) {
				leftMove = false;
				rightMove = false;
				upMove = true;
				downMove = false;
			}
			if (KeyEvent.VK_DOWN == key && !upMove) {
				leftMove = false;
				rightMove = false;
				upMove = false;
				downMove = true;
			}
			if (KeyEvent.VK_ESCAPE == key) {
				System.exit(0);
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub

		}

	}

}