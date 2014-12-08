package com.ruleengine;

import android.content.Context;
import android.util.Log;

import com.controller.Controller_Normal;
import com.controller.Controller_Simulation;
import com.phoneActuatingSystem.PhoneActuatingSystem;
import com.vars.Constants;

public class RuleEngine {

	// SINGLETON PATTERN
	private static RuleEngine instance = null;

	private Controller_Simulation __myControllerSimulation;

	private Controller_Normal __myControllerNormal;

	/* the actuating system */
	private PhoneActuatingSystem __myActuatingSystem;

	// temp
	private Context _context;

	// inference is on or not
	private boolean __inference_blocked;

	// sensing facts
	private int __EQ_Intensity; // the intensity of EQ - 1 - 10
	private boolean __falling_Motion; // user fell down or not
	private boolean __alarm_Deactivated; // user deactivated the alarm
	private String __heartBeat; // the heart beat rate of the user - low, normal
	// or high
	private String __phoneBattery; // the status of the phone battery - low or
	// normal
	private String __gearBattery; // the status of the gear battery - low or
	// normal
	private boolean __timeToAnswer_OUT; // the alarm has been started, but the
	// user didn't turn it off

	// about calling
	private boolean __no_answer_from_Family;
	private boolean __no_answer_from_Emergency;

	// facts that can be inferred from sensing facts
	private boolean __danger; // is the user in danger?
	private boolean __severeDanger; // is the user in a severe danger?

	// facts that are used not to start twice a new action - whether or not we
	// already took a decision
	private boolean __alarmStarted; // was the alarm already started by the rule
	// engine?
	private boolean __emergencyCalled; // was the emergency already called?
	private boolean __familyCalled; // was the family already called?
	private boolean __phoneBattery_Managed; // was the low battery of the phone
	// managed already?
	private boolean __gearBattery_Managed; // was the low battery of the gear
	// managed already?

	/**
	 * Constructor, set default values controller and actuating systems have to
	 * be set separately (using setters)
	 */
	protected RuleEngine(/* Context context,Controller controller */) {
		initialize();
		/*
		 * this._context=context; this.__myController = controller;
		 */
	}

	public static RuleEngine getInstance() {
		if (instance == null) {
			instance = new RuleEngine();
		}
		return instance;
	}

	// ---------------------- Setters ------------------ \\

	/* set the controller this engine is associated with */
	public void setController(Controller_Simulation __myController) {
		this.__myControllerSimulation = __myController;
	}

	// set context
	public void setContext(Context context) {
		this._context = context;
	}

	/* updating the rule engine with new facts */
	public void setEQ_Intensity(int __EQ_Intensity) {
		this.__EQ_Intensity = __EQ_Intensity;
		updateNormalDisplay();
		Reason();
	}

	public void setFalling_Motion(boolean __falling_Motion) {
		this.__falling_Motion = __falling_Motion;
		updateNormalDisplay();
		Reason();
	}

	public void setAlarm_Deactivated(boolean __alarm_Deactivated) {
		this.__alarm_Deactivated = __alarm_Deactivated;
		Reason();
	}

	public void setHeartBeat(String __heartBeat) {
		this.__heartBeat = __heartBeat;
		updateNormalDisplay();
		Reason();
	}

	public void setPhoneBattery(String __phoneBattery) {
		this.__phoneBattery = __phoneBattery;
		updateNormalDisplay();
		Reason();
	}

	public void setGearBattery(String __gearBattery) {
		this.__gearBattery = __gearBattery;
		updateNormalDisplay();
		Reason();
	}

	public void setNo_answer_from_Family(boolean __no_answer_from_Family) {
		this.__no_answer_from_Family = __no_answer_from_Family;
		Reason();
	}

	public void setNo_answer_from_Emergency(boolean __no_answer_from_Emergency) {
		this.__no_answer_from_Emergency = __no_answer_from_Emergency;
		Reason();
	}

	/*
	 * simulated, to improve -- for instance whenever the alarm is started we
	 * should wait for a time t, if the alarm is not deactivated by this time
	 * then we will call emergency
	 */
	public void setTimeToAnswer_OUT(boolean __timeToAnswer_OUT) {
		this.__timeToAnswer_OUT = __timeToAnswer_OUT;
		Reason();
	}

	/* update of internal facts */
	public void setDanger(boolean __danger) {
		this.__danger = __danger;
		updateNormalDisplay();
	}

