package es.alrocar.poiproxy.fiware.poidp.schema.utils;

import java.util.ArrayList;
import java.util.List;

public class Relationship {
	
	private String subject = "";
	private String predicate = "";
	
	private List<String> objects = new ArrayList<String>();
	
	private UpdateStamp last_update = null;

}
