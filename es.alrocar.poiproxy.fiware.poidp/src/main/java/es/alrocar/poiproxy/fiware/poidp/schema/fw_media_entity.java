package es.alrocar.poiproxy.fiware.poidp.schema;

public class fw_media_entity {

	private String short_label = "";
	private String caption = "";

	private String description = "";
	private String copyright = "";

	private String thumbnail = "";
	private String type = ""; // folder, photo, video, audio

	public String getShort_label() {
		return short_label;
	}

	public String getCaption() {
		return caption;
	}

	public String getDescription() {
		return description;
	}

	public String getCopyright() {
		return copyright;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public String getType() {
		return type;
	}

	public void setShort_label(String short_label) {
		this.short_label = short_label;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public void setType(String type) {
		this.type = type;
	}
}
