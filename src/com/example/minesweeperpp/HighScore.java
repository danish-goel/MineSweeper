package com.example.minesweeperpp;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

public class HighScore extends ActionBarActivity 
{
	TextView TxtHighScores;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_high_score);
		
		Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        toolbar.setTitle("High Scores");
        setSupportActionBar(toolbar);
        TxtHighScores=(TextView)findViewById(R.id.Scores);
        
        readHighScores();

	}
	
	public void readHighScores()
	{
			
		    StringBuilder text = new StringBuilder();
		    try 
		    {
			    File sdcard = Environment.getExternalStorageDirectory();
			    File file = new File(sdcard,"high_scores_minesweeper.txt");
		
			        BufferedReader br = new BufferedReader(new FileReader(file));  
			        String line;   
			        while ((line = br.readLine()) != null) 
			        {
			                    text.append(line);
			                    text.append("\n");
			                    Log.i("Test", "text : "+text+" : end");
			        } 
			        br.close();
		     }
		    catch (IOException e) 
		    {
		    }
		    
		    String scores=text.toString();
		    TxtHighScores.setText(scores);

	  }
}

