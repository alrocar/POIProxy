package es.alrocar.poiproxy.configuration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class DescribeServices {

	private Map<String, DescribeService> services = new HashMap<String, DescribeService>();
	private ObjectMapper mapper = new ObjectMapper();

	public Map<String, DescribeService> getServices() {
		return services;
	}

	public void setServices(Map<String, DescribeService> services) {
		this.services = services;
	}

	public void put(String id, DescribeService service) {
		services.put(id, service);
	}

	public String asJSON() throws JsonGenerationException,
			JsonMappingException, IOException {
		return mapper.writeValueAsString(this);
	}

	public DescribeServices fromJSON(String src) throws JsonParseException,
			JsonMappingException, IOException {
		return mapper.readValue(src, DescribeServices.class);
	}

}
