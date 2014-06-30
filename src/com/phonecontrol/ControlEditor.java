package com.phonecontrol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ControlEditor extends Activity {
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
	
	private int id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		id = intent.getIntExtra("id", 0);
		setContentView(R.layout.edit);
	      Toast.makeText(getApplicationContext(),""+id,
	    		  Toast.LENGTH_SHORT).show();
	      
	    controls = new ArrayList<Control>();
		movementNames = new ArrayList<String>();
		elementNames = new ArrayList<String>();
		
		myListView = (ListView)findViewById(R.id.listView1);		 
			
	      // create the arrayAdapter that contains the BTDevices, and set it to the ListView
		ControlArrayAdapter = new ArrayAdapter<String>(this, R.layout.listviewlayout);
	    myListView.setAdapter(ControlArrayAdapter);
	      
	    
	    loadDBs();     	
	    
	    TextView MainName = (TextView)findViewById(R.id.textView1);
	    MainName.setText(controls.get(id).name());
	    
	    populateList();    

	     myListView.setOnItemClickListener(new OnItemClickListener() {
	    	  @Override
	    	  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	    		  if(position == 1) // Element
	    		  {
	    			  Intent t = new Intent(ControlEditor.this,ElementPicker.class);	    			  
		    		  t.putExtra("Elements",GenerateElement());
		        	 startActivity(t);  
	    		  }
	    		  else if(position == 0) //Movement
	    		  {
	    			  
	    		  }
	    		  
	    	  }


	    	}); 
	}
	
	private int GenerateElement() 
	{
		int result = 0;
		for(int element : controls.get(id).elements())
		{
			result += Math.pow(10, element);
		}
		return result;
	}
	
	private void loadDBs() {
		loadControlDB();
		loadMovementDB();
		loadElementDB();
		
	}
	
	private void populateList() 
	{	
		ControlArrayAdapter.add(GenerateMoveString());
		ControlArrayAdapter.add(GenerateElementString());		
	}
	
	private String GenerateMoveString() {
		String result ="Trigger Movement\n";
		result+= movementNames.get(controls.get(id).Move());
		
		return result;
	}

	private String GenerateElementString() {
		String result ="Elements Affected\n";
		for(int element : controls.get(id).elements())
		{
			result+= elementNames.get(element);
			result+= "\n";
		}
		//result+= movementNames.get(controls.get(id).Move());
		
		return result;
	}

	/*private String GenerateAdapterString(int i) {
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
	}*/

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
		getMenuInflater().inflate(R.menu.control_editor, menu);
		return true;
	}

}
