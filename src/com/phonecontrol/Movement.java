package com.phonecontrol;

public class Movement {
	public Movement(float[] acAcumulators, float[] gyAcumulators, float[] grStart, float[] grEnd)
	{
		acelerometerData = acAcumulators;
		gyroscopeData = gyAcumulators;
		startGravity = grStart;
		endGravity = grEnd;
		
		normalizedGyroscope = normalize(gyroscopeData);
		normalizedAccelerometer = normalize(gyroscopeData);
	}
	
	public float CheckFielty(float[] accelerometer, float[] gyroscope, float[] gravityStart, float[] gravityEnd)
	{
		if(rawDifference(gravityStart,startGravity) > 2.0f || rawDifference(gravityEnd,endGravity) > 2.0f)
			return 0.0f;
		else
		{
			float[] normalizedAc = normalize(accelerometer);
			float[] normalizedGy = normalize(gyroscope);
			
			float acumulator = rawDiference(normalizedAc, normalizedAccelerometer);
			acumulator += rawDiference(normalizedGy, normalizedGyroscope);
			//= rawDifference(accelerometer,acelerometerData);
			//acumulator += rawDifference(gyroscope,gyroscopeData);
			

			
			
			
			return acumulator;
		}			
	}


	private float rawDiference(float[] inputValue, float[] movementValue) {
		float acumulator = 0.0f;
		for(int i = 0; i < 3; i++)
		{
			acumulator += (float)Math.abs(inputValue[i]-movementValue[i]);
		}
		return acumulator;
	}

	private float[] normalize(float[] sensorData) {
		float base = Math.max(sensorData[0], sensorData[1]);
		base = Math.max(base, sensorData[2]);
		
		float[] result = new float[]{sensorData[0]/base, sensorData[1]/base, sensorData[2]/base};
		return result;
	}

	private float rawDifference(float[] inputValue, float[] movementValue) {
		float acumulator = 0.0f;
		for(int i = 0; i < 3; i++)
		{
			acumulator += (float)Math.abs(inputValue[i]-movementValue[i]);
		}
		return acumulator;
	}

	private float[] acelerometerData;
	private float[] gyroscopeData;
	private float[] startGravity;
	private float[] endGravity;
	
	private float[] normalizedAccelerometer;
	private float[] normalizedGyroscope;
}
