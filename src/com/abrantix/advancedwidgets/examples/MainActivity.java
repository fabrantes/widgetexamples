package com.abrantix.advancedwidgets.examples;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		View v;
		v = findViewById(R.id.reordable_list_view_btn);
		v.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		Intent i = null;
		switch(id) {
		case R.id.reordable_list_view_btn:
			i = new Intent(this, ReordableListViewActivity.class);
			startActivity(i);
			break;
		}
	}
}
