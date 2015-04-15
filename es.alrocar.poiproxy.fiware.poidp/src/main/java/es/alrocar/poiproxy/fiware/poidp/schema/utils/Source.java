package es.alrocar.poiproxy.fiware.poidp.schema.utils;

import java.util.HashMap;

public class Source extends HashMap<String, String> {
	
	public Source() {
		
	}
	
	public void setName(String name) {
		this.put("name", name);
		// website id license
	}
	
	public void setWebsite(String str) {
		this.put("website", str);
		// website id license
	}

	public void setId(String str) {
		this.put("id", str);
		// website id license
	}
	
	public void setLicense(String str) {
		this.put("license", str);
		// website id license
	}


}
