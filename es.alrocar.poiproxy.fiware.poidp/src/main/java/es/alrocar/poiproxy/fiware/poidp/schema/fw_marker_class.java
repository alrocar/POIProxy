package es.alrocar.poiproxy.fiware.poidp.schema;

import java.util.HashMap;
import java.util.Map;

import es.alrocar.poiproxy.fiware.poidp.schema.utils.Source;
import es.alrocar.poiproxy.fiware.poidp.schema.utils.UpdateStamp;

public class fw_marker_class {

	// alvar3x3, alvar5x5
	private Map<String, String> image = new HashMap<String, String>();

	public void setImageRef(String uri) {
		image.put("image_ref", uri);
	}

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

}