	public void setAlarmStarted(boolean __alarmStarted) {
		this.__alarmStarted = __alarmStarted;
		updateNormalDisplay();
	}

	public void setEmergencyCalled(boolean __emergencyCalled) {
		this.__emergencyCalled = __emergencyCalled;
		updateNormalDisplay();
	}

	public void setFamilyCalled(boolean __familyCalled) {
		this.__familyCalled = __familyCalled;
		updateNormalDisplay();
	}

	public void set__inference_blocked(boolean __inference_blocked) {
		this.__inference_blocked = __inference_blocked;
	}

	public void alarmStoppedFromUser() {
		this.__alarm_Deactivated = true;
		this.__alarmStarted = false;
		this.set__inference_blocked(true);
		__myControllerSimulation.blockInference();
	}

	/**
	 * This function is called to start the reasoning. Every current fact's
	 * value is considered and if new rules have to be fired they are: new facts
	 * are created and new actions are triggered
	 */
	public void Reason() {

		// if we are not blocking the inference
		if (!__inference_blocked) {
			// infer danger level
			if (__EQ_Intensity >= 2)
				__danger = true;
			if (__EQ_Intensity >= 4)
				__severeDanger = true;
			if (__EQ_Intensity < 4) {
				__severeDanger = false;
			}
			if (__EQ_Intensity < 2) {
				__danger = false;
				__severeDanger = false;
			}

			// if severe danger, different alarm
			if (__severeDanger && !__alarmStarted) {
				activateAlarm_severeDanger();
				Log.d("RuleEngine", "rule 0 fired");
			} // if any severe danger, start severe alarm to warn user

			// if danger
			if (__danger && !__alarmStarted) {
				activateAlarm_danger();
				Log.d("RuleEngine", "rule 1 fired");
			} // if any danger, start alarm to warn user
			// danger + some dangerous additional information that makes it more
			// serious
			if (__danger && __falling_Motion && !__familyCalled) {
				callFamily();
				Log.d("RuleEngine", "rule 2 fired");
			} // likely a bit hurt so call family
			if (__danger && __heartBeat == Constants.heartBeat_status_LOW
					&& !__emergencyCalled) {
				callEmergency();
				Log.d("RuleEngine", "rule 3 fired");
			} // likely hurt so call emergency
			if (__danger && __heartBeat == Constants.heartBeat_status_HIGH
					&& !__familyCalled) {
				callFamily();
				Log.d("RuleEngine", "rule 4 fired");
			} // likely due to stress so call family

			// dealing with calling family and emergency special cases
			if (__no_answer_from_Family && !__emergencyCalled) {
				callEmergency();
				Log.d("RuleEngine", "rule 5 fired");
			} // if nobody of the family answers then we call emergency
			if (__no_answer_from_Emergency) {
				callEmergency();
				Log.d("RuleEngine", "rule 6 fired");
			} // we just keep calling emergency

			// if waiting for answer, we actually set waiting response to true a
			// certain time after the first reasoning to let the user turn off
			// the alarm (ex: 2 seconds)
			if (__severeDanger && !__alarm_Deactivated && !__emergencyCalled
					&& __timeToAnswer_OUT) {
				callEmergency();
				__alarm_Deactivated = true;
				__timeToAnswer_OUT = false;
				blockInference();
				Log.d("RuleEngine", "rule 7 fired");
			}
			if (__danger && !__alarm_Deactivated && !__familyCalled
					&& __timeToAnswer_OUT) {
				callFamily();
				__alarm_Deactivated = true;
				__timeToAnswer_OUT = false;
				Log.d("RuleEngine", "rule 8 fired");
			}

			// Considering the batter
			if (__phoneBattery == Constants.battery_Phone_status_LOW
					&& !__phoneBattery_Managed) {
				managePhoneBattery();
				Log.d("RuleEngine", "rule 9 fired");
			}
			if (__gearBattery == Constants.battery_Gear_status_LOW
					&& !__gearBattery_Managed) {
				managerGearBattery();
				__gearBattery_Managed = true;
				Log.d("RuleEngine", "rule 10 fired");
			}

			// informing the user of the decisions -- it is for simulation now
			InformOfResult();
		}
	}

	private void startDestressSignal() {
		__myActuatingSystem.startDestressSignal();
	}

	private void managerGearBattery() {
		__phoneBattery_Managed = true;
		__myActuatingSystem.manageGearBattery();
	}

	private void managePhoneBattery() {
		__phoneBattery_Managed = true;
		__myActuatingSystem.managePhoneBattery();
	}

	/**
	 * blocks the inference. For example, if the user wants to block the
	 * inference, or if the most critical actions have already been taken.
	 */
	void blockInference() {
		this.__inference_blocked = true;
		__myControllerSimulation.blockInference();
	}

	// to update, the function so far just block the GUI, such function should
	// be in controller.
	// Or in the setters. Or in the reasoning, over the whole set of rules
	// (block or not)

	private void activateAlarm_danger() {
		// update of facts
		__alarmStarted = true;
		__alarm_Deactivated = false;
		// activate the alarm here
		Log.d("RuleEngine", "context status in RE: "+_context);

		__myActuatingSystem.normalAlarm();
	}

	private void activateAlarm_severeDanger() {
		__alarmStarted = true;
		__alarm_Deactivated = false;
		__myActuatingSystem.severeAlarm();

	}

	/**
	 * when finished, should unblock inference
	 */
	void callFamily() {
		// update of facts
		__familyCalled = true;

		blockInference();

		// call the family here }
		__myActuatingSystem.callFamily();
	}

	/**
	 * when finished, should unblock inference
	 */
	void callEmergency() {
		// update of facts
		__emergencyCalled = true;

		blockInference();

		// call the emergency here
		__myActuatingSystem.callEmergency();
		startDestressSignal();
	}

	public void InformOfResult() {
		String str = getFactsInformation();
		// update for alarm ---------------------- NOT THE RE JOB / TO BE
		// CHANGED TO ACTUATING SYSTEM
		if(__myControllerSimulation != null) 
		{
			__myControllerSimulation.setAlarm(__alarmStarted);
			// display information
			__myControllerSimulation.displayInformation(str);
		}
	}

	public String getFactsInformation() {
		// build information
		String str = "Status: ";
		str += "\n ------ Sensing facts ------ ";
		str += "\nintensity: ";
		str += __EQ_Intensity;
		str += ", falling: " + __falling_Motion;
		str += ", HeartBeat: " + __heartBeat;
		//str += ", alarm deactivation: " + __alarm_Deactivated;
		//str += "\n time for answering the alarm is out: " + __timeToAnswer_OUT;
		str += "\n ------ Inferred facts ------";
		str += "\ndanger: " + __danger;
		str += ", severeDanger: " + __severeDanger;
		str += "\n------  Actions taken: ------ ";
		str += "\nalarmStarted: " + __alarmStarted;
		str += ", emergencyCalled: " + __emergencyCalled;
		str += ", familyCalled: " + __familyCalled;

		return str;
	}

	public String getActionsInformation() {
		// build information
		String str = "Status: ";
		str += "\n------  Actions taken: ------ ";
		if (__alarmStarted)
			str += "\n The alarm has been started";
		if (__emergencyCalled)
			str += "The emergency has been called";
		if (__familyCalled)
			str += "Your family has been called";

		return str;
	}

	/**
	 * Reinitialize the default facts of the rule engine
	 */
	public void initialize() {

		// actuating system
		// __myActuatingSystem = new PhoneActuatingSystem(_context);
		__myActuatingSystem = PhoneActuatingSystem.getInstance();
		//__myActuatingSystem.setContext(_context);

		// blocking inference
		__inference_blocked = false;

		// about calling
		__no_answer_from_Family = false;
		__no_answer_from_Emergency = false;

		// sensing facts
		__EQ_Intensity = 1;
		__falling_Motion = false;
		__alarm_Deactivated = false;
		__heartBeat = Constants.heartBeat_status_NORMAL;
		__phoneBattery = Constants.battery_Phone_status_NORMAL;
		__gearBattery = Constants.battery_Gear_status_NORMAL;

		// internal facts
		__danger = false;
		__severeDanger = false;
		__alarmStarted = false;
		__emergencyCalled = false;
		__familyCalled = false;
		__timeToAnswer_OUT = false;

		__phoneBattery_Managed = false;
		__gearBattery_Managed = false;
	}

	public void setNormalController(Controller_Normal controller_Normal) {
		__myControllerNormal = controller_Normal;		
	}

	/**
	 * whenever there is a change in the status informations, it is updated in the normal GUI
	 */
	public void updateNormalDisplay() {
		String info = getFactsInformation();
		if(__myControllerNormal != null)
			__myControllerNormal.setNewFacts(info);
	}

}
