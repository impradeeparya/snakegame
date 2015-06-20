package com.mysnakegame;

import java.awt.Graphics;

public class SnakeBody {

	private int x;
	private int y;
	private int height;
	private int width;

	public SnakeBody(int x, int y, int height, int width) {
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
	}

	public void drawSnake(Graphics graphic) {
		graphic.fill3DRect(this.x, this.y, this.height, this.width, true);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

}
