package com.example.calapalamos;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class LaFoscaMain extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_la_fosca_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.la_fosca_main, menu);
		return true;
	}

}
