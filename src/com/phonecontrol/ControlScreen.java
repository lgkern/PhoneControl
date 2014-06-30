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
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;



public class ControlScreen extends Activity {
	
	private ListView myListView;
	private ArrayAdapter<String> ControlArrayAdapter;
	private List<String> rawDB;
	private List<Control> controls;
	
	private List<String> elementNames;
	private List<String> movementNames;
	
	private String outterSeparator = ";";
	private String innerSeparator = "-";
	final private String controlDB = "ControlDB";
	final private String movementDB = "MovementDB";
	final private String elementDB = "ElementDB";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.control);
		controls = new ArrayList<Control>();
		movementNames = new ArrayList<String>();
		elementNames = new ArrayList<String>();
		
		 myListView = (ListView)findViewById(R.id.listView1);		 
			
	      // create the arrayAdapter that contains the BTDevices, and set it to the ListView
		 ControlArrayAdapter = new ArrayAdapter<String>(this, R.layout.listviewlayout);
	      myListView.setAdapter(ControlArrayAdapter);
	      
	     loadDBs();
	     
	     populateList();
	     
			
	     
	}


	private void populateList() 
	{
		for(int i = 0; i < controls.size(); i++)
		{			
			ControlArrayAdapter.add(GenerateAdapterString(i));
		}
		
		 //Toast.makeText(getApplicationContext(),"Found :"+controls.size(),
	    	//	  Toast.LENGTH_LONG).show();
		
	}


	private String GenerateAdapterString(int i) {
		String result = new String();
		Control current = controls.get(i);
		result = current.name() + "\n";
		result+= "Elements: ";
		for(int index : current.elements())
		{
			result+=elementNames.get(index) + " ";
		}
		result+="\n";
		result+= "Movement: ";
		result+=movementNames.get(current.Move());
		return result;
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
		
		temp = string.split(innerSeparator);
		tempElements = new int[temp.length];
		for(int i = 0; i < temp.length; i++)
		{
			tempElements[i] = Integer.parseInt(temp[i]);		
		}
		
		return tempElements;
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.control_screen, menu);
		return true;
	}
	
	private void rwTest()
	{	                   
		try 
		{
		    FileOutputStream fos = openFileOutput("PhoneControlDB", Context.MODE_APPEND);
		    fos.write("teste".getBytes());
		    fos.close();		    
		}
		catch (Exception e) 
		{
		    e.printStackTrace();
		}
		
		try 
		{
		    BufferedReader inputReader = new BufferedReader(new InputStreamReader(
		            openFileInput("DayTwentyTwoFile")));
		    String inputString;
		    StringBuffer stringBuffer = new StringBuffer();                
		    while ((inputString = inputReader.readLine()) != null) 
		    {
		        stringBuffer.append(inputString + "\n");
		    }
		    //
		    
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}

	}

}
