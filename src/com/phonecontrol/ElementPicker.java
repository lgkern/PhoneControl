package com.phonecontrol;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class ElementPicker extends Activity {
	private List<String> rawDB;
	private List<Control> controls;
	
	private List<String> elementNames;
	private List<String> movementNames;
	
	private String outterSeparator = ";";
	private String innerSeparator = "-";
	final private String controlDB = "ControlDB";
	final private String movementDB = "MovementDB";
	final private String elementDB = "ElementDB";
	
	private int[] activeElements;
	private int id;
	
    Button btn01;
    Button btn02;
    Button btn03;
    Button btn04;
    Button btn05;
    Button btn06;
    Button btn07;
    Button btn08;
    Button btn09;
    Button btnConfirm;
    Button btnDiscard;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		int rawElements = 0;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.elements_selection);
		//Intent intent = getIntent();
		//rawElements = intent.getIntExtra("Elements", 0);	
		rawElements = getIntent().getExtras().getInt("Elements");
		id = getIntent().getExtras().getInt("id");
		
		Toast.makeText(getApplicationContext(),"Recieved ID: "+id,Toast.LENGTH_LONG).show();
	      
	    controls = new ArrayList<Control>();
		movementNames = new ArrayList<String>();
		elementNames = new ArrayList<String>();
	    
	    loadDBs();    
	    
	    readActiveElements(rawElements);
	    loadButtons();
	    setCallBacks();
	    setColors();
	}	
	
	private void setCallBacks() {
		btnConfirm.setOnClickListener(new OnClickListener()
		{
        	@Override
        	public void onClick(View arg0){
        		writeNewData();
        	}
        });
		
		btnDiscard.setOnClickListener(new OnClickListener()
		{
        	@Override
        	public void onClick(View arg0){
        		finish();
        	}
        });
		
		btn01.setOnClickListener(new OnClickListener()
		{
        	@Override
        	public void onClick(View arg0){        		
        		activeElements[0]^=1;
        		setColors();
        	}
        });
		
		btn02.setOnClickListener(new OnClickListener()
		{
        	@Override
        	public void onClick(View arg0){        		
        		activeElements[1]^=1;
        		setColors();
        	}
        });
		
		btn03.setOnClickListener(new OnClickListener()
		{
        	@Override
        	public void onClick(View arg0){        		
        		activeElements[2]^=1;
        		setColors();
        	}
        });
		
		btn04.setOnClickListener(new OnClickListener()
		{
        	@Override
        	public void onClick(View arg0){        		
        		activeElements[3]^=1;
        		setColors();
        	}
        });
		
		btn05.setOnClickListener(new OnClickListener()
		{
        	@Override
        	public void onClick(View arg0){        		
        		activeElements[4]^=1;
        		setColors();
        	}
        });
		
		btn06.setOnClickListener(new OnClickListener()
		{
        	@Override
        	public void onClick(View arg0){        		
        		activeElements[5]^=1;
        		setColors();
        	}
        });
		
		btn07.setOnClickListener(new OnClickListener()
		{
        	@Override
        	public void onClick(View arg0){        		
        		activeElements[6]^=1;
        		setColors();
        	}
        });
		
		btn08.setOnClickListener(new OnClickListener()
		{
        	@Override
        	public void onClick(View arg0){        		
        		activeElements[7]^=1;
        		setColors();
        	}
        });
		
		btn09.setOnClickListener(new OnClickListener()
		{
        	@Override
        	public void onClick(View arg0){        		
        		activeElements[8]^=1;
        		setColors();
        	}
        });
	}

	private void loadButtons() {
		 btn01 = (Button) findViewById(R.id.Button001);
		 btn02 = (Button) findViewById(R.id.Button002);
		 btn03 = (Button) findViewById(R.id.Button003);
		 btn04 = (Button) findViewById(R.id.Button004);
		 btn05 = (Button) findViewById(R.id.Button005);
		 btn06 = (Button) findViewById(R.id.Button006);
		 btn07 = (Button) findViewById(R.id.Button007);
		 btn08 = (Button) findViewById(R.id.Button008);
		 btn09 = (Button) findViewById(R.id.Button009);
		 btnConfirm = (Button) findViewById(R.id.button1);
		 btnDiscard = (Button) findViewById(R.id.Button01);
		 btnConfirm.setText("Confirm");
		 btnDiscard.setText("Discard");
		 
	}

	private void setColors() {
		if(activeElements[0] == 0) btn01.setBackgroundResource(R.drawable.a0); else btn01.setBackgroundResource(R.drawable.a1);
		if(activeElements[1] == 0) btn02.setBackgroundResource(R.drawable.b0); else btn02.setBackgroundResource(R.drawable.b1);
		if(activeElements[2] == 0) btn03.setBackgroundResource(R.drawable.c0); else btn03.setBackgroundResource(R.drawable.c1); 
		if(activeElements[3] == 0) btn04.setBackgroundResource(R.drawable.d0); else btn04.setBackgroundResource(R.drawable.d1);
		if(activeElements[4] == 0) btn05.setBackgroundResource(R.drawable.e0); else btn05.setBackgroundResource(R.drawable.e1); 
		if(activeElements[5] == 0) btn06.setBackgroundResource(R.drawable.f0); else btn06.setBackgroundResource(R.drawable.f1);
		if(activeElements[6] == 0) btn07.setBackgroundResource(R.drawable.g0); else btn07.setBackgroundResource(R.drawable.g1);
		if(activeElements[7] == 0) btn08.setBackgroundResource(R.drawable.h0); else btn08.setBackgroundResource(R.drawable.h1);
		if(activeElements[8] == 0) btn09.setBackgroundResource(R.drawable.i0); else btn09.setBackgroundResource(R.drawable.i1);
	}

	private void writeNewData()
	{
		controls.get(id).updateElements(activeElements);
		try 
		{
			FileOutputStream fos = openFileOutput("ControlDB", Context.MODE_PRIVATE);
			for(Control control : controls)
			{		
				fos.write(control.getDBFormat().getBytes());	    
			}
			fos.close();	
		}
		catch (Exception e) 
		{
		    e.printStackTrace();
		}
		
		finish();
	}
	
	private void readActiveElements(int rawElements)
	{
		int raw = rawElements;
		activeElements = new int[9];
		for(int i = 0; i < 9; i++)
		{
			activeElements[i] = raw%10;
			raw/= 10;
		}
	}

	private void loadDBs() {
		loadControlDB();
		loadMovementDB();
		loadElementDB();
		
	}
	
	private void loadMovementDB() {
		
		try 
		{			
		    BufferedReader inputReader = new BufferedReader(new InputStreamReader(
		            openFileInput(movementDB)));
		    String inputString;
		    //StringBuffer stringBuffer = new StringBuffer();                
		    while ((inputString = inputReader.readLine()) != null) 
		    {		    	
		        movementNames.add(inputString);
		    }
		    
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}


	private void loadElementDB() {
		
		try 
		{			
		    BufferedReader inputReader = new BufferedReader(new InputStreamReader(
		            openFileInput(elementDB)));
		    String inputString;
		    //StringBuffer stringBuffer = new StringBuffer();                
		    while ((inputString = inputReader.readLine()) != null) 
		    {
		        elementNames.add(inputString);
		    }
		    
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
	}


	private void loadControlDB() 
	{

		try 
		{
			rawDB = new ArrayList<String>();
		    BufferedReader inputReader = new BufferedReader(new InputStreamReader(
		            openFileInput(controlDB)));
		    String inputString;
		    //StringBuffer stringBuffer = new StringBuffer();                
		    while ((inputString = inputReader.readLine()) != null) 
		    {		    	
		        rawDB.add(inputString);
		    }
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		processDB();
	}
	

	private void processDB() 
	{		
		int index = 0;
		String[] temp;
		int[] tempElements;		
		for(String entry : rawDB)
		{
			//if(entry.length() < 2)	break;
				
			temp = entry.split(outterSeparator);
			tempElements = retrieveElements(temp[1]);
			controls.add(new Control(index, temp[0], tempElements, Integer.parseInt(temp[2])));			
			index++;
		}
	//	Toast.makeText(getApplicationContext(),"RawDB: "+rawDB.size(),Toast.LENGTH_LONG).show();
		
	}

	private int[] retrieveElements(String string) {
		String[] temp;
		int[] tempElements;
		
		if (string.length() == 0)
			return null;
		
		temp = string.split(innerSeparator);
		tempElements = new int[temp.length];
		for(int i = 0; i < temp.length; i++)
		{
			if(temp[i] == "")
				continue;
			tempElements[i] = Integer.parseInt(temp[i]);		
		}
		
		return tempElements;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.element_picker, menu);
		return true;
	}

}
