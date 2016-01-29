package de.fhg.fokus.nubomedia.paas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Alice Cheambe <alice.cheambe[at]fokus.fraunhofer.de>
 *
 */

public class VNFRServiceProfile {


	private static VNFRServiceProfile instance;
	private static final Logger logger = LoggerFactory.getLogger(VNFRServiceProfile.class);

	private String serverAddress;
	private int serverPort;
	private String vnfrId;
	private String serviceApiUrl;
	private String serviceBaseUrl;

	public VNFRServiceProfile() {
		super();
		logger.info("Instatiating the VNFR service profile with ff System Properties;\n VNFM_IP: "
		+ System.getenv("VNFM_IP")+"\nVNFM_PORT: "
		+ System.getenv("VNFM_PORT")+"\n"
		+ System.getenv("VNFR_ID"));
		
		//max = (a > b) ? a : b;
		serverAddress = (System.getenv("VNFM_IP") != null)? System.getenv("VNFM_IP"): "80.96.122.80";
		serverPort = Integer.parseInt((System.getenv("VNFM_PORT")!=null)?  System.getenv("VNFM_PORT"): "9000");
		vnfrId = System.getenv("VNFR_ID");
		
		if(vnfrId == null)
			instance  = null;
		else{
			this.serviceBaseUrl = "http://"+serverAddress+":"+serverPort+"/vnfr";
			this.serviceApiUrl = serviceBaseUrl+"/"+vnfrId+"/app";
			instance = this;
		}		
	}


	public static VNFRServiceProfile getInstance() {

		if(instance == null)
			new VNFRServiceProfile();		

		return instance;
	}

	public String getServiceBaseUrl() {
		return serviceBaseUrl;
	}

	public String getVnfrId() {
		return vnfrId;
	}

	public void setVnfrId(String vnfrId) {
		this.vnfrId = vnfrId;
	}

	public static void setInstance(VNFRServiceProfile instance) {
		VNFRServiceProfile.instance = instance;
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public String getServiceApiUrl() {
		return serviceApiUrl;
	}	


}
