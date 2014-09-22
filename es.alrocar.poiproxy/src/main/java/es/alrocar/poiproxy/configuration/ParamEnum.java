package es.alrocar.poiproxy.configuration;


/**
 * The different parameter names that can be provided to a POIProxy request
 * 
 * @author aromeu
 * 
 */
public enum ParamEnum {

	QUERY("query"), APIKEY("apiKey"), SERVICE("service"), X("x"), Y("y"), Z("z"), MINX(
			"minX"), MINY("minY"), MAXX("maxX"), MAXY("maxY"), LON("lon"), LAT(
			"lat"), DIST("dist"), CALLBACK("callback"), FROMDATE("fromDate"), TODATE(
			"toDate"), LIMIT("limit"), OFFSET("offset"), SEARCH("search");

	public String name;

	ParamEnum(String name) {
		this.name = name;
	}

	public static boolean from(String key) {
		for (ParamEnum pe : values()) {
			if (pe.name.compareTo(key) == 0) {
				return true;
			}
		}

		return false;
	}
}
