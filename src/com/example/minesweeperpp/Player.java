package com.example.minesweeperpp;

public class Player 
{
	String name;
	int noOfLives=1;
	int level=0;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getNoOfLives() {
		return noOfLives;
	}
	public void setNoOfLives(int noOfLives) {
		this.noOfLives = noOfLives;
	}
	
	public void incrementLives()
	{
		this.noOfLives++;
	}
	
	public void decrementLives()
	{
		this.noOfLives--;
	}
	
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	
}
