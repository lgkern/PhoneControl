package com.phonecontrol;

import java.io.IOException;
import java.io.OutputStreamWriter;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class Configuration extends Activity implements SensorEventListener {	
	private SensorManager senSensorManager;
	private Sensor senAccelerometer;
	private Sensor senGyroscope;
	private Sensor senGravity;

	private boolean logging = false;
	
	private long lastClick = 0;
		
	private long refreshRate = 100;
	private long lastUpdate = 0;
	private long lastUpdate2 = 0;
	private long lastUpdate3 = 0;
	private long lastLog = 0;
	private float ac_x, ac_y, ac_z;
	private float gy_x, gy_y, gy_z;
	private float gr_x, gr_y, gr_z;
	
	private float[] acAccelerometer;
	private float[] acGyroscope;
	
	private float[] ac_LastVelocity;
	private float[] gy_LastVelocity;	
	
	private float[] ac_lastState;
	private float[] gy_lastState;
	
	private static final int SHAKE_THRESHOLD = 600;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	lastLog = System.currentTimeMillis();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
        
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        senGyroscope = senSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        senGravity = senSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        //senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
        //senSensorManager.registerListener(this, senGyroscope , SensorManager.SENSOR_DELAY_NORMAL);
        //senSensorManager.registerListener(this, senGravity , SensorManager.SENSOR_DELAY_NORMAL);
        
        Button btnStart = (Button) findViewById(R.id.button1);
        Button btnStop = (Button) findViewById(R.id.button2);
        
        btnStart.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View arg0){
        		logging = true;
        	}
        });
        
        btnStop.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View arg0){
        		logging = false;
        	}
        });
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
	            TextView results = (TextView)findViewById(R.id.TextView01);
	            ac_lastState = new float[3];
	            gy_lastState = new float[3];
	            results.setText("Live logging");
    		}
    		lastClick = currentClick;
    		return true;
    	
    	}
    	return false;
    }
    
    private void resetAcumulators() {
    	ac_LastVelocity = new float[3];
    	gy_LastVelocity = new float[3];
    	acAccelerometer = new float[3];
    	acGyroscope = new float[3];
	}

	@Override
    public boolean onKeyUp(int keyCode, KeyEvent event){
    	if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){    		
            senSensorManager.unregisterListener(this);  
            TextView acTextX = (TextView)findViewById(R.id.textView1);
            TextView acTextY = (TextView)findViewById(R.id.textView3);
            TextView acTextZ = (TextView)findViewById(R.id.textView4);
            TextView gyTextX = (TextView)findViewById(R.id.textView5);
            TextView gyTextY = (TextView)findViewById(R.id.textView6);
            TextView gyTextZ = (TextView)findViewById(R.id.textView7);
            TextView results = (TextView)findViewById(R.id.TextView01);
            results.setText("Acumulator Results");
            
            acTextX.setText("Ac x:" + Float.toString(acAccelerometer[0]));
            acTextY.setText("Ac y:" + Float.toString(acAccelerometer[1]));
            acTextZ.setText("Ac z:" + Float.toString(acAccelerometer[2]));
            
            gyTextX.setText("Gy x:" + Float.toString(acGyroscope[0]));
            gyTextY.setText("Gy y:" + Float.toString(acGyroscope[1]));
            gyTextZ.setText("Gy z:" + Float.toString(acGyroscope[2]));
    		return true;
    	}
    	return false;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.configuration, menu);
        return true;
    }
    
    @Override
	public void onSensorChanged(SensorEvent sensorEvent) {
		Sensor mySensor = sensorEvent.sensor;
	     
        if (mySensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];
            
            TextView xtext = (TextView)findViewById(R.id.textView1);
            TextView ytext = (TextView)findViewById(R.id.textView3);
            TextView ztext = (TextView)findViewById(R.id.textView4);
     
            long curTime = System.currentTimeMillis();
     
            if ((curTime - lastUpdate) > refreshRate) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;
     
                float speed = Math.abs(x + y + z - ac_x - ac_y - ac_z)/ diffTime * 10000;
     
                if (speed > SHAKE_THRESHOLD) {
     
                }
                ac_x = x;
                ac_y = y;
                ac_z = z;
                
                updateDistance(0, x, y, z, diffTime);
                xtext.setText("Ac x:" + Float.toString(x));
                ytext.setText("Ac y:" + Float.toString(y));
                ztext.setText("Ac z:" + Float.toString(z));
            }
        }
        
        if(mySensor.getType() == Sensor.TYPE_GYROSCOPE) {
        	 float x = sensorEvent.values[0];
             float y = sensorEvent.values[1];
             float z = sensorEvent.values[2];
             
             TextView xtext = (TextView)findViewById(R.id.textView5);
             TextView ytext = (TextView)findViewById(R.id.textView6);
             TextView ztext = (TextView)findViewById(R.id.textView7);
      
             long curTime2 = System.currentTimeMillis();
      
             if ((curTime2 - lastUpdate2) > refreshRate) {
                 long diffTime = (curTime2 - lastUpdate2);
                 lastUpdate2 = curTime2;
      
                 float speed = Math.abs(x + y + z - gy_x - gy_y - gy_z)/ diffTime * 10000;
      
                 if (speed > SHAKE_THRESHOLD) {
      
                 }
                 gy_x = x;
                 gy_y = y;
                 gy_z = z;
                 updateDistance(1, x, y, z, diffTime);
                 xtext.setText("Gy x:" + Float.toString(x));
                 ytext.setText("Gy y:" + Float.toString(y));
                 ztext.setText("Gy z:" + Float.toString(z));
             }
        	
        }
        if (mySensor.getType() == Sensor.TYPE_GRAVITY) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];
            
            TextView xtext = (TextView)findViewById(R.id.textView8);
            TextView ytext = (TextView)findViewById(R.id.textView9);
            TextView ztext = (TextView)findViewById(R.id.textView10);
     
            long curTime = System.currentTimeMillis();
     
            if ((curTime - lastUpdate3) > refreshRate) {
                long diffTime = (curTime - lastUpdate3);
                lastUpdate3 = curTime;
     
                float speed = Math.abs(x + y + z - gr_x - gr_y - gr_z)/ diffTime * 10000;
     
                if (speed > SHAKE_THRESHOLD) {
     
                }
                  
                gr_x = x;
                gr_y = y;
                gr_z = z;
                
                xtext.setText("Gr x:" + Float.toString(x));
                ytext.setText("Gr y:" + Float.toString(y));
                ztext.setText("Gr z:" + Float.toString(z));
            }
        }
        
        String data = "";
        long currTime = System.currentTimeMillis();
        if((currTime - lastLog) > 100 && logging)
	    {
        	lastLog = currTime;
	        data += "[" + Long.toString(currTime) + "] ";
	        data += "ac: " + Float.toString(ac_x) + "\t" + Float.toString(ac_y) + "\t" + Float.toString(ac_z) + "\t";
	        data += "gy: " + Float.toString(gy_x) + "\t" + Float.toString(gy_y) + "\t" + Float.toString(gr_z) + "\t";
	        data += "gr: " + Float.toString(gr_x) + "\t" + Float.toString(gr_y) + "\t" + Float.toString(gy_z) + "\n";
	     //   writeToFile(data);
	    }
    }
    
    
    private void updateDistance(int storeVariable, float x, float y, float z, long timeFrame){
    	float X = (float) Math.sqrt(x*x);
    	float Y = (float) Math.sqrt(y*y);
    	float Z = (float) Math.sqrt(z*z);
    	float tolerance = 0.0f;    	
    	float[] positions = new float[3];    
    	float s;
    	float u;
    	float[] velocity = new float[3];
    	float a = 0.0f;
    	for(int i = 0; i < 3; i++){
    		if(i == 0) { 
    			if (X > (float)tolerance*Math.sqrt((storeVariable-1)*(storeVariable-1))) 
    				a = exponentialSmoothing(storeVariable, i, X); 
    			else
    				a = 0.0f;
    		}
    		if(i == 1) {
    			if (Y > (float)tolerance*Math.sqrt((storeVariable-1)*(storeVariable-1))) 
    				a = exponentialSmoothing(storeVariable, i, Y); 
    			else 
    				a = 0.0f;
    		}
    		if(i == 2) {
    			if (Z > (float)tolerance*Math.sqrt((storeVariable-1)*(storeVariable-1))) 
    				a = exponentialSmoothing(storeVariable, i, Z);
    			else 
    				a = 0.0f;
    			}
        	if(storeVariable == 0){ //accelerometer
        		u = ac_LastVelocity[i];
        		s = acAccelerometer[i];
        	}
        	else{
        		u = gy_LastVelocity[i];
        		s = acGyroscope[i];
        	}
        	velocity[i] = u + (a*timeFrame);
        	positions[i] = a*timeFrame;//s + (velocity[i] * timeFrame);        
    	}
    	if(storeVariable == 0){
    		for(int i = 0; i < 3; i++){
    			ac_LastVelocity[i] = velocity[i];
        		acAccelerometer[i] = positions[i];
    		}    		
    	}	
    	else{
    		for(int i = 0; i < 3; i++){
    			gy_LastVelocity[i] = velocity[i];
    			acGyroscope[i] = positions[i];
    		}
    	}
    	
        TextView results = (TextView)findViewById(R.id.TextView01);
        results.setText(Float.toString(acAccelerometer[0]));
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
    	float alpha = 0.2f;
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

