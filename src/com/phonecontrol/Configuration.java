package com.phonecontrol;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
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
	
	private long lastUpdate = 0;
	private long lastUpdate2 = 0;
	private long lastUpdate3 = 0;
	private float last_x, last_y, last_z;
	private static final int SHAKE_THRESHOLD = 600;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
     
            if ((curTime - lastUpdate) > 500) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;
     
                float speed = Math.abs(x + y + z - last_x - last_y - last_z)/ diffTime * 10000;
     
                if (speed > SHAKE_THRESHOLD) {
     
                }
                
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
      
             if ((curTime2 - lastUpdate2) > 500) {
                 long diffTime = (curTime2 - lastUpdate2);
                 lastUpdate2 = curTime2;
      
                 float speed = Math.abs(x + y + z - last_x - last_y - last_z)/ diffTime * 10000;
      
                 if (speed > SHAKE_THRESHOLD) {
      
                 }
                 
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
     
            if ((curTime - lastUpdate3) > 500) {
                long diffTime = (curTime - lastUpdate3);
                lastUpdate3 = curTime;
     
                float speed = Math.abs(x + y + z - last_x - last_y - last_z)/ diffTime * 10000;
     
                if (speed > SHAKE_THRESHOLD) {
     
                }
                
                xtext.setText("Gr x:" + Float.toString(x));
                ytext.setText("Gr y:" + Float.toString(y));
                ztext.setText("Gr z:" + Float.toString(z));
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
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        senSensorManager.registerListener(this, senGyroscope , SensorManager.SENSOR_DELAY_NORMAL);
        senSensorManager.registerListener(this, senGravity , SensorManager.SENSOR_DELAY_NORMAL);
    }
	
} 

