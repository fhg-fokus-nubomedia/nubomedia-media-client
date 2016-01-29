package de.fhg.fokus.nubomedia.kmc;

import java.util.TimerTask;

import de.fhg.fokus.nubomedia.paas.VNFRService;


public class HeartBeatTimerTask extends TimerTask{

	private final String appId;
	private VNFRService vnfrService;

	public HeartBeatTimerTask( VNFRService vnfrService,String applicationId)
	{
		this.appId = applicationId;
	}

	public void run()
	{
		 vnfrService.sendHeartBeat(appId);
	}
}
