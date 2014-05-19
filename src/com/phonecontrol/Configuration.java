package com.phonecontrol;

import java.io.IOException;
import java.io.OutputStreamWriter;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
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
	
	private long firstUpdate = 0;
	private long lastUpdate = 0;
	private long lastUpdate2 = 0;
	private long lastUpdate3 = 0;
	private long lastLog = 0;
	private float ac_x, ac_y, ac_z;
	private float gy_x, gy_y, gy_z;
	private float gr_x, gr_y, gr_z;
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
        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
        senSensorManager.registerListener(this, senGyroscope , SensorManager.SENSOR_DELAY_NORMAL);
        senSensorManager.registerListener(this, senGravity , SensorManager.SENSOR_DELAY_NORMAL);
        
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
     
            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;
     
                float speed = Math.abs(x + y + z - ac_x - ac_y - ac_z)/ diffTime * 10000;
     
                if (speed > SHAKE_THRESHOLD) {
     
                }
                ac_x = x;
                ac_y = y;
                ac_z = z;
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
      
             if ((curTime2 - lastUpdate2) > 100) {
                 long diffTime = (curTime2 - lastUpdate2);
                 lastUpdate2 = curTime2;
      
                 float speed = Math.abs(x + y + z - gy_x - gy_y - gy_z)/ diffTime * 10000;
      
                 if (speed > SHAKE_THRESHOLD) {
      
                 }
                 gy_x = x;
                 gy_y = y;
                 gy_z = z;
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
     
            if ((curTime - lastUpdate3) > 100) {
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
        if((currTime - lastLog) > 100)
	    {
        	lastLog = currTime;
	        data += "[" + Long.toString(currTime) + "] ";
	        data += "ac: " + Float.toString(ac_x) + "\t" + Float.toString(ac_y) + "\t" + Float.toString(ac_z) + "\t";
	        data += "gy: " + Float.toString(gy_x) + "\t" + Float.toString(gy_y) + "\t" + Float.toString(gr_z) + "\t";
	        data += "gr: " + Float.toString(gr_x) + "\t" + Float.toString(gr_y) + "\t" + Float.toString(gy_z) + "\n";
	        writeToFile(data);
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
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        senSensorManager.registerListener(this, senGyroscope , SensorManager.SENSOR_DELAY_NORMAL);
        senSensorManager.registerListener(this, senGravity , SensorManager.SENSOR_DELAY_NORMAL);
    }
    
    private void writeToFile(String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("config.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        } 
    }
	
} 

