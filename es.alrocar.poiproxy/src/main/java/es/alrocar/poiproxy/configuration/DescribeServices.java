/*
 * Licensed to Prodevelop SL under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The Prodevelop SL licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 * For more information, contact:
 *
 *   Prodevelop, S.L.
 *   Pza. Don Juan de Villarrasa, 14 - 5
 *   46001 Valencia
 *   Spain
 *
 *   +34 963 510 612
 *   +34 963 510 968
 *   prode@prodevelop.es
 *   http://www.prodevelop.es
 * 
 * @author Alberto Romeu Carrasco http://www.albertoromeu.com
 */

package es.alrocar.poiproxy.configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value = "DescribeServices", description = "Contains the available services to use in the browse API operations of POIProxy")
public class DescribeServices {

	private Map<String, DescribeService> services = new HashMap<String, DescribeService>();
	private ObjectMapper mapper = new ObjectMapper();

	@ApiModelProperty(value = "Services registered in POIProxy to use in the browse API operations", notes = "key-value pairs, where key is the name of the service and value its configuration. Use the name of the service as the service parameter in browse API operations")
	public Map<String, DescribeService> getServices() {
		return services;
	}

	public void setServices(Map<String, DescribeService> services) {
		this.services.clear();
		for (String key : services.keySet()) {
			this.put(key, services.get(key));
		}
	}

	public void put(String id, DescribeService service) {
		service.setId(id);
		services.put(id, service);
	}

	public String asJSON() throws JsonGenerationException,
			JsonMappingException, IOException {
		return mapper.defaultPrettyPrintingWriter().writeValueAsString(this);
	}

	public DescribeServices fromJSON(String src) throws JsonParseException,
			JsonMappingException, IOException {
		return mapper.readValue(src, DescribeServices.class);
	}

	/**
	 * Iterates the {@link DescribeService} registered and returns a list of
	 * available categories
	 * 
	 * @return
	 */
	@ApiModelProperty(value = "Categories of services registered. Not used at the moment")
	public List<String> getCategories() {
		Set<String> keySet = this.services.keySet();
		Iterator<String> it = keySet.iterator();

		List<String> categories = new ArrayList<String>();

		DescribeService service;
		while (it.hasNext()) {
			service = this.services.get(it.next());
			for (String category : service.getCategories()) {
				if (!categories.contains(category)) {
					categories.add(category);
				}
			}
		}

		return categories;
	}

	/**
	 * Iterates the list of registered services and for those that support the
	 * category passed as a parameter return their ID
	 * 
	 * @param category
	 * @return
	 */
	public List<String> getServicesIDByCategory(String category) {
		Set<String> servicesID = services.keySet();
		Iterator<String> it = servicesID.iterator();
		List<String> ids = new ArrayList<String>();

		if (category == null) {
			return ids;
		}

		String key;
		while (it.hasNext()) {
			key = it.next();
			DescribeService service = services.get(key);
			if (service.containsCategory(category)) {
				ids.add(key);
			}
		}

		return ids;
	}

	/**
	 * Returns true if there is any service registered that supports the
	 * category parameter
	 * 
	 * @param category
	 * @return
	 */
	public boolean supportsCategory(String category) {
		if (category == null) {
			return false;
		}

		List<String> categories = this.getCategories();
		for (String cat : categories) {
			if (cat.compareToIgnoreCase(category) == 0) {
				return true;
			}
		}

		return false;
	}
}
