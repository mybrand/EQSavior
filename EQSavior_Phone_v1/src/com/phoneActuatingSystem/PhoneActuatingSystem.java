package com.phoneActuatingSystem;

//import com.example.foremusic.R;
import com.vars.Constants;
import com.view.EarthquakeActivity;
import com.view.SimulationActivity;

import com.communication.CommunicationSystem;
import com.ruleengine.R;
import com.ruleengine.RuleEngine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class PhoneActuatingSystem extends Activity {

	private Context _context;
	private MediaPlayer mPlayer;
	private Vibrator mVibrator;

	private RuleEngine _myRuleEngine;
	// singleton
	private static PhoneActuatingSystem instance = null;

	private CommunicationSystem _myCommunicationSystem;



	///////////////////

	TelephonyManager manager;
	StatePhoneReceiver myPhoneStateListener;
	boolean callFromApp=false; // To control the call has been made from the application
	boolean callFromOffHook=false; // To control the change to idle state is from the app call

	////////////////////

	// private OutgoingBroadcastReceiver _outG;

	// ========================timers
	// ================================================

	public CountDownTimer timerSoundVibration = new CountDownTimer(15000, 1000) {
		@Override
		public void onTick(long millisUntilFinished) {
		}

		@Override
		public void onFinish() {
			stopSoundAndVibration();
			_myRuleEngine.setTimeToAnswer_OUT(true);
		}
	};

	public CountDownTimer timerSound = new CountDownTimer(15000, 1000) {
		@Override
		public void onTick(long millisUntilFinished) {
		}

		@Override
		public void onFinish() {
			stopSound();
			_myRuleEngine.setTimeToAnswer_OUT(true);
		}
	};

	// ========================timers
	// ================================================

	protected PhoneActuatingSystem(/* Context context,Controller controller */) {
		mPlayer = new MediaPlayer();
		//_myRuleEngine = RuleEngine.getInstance();
		// _outG = new OutgoingBroadcastReceiver();
		// initialize();
		/*
		 * this._context=context; this.__myController = controller;
		 */

		//To be notified of changes of the phone state create an instance
		//of the TelephonyManager class and the StatePhoneReceiver class


	}

	public static PhoneActuatingSystem getInstance() {
		if (instance == null) {
			instance = new PhoneActuatingSystem();
		}
		return instance;
	}

	public void setContext(Context context) {
		this._context = context;
	}

	public void setCommunicationSystem() {
		_myCommunicationSystem = CommunicationSystem.getInstance();
		_myCommunicationSystem.setContext(_context);
	}

	public void init() {
		myPhoneStateListener = new StatePhoneReceiver(_context);
		manager = ((TelephonyManager) _context.getSystemService(Context.TELEPHONY_SERVICE));
	}

	/*
	 * public PhoneActuatingSystem(Context context) { this._context=context;
	 * mPlayer = new MediaPlayer(); }
	 */

	public void stopSoundAndVibration() {
		if(mVibrator != null)
			mVibrator.cancel();
		if(mPlayer != null)
			mPlayer.stop();
		if(timerSoundVibration != null)
			timerSoundVibration.cancel();
		Log.d("RuleEngine", "stopSoundAndVibration");
	}

	public void stopSound() {
		mPlayer.stop();
		timerSound.cancel();
		//_myCommunicationSystem.sendInformationToGear("hello this is francois' message");
		Log.d("RuleEngine", "stopSound");
	}

	/*
	 * --------------------------------------------------------------------------
	 */

	static final int CALL_FAMILY_REQUEST = 1; // The request code

	/**
	 * when finished, should unblock inference of RE
	 */
	public void callFamily() {

		stopSoundAndVibration();



		//////
		manager.listen(myPhoneStateListener,
				PhoneStateListener.LISTEN_CALL_STATE); // start listening to the phone changes
		callFromApp=true;
		//////
		Intent myIntent = new Intent(Intent.ACTION_CALL);
		myIntent.setData(Uri.parse("tel:" + Constants.phone1));
		myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// getParent().startActivityForResult(myIntent, CALL_FAMILY_REQUEST);

		// startActivityForResult(myIntent, CALL_FAMILY_REQUEST);

		_context.startActivity(myIntent);
	}

	/*
	 * --------------------------------------------------------------------------
	 */

	/**
	 * when finished, should unblock inference of RE
	 */
	public void callEmergency() {

		stopSoundAndVibration();

		////
		manager.listen(myPhoneStateListener,
				PhoneStateListener.LISTEN_CALL_STATE); // start listening to the phone changes
		callFromApp=true;
		/////

		Intent myIntent = new Intent(Intent.ACTION_CALL);
		myIntent.setData(Uri.parse("tel:" + Constants.phone2));
		myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		_context.startActivity(myIntent);
	}

	/**
	 * Strong, noisy alarm for danger
	 */
	public void normalAlarm() {
		// Uri alert = Uri.parse("/audio/alarm_buzzer.mp3");

		Log.d("RuleEngine", "context status in ACTUATING: "+_context);
		
		_myCommunicationSystem.sendInformationToGear("alert");


		mPlayer = MediaPlayer.create(_context, R.raw.alarmbuzzer);

		mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		/*
		 * try { mPlayer.setDataSource(_context, alert); } catch
		 * (IllegalArgumentException e) {
		 * Toast.makeText(getApplicationContext(),
		 * "You might not set the URI correctly!", Toast.LENGTH_LONG).show(); }
		 * catch (SecurityException e) { Toast.makeText(getApplicationContext(),
		 * "You might not set the URI correctly!", Toast.LENGTH_LONG).show(); }
		 * catch (IllegalStateException e) {
		 * Toast.makeText(getApplicationContext(),
		 * "You might not set the URI correctly!", Toast.LENGTH_LONG).show(); }
		 * catch (IOException e) { e.printStackTrace(); }
		 */
		/*
		 * try { mPlayer.prepare(); } catch (IllegalStateException e) {
		 * Toast.makeText(getApplicationContext(),
		 * "You might not set the URI correctly!", Toast.LENGTH_LONG).show(); }
		 * catch (IOException e) { Toast.makeText(getApplicationContext(),
		 * "You might not set the URI correctly!", Toast.LENGTH_LONG).show(); }
		 */

		mPlayer.start();

		mVibrator = (Vibrator) _context
				.getSystemService(Context.VIBRATOR_SERVICE);
		mVibrator.vibrate(new long[] { 500, 100, 500, 100 }, 0);

		timerSoundVibration.start();

		// open GUI alarm
		openAlarmScreen();
	}

	/**
	 * Strong, noisy alarm for severe danger
	 */
	public void severeAlarm() {

		_myCommunicationSystem.sendInformationToGear("alert");

		
		mPlayer = MediaPlayer.create(_context, R.raw.sirennoise);

		mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		/*
		 * try { mPlayer.setDataSource(_context, alert); } catch
		 * (IllegalArgumentException e) {
		 * Toast.makeText(getApplicationContext(),
		 * "You might not set the URI correctly!", Toast.LENGTH_LONG).show(); }
		 * catch (SecurityException e) { Toast.makeText(getApplicationContext(),
		 * "You might not set the URI correctly!", Toast.LENGTH_LONG).show(); }
		 * catch (IllegalStateException e) {
		 * Toast.makeText(getApplicationContext(),
		 * "You might not set the URI correctly!", Toast.LENGTH_LONG).show(); }
		 * catch (IOException e) { e.printStackTrace(); }
		 */
		/*
		 * try { mPlayer.prepare(); } catch (IllegalStateException e) {
		 * Toast.makeText(getApplicationContext(),
		 * "You might not set the URI correctly!", Toast.LENGTH_LONG).show(); }
		 * catch (IOException e) { Toast.makeText(getApplicationContext(),
		 * "You might not set the URI correctly!", Toast.LENGTH_LONG).show(); }
		 */

		mPlayer.start();

		mVibrator = (Vibrator) _context
				.getSystemService(Context.VIBRATOR_SERVICE);
		mVibrator.vibrate(new long[] { 100, 500, 100, 500 }, 0);

		timerSoundVibration.start();

		// open GUI alarm
		openAlarmScreen();
	}

	/**
	 * @TODO
	 */
	public void startDestressSignal() {
		// TODO Auto-generated method stub

	}

	private void openAlarmScreen() {
		Intent intent = new Intent(_context, EarthquakeActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		_context.startActivity(intent);
	}

	public void openNormalScreen() {
		Intent intent = new Intent(_context, SimulationActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		_context.startActivity(intent);
	}

	public void manageGearBattery() {
		// TODO Auto-generated method stub

	}

	public void managePhoneBattery() {
		// TODO Auto-generated method stub

	}

	// Monitor for changes to the state of the phone
	public class StatePhoneReceiver extends PhoneStateListener {
		Context context;
		public StatePhoneReceiver(Context context) {
			this.context = context;
		}

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);

			switch (state) {

			case TelephonyManager.CALL_STATE_OFFHOOK: //Call is established
			if (callFromApp) {
				callFromApp=false;
				callFromOffHook=true;

				try {
					Thread.sleep(500); // Delay 0,5 seconds to handle better turning on loudspeaker
				} catch (InterruptedException e) {
				}

				//Activate loudspeaker
				AudioManager audioManager = (AudioManager)
						_context.getSystemService(Context.AUDIO_SERVICE);
				audioManager.setMode(AudioManager.MODE_IN_CALL);
				audioManager.setSpeakerphoneOn(true);
			}
			break;

			case TelephonyManager.CALL_STATE_IDLE: //Call is finished
				if (callFromOffHook) {
					callFromOffHook=false;
					AudioManager audioManager = (AudioManager) _context.getSystemService(Context.AUDIO_SERVICE);
					audioManager.setMode(AudioManager.MODE_NORMAL); //Deactivate loudspeaker
					manager.listen(myPhoneStateListener, // Remove listener
							PhoneStateListener.LISTEN_NONE);
				}
				break;
			}
		}
	}

	public void setRuleEngine(RuleEngine _myRuleEngine) {
		this._myRuleEngine = _myRuleEngine;
	}



}
