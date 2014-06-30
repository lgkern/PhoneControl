package com.phonecontrol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.Button;
import android.widget.Toast;

public class MovementRecognition extends Activity implements SensorEventListener {
	private List<String> rawDB;
	private List<Control> controls;
	
	private String outterSeparator = ";";
	private String innerSeparator = "-";
	final private String controlDB = "ControlDB";
	
	private SensorManager senSensorManager;
	private Sensor senAccelerometer;
	private Sensor senGyroscope;
	private Sensor senGravity;	
		
	private long lastClick = 0;
		
	private long refreshRate = 100;
	private long lastUpdate = 0;
	private long lastUpdate2 = 0;
	private long lastUpdate3 = 0;	
	
	private float[] acAccelerometer;
	private float[] acGyroscope;
	
	private float[] ac_lastState;
	private float[] gy_lastState;
	
	private float[] gravityStart;
	private float[] gravityEnd;
	
	private int[] currentLights;
	
	private List<Movement> movements;
	
	
    Button btn01;
    Button btn02;
    Button btn03;
    Button btn04;
    Button btn05;
    Button btn06;
    Button btn07;
    Button btn08;
    Button btn09;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.movement_recognition);
		
		 senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	     senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
	     senGyroscope = senSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
	     senGravity = senSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
	        //senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
	        //senSensorManager.registerListener(this, senGyroscope , SensorManager.SENSOR_DELAY_NORMAL);
	        //senSensorManager.registerListener(this, senGravity , SensorManager.SENSOR_DELAY_NORMAL);
	     controls = new ArrayList<Control>();
	     movements = new ArrayList<Movement>();
	        
	        movements.add(new Movement(0, new float[]{78.0f,103.0f,64.0f},new float[]{11.0f,11.0f,11.0f},new float[]{0.0f, 9.7f, 0.0f},new float[]{0.0f, 9.7f, 0.0f}));
	        movements.add(new Movement(1, new float[]{505.0f,350.0f,3062.0f},new float[]{34.0f,1232.0f,98.0f},new float[]{0.0f, 0.0f, -9.8f},new float[]{8.78f, 2.04f, -3.84f}));
	        movements.add(new Movement(2, new float[]{456.0f,212.0f,51.0f},new float[]{37.0f,223.0f,92.0f},new float[]{0.0f, 0.0f, -9.8f},new float[]{1.98f, 7.98f, 3.38f}));
	        movements.add(new Movement(3, new float[]{232.0f,313.0f,87.0f},new float[]{70.0f,137.0f,164.0f},new float[]{0.0f, 0.0f, -9.8f},new float[]{-2.19f, -1.73f, 9.4f}));
	        movements.add(new Movement(4, new float[]{206.0f,165.0f,311.0f},new float[]{8.0f,40.0f,18.0f},new float[]{0.0f, 0.0f, -9.8f},new float[]{1.26f, 7.61f, 6.04f}));
	        movements.add(new Movement(5, new float[]{788.0f,1434.0f,236.0f},new float[]{40.0f,110.0f,63.0f},new float[]{0.0f, 0.0f, -9.8f},new float[]{-5.37f, 8.16f, -0.78f}));
	   
	        loadDBs(); 
	        currentLights = new int[9];
		    loadButtons();		    
		    setColors();
	}

	private void loadDBs() {
		loadControlDB();		
	}

	private void loadControlDB() {
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
	

	private void processDB() {
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
	private void setColors() {
		if(currentLights[0] == 0) btn01.setBackgroundResource(R.drawable.a0); else btn01.setBackgroundResource(R.drawable.a1);
		if(currentLights[1] == 0) btn02.setBackgroundResource(R.drawable.b0); else btn02.setBackgroundResource(R.drawable.b1);
		if(currentLights[2] == 0) btn03.setBackgroundResource(R.drawable.c0); else btn03.setBackgroundResource(R.drawable.c1); 
		if(currentLights[3] == 0) btn04.setBackgroundResource(R.drawable.d0); else btn04.setBackgroundResource(R.drawable.d1);
		if(currentLights[4] == 0) btn05.setBackgroundResource(R.drawable.e0); else btn05.setBackgroundResource(R.drawable.e1); 
		if(currentLights[5] == 0) btn06.setBackgroundResource(R.drawable.f0); else btn06.setBackgroundResource(R.drawable.f1);
		if(currentLights[6] == 0) btn07.setBackgroundResource(R.drawable.g0); else btn07.setBackgroundResource(R.drawable.g1);
		if(currentLights[7] == 0) btn08.setBackgroundResource(R.drawable.h0); else btn08.setBackgroundResource(R.drawable.h1);
		if(currentLights[8] == 0) btn09.setBackgroundResource(R.drawable.i0); else btn09.setBackgroundResource(R.drawable.i1);
		
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.movement_recognition, menu);
		return true;
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event){

    	if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
        	long currentClick = System.currentTimeMillis(); 
    		if(currentClick - lastClick > 1000){    
	    		resetAcumulators();
	            senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	            senSensorManager.registerListener(this, senGyroscope , SensorManager.SENSOR_DELAY_NORMAL);
	            senSensorManager.registerListener(this, senGravity , SensorManager.SENSOR_DELAY_NORMAL);
	            //TextView results = (TextView)findViewById(R.id.TextView01);
	            ac_lastState = new float[3];
	            gy_lastState = new float[3];
	          //  results.setText("Live logging");
    		}
    		lastClick = currentClick;
    		return true;
    	
    	}
   	    if ((keyCode == KeyEvent.KEYCODE_BACK))
    	{
    	    finish();
    	}
    	return super.onKeyDown(keyCode, event);
    }
    
    private void resetAcumulators() {
    	acAccelerometer = new float[3];
    	acGyroscope = new float[3];
	}

	@Override
    public boolean onKeyUp(int keyCode, KeyEvent event){
    	if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){    		
            senSensorManager.unregisterListener(this);  
            
            lightByBestMovement(bestMovement());
            
    /*        TextView acTextX = (TextView)findViewById(R.id.textView1);
            TextView acTextY = (TextView)findViewById(R.id.textView3);
            TextView acTextZ = (TextView)findViewById(R.id.textView4);
            TextView gyTextX = (TextView)findViewById(R.id.textView5);
            TextView gyTextY = (TextView)findViewById(R.id.textView6);
            TextView gyTextZ = (TextView)findViewById(R.id.textView7);
            TextView results = (TextView)findViewById(R.id.TextView01);
            results.setText("Best Movement: "+bestMovement());
            
           // acTextX.setText("Move 0 fielty:" + Float.toString(movements.get(0).CheckFielty(acAccelerometer, acGyroscope, gravityStart, gravityEnd)));
           // acTextY.setText("Move 1 fielty:" + Float.toString(movements.get(1).CheckFielty(acAccelerometer, acGyroscope, gravityStart, gravityEnd)));
            
            acTextX.setText("Ac x:" + Float.toString(acAccelerometer[0]));
            acTextY.setText("Ac y:" + Float.toString(acAccelerometer[1]));
            acTextZ.setText("Ac z:" + Float.toString(acAccelerometer[2]));
            
            
            gyTextX.setText("Gy x:" + Float.toString(acGyroscope[0]));
            gyTextY.setText("Gy y:" + Float.toString(acGyroscope[1]));
            gyTextZ.setText("Gy z:" + Float.toString(acGyroscope[2]));
      */      
            gravityStart = null;
            gravityEnd = null;
    		return true;
    	}
    	return false;
    }
	
	private void lightByBestMovement(int bestMovement) {		
		Control match = controls.get(bestMovement);
		if(match.elements() != null)
			for(int element : match.elements())
			{
				currentLights[element]^=1;
			}
		setColors();
		Toast.makeText(getApplicationContext(),"Movement Matched: "+bestMovement,Toast.LENGTH_SHORT).show();
	}

	private int	bestMovement()
	{
		float bestValue = 100000000.0f;
		int bestId = -1;
		for(Movement move : movements)
		{
			float currentValue = move.CheckFielty(acAccelerometer, acGyroscope, gravityStart, gravityEnd);
			if(currentValue < bestValue)
			{
				bestValue = currentValue;
				bestId = move.getId();
			}
		}
		return bestId;
	}
    
    @Override
	public void onSensorChanged(SensorEvent sensorEvent) {
		Sensor mySensor = sensorEvent.sensor;
	     
        if (mySensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];
            
      /*      TextView xtext = (TextView)findViewById(R.id.textView1);
            TextView ytext = (TextView)findViewById(R.id.textView3);
            TextView ztext = (TextView)findViewById(R.id.textView4);
            */
     
            long curTime = System.currentTimeMillis();
     
            if ((curTime - lastUpdate) > refreshRate) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;     

                
                updateDistance(0, x, y, z, diffTime);
    /*            
                xtext.setText("Ac x:" + Float.toString(x));
                ytext.setText("Ac y:" + Float.toString(y));
                ztext.setText("Ac z:" + Float.toString(z));
       */
            }
        }
        
        if(mySensor.getType() == Sensor.TYPE_GYROSCOPE) {
        	 float x = sensorEvent.values[0];
             float y = sensorEvent.values[1];
             float z = sensorEvent.values[2];
       /*      
             TextView xtext = (TextView)findViewById(R.id.textView5);
             TextView ytext = (TextView)findViewById(R.id.textView6);
             TextView ztext = (TextView)findViewById(R.id.textView7);
             
             */
      
             long curTime2 = System.currentTimeMillis();
      
             if ((curTime2 - lastUpdate2) > refreshRate) {
                 long diffTime = (curTime2 - lastUpdate2);
                 lastUpdate2 = curTime2;
                 
                 updateDistance(1, x, y, z, diffTime);
        /*         
                 xtext.setText("Gy x:" + Float.toString(x));
                 ytext.setText("Gy y:" + Float.toString(y));
                 ztext.setText("Gy z:" + Float.toString(z));
                 */
             }
        	
        }
        if (mySensor.getType() == Sensor.TYPE_GRAVITY) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];
            
			if(gravityStart == null)
			{
				gravityEnd = new float[3];
				gravityStart = new float[3];
				gravityStart[0] = x;
				gravityStart[1] = y;
				gravityStart[2] = z;				
			}
            
           /* TextView xtext = (TextView)findViewById(R.id.textView8);
            TextView ytext = (TextView)findViewById(R.id.textView9);
            TextView ztext = (TextView)findViewById(R.id.textView10);
     */
            long curTime = System.currentTimeMillis();
     
            if ((curTime - lastUpdate3) > refreshRate) {
            	
            	gravityEnd[0] = x;
    			gravityEnd[1] = y;
    			gravityEnd[2] = z;            	

                lastUpdate3 = curTime;

            /*    
                xtext.setText("Gr x:" + Float.toString(x));
                ytext.setText("Gr y:" + Float.toString(y));
                ztext.setText("Gr z:" + Float.toString(z));
                */
            }
        }
    }
    
    
    private void updateDistance(int storeVariable, float x, float y, float z, long timeFrame){
    	float X = (float) Math.abs(x);
    	float Y = (float) Math.abs(y);
    	float Z = (float) Math.abs(z);
    	float tolerance = 0.0f;    	
    	float[] positions = new float[3];    
  //  	float s;
  //  	float u;
    	float a = 0.0f;
    	for(int i = 0; i < 3; i++){
    		if(i == 0) { 
    			if (X > (float)tolerance*Math.abs((storeVariable-1))) 
    				a = exponentialSmoothing(storeVariable, i, X); 
    			else
    				a = 0.0f;
    		}
    		if(i == 1) {
    			if (Y > (float)tolerance*Math.abs((storeVariable-1))) 
    				a = exponentialSmoothing(storeVariable, i, Y); 
    			else 
    				a = 0.0f;
    		}
    		if(i == 2) {
    			if (Z > (float)tolerance*Math.abs((storeVariable-1))) 
    				a = exponentialSmoothing(storeVariable, i, Z);
    			else 
    				a = 0.0f;
    			}
        	positions[i] = a*(float)timeFrame;        
    	}
    	if(storeVariable == 0){
    		for(int i = 0; i < 3; i++){
        		acAccelerometer[i] = positions[i];
    		}    		
    	}	
    	else{
    		for(int i = 0; i < 3; i++){
    			acGyroscope[i] = positions[i];
    		}
    	}
    }
		
     
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
     
    }
    
    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }
    
    protected void onResume() {
        super.onResume();

    }
    
    private float exponentialSmoothing(int method, int variable, float output) {
    	float alpha = 0.5f;
    	float input = 0.0f;
    	if(method == 0){
    		input = ac_lastState[variable];
    		output = output + alpha * (input - output);
    		ac_lastState[variable] = output;
    	}
    	else{
    		input = gy_lastState[variable];
    		output = output + alpha * (input - output);
    		gy_lastState[variable] = output;
    	}
        
        
        return output;
}
/*    
    private void writeToFile(String data) {
        try {
    	//	TextView logText = (TextView)findViewById(R.id.textView2);
    		logText.setText("Writing to file");
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("config.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
        //	TextView logText = (TextView)findViewById(R.id.textView2);
            Log.e("Exception", "File write failed: " + e.toString());
            logText.setText("File write failed:" + e.toString());
        } 
    }
	*/

}
