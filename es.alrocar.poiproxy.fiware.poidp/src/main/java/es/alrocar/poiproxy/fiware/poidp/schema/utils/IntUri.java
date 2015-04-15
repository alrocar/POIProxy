package es.alrocar.poiproxy.fiware.poidp.schema.utils;

import java.util.HashMap;

public class IntUri extends HashMap<String, String>{
	
	public IntUri(String tit) {
		this.put("", tit);
	}
	
	
	public void setDefLang(String def) {
		this.put("_def", def);
	}
	
	

	

}
