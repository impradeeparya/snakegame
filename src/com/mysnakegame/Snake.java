package com.mysnakegame;

import javax.swing.JFrame;

public class Snake extends JFrame {

	private static final long serialVersionUID = 1L;

	public Snake() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("MySnake");
		setResizable(false);
		startGame();
	}

	private void startGame() {
		System.out.println("starting game.........");
		createBoard();
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void createBoard() {
		add(new GameBoard());
	}

	public static void main(String[] args) {
		new Snake();
	}
}