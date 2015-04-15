package es.alrocar.poiproxy.fiware.poidp.schema.utils;

import java.util.HashMap;

public class IntString extends HashMap<String, String>{
	
	public IntString(String tit) {
		this.put("", tit);
	}
	
	
	public void setDefLang(String def) {
		this.put("_def", def);
	}
	
	

	

}
