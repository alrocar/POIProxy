package es.alrocar.poiproxy.fiware.poidp.schema;

import es.alrocar.poiproxy.fiware.poidp.schema.utils.Source;
import es.alrocar.poiproxy.fiware.poidp.schema.utils.UpdateStamp;

public class fw_contact_class {

	private String visit = "";
	private String[] postal = new String[0];

	private String mailto = "";
	private String[] phone = new String[0];

	private Source source = null;
	private UpdateStamp last_update = null;

	public Source getSource() {
		return source;
	}

	public void setSource(Source source) {
		this.source = source;
	}

	public UpdateStamp getLast_update() {
		return last_update;
	}

	public void setLast_update(UpdateStamp last_update) {
		this.last_update = last_update;
	}

	public String getVisit() {
		return visit;
	}

	public void setVisit(String visit) {
		this.visit = visit;
	}

	public String[] getPostal() {
		return postal;
	}

	public void setPostal(String[] postal) {
		this.postal = postal;
	}

	public String getMailto() {
		return mailto;
	}

	public void setMailto(String mailto) {
		this.mailto = mailto;
	}

	public String[] getPhone() {
		return phone;
	}

	public void setPhone(String[] phone) {
		this.phone = phone;
	}

}
