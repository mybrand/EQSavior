package com.view;

import com.controller.Controller_Simulation;
import com.ruleengine.R;
import com.vars.Constants;

import android.support.v7.app.ActionBarActivity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class SimulationActivity extends ActionBarActivity implements
		OnClickListener {

	private Controller_Simulation __myController;
	private String __EQ_Intensity;
	/*
	 * private boolean __falling_Motion; private boolean __alarm_Deactivated;
	 * private boolean __timeOut;
	 */
	private String __heartBeatStatus; // new

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_simulation);

		// initialize attributes
		init();
		// Disable / enable relevant fields and buttons (alarm start not
		// clickable)
		// add the listeners
		manageControlsAndListeners();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void init() {
		initAttributes();
		Context c = getBaseContext();

		__myController = new Controller_Simulation(c);
		__myController.setSimulationInterface(this);
	}

	public void initAttributes() {
		__EQ_Intensity = "1";
		/*
		 * __falling_Motion=false; __alarm_Deactivated=false; // from true to
		 * false __timeOut=false;
		 */

		__heartBeatStatus = Constants.heartBeat_status_NORMAL;
	}

	public void manageControlsAndListeners() {
		// find the buttons and check-boxes
		Button button_STOP = (Button) findViewById(R.id.button_STOP);
		Button button_Reinitialize = (Button) findViewById(R.id.button_Reinitialize);
		Button button_StartReasoning = (Button) findViewById(R.id.button_StartReasoning);

		CheckBox checkBox_Deactivation = (CheckBox) findViewById(R.id.checkBox_Deactivation);
		CheckBox checkBox_FallingMotion = (CheckBox) findViewById(R.id.checkBox_FallingMotion);

		CheckBox checkBox_HeartBeat_LOW = (CheckBox) findViewById(R.id.checkBox_HB_LOW);
		CheckBox checkBox_HeartBeat_HIGH = (CheckBox) findViewById(R.id.checkBox_HB_HIGH);

		// register clicks on buttons and check-boxes
		button_STOP.setOnClickListener(this);
		button_Reinitialize.setOnClickListener(this);
		button_StartReasoning.setOnClickListener(this);

		checkBox_Deactivation.setOnClickListener(this);
		checkBox_FallingMotion.setOnClickListener(this);

		checkBox_HeartBeat_LOW.setOnClickListener(this);
		checkBox_HeartBeat_HIGH.setOnClickListener(this);

		// button stop not clickable
		button_STOP.setEnabled(false);

		// access edit field and make it not editable
		EditText editText_Alarm = (EditText) findViewById(R.id.editText_Alarm);
		editText_Alarm.setEnabled(false);
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

	@Override
	public void onClick(View v) {

		CheckBox checkBox_HeartBeat_HIGH = (CheckBox) findViewById(R.id.checkBox_HB_HIGH);
		CheckBox checkBox_HeartBeat_LOW = (CheckBox) findViewById(R.id.checkBox_HB_LOW);

		switch (v.getId()) {
		case R.id.button_Reinitialize:
			__myController.reinitialize();
			this.initAttributes();
			displayInformationToUser("Information reinitialized in the rule engine. \nYou can run the reasoning again");
			setAlarm(false);
			blockInference(false);
			break;
		case R.id.button_StartReasoning:
			EditText editText_EQ_Intensity = (EditText) findViewById(R.id.editText_EQ_Intensity);

			if (android.text.TextUtils.isDigitsOnly(editText_EQ_Intensity
					.getText().toString()))
				__EQ_Intensity = editText_EQ_Intensity.getText().toString();
			else
				__EQ_Intensity = "1";

			// __myController.SetNewInformationFromUI(__EQ_Intensity,__falling_Motion,__alarm_Deactivated,
			// __timeOut, __heartBeatStatus);
			__myController.setEQIntensity(Integer.parseInt(__EQ_Intensity));
			break;
		case R.id.button_STOP:
			// not the role of GUI
			// __alarm_Deactivated=true;
			setAlarm(false); // update GUI
			__myController.setAlarmDeactivated(); // update RE

			// SHOULD BE DONE BY RE
			// stop reasoning
			// __myController.stopReasoning();
			// block reasoning button in GUI
			// blockInference(true);
			break;
		case R.id.checkBox_Deactivation:
			CheckBox checkBox_Deactivation = (CheckBox) findViewById(R.id.checkBox_Deactivation);
			boolean checkedDea = checkBox_Deactivation.isChecked(); // not
																	// __alarmdeactivated!!

			// __timeOut = checkBox_Deactivation.isChecked(); // not
			// __alarmdeactivated!!
			__myController.setTimeOut(checkedDea);
			break;
		case R.id.checkBox_FallingMotion:
			CheckBox checkBox_FallingMotion = (CheckBox) findViewById(R.id.checkBox_FallingMotion);
			// __falling_Motion = checkBox_FallingMotion.isChecked();
			boolean checkedFall = checkBox_FallingMotion.isChecked();
			__myController.setFallingMotion(checkedFall);
			break;
		case R.id.checkBox_HB_LOW:
			if (checkBox_HeartBeat_LOW.isChecked()) {
				__heartBeatStatus = Constants.heartBeat_status_LOW;
				checkBox_HeartBeat_HIGH.setEnabled(false);
			} else {
				__heartBeatStatus = Constants.heartBeat_status_NORMAL;
				checkBox_HeartBeat_HIGH.setEnabled(true);
			}
			__myController.setHB(__heartBeatStatus);
			break;
		case R.id.checkBox_HB_HIGH:
			if (checkBox_HeartBeat_HIGH.isChecked()) {
				__heartBeatStatus = Constants.heartBeat_status_HIGH;
				checkBox_HeartBeat_LOW.setEnabled(false);
			} else {
				__heartBeatStatus = Constants.heartBeat_status_NORMAL;
				checkBox_HeartBeat_LOW.setEnabled(true);
			}
			__myController.setHB(__heartBeatStatus);
			break;
		default:
			break;
		}

	}

	public void blockInference(boolean block) {
		Button button_StartReasoning = (Button) findViewById(R.id.button_StartReasoning);
		if (block)
			button_StartReasoning.setEnabled(false);
		else
			button_StartReasoning.setEnabled(true);
	}

	public void setAlarm(boolean activated) {
		// Tizen::Graphics::COLOR_ID_RED
		EditText editText_Alarm = (EditText) findViewById(R.id.editText_Alarm);
		Button button_STOP = (Button) findViewById(R.id.button_STOP);

		if (activated) {
			editText_Alarm.setText("ALARM ON");
			editText_Alarm.setEnabled(true);
			editText_Alarm.getBackground().setColorFilter(Color.RED,
					PorterDuff.Mode.SRC_ATOP);
			editText_Alarm.setEnabled(false);

			// editText_Alarm.setColor(EDIT_STATUS_NORMAL,Color::GetColor(COLOR_ID_RED));
			// editText_Alarm.SetEnabled(false);

			button_STOP.setEnabled(true);

		} else {
			editText_Alarm.setText("ALARM OFF");
			editText_Alarm.setEnabled(true);
			editText_Alarm.getBackground().setColorFilter(Color.GREEN,
					PorterDuff.Mode.SRC_ATOP);
			editText_Alarm.setEnabled(false);
			// editText_Alarm.SetEnabled(false);

			button_STOP.setEnabled(false);
		}
		// NEED A REFRESH
		button_STOP.refreshDrawableState();
	}

	public void displayInformationToUser(String information) {
		// debug in console
		// AppLog("Invoked with value: %ls", information.GetPointer());

		EditText editText_Results = (EditText) findViewById(R.id.editText_Results);
		editText_Results.setText(information);
		editText_Results.refreshDrawableState();
	}

	public void displayGPS(String information) {
		TextView textView_GPS = (TextView) findViewById(R.id.textView_GPS);
		textView_GPS.setText(information);
		textView_GPS.refreshDrawableState();
	}

	public void displayBattery(String information) {
		TextView textView_Battery = (TextView) findViewById(R.id.textView_Battery);
		textView_Battery.setText(information);
		textView_Battery.refreshDrawableState();
	}

	public void displayReceivedInformationFromGear(String information) {
		EditText editText_receiver = (EditText) findViewById(R.id.editText_receiver);
		editText_receiver.setText(information);
		editText_receiver.refreshDrawableState();
	}

}
