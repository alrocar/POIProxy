package es.alrocar.poiproxy.fiware.poidp.schema.utils;

public class UpdateStamp {

	private long timestamp = 0;
	private String responsible = "";

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getResponsible() {
		return responsible;
	}

	public void setResponsible(String responsible) {
		this.responsible = responsible;
	}

}
