package es.alrocar.poiproxy.fiware.poidp.schema;

public class POI {

	private fw_core_class fw_core = new fw_core_class();
	private fw_contact_class fw_contact = new fw_contact_class();
	private fw_marker_class fw_marker = new fw_marker_class();
	private fw_media_class fw_media = new fw_media_class();
	private fw_relationships_class fw_relationships = new fw_relationships_class();
	private fw_time_class fw_time = new fw_time_class();
	private fw_xml3d_class fw_xml3d = new fw_xml3d_class();

	public fw_core_class getCore() {
		return fw_core;
	}

	public fw_contact_class getContact() {
		return fw_contact;
	}

	public fw_marker_class getMarker() {
		return fw_marker;
	}

	public fw_media_class getMedia() {
		return fw_media;
	}

	public fw_relationships_class getRelationships() {
		return fw_relationships;
	}

	public fw_time_class getTime() {
		return fw_time;
	}

	public fw_xml3d_class getXml3d() {
		return fw_xml3d;
	}

	/*
	 * public static final String POI_COMPONENT_FW_CORE = "fw_core"; public
	 * static final String POI_COMPONENT_FW_MEDIA = "fw_media"; public static
	 * final String POI_COMPONENT_FW_XML3D = "fw_xml3d"; public static final
	 * String POI_COMPONENT_FW_RELATIONSHIPS = "fw_relationships"; public static
	 * final String POI_COMPONENT_FW_MARKER = "fw_marker"; public static final
	 * String POI_COMPONENT_FW_TIME = "fw_time"; public static final String
	 * POI_COMPONENT_FW_CONTACT = "fw_contact"; public static List<String>
	 * AVAILABLE_POI_COMPONENTS = null;
	 */

}
