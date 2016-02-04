package de.fhg.fokus.nubomedia.paas;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 *
 * @author Alice Cheambe <alice.cheambe[at]fokus.fraunhofer.de>
 * 
 */
@Service
public class VNFRServiceImpl implements VNFRService{

	private static final Logger logger = LoggerFactory.getLogger(VNFRServiceImpl.class);

	private VNFRServiceProfile serviceProfile;
	@Autowired private RestTemplate restTemplate;
	@Autowired private Gson mapper;

	public VNFRServiceImpl() {
		this.serviceProfile = VNFRServiceProfile.getInstance();
		restTemplate = new RestTemplate();
	}
	

	/**
	 * Registers a new App to the VNFR with a specific VNFR ID
	 * @param vnfrID - The virtual network function record identifier
	 * @param loadPoints - capacity
	 * @param externalAppId - application identifier
	 */
	public ApplicationRecord registerApplication(String externalAppId, int points)
	{
		try 
		{   
			if(serviceProfile == null)
			{
				logger.info("Service Profile not set. make sure the VNFR_ID, VNFM_IP and VNFM_PORT are available ");
				return null;
			}
			String URL = serviceProfile.getServiceApiUrl();	
			ApplicationRecordBody bodyObj = new ApplicationRecordBody(externalAppId, points);
			Gson mapper = new GsonBuilder().create();
			String body = mapper.toJson(bodyObj, ApplicationRecordBody.class);
					
			logger.info("registering new application: \nURL: "+URL+"\n + "+body);
			HttpHeaders creationHeader = new HttpHeaders();
	        creationHeader.add("Accept","application/json");
	        creationHeader.add("Content-type","application/json");
	        
			HttpEntity<String> registerEntity = new HttpEntity<String>(body,creationHeader);
	        ResponseEntity response = restTemplate.exchange(URL, HttpMethod.POST,registerEntity,String.class);
	        
	        HttpStatus status = response.getStatusCode();
	        if(status.equals(HttpStatus.CREATED)  || status.equals(HttpStatus.OK))
	        {
	        	logger.info("Deployment status: "+status + " response: " + response);
		        ApplicationRecord  responseBody = mapper.fromJson((String) response.getBody(), ApplicationRecord.class);
				
				logger.info("returned object "+ responseBody.toString());
				return responseBody;
	        }else{
	        	return null;
	        }	        		
		} catch (RestClientException e) {
			logger.error("Error registering application to VNFR - " + e.getMessage());
			return null;
		}
	}

	/**
	 * Unregisters an App to the VNFR with the specific internal application identify
	 * 
	 * @param internalAppId - identifier of the registered application on the VNFR
	 */
	public void unregisterApplication(String internalAppId){
		String webServiceUrl = serviceProfile.getServiceApiUrl()+"/"+internalAppId;
		logger.info("unregistering application "+ webServiceUrl);
		
		ResponseEntity<Void> response = restTemplate.exchange(webServiceUrl, HttpMethod.DELETE,
                null, Void.class);
		Void body = response.getBody();
	}

	@Override
	public List<VirtualNetworkFunctionRecord> getListRegisteredVNFR() {
		VirtualNetworkFunctionRecord[] list = restTemplate.getForObject(serviceProfile.getServiceBaseUrl(), VirtualNetworkFunctionRecord[].class);
	   return Arrays.asList(list);		
	}

	@Override
	public List<ApplicationRecord> getListRegisteredApplications(String vnfrId) {
		ApplicationRecord[] list = restTemplate.getForObject(serviceProfile.getServiceApiUrl(), ApplicationRecord[].class);
		   return Arrays.asList(list);
	}


	@Override
	public void sendHeartBeat(String internalAppId) {
		// PUT on /vnfr/<vnfr_id>/app/<app_id>/heartbeat
		String webServiceUrl = serviceProfile.getServiceApiUrl()+"/"+internalAppId+"/heartbeat";
		logger.info("sending heartbeat to EMM "+ webServiceUrl);
		
		ResponseEntity<Void> response = restTemplate.exchange(webServiceUrl, HttpMethod.PUT,
                null, Void.class);
		Void body = response.getBody();
		logger.info("response :"+ response);
	}
}
