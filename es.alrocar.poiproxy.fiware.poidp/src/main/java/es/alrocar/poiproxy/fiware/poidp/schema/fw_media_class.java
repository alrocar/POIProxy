package es.alrocar.poiproxy.fiware.poidp.schema;

import java.util.ArrayList;
import java.util.List;

import es.alrocar.poiproxy.fiware.poidp.schema.utils.UpdateStamp;

public class fw_media_class {

	private List<fw_media_entity> entities = new ArrayList<fw_media_entity>();
	private UpdateStamp last_update = null;

	public UpdateStamp getLast_update() {
		return last_update;
	}

	public void setLast_update(UpdateStamp last_update) {
		this.last_update = last_update;
	}

}
