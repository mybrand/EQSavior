package com.view;

import com.ruleengine.R;
import com.vars.Constants;

import android.support.v7.app.ActionBarActivity;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends ActionBarActivity implements
		OnClickListener {

	EditText editText_Family1;
	EditText editText_Family2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		editText_Family1 = (EditText) findViewById(R.id.editText_Family1);
		editText_Family2 = (EditText) findViewById(R.id.editText_Family2);

		setPhoneNumber1(Constants.phone1);
		setPhoneNumber2(Constants.phone2);

		Button button_PhoneNumber = (Button) findViewById(R.id.button_PhoneNumber);
		button_PhoneNumber.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
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

	@Override
	public void onClick(View v) {

		String phone1;
		String phone2;

		switch (v.getId()) {
		case R.id.button_PhoneNumber:
			phone1 = editText_Family1.getText().toString();
			phone2 = editText_Family2.getText().toString();
			if (android.text.TextUtils.isDigitsOnly(phone1)
					&& android.text.TextUtils.isDigitsOnly(phone2)) {
				Constants.phone1 = phone1;
				Constants.phone2 = phone2;
			} else {
				editText_Family1.setText("wrong format");
				editText_Family2.setText("wrong format");
			}
			break;
		default:
			break;
		}

		// back to previous activity
		this.finish();
	}

	public void setPhoneNumber1(String number) {
		editText_Family1.setText(number);
	}

	public void setPhoneNumber2(String number) {
		editText_Family2.setText(number);
	}

}
