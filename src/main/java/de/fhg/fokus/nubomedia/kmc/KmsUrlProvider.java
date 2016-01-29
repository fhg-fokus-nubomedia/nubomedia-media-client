package de.fhg.fokus.nubomedia.kmc;

import java.util.Timer;
import java.util.TimerTask;

import org.kurento.client.internal.KmsProvider;
import org.kurento.client.internal.NotEnoughResourcesException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import de.fhg.fokus.nubomedia.paas.ApplicationRecord;
import de.fhg.fokus.nubomedia.paas.VNFRService;
import de.fhg.fokus.nubomedia.paas.VNFRServiceImpl;

/**
 * Provider of URLs from NUBOMEDIA cloud platform for instantiating and managing the life cycle of the Kurento Media Server
 * @author Alice Cheambe <alice.cheambe[at]fokus.fraunhofer.de>
 *
 */
@Service
public class KmsUrlProvider implements KmsProvider {

	private static final Logger logger = LoggerFactory.getLogger(KmsUrlProvider.class);

	private VNFRService vnfrService;
	private ApplicationRecord record; 
	private TimerTask timerTask;
	private Timer timer = new Timer(true);
	private int timerDelay = 300000; //delays 5 mins after the application is registered before sending the first heartbeat
	private int timerPeriod = 300000; //periodic every 5 minutes

	public KmsUrlProvider(){
		vnfrService = new VNFRServiceImpl();
	}

	public void releaseKms(String applicationId) throws NotEnoughResourcesException {
		try {
			logger.info("releasing KMS..."+applicationId);
			if(record == null)
				return;
			vnfrService.unregisterApplication(record.getInternalAppId());
			timerTask.cancel();
		} catch (Exception e) {
			throw new NotEnoughResourcesException("An error occured in releasing the KMS - "+e.getMessage());
		}
	}

	public String reserveKms(String applicationId) throws NotEnoughResourcesException {
		return reserveKms(applicationId, 50);		
	}

	public String reserveKms(String applicationId, int loadPoints) throws NotEnoughResourcesException {
		try {
			record = vnfrService.registerApplication(applicationId, loadPoints);
			if(record == null)
				throw new NotEnoughResourcesException("An error occured in reserving the KMS - No record set. Make sure the configuration to the VNFM interface is configured");
			logger.info(record.toString());
			
			timerTask = new HeartBeatTimerTask(vnfrService, applicationId);
			timer.schedule(timerTask, timerDelay, timerPeriod);
			return "ws://"+record.getIP()+":8888/kurento";
		} catch (Exception e) {
			throw new NotEnoughResourcesException("An error occured in reserving the KMS - "+e.getMessage());
		}		
	}
}