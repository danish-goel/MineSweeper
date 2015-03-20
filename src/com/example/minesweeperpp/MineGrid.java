package com.example.minesweeperpp;

import java.util.Random;

import android.content.Context;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;

public class MineGrid 
{
	private int numRowsMineField;
	private int numColumnsMineField;
	private Block blocks[][];
	private int numTotalMines;
	private Context mContext;
	
	public MineGrid(int rows,int columns,int mines,Context context)
	{
		this.numRowsMineField=rows;
		this.numColumnsMineField=columns;
		this.blocks=new Block[rows][columns];
		this.numTotalMines=mines;
		this.mContext=context;
	}
	
	
	public void setMines()
	{
		// set mines excluding the location where user clicked
		Random rand = new Random();
		int newMineRow, newMineColumn;

		for (int row = 0; row < numTotalMines; row++)
		{
			newMineRow = rand.nextInt(numRowsMineField);
			newMineColumn = rand.nextInt(numColumnsMineField);
			
			if (blocks[newMineColumn][newMineRow].isMine())
			{
				row--; // mine is already there, don't repeat for same block
			}
			// plant mine at this location
			else
			{
				blocks[newMineColumn][newMineRow].setMine();
			}
		
		}
	}
	
	public void setPowerSource()
	{
		int newPowerSourceRow, newPowerSourceColumn;
		boolean setPowerSourceDone=true;
		
		Random rand = new Random();
		while(setPowerSourceDone)
		{	
			newPowerSourceRow = rand.nextInt(numRowsMineField);
			newPowerSourceColumn = rand.nextInt(numColumnsMineField);
			if (blocks[newPowerSourceColumn][newPowerSourceRow].isMine())
			{
			}
			else
			{
				setPowerSourceDone=false;
				blocks[newPowerSourceColumn][newPowerSourceRow].setPowerSource(true);
			}
		}
	}
	
	public boolean checkIfWonGame()
	{
		boolean wonGame=true;
		for (int row = 0; row < numRowsMineField; row++)
		{
			for (int column = 0; column < numColumnsMineField; column++)
			{
				if(!blocks[row][column].isMine()&&blocks[row][column].isClickable())
				{
					wonGame=false;
				}
			}
		}	
		return wonGame;
	}
	public Block[][] getBlocks() 
	{
		return blocks;
	}

	public void setBlocksCell(int i,int j,Block block) 
	{
		this.blocks[i][j] = block;
	}
	public Block getBlocksCell(int i,int j) 
	{
		return this.blocks[i][j];
	}

	public int getNumRowsMineField() 
	{
		return numRowsMineField;
	}

	public int getNumColumnsMineField() 
	{
		return numColumnsMineField;
	}
	public void showAllMines()
	{
		for (int row = 0; row < numRowsMineField; row++)
		{
			for (int column = 0; column < numColumnsMineField; column++)
			{
				if(blocks[row][column].isMine()&&blocks[row][column].isClickable())
				{
					
					blocks[row][column].setEnabled(false);
					blocks[row][column].setClickable(false);
					blocks[row][column].setBackgroundResource(R.drawable.mine);
				}
			}
		}
	}
	public void setSurroundingMines(int row,int column)
	{
			int mineCount=0;
			if(!blocks[row][column].isMine())
			{
						//Left Upper
						if(checkPositionForMine(row-1,column-1))
						{
							mineCount++;
						}
						//Upper
						if(checkPositionForMine(row-1,column))
						{
							mineCount++;
						}
						//Right Upper
						if(checkPositionForMine(row-1,column+1))
						{
							mineCount++;
						}
					
					
					//Left
					if(checkPositionForMine(row,column-1))
					{
						mineCount++;
					}
					//Right
					if(checkPositionForMine(row,column+1))
					{
						mineCount++;
					}
					
					
						//Left Down
						if(checkPositionForMine(row+1,column-1))
						{
							mineCount++;
						}
						//Down
						if(checkPositionForMine(row+1,column))
						{
							mineCount++;
						}
						//Right Down
						if(checkPositionForMine(row+1,column+1))
						{
							mineCount++;
						}
				
					blocks[row][column].setNumSurroundingMines(mineCount);
			}
	}
	
	public void setSurroundingMinesCountAll()
	{
		for (int row = 0; row < numRowsMineField; row++)
		{
			for (int column = 0; column < numColumnsMineField; column++)
			{
				setSurroundingMines(row, column);
			}
		}	
	}
	
	public boolean checkPositionForMine(int i,int j)
	{
		try
		{
			if(blocks[i][j].isMine())
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	public int getScore()
	{
		int score=0;
		for (int row = 0; row < numRowsMineField; row++)
		{
			for (int column = 0; column < numColumnsMineField; column++)
			{
				if(!blocks[row][column].isClickable())
				{
					score++;
				}
			}
		}
		return score;
	}
	
	public boolean checkPositionIfExists(int i,int j)
	{
		try
		{
			if(blocks[i][j].isClickable())
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	public void rippleUncover(int row,int column,MineGrid grid)
	{
			if(blocks[row][column].isMine())
			{
				return ;
			}
			else if(blocks[row][column].isPowerSource())
			{
				return ;
			}
			else if(blocks[row][column].getNumSurroundingMines()>0)
			{
				grid.getBlocksCell(row, column).setEnabled(false);
				grid.getBlocksCell(row, column).setClickable(false);
				
				int nearbyMineCount=blocks[row][column].getNumSurroundingMines();
				blocks[row][column].setText(nearbyMineCount+"");
			}
			else if(blocks[row][column].getNumSurroundingMines()==0)
			{
				grid.getBlocksCell(row, column).setEnabled(false);
				grid.getBlocksCell(row, column).setClickable(false);
				
				//Left Upper
				if(checkPositionIfExists(row-1,column-1))
				{
					rippleUncover(row-1, column-1,grid);
				}
				//Upper
				if(checkPositionIfExists(row-1,column))
				{
					rippleUncover(row-1, column,grid);
				}
				//Right Upper
				if(checkPositionIfExists(row-1,column+1))
				{
					rippleUncover(row-1, column+1,grid);
				}
				
				//Left
				if(checkPositionIfExists(row,column-1))
				{
					rippleUncover(row, column-1,grid);
				}
				//Right
				if(checkPositionIfExists(row,column+1))
				{
					rippleUncover(row, column+1,grid);
				}
				
				
					//Left Down
					if(checkPositionIfExists(row+1,column-1))
					{
						rippleUncover(row+1, column-1,grid);
					}
					//Down
					if(checkPositionIfExists(row+1,column))
					{
						rippleUncover(row+1, column,grid);
					}
					//Right Down
					if(checkPositionIfExists(row+1,column+1))
					{
						rippleUncover(row+1, column+1,grid);
					}
			}
	}


}

