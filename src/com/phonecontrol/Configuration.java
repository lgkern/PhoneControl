package com.phonecontrol;

import java.io.FileOutputStream;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Configuration extends Activity{	
	Button btnStart;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
   // 	Intent t = new Intent(Configuration.this,BluetoothConfiguration.class);
   // 	startActivity(t);    	
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);
        
       
        btnStart = (Button) findViewById(R.id.button2);
        Button btnStatus = (Button) findViewById(R.id.button1);
        Button btnControls = (Button) findViewById(R.id.Button009);
        Button btnAbout = (Button) findViewById(R.id.Button007);
        Button btnHelp = (Button) findViewById(R.id.Button006);
        
        btnStatus.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View arg0){
        		ResetDBs();
        	}
        });
        
        btnStart.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View arg0){
        		Intent t = new Intent(Configuration.this,MovementRecognition.class);
        		startActivity(t);
        	}
        });
        
        btnControls.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View arg0){
        		Intent t = new Intent(Configuration.this,ControlScreen.class);
        		startActivity(t);
        	}
        });
        	
       	btnAbout.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View arg0){
        	
        	}
        });
        	
        btnHelp.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View arg0){
        	
        	}
        });
        
        
     }
    
	private void ResetDBs() {
		boolean error = false;
		try 
		{
		    FileOutputStream fos = openFileOutput("ControlDB", Context.MODE_PRIVATE);
		    fos.write("Such Movements;0-8;0\n".getBytes());
		    fos.write("Very Test;1-2-4-0;1\n".getBytes());
		    fos.write("Much lines;2;2\n".getBytes());
		    fos.write("Many codes;3-7;3\n".getBytes());
		    fos.write("Wow;0-1-2-3-4-5-6-7-8;4\n".getBytes());
		    fos.write("Wat;3-5;5\n".getBytes());
		    fos.close();		    
		}
		catch (Exception e) 
		{
			error = true;
		    e.printStackTrace();
		}
		
		try  
		{
		    FileOutputStream fos = openFileOutput("MovementDB", Context.MODE_PRIVATE);
		    fos.write("Up\n".getBytes());
		    fos.write("Door Knob\n".getBytes());
		    fos.write("Wave\n".getBytes());
		    fos.write("Waiter\n".getBytes());
		    fos.write("Tilt\n".getBytes());
		    fos.write("To the Side\n".getBytes());
		    fos.close();		    
		}
		catch (Exception e) 
		{
			error = true;
		    e.printStackTrace();
		}
		
		try 
		{
		    FileOutputStream fos = openFileOutput("ElementDB", Context.MODE_PRIVATE);
		    fos.write("Hue0\n".getBytes());
		    fos.write("Hue1\n".getBytes());
		    fos.write("Hue2\n".getBytes());
		    fos.write("Hue3\n".getBytes());
		    fos.write("Hue4\n".getBytes());
		    fos.write("Hue5\n".getBytes());
		    fos.write("Hue6\n".getBytes());
		    fos.write("Hue7\n".getBytes());
		    fos.write("Hue8\n".getBytes());		    
		    
		    fos.close();		    
		}
		catch (Exception e) 
		{
			error = true;
		    e.printStackTrace();
		}
		
		Button btnStatus = (Button) findViewById(R.id.button1);
		if(!error) btnStatus.setText("Database Reseted");
		else btnStatus.setText("Deu merda");
			
	}    
    
} 

