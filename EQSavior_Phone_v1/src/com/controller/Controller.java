package com.controller;

import com.SensingSystem.SensingSystem;
import com.ruleengine.R;
import com.ruleengine.RuleEngine;
import com.view.EarthquakeActivity;
import com.view.MonitorActivity;

import android.content.Context;
import android.widget.EditText;

public class Controller {

	// Domain classes
	private RuleEngine __ruleEngine;
	private SensingSystem __sensingSystem;
	private MonitorActivity __simulationInterface;
	
	// private ReceiverHandler __receiverHandler;	
	
	// attributes
	private int __EQ_Intensity;
	private boolean __falling_Motion;
	private boolean __alarm_Deactivated;
	private boolean __timeOut;
	private String __informationToDisplay;

	private String __heartBeat;

	//new
	Context _context;
	

	public Controller(Context context)
	{	
		
		__EQ_Intensity=0;
		__falling_Motion=false;
		__alarm_Deactivated=false;
		__timeOut=false;
		this._context=context;
		//__ruleEngine = new RuleEngine(_context, this);
		__ruleEngine = RuleEngine.getInstance(); // Initialization is done here, because first instantiation
		__ruleEngine.setController(this);
		__ruleEngine.setContext(_context);
		__ruleEngine.initialize();

		__sensingSystem = new SensingSystem(this, _context);
		//__receiverHandler = new ReceiverHandler(this);
	}

	public void setSimulationInterface(MonitorActivity mf)
	{ 	__simulationInterface = mf; }
	

	public void SetNewInformationFromUI(String intensity, boolean falling, boolean alarmDeactivated, boolean timeOut, String hearBeat)
	{
		//__EQ_Intensity = atoi(intensity. something);
		__EQ_Intensity= Integer.parseInt(intensity);
		__falling_Motion = falling;
		__alarm_Deactivated = alarmDeactivated;
		__timeOut = timeOut;

		__heartBeat=hearBeat;

		updateNewFacts();
	}

	void updateNewFacts()
	{
		__ruleEngine.setEQ_Intensity(__EQ_Intensity);
		__ruleEngine.setFalling_Motion(__falling_Motion);
		__ruleEngine.setAlarm_Deactivated(__alarm_Deactivated);
		__ruleEngine.setTimeToAnswer_OUT(__timeOut);

		__ruleEngine.setHeartBeat(__heartBeat);

		__ruleEngine.Reason();
	}

	/*// display information to the view
	void displayInformation()
	{
		__simulationInterface.displayInformationToUser(__informationToDisplay);
	}*/

	// update information to display
	public void displayInformation(String info)
	{
			__simulationInterface.displayInformationToUser(info);
			//__earthquakeInterface.displayInformationToUser(info);
	}

	public void reinitialize() {
		__ruleEngine.initialize();
	}

	public void setAlarm(boolean activated)
	{
		__simulationInterface.setAlarm(activated);
	}

	public void setAlarmDeactivated()
	{
		__ruleEngine.alarmStoppedFromUser();
		// the alarm has been started, and the rule engine just needs to know that it has been deactivated.
		//__ruleEngine.setAlarm(false);
		//__ruleEngine.set__inference_blocked(true);	
		//__simulationInterface.blockInference(true);
	}

	public void setTimeOut(boolean isOut)
	{
		__ruleEngine.setTimeToAnswer_OUT(isOut);
	}

	public void setFallingMotion(boolean fall) {
		__ruleEngine.setFalling_Motion(fall);
	}

	public void setHB(String hb) {
		__ruleEngine.setHeartBeat(hb);
	}
	
	public void setEQIntensity(int EQint) {
		__ruleEngine.setEQ_Intensity(EQint);		
	}

	public void blockInference()
	{
		__simulationInterface.blockInference(true);
	}

	public void displayGPS(String information) {
		__simulationInterface.displayGPS(information);
	}

	public void displayBattery(String information) {
		__simulationInterface.displayBattery(information);
	}

	public void displayReceivedInformationFromGear(String information)
	{
		__simulationInterface.displayReceivedInformationFromGear(information);
	}

	public void displayReceivedInformationFromServer(String information)
	{
		__simulationInterface.displayReceivedInformationFromGear(information);
	}

	

	/*public void stopReasoning() {
		__ruleEngine.set__inference_blocked(true);	
	}*/
}
