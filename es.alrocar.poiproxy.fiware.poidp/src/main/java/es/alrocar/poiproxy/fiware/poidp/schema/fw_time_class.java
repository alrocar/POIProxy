package es.alrocar.poiproxy.fiware.poidp.schema;

import es.alrocar.poiproxy.fiware.poidp.schema.utils.Schedule;
import es.alrocar.poiproxy.fiware.poidp.schema.utils.Source;
import es.alrocar.poiproxy.fiware.poidp.schema.utils.UpdateStamp;

public class fw_time_class {
	
	
	private String type = "open"; // "open", "show_time
	private String time_zone = "time_zone"; // not decided
	
	private Schedule schedule = null;
	private Source source = null;
	private UpdateStamp last_update = null;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTime_zone() {
		return time_zone;
	}
	public void setTime_zone(String time_zone) {
		this.time_zone = time_zone;
	}
	public Schedule getSchedule() {
		return schedule;
	}
	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
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
