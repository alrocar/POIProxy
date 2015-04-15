package es.alrocar.poiproxy.fiware.poidp.schema.utils;

import java.util.HashMap;
import java.util.Map;

public class Location extends HashMap<String, Map> {

	/*
	private double longitude = 0;
	private double latitude = 0;
	private double height = 0;
	*/
	
	public Location(double lon, double lat, double hei) {
		/*
		longitude = lon;
		latitude = lat;
		height = hei;
		*/
		
		Map m = new HashMap<String, Double>();
		m.put("longitude", lon);
		m.put("latitude", lat);
		m.put("height", hei);
		
		this.put("wgs84", m);
	}
	
	public Location(double lon, double lat) {
		this(lon, lat, 0);
	}


}
