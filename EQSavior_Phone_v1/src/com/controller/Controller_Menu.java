package com.controller;

import android.content.Context;

import com.SensingSystem.SensingSystem;
import com.phoneActuatingSystem.PhoneActuatingSystem;
import com.ruleengine.RuleEngine;

public class Controller_Menu {

	private RuleEngine _myRuleEngine;
	private SensingSystem _mySensingSystem;
	private Context _context;
	private PhoneActuatingSystem _myActuatingSystem;
	
	
	public Controller_Menu(Context context) {
		this._context = context;
		_myActuatingSystem = PhoneActuatingSystem.getInstance();
		_myActuatingSystem.setContext(_context);
		_myActuatingSystem.init();
		_myRuleEngine = RuleEngine.getInstance();
		_myRuleEngine.setContext(_context);
	
		_myActuatingSystem.setRuleEngine(_myRuleEngine);
		
		_myActuatingSystem.setCommunicationSystem();
		_mySensingSystem = new SensingSystem(_context);
	}
	
	
}
