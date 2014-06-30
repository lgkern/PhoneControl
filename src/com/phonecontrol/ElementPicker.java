package com.phonecontrol;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ElementPicker extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.elements_selection);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.element_picker, menu);
		return true;
	}

}
