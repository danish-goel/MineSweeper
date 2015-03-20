package com.example.minesweeperpp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Timer;
import java.util.TimerTask;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.RippleDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableRow.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements OnClickListener
{
	
	private TextView txtScore;
	private TextView txtTimer;
	private ImageButton btnSmile;
	private TextView txtLivesCount;
	
	private TableLayout mineField; // table layout to add mines to
	
	private int blockDimension =130; // width of each block
	private int blockPadding = 2; // padding between blocks
	MineGrid grid;
	Block blocks[][];

	private final int GAME_LENGTH=60;
	Timer timer;
	private int TimeCounter = 1;
	static Player p;
	Toolbar toolbar;
	MediaPlayer mp;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        toolbar.setTitle("Beginner");
        setSupportActionBar(toolbar);
        
        txtScore = (TextView) findViewById(R.id.Score);
		txtTimer = (TextView) findViewById(R.id.Timer);
		mineField = (TableLayout)findViewById(R.id.MineField);
		btnSmile=(ImageButton)findViewById(R.id.Smiley);
		txtLivesCount = (TextView) findViewById(R.id.LivesCount);
		
		mp = MediaPlayer.create(getApplicationContext(), R.raw.music);
		mp.start();
		
		Toast.makeText(this, "Click on Smiley to start new Game",0).show();
		
		p=new Player();
		
		btnSmile.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				endExistingGame();
				if(p.getLevel()==0)
				{
					startNewBeginnerGame();
				}
				else
				{
					startNewIntermediateGame();
				}
			}
		});
		
//		blocks[][]=new Button[numRowsMineField][numColumnsMineField]
//		timer.schedule(n
		
		// set font style for timer and mine count to LCD style
