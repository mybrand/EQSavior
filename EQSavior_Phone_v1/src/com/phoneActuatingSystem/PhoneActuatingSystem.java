package com.phoneActuatingSystem;

//import com.example.foremusic.R;
import com.vars.Constants;
import com.view.EarthquakeActivity;
import com.view.MonitorActivity;

import com.ruleengine.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.Log;

public class PhoneActuatingSystem extends Activity {

	private Context _context;
	private MediaPlayer mPlayer;
	private Vibrator mVibrator;

	// singleton
	private static PhoneActuatingSystem instance = null;

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
		}
	};

	public CountDownTimer timerSound = new CountDownTimer(15000, 1000) {
		@Override
		public void onTick(long millisUntilFinished) {
		}

		@Override
		public void onFinish() {
			stopSound();
		}
	};

	// ========================timers
	// ================================================

	protected PhoneActuatingSystem(/* Context context,Controller controller */) {
		mPlayer = new MediaPlayer();
		// _outG = new OutgoingBroadcastReceiver();
		// initialize();
		/*
		 * this._context=context; this.__myController = controller;
		 */
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

	/*
	 * public PhoneActuatingSystem(Context context) { this._context=context;
	 * mPlayer = new MediaPlayer(); }
	 */

	public void stopSoundAndVibration() {
		mVibrator.cancel();
		mPlayer.stop();
		timerSoundVibration.cancel();
		Log.d("RuleEngine", "stopSoundAndVibration");
	}

	public void stopSound() {
		mPlayer.stop();
		timerSound.cancel();
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
		Intent myIntent = new Intent(Intent.ACTION_CALL);
		myIntent.setData(Uri.parse("tel:" + Constants.phone2));
		myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		_context.startActivity(myIntent);
		;
	}

	/**
	 * Strong, noisy alarm for danger
	 */
	public void normalAlarm() {
		// Uri alert = Uri.parse("/audio/alarm_buzzer.mp3");
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
		Intent intent = new Intent(_context, MonitorActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		_context.startActivity(intent);
	}

}
