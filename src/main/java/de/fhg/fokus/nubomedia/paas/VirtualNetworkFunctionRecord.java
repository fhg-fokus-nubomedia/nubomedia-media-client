package de.fhg.fokus.nubomedia.paas;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VirtualNetworkFunctionRecord {
	
	private String id;
	private String vnfrId;
	private String nsrId;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getVnfrId() {
		return vnfrId;
	}
	public void setVnfrId(String vnfrId) {
		this.vnfrId = vnfrId;
	}
	public String getNsrId() {
		return nsrId;
	}
	public void setNsrId(String nsrId) {
		this.nsrId = nsrId;
	}
	
	public String toString(){
		return  "Response{" +
                "id='" + id + '\'' +
                ", vnfrId=" + vnfrId + '\'' +
                ", nsrId=" + nsrId+    
                '}';
	}
}
