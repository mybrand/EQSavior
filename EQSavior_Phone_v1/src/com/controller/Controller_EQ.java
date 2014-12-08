package com.controller;

import com.phoneActuatingSystem.PhoneActuatingSystem;
import com.ruleengine.RuleEngine;
import com.view.EarthquakeActivity;

public class Controller_EQ {

	private EarthquakeActivity _myEQ_Activity;
	private RuleEngine _myRuleEngine;
	private PhoneActuatingSystem _phoneActuationSystem;

	public Controller_EQ(EarthquakeActivity earthquakeActivity) {
		this._myEQ_Activity = earthquakeActivity;
		this._myRuleEngine = RuleEngine.getInstance();
		this._phoneActuationSystem = PhoneActuatingSystem.getInstance();
		init();
	}

	private void init() {
		String info = _myRuleEngine.getActionsInformation();
		_myEQ_Activity.displayInformationToUser(info);
	}

	/**
	 * There is an EQ alert but the user says it is wrong. We are going to block
	 * inference, until the user reinitialize the RE
	 */
	public void notEQ() {
		//_myRuleEngine.set__inference_blocked(true);
		_myRuleEngine.initialize();
		stopAlarm();
	}

	public void stopAlarm() {
		_phoneActuationSystem.stopSoundAndVibration();
	}

}
