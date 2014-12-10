package com.communication;

import com.phoneActuatingSystem.PhoneActuatingSystem;
import com.ruleengine.RuleEngine;
import com.vars.Constants;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class CommunicationSystem {

	String header;
	String content;
	RuleEngine _ruleEngine;
	private static CommunicationSystem instance = null;

	PhoneActuatingSystem _myPhoneActuationSystem;

	private Context _context;

	//private SAPServiceProvider _mySAPprovider;

	protected CommunicationSystem() {
		// init attributes
		header = "";
		content = "";
		_ruleEngine = RuleEngine.getInstance();
		_myPhoneActuationSystem = PhoneActuatingSystem.getInstance();
		//_mySAPprovider = new SAPServiceProvider();
	}

	public static CommunicationSystem getInstance() {
		if (instance == null) {
			instance = new CommunicationSystem();
		}
		return instance;
	}

	public void setContext(Context c) {
		this._context = c;
	}

	/**
	 * This function sends information from the phone to the gear
	 * @param str: the information that is sent.
	 * Information can be:
	 * 
	 * tellEmergency_SEVERE
	 * tellEmergency_NORMAL
	 * alarm_stopped
	 * information to display
	 * 
	 */
	public void sendInformationToGear(String str) {
		Log.d("Communication", "Message sent from Phone to gear: " + str);
		// here, use the SAP provider
		//_mySAPprovider.sendMessageToGear(str);
		Intent intent = new Intent("myData");
		intent.putExtra("data", str);
		_context.sendBroadcast(intent);

	}

	/**
	 * This method treat any String that can be sent from the gear
	 * @param str: the string received
	 */
	public void ReceiveInformationFromGear(String str) {
		Log.d("Communication",
				" ------------ Message received from gear !! -----------");

		switch (str) {
		case Constants.battery_Gear_status_NORMAL:
			Log.d("Communication", "Message received " + str);
			_ruleEngine.setGearBattery(Constants.battery_Gear_status_NORMAL);
			break;
		case Constants.battery_Gear_status_LOW:
			Log.d("Communication", "Message received " + str);
			_ruleEngine.setGearBattery(Constants.battery_Gear_status_LOW);
			break;
		case Constants.heartBeat_status_NORMAL:
			Log.d("Communication", "Message received " + str);
			_ruleEngine.setHeartBeat(Constants.heartBeat_status_NORMAL);
			break;
		case Constants.heartBeat_status_HIGH:
			Log.d("Communication", "Message received " + str);
			_ruleEngine.setHeartBeat(Constants.heartBeat_status_HIGH);
			break;
		case Constants.heartBeat_status_LOW:
			Log.d("Communication", "Message received " + str);
			_ruleEngine.setHeartBeat(Constants.heartBeat_status_LOW);
			break;
		case "alarm_stopped": // the user pressed stop on the gear
			Log.d("Communication", "Message received " + str);
			_myPhoneActuationSystem.stopSoundAndVibration();
			break;
		case "not_an_EQ": // the user said it is a false alarm on the gear
			Log.d("Communication", "Message received " + str);
			_ruleEngine.initialize();
			_myPhoneActuationSystem.stopSoundAndVibration();
			break;
		case "manual_EQ": // the user says there is an EQ happening on the gear
			Log.d("Communication", "Message received " + str);
			_ruleEngine.setEQ_Intensity(6);
			break;
		case "falling_motion": // the user says there is an EQ happening on the gear
			Log.d("Communication", "Message received " + str);
			
			_ruleEngine.setFalling_Motion(true);
			break;
		default:
			Log.d("Communication", "message unknown from gear" + str);
			break;
		}
	}

	public void receiveInformationFromServer(String str) {
		switch (str) {
		case "1":
			Log.d("Communication", "Message received " + str);
			_ruleEngine.setEQ_Intensity(1);
			break;
		case "2":
			Log.d("Communication", "Message received " + str);
			_ruleEngine.setEQ_Intensity(2);
			break;
		case "3":
			Log.d("Communication", "Message received " + str);
			_ruleEngine.setEQ_Intensity(3);
			break;
		case "4":
			Log.d("Communication", "Message received " + str);
			_ruleEngine.setEQ_Intensity(4);
			break;
		case "5":
			Log.d("Communication", "Message received " + str);
			_ruleEngine.setEQ_Intensity(5);
			break;
		case "6":
			Log.d("Communication", "Message received " + str);
			_ruleEngine.setEQ_Intensity(6);
			break;
		case "7":
			Log.d("Communication", "Message received " + str);
			_ruleEngine.setEQ_Intensity(7);
			break;
		case "8":
			Log.d("Communication", "Message received " + str);
			_ruleEngine.setEQ_Intensity(8);
			break;
		case "9":
			Log.d("Communication", "Message received " + str);
			_ruleEngine.setEQ_Intensity(9);
			break;
		default:
			Log.d("Communication", "message unknown from server " + str);
			break;
		}
	}

	// if normal danger is detected
	public void tellEmergency_NORMAL() {
		header = "normal";
		content = "";
		sendInformationToGear(header + ";" + content);
	}

	// if severe danger is detected
	public void tellEmergency_SEVERE() {
		header = "severe";
		content = "";
		sendInformationToGear(header + ";" + content);
	}

	// the information to display
	public void tellInformatioNToDisplay(String info) {
		header = "information";
		content = info;
		sendInformationToGear(header + ";" + content);
	}

	// not used, could be if battery low
	public void tellStopReasoning() {
		header = "stop";
		content = "";
		sendInformationToGear(header + ";" + content);
	}

}
