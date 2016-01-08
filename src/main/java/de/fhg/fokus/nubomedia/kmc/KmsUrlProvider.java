package de.fhg.fokus.nubomedia.kmc;

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

	public KmsUrlProvider(){
		vnfrService = new VNFRServiceImpl();
	}


	public void releaseKms(String applicationId) throws NotEnoughResourcesException {
		try {
			logger.info("releasing KMS..."+applicationId);
			if(record == null)
				return;
			vnfrService.unregisterApplication(record.getInternalAppId());
		} catch (Exception e) {
			throw new NotEnoughResourcesException("An error occured in releasing the KMS - "+e.getMessage());
		}

	}

	public String reserveKms(String applicationId) throws NotEnoughResourcesException {
		try {
			record = vnfrService.registerApplication(applicationId, 50); 
			//TODO check with Michael if the points could be null
			if(record == null)
				throw new NotEnoughResourcesException("An error occured in reserving the KMS - No record set. Make sure the configuration to the VNFM interface is configured");
			else
			{ 
				logger.info(record.toString());
			//	    	return "ws://192.168.149.172:8888/kurento";
				return "ws://"+record.getIP()+":8888/kurento";
			}
		} catch (Exception e) {
			throw new NotEnoughResourcesException("An error occured in reserving the KMS - "+e.getMessage());
		}		
	}

	public String reserveKms(String applicationId, int loadPoints) throws NotEnoughResourcesException {
		try {
			record = vnfrService.registerApplication(applicationId, loadPoints);
			if(record == null)
				throw new NotEnoughResourcesException("Internal Server Error");
			logger.info(record.toString());
			return "ws://"+record.getIP()+":8888/kurento";
		} catch (Exception e) {
			throw new NotEnoughResourcesException("An error occured in reserving the KMS - "+e.getMessage());
		}		
	}
}
