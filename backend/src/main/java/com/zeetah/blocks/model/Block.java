package com.zeetah.blocks.model;

public class Block {
	int x;
	int y;
	String name;
	String user;
	
	public Block(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
	}
	
	public Block(Block other, int x, int y) {
        this.name = other.name;
        this.x = x;
        this.y = y;
	}
	
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
}
