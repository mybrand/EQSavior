package com.view;

import com.ruleengine.R;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

//GM : import for server comm
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.communication.SAPServiceProvider;
import com.communication.RegisterApp;
import com.controller.Controller_Menu;

import java.util.concurrent.atomic.AtomicInteger;

import android.content.SharedPreferences;
import android.util.Log;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

public class MenuActivity extends ActionBarActivity {

	private Button buttonSettings;
	//private Button buttonEQ;
	//private Button button_noEQ;
	private Button button_simulation;
	private Button button_Monitor;

	private Context _context;
	private EditText editText;
	private Intent intent;

	private Controller_Menu _myController;
	
	// GM : Variable for server comm
	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	private static final String TAG = "GCMRelated";
	private GoogleCloudMessaging gcm;
	private AtomicInteger msgId = new AtomicInteger();
	private String regid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		// create all the necessary classes in the controller 
		_context= getBaseContext();
		_myController = new Controller_Menu(_context);
		
		
		addListenerOnButton();

		// GM for device ID register
		if (checkPlayServices()) {
			gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
			regid = getRegistrationId(getApplicationContext());
			if (!regid.isEmpty()) {
				// ID registerd
			} else {
				Toast.makeText(getApplicationContext(), "Not registerd",
						Toast.LENGTH_LONG).show();
				gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
				regid = getRegistrationId(getApplicationContext());

				if (regid.isEmpty()) {
					new RegisterApp(getApplicationContext(), gcm,
							getAppVersion(getApplicationContext())).execute();
				} else {
					Toast.makeText(getApplicationContext(),
							"Device already Registered", Toast.LENGTH_SHORT)
							.show();
				}

			}

			// GM for gear receiver service
			this.startService(new Intent(this, SAPServiceProvider.class));
			// GM End
		}
	}

	private String getRegistrationId(Context applicationContext) {
		// GM for device ID register
		final SharedPreferences prefs = getGCMPreferences(_context);
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		if (registrationId.isEmpty()) {
			Log.i(TAG, "Registration not found.");
			return "";
		}
		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.
		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
				Integer.MIN_VALUE);
		int currentVersion = getAppVersion(getApplicationContext());
		if (registeredVersion != currentVersion) {
			Log.i(TAG, "App version changed.");
			return "";
		}
		return registrationId;
	}

	private int getAppVersion(Context applicationContext) {
		// GM for device ID register
		try {
			PackageInfo packageInfo = _context.getPackageManager()
					.getPackageInfo(_context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	private SharedPreferences getGCMPreferences(Context context2) {
		// GM for device ID register
		return getSharedPreferences(MenuActivity.class.getSimpleName(),
				Context.MODE_PRIVATE);
	}

	private boolean checkPlayServices() {
		// GM for device ID register
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Log.i(TAG, "This device is not supported.");
				finish();
			}
			return false;
		}
		return true;
	}

	public void addListenerOnButton() {
		buttonSettings = (Button) findViewById(R.id.buttonSettings);		
		button_simulation = (Button) findViewById(R.id.button_simulation);
		button_Monitor = (Button) findViewById(R.id.button_Monitor);	
		
		//buttonEQ = (Button) findViewById(R.id.buttonEQ);
		//button_noEQ = (Button) findViewById(R.id.button_noEQ);

		buttonSettings.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				intent = new Intent(_context, SettingsActivity.class);

				startActivity(intent);
			}
		});
		
		button_simulation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				intent = new Intent(_context, SimulationActivity.class);
				startActivity(intent);
			}
		});

		button_Monitor.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				intent = new Intent(_context, NormalActivity.class);
				startActivity(intent);
			}
		});

		
		/*buttonEQ.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				intent = new Intent(_context, EarthquakeActivity.class);
				startActivity(intent);
			}
		});

		button_noEQ.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				intent = new Intent(_context, NormalActivity.class);
				startActivity(intent);
			}
		});*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
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
}
