package com.view;

import com.controller.Controller_Normal;
import com.ruleengine.R;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class NormalActivity extends ActionBarActivity implements
OnClickListener  {

	private Controller_Normal _myController;
	private Button button_ManualEQ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_normal);
		_myController = new Controller_Normal(this);
		button_ManualEQ = (Button) findViewById(R.id.button_ManualEQ);
		button_ManualEQ.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.normal, menu);
		return true;
	}
	
	
	//button_ManualEQ

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void displayInformationToUser(String information) {
		try {
			TextView textView_infoEQ = (TextView) findViewById(R.id.textView_infoEQ);
			textView_infoEQ.setText(information);
			textView_infoEQ.refreshDrawableState();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_ManualEQ:
			_myController.manualEQ();
			break;
		default:
			break;
		}		
	}
}
