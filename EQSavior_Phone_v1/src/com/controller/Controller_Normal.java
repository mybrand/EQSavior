package com.controller;

import com.ruleengine.RuleEngine;
import com.view.NormalActivity;

public class Controller_Normal {
	private NormalActivity _myNormal_Activity;
	private RuleEngine _myRuleEngine;

	public Controller_Normal(NormalActivity normal_Activity ) {
		this._myNormal_Activity = normal_Activity;	
		this._myRuleEngine = RuleEngine.getInstance();
		init();
	}

	private void init() {
		String info = _myRuleEngine.getFactsInformation();
		_myNormal_Activity.displayInformationToUser(info);
	}

}
