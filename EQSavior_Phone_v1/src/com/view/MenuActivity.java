package com.view;

import com.ruleengine.R;
import com.ruleengine.R.id;
import com.ruleengine.R.layout;
import com.ruleengine.R.menu;

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
import com.communication.HelloAccessoryProviderService;
import com.communication.RegisterApp;
import java.util.concurrent.atomic.AtomicInteger;
import android.content.SharedPreferences;
import android.util.Log;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

public class MenuActivity extends ActionBarActivity {

	Button button;
	Button EQ;
	Button noEQ;
	Button button_monitor;

	final Context context = this;
	EditText editText;
	Intent intent;
	// GM : Variable for server comm
	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	private static final String TAG = "GCMRelated";
	GoogleCloudMessaging gcm;
	AtomicInteger msgId = new AtomicInteger();
	String regid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);

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
			this.startService(new Intent(this,
					HelloAccessoryProviderService.class));
			// GM End
		}
	}

	private String getRegistrationId(Context applicationContext) {
		// GM for device ID register
		final SharedPreferences prefs = getGCMPreferences(context);
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
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
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
		button = (Button) findViewById(R.id.Settings);
		noEQ = (Button) findViewById(R.id.button_noEQ);
		EQ = (Button) findViewById(R.id.EQ);
		button_monitor = (Button) findViewById(R.id.button_monitor);

		button_monitor.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				intent = new Intent(context, MonitorActivity.class);
				startActivity(intent);
			}
		});

		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				intent = new Intent(context, SettingsActivity.class);

				startActivity(intent);
			}
		});

		EQ.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				intent = new Intent(context, EarthquakeActivity.class);
				startActivity(intent);
			}
		});

		noEQ.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				intent = new Intent(context, NormalActivity.class);
				startActivity(intent);
			}
		});
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
