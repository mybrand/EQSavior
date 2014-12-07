package com.SensingSystem;

import com.controller.Controller;
import com.ruleengine.RuleEngine;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Bundle;

/**
 * A Phone sensing system senses various raw data. It is supposed to give
 * semantic to this data and to send it to the rule engine.
 * 
 * @author francois
 *
 */
public class SensingSystem implements LocationListener {
	// for battery and GPS
	private BroadcastReceiver batteryLevel;
	private LocationManager locationManager;

	private RuleEngine _ruleEngine; // to init
	private Controller _controller;

	private Context _context;

	public SensingSystem(Controller controller, Context context) {
		this._context = context;
		this._controller = controller;
		initBatterySensing();
		initGPSSensing();
	}

	// ----------------BEGINNING BATTERY --------------- \\\\\\\\\\
	private void initBatterySensing() {
		// create a broadcast receiver to receive data in real-time
		batteryLevel = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				context.unregisterReceiver(this);
				int currentLevel = intent.getIntExtra(
						BatteryManager.EXTRA_LEVEL, -1);
				int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
				int level = -1;
				if (currentLevel >= 0 && scale > 0) {
					level = (currentLevel * 100) / scale;
				}

				// if we want to update some view, we do it here
				_controller.displayBattery(level + "%");

				// classifier
				if (level < 10) // low battery - this threshold is selected by
								// us
				{
					// update rule engine with low battery
				}
			}
		};

		// we register our receiver to our context to receive battery
		// information
		IntentFilter batteryLevelFilter = new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED);
		_context.registerReceiver(batteryLevel, batteryLevelFilter);
	}

	/**
	 * blocks the sensing of battery
	 */
	public void blockBatterySensing() {
		batteryLevel.abortBroadcast();
	}

	// ----------------END BATTERY --------------- \\\\\\\\\\

	// ----------------BEGIN GPS -------------- \\\\\\\\\\
	// Example:
	// http://androidexample.com/GPS_Basic__-__Android_Example/index.php?view=article_discription&aid=68&aaid=93

	private void initGPSSensing() {
		// create the location manager
		locationManager = (LocationManager) _context
				.getSystemService(Context.LOCATION_SERVICE);
		// every 3 seconds, if the location is more than 5 meters different from
		// previous spot
		this.locationManager.requestLocationUpdates(
				LocationManager.GPS_PROVIDER, 3000, // 3 sec
				5, this);
	}

	// called every 3 seconds
	@Override
	public void onLocationChanged(Location location) {
		String str = "Lat. " + location.getLatitude() + " ; Long. "
				+ location.getLongitude();
		// if we want to update some view, we do it here
		_controller.displayGPS(str);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public void onProviderEnabled(String provider) { /* GPS ON */
	}

	@Override
	public void onProviderDisabled(String provider) { /* GPS OFF */
	}

	// ----------------END GPS --------------- \\\\\\\\\\

}
