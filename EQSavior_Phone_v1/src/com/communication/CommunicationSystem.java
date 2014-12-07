package com.communication;

import com.ruleengine.RuleEngine;
import com.vars.Constants;

import android.util.Log;

public class CommunicationSystem {

	String header;
	String content;
	RuleEngine _ruleEngine;

	public CommunicationSystem() {
		// init attributes
		header="";
		content="";
		_ruleEngine.getInstance();
	}

	private void ReceiveInformationFromGear(String str) {
		Log.d("RuleEngine"," ------------ Message received from gear !! -----------" );

		switch (str)
		{
		case Constants.battery_Gear_status_NORMAL :
			Log.d("RuleEngine","Message received "+str );
			_ruleEngine.setGearBattery(Constants.battery_Gear_status_NORMAL);
			break;
		case Constants.battery_Gear_status_LOW :
			Log.d("RuleEngine","Message received "+str );
			_ruleEngine.setGearBattery(Constants.battery_Gear_status_LOW);
			break;
		case Constants.heartBeat_status_NORMAL :
			Log.d("RuleEngine","Message received "+str );
			_ruleEngine.setHeartBeat(Constants.heartBeat_status_NORMAL);
			break;
		case Constants.heartBeat_status_HIGH:
			Log.d("RuleEngine","Message received "+str );
			_ruleEngine.setHeartBeat(Constants.heartBeat_status_HIGH);
			break;
		case Constants.heartBeat_status_LOW :
			Log.d("RuleEngine","Message received "+str );
			_ruleEngine.setHeartBeat(Constants.heartBeat_status_LOW);
			break;
		default:
			Log.d("RuleEngine","Message Unknown "+str );
			break;
		}

	}

	private void sendInformationToGear(String str) {
		Log.d("RuleEngine","Message sent from Phone to gear: "+str );
	}

	public void tellEmergency_NORMAL() {
		header="normal";
		content="";
		sendInformationToGear(header+";"+content);
	}

	public void tellEmergency_SEVERE() {
		header="severe";
		content="";
		sendInformationToGear(header+";"+content);
	}

	public void tellStopReasoning() {
		header="stop";
		content="";
		sendInformationToGear(header+";"+content);
	}

	public void tellInformatioNToDisplay(String info) {
		header="information";
		content=info;
		sendInformationToGear(header+";"+content);
	}


}
