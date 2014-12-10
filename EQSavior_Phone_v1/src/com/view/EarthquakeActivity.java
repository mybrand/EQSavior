package com.view;

import com.controller.Controller_EQ;
import com.ruleengine.R;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class EarthquakeActivity extends ActionBarActivity implements
		OnClickListener {

	public Controller_EQ _myController;
	private Button button_noEQ;
	private Button button_StopAlarm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_earthquake);
		_myController = new Controller_EQ(this);

		button_noEQ = (Button) findViewById(R.id.button_noEQ);
		button_noEQ.setOnClickListener(this);

		button_StopAlarm = (Button) findViewById(R.id.button_StopAlarm);
		button_StopAlarm.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.earthquake, menu);
		return true;
	}

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

	// ////////////////////////////////
	// static final int CALL_FAMILY_REQUEST = 1; // The request code

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_noEQ:
			_myController.notEQ();
			sendInformationToGear("NOQ");
			this.finish();
			break;
		case R.id.button_StopAlarm:
			_myController.stopAlarm();
			sendInformationToGear("stop");
			/*
			 * Intent myIntent = new Intent(Intent.ACTION_CALL);
			 * myIntent.setData(Uri.parse("tel:"+Constants.phone1));
			 * //myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			 * startActivityForResult(myIntent, CALL_FAMILY_REQUEST);
			 */

			// _context.startActivity(myIntent);
			break;
		default:
			break;
		}
	}
	
	
	public void sendInformationToGear(String str) {
		Log.d("Communication", "Message sent from Phone to gear: " + str);
		// here, use the SAP provider
		//_mySAPprovider.sendMessageToGear(str);
		Intent intent = new Intent("myData");
		intent.putExtra("data", str);
		sendBroadcast(intent);

	}
	

	/*
	 * @Override protected void onActivityResult(int requestCode, int
	 * resultCode, Intent data) { // Check which request we're responding to if
	 * (requestCode == CALL_FAMILY_REQUEST) { // Make sure the request was
	 * successful Log.d("RuleEngine", "CALL_FAMILY_REQUEST");
	 * 
	 * if (resultCode == RESULT_OK) { Log.d("RuleEngine",
	 * "RESULT OK, we have to do smth here"); } } }
	 */

	// /////////////////

	public void displayInformationToUser(String information) {
		TextView textView_infoEQ = (TextView) findViewById(R.id.textView_infoEQ);
		textView_infoEQ.setText(information);
		textView_infoEQ.refreshDrawableState();

	}

}
