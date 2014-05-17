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
	
	private long lastUpdate = 0;
	private float last_x, last_y, last_z;
	private static final int SHAKE_THRESHOLD = 600;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
        
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
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
	     
        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];
            
            TextView xtext = (TextView)findViewById(R.id.textView1);
            TextView ytext = (TextView)findViewById(R.id.textView3);
            TextView ztext = (TextView)findViewById(R.id.textView4);
     
            long curTime = System.currentTimeMillis();
     
            if ((curTime - lastUpdate) > 1000) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;
     
                float speed = Math.abs(x + y + z - last_x - last_y - last_z)/ diffTime * 10000;
     
                if (speed > SHAKE_THRESHOLD) {
     
                }
                
                xtext.setText(Float.toString(x));
                ytext.setText(Float.toString(y));
                ztext.setText(Float.toString(z));
                last_y = y;
                last_z = z;
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
    }
	
} 

