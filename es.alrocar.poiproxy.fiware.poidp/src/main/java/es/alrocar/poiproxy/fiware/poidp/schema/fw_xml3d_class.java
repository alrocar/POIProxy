package es.alrocar.poiproxy.fiware.poidp.schema;

import es.alrocar.poiproxy.fiware.poidp.schema.utils.Source;
import es.alrocar.poiproxy.fiware.poidp.schema.utils.UpdateStamp;

public class fw_xml3d_class {

	private String model_id = "";
	private String model = "";

	private Source source = null;
	private UpdateStamp last_update = null;

	public String getModel_id() {
		return model_id;
	}

	public void setModel_id(String model_id) {
		this.model_id = model_id;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

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

}
