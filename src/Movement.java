
public class Movement {
	private float grTolerance = 1.5f;
	private float acTolerance = 0.5f;
	private float gyTolerance = 0.5f;
	
	private boolean enableAc = false, enableGy = false, enableGr = false;
	private float[] acRange;
	private float[] gyRange;
	private float[] grStart, grEnd;
	
	private float[] acPos;
	private float[] gyPos;
	//private float[] grPos;
	
	private float[] acVT;
	private float[] gyVT;
	//private float[] grVT;
	
	private long firstTimestamp;
	private long lastTimestamp;
	
	private boolean active = false;
	
	
	public Movement(float[] acRange, float[] gyRange, float[] grStart, float[] grEnd){
		enableAc = true;
		enableGy = true;
		enableGr = true;
		
		this.acRange = acRange;

		this.gyRange = gyRange;
		
		this.grStart = grStart;
		this.grEnd = grEnd;
	}	
	
	public boolean checkMovement(long timeStamp, float[] ac, float[] gy, float gr[])
	{
		if(!active && checkGravity(gr)){
			firstTimestamp = timeStamp;
			lastTimestamp = timeStamp;
			active = true;
			
			return false;			
		}
		
		if(checkEndGravity(gr)){
			if(checkPositions()){
				return true;
			}
		}
		
		float timeElapsed = (timeStamp - lastTimestamp)/1000;
		
		for(int i = 0; i < 3; i++){
			acPos[i] = acPos[i] + acVT[i]*timeElapsed + ac[i]*timeElapsed*timeElapsed*0.5f;
			gyPos[i] = gyPos[i] + gyVT[i]*timeElapsed + gy[i]*timeElapsed*timeElapsed*0.5f;
			//grPos[i] = grPos[i] + grVT[i]*timeElapsed + gr[i]*timeElapsed*timeElapsed*0.5f;
			
			
			acVT[i] = acVT[i] + ac[i] * timeElapsed;
			gyVT[i] = gyVT[i] + gy[i] * timeElapsed;
			//grVT[i] = grVT[i] + gr[i] * timeElapsed;
		}
		
		
		return false;
	}

	private boolean checkPositions() {
		for(int i = 0; i < 3; i++){
			if(Math.abs(Math.abs(acPos[i]) - Math.abs(acRange[i])) < acTolerance)
				if(Math.abs(Math.abs(gyPos[i]) - Math.abs(gyRange[i])) < gyTolerance)
					return true;
		}
		return false;
	}

	private boolean checkEndGravity(float[] gr) {
		for(int i = 0; i < 3; i++){
			if(Math.abs(Math.abs(gr[i]) - Math.abs(grEnd[i])) > this.grTolerance)
				return false;
		}
		return true;
	}

	private boolean checkGravity(float[] gr) {
		for(int i = 0; i < 3; i++){
			if(Math.abs(Math.abs(gr[i]) - Math.abs(grStart[i])) > this.grTolerance)
				return false;
		}
		return true;
	}
}
