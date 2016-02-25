/*******************************************************************************
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * @author Alice Cheambe <alice.cheambe[at]fokus.fraunhofer.de>
 *******************************************************************************/
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
 * Provider of URLs from NUBOMEDIA cloud platform for instantiating and managing the life cycle of the 
 * Kurento Media Server
 */
@Service
public class KmsUrlProvider implements KmsProvider {

	private static final Logger logger = LoggerFactory.getLogger(KmsUrlProvider.class);

	private VNFRService vnfrService;
	private ApplicationRecord record; 
	private TimerTask timerTask;
	private Timer timer = new Timer(true);
	private int timerDelay = 15000; //delays 30 secs after the application is registered before sending the first heartbeat
	private int timerPeriod = 60000; //periodic every 60 secs minutes

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
			if(record != null)
			{
				logger.info(record.toString());
				timerTask = new HeartBeatTimerTask(vnfrService, record.getInternalAppId());
				timer.schedule(timerTask, timerDelay, timerPeriod);
				return "ws://"+record.getIP()+":8888/kurento";
			}
			return null;
		} catch (NotEnoughResourcesException e) {
			logger.info("An error occured in reserving the KMS - "+e.getMessage());
		}	
		return null;
	}
}