package com.example.minesweeperpp;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

public class Block extends Button
{
	private boolean isCovered;
	private boolean isClickable=true;
	private boolean isMine=false;
	private boolean isPowerSource=false;
	private int numSurroundingMines;
	

	public Block(Context context)
	{
		super(context);
	}
	public Block(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public Block(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}
	
	public int getNumSurroundingMines() {
		return numSurroundingMines;
	}
	public void setNumSurroundingMines(int numSurroundingMines) {
		this.numSurroundingMines = numSurroundingMines;
	}
	public boolean isClickable() {
		return isClickable;
	}
	public void setClickable(boolean isClickable) {
		this.isClickable = isClickable;
	}
	public boolean isPowerSource() {
		return isPowerSource;
	}
	public void setPowerSource(boolean isPowerSource) {
		this.isPowerSource = isPowerSource;
	}
	public boolean isMine() 
	{
		return isMine;
	}
	public void setMine()
	{
		isMine=true;
	}


}
