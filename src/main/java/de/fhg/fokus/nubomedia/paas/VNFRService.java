package de.fhg.fokus.nubomedia.paas;

import java.util.List;

/**
 * Service provider for the Rest interface to the Elastic Media Manger on the NUBOMEDIA cloud repository
 * @author Alice Cheambe <alice.cheambe[at]fokus.fraunhofer.de>
 *
 */
public interface VNFRService {
	
	
	/**
	 * Returns a list of VNFRs managed by the Elastic Media Manager
	 * @return a list of all virtual network function records
	 */
	public List<VirtualNetworkFunctionRecord> getListRegisteredVNFR();
	
	/**
	 * Returns a list with detailed information about all apps registered to the VNFR with this <vnfr_id>
	 * @param vnfrId - the virtual network function record identifier
	 * @return
	 */
	public List<ApplicationRecord> getListRegisteredApplications(String vnfrId);
	
	/**
	 * Registers a new App to the VNFR with a specific VNFR ID
	 * @param vnfrId - The virtual network function record identifier
	 * @param loadPoints - capacity
	 * @param externalAppId - application identifier
	 */
	public ApplicationRecord registerApplication(String externalAppId, int loadPoints);
	
	
	/**
	 * Unregisters an App to the VNFR with the specific internal application identify
	 * 
	 * @param internalAppId - identifier of the registered application on the VNFR
	 */
	public void unregisterApplication(String internalAppId);
	
	/**
	 * Sends a heatbeat to the elastic media manager as a keep alive mechanism for registered sessions
	 * @param internalAppId
	 */
	public void sendHeartBeat(String internalAppId);

}