//		Typeface lcdFont = Typeface.createFromAsset(getAssets(),
//				"fonts/lcd2mono.ttf");
//		txtMineCount.setTypeface(lcdFont);
//		txtTimer.setTypeface(lcdFont);
		
		
    }
    

	public void startNewBeginnerGame()
	{
		startTimer();
		grid=new MineGrid(6,6,6,MainActivity.this);
		blocks=grid.getBlocks();
		setMineFieldLayout();
		grid.setMines();
		grid.setSurroundingMinesCountAll();
		grid.setPowerSource();
	}
	
	public void startNewIntermediateGame()
	{
		startTimer();
		grid=new MineGrid(8,8,8,MainActivity.this);
		blocks=grid.getBlocks();
		setMineFieldLayout();
		grid.setMines();
		grid.setSurroundingMinesCountAll();
		grid.setPowerSource();
	}
	
	private void endExistingGame()
	{
		if(timer!=null)
		{
			timer.cancel(); // stop if timer is running
		}
		txtTimer.setText("60"); // revert all text
		txtLivesCount.setText("1");
		btnSmile.setBackgroundResource(R.drawable.smile);
		TimeCounter=0;
		p.setNoOfLives(1);
		// remove all rows from mineField TableLayout
		mineField.removeAllViews();
	}

	private void setMineFieldLayout()
	{
		for (int row = 0; row < grid.getNumRowsMineField(); row++)
		{
			TableRow tableRow = new TableRow(this);  
			tableRow.setLayoutParams(new LayoutParams((blockDimension + 2 * blockPadding) * grid.getNumColumnsMineField(), blockDimension + 2 * blockPadding));
			for (int column = 0; column < grid.getNumColumnsMineField(); column++)
			{
				blocks[row][column]=new Block(this);
				blocks[row][column].setLayoutParams(new LayoutParams(  
						blockDimension + 2 * blockPadding,  
						blockDimension + 2 * blockPadding)); 
				blocks[row][column].setPadding(blockPadding, blockPadding, blockPadding, blockPadding);
				tableRow.addView(blocks[row][column]);
				blocks[row][column].setTag(row+","+column);
				
				blocks[row][column].setOnClickListener(this);
				
				grid.setBlocksCell(row,column,blocks[row][column]);
			}
			mineField.addView(tableRow,new TableLayout.LayoutParams(  
					(blockDimension + 2 * blockPadding) * grid.getNumColumnsMineField(), blockDimension + 2 * blockPadding));  
		}
	}

	
	public void WonGame()
	{
		if(p.getLevel()==0)
		{
			   final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		        builder.setMessage("Congraluation,You Won Level 1 :)\nStart next Level !!!")
		               .setCancelable(false)
		               .setPositiveButton("Yes", new DialogInterface.OnClickListener() 
		               {
		                   public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) 
		                   {
		                	endExistingGame();
		           			startNewIntermediateGame();
		           			p.setLevel(1);
		           			toolbar.setTitle("Intermediate");
		                   }
		               });
		        final AlertDialog alert = builder.create();
		        alert.show();
		        saveUserName();
			
		}
		else
		{
			final AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        builder.setMessage("Congraluation,You Won the Game :)")
	               .setCancelable(false)
	               .setPositiveButton("Yes", new DialogInterface.OnClickListener() 
	               {
	                   public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) 
	                   {
	                	   saveUserName();
	                   }
	               });
	        final AlertDialog alert = builder.create();
	        alert.show();
		}
	     
	}
	
	public void LostGame()
	{
		grid.showAllMines();
		btnSmile.setBackgroundResource(R.drawable.sad);
		 final AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        builder.setMessage("Sorry,You Lost the Game :( !!!")
	               .setCancelable(false)
	               .setPositiveButton("OK", new DialogInterface.OnClickListener() 
	               {
	                   public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) 
	                   {
	                	   endExistingGame();
	                   }
	               });
	        final AlertDialog alert = builder.create();
	        alert.show();
	}

	public void startTimer()
	{
		 timer = new Timer();
	      timer.scheduleAtFixedRate(new TimerTask() 
	  	{

	          @Override
	          public void run() 
	  		{
	              // TODO Auto-generated method stub
	        	runOnUiThread(new Runnable() 
	  			{
	                  public void run() 
	  				{
	                	  txtTimer.setText(String.format("%02d",GAME_LENGTH-TimeCounter)); // you can set it to a textView to show it to the user to see the time passing while he is writing.
		                   if(GAME_LENGTH-TimeCounter==0)
		                   {
		                	   p.decrementLives();
		                	   if(p.getNoOfLives()==0)
		                	   {
		                		   txtLivesCount.setText(p.getNoOfLives()+"");
		                		   timer.cancel();
		       						LostGame();
		                	   }
		                	   else
		                	   {
		                		   txtLivesCount.setText(p.getNoOfLives()+"");
		                		   TimeCounter=0;
		                	   }
		                   }
		                   else
		                   {
		                      TimeCounter++;
		                   }
	                  }
	              });

	          }
	      }, 1000, 1000);
	      
	}

	
	@Override
	public void onClick(View v)
	{
		String position=(String)v.getTag();
		String positionArray[]=position.split(",");
		int row=Integer.parseInt(positionArray[0]);
		int column=Integer.parseInt(positionArray[1]);
		
		grid.getBlocksCell(row, column).setEnabled(false);
		grid.getBlocksCell(row, column).setClickable(false);
		
		if(grid.checkIfWonGame())
		{
			WonGame();
		}
		if(blocks[row][column].isMine())
		{
			blocks[row][column].setBackgroundResource(R.drawable.mine);
			p.decrementLives();
			if(p.getNoOfLives()<=0)
			{
				txtLivesCount.setText("0");
				timer.cancel();
				LostGame();
			}
			else
			{
				txtLivesCount.setText(p.getNoOfLives()+"");
			}
		}
		else if(blocks[row][column].isPowerSource())
		{
			blocks[row][column].setBackgroundResource(R.drawable.flag);
			p.incrementLives();
			txtLivesCount.setText(p.getNoOfLives()+"");
		}
		else
		{
			int nearbyMineCount=blocks[row][column].getNumSurroundingMines();
			if(nearbyMineCount==0)
			{
				grid.rippleUncover(row, column, grid);
			}
			else
			{
				blocks[row][column].setText(nearbyMineCount+"");
			}
			txtScore.setText(grid.getScore()+"");
		}
		
	}
	
	public void saveScoreToDatabase()
	{
		 try 
		 {
	 
			 	String workingDir=Environment.getExternalStorageDirectory().toString();
				File file = new File(workingDir+"//high_scores_minesweeper.txt");
				// if file doesnt exists, then create it
				if (!file.exists()) 
				{
					file.createNewFile();
				}
	 
				FileWriter fw = new FileWriter(file.getAbsoluteFile(),true);
				BufferedWriter bw = new BufferedWriter(fw);
				if(p.getLevel()==0)
				{
					bw.append(p.getName()+"\t\t"+grid.getScore()+"\t\t"+"Beginner"+"\n");
				}
				else
				{
				bw.append(p.getName()+"\t\t"+grid.getScore()+"\t\t"+"Intermediate"+"\n");
				}
				bw.close();
	 
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	   private void saveUserName()
	    {
		    	AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
		    	alert.setTitle("Enter your name\n"); //Set Alert dialog title here
		
		        final EditText input = new EditText(MainActivity.this);
		        alert.setView(input);
		
		    	alert.setPositiveButton("OK", new DialogInterface.OnClickListener() 
		    	{
			    	public void onClick(DialogInterface dialog, int whichButton) 
			    	{
				    	 String srt = input.getEditableText().toString();
				    	 p.setName(srt);     
				    	 saveScoreToDatabase();
				    }
		    	});
		    	
		    	alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() 
		    	{
			    	  public void onClick(DialogInterface dialog, int whichButton) 
			    	  {
			    		  dialog.cancel();
			    	  }
		    	});
		    	
		    	AlertDialog alertDialog = alert.create();
		    	alertDialog.show();
	    }
	   
	   @Override
	    public boolean onCreateOptionsMenu(Menu menu) 
		{
	        // Inflate the menu; this adds items to the action bar if it is present.
	        getMenuInflater().inflate(R.menu.main, menu);
	        return true;
	    }

	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        // Handle action bar item clicks here. The action bar will
	        // automatically handle clicks on the Home/Up button, so long
	        // as you specify a parent activity in AndroidManifest.xml.
	        int id = item.getItemId();
	        if (id == R.id.action_settings) 
	        {
	        	Intent high_score=new Intent("android.intent.action.HIGH_SCORE");
	        	startActivity(high_score);
	            return true;
	        }
	        return super.onOptionsItemSelected(item);
	    }


}
