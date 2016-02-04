package de.fhg.fokus.nubomedia.kmc;

import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.fokus.nubomedia.paas.VNFRService;


public class HeartBeatTimerTask extends TimerTask{

	private static final Logger logger = LoggerFactory.getLogger(HeartBeatTimerTask.class);

	private final String appId;
	private VNFRService vnfrService;

	public HeartBeatTimerTask( VNFRService vnfrService,String applicationId)
	{
		this.appId = applicationId;
		this.vnfrService = vnfrService;
	}

	public void run()
	{
		try {
			vnfrService.sendHeartBeat(appId);
		} catch (Exception e) {
			logger.info("Error sending heartbeat to VNFR..."+e.getMessage());
		}
		
	}
}
