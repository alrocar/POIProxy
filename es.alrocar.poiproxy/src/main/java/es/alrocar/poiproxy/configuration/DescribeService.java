/* POIProxy. A proxy service to retrieve POIs from public services
 *
 * Copyright (C) 2011 Alberto Romeu.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
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
 *   aromeu@prodevelop.es
 *   http://www.prodevelop.es
 *   
 *   2011.
 *   author Alberto Romeu aromeu@prodevelop.es  
 *   
 */

package es.alrocar.poiproxy.configuration;

import java.util.ArrayList;
import java.util.HashMap;

import sun.security.krb5.internal.crypto.Des;

import es.alrocar.poiproxy.servlet.BaseServerResource;

public class DescribeService {

	public final static String SEARCH_TYPE = "search";
	public final static String BROWSE_TYPE = "browse";

	private String apiKey;
	private HashMap<String, RequestType> requestTypes = new HashMap<String, RequestType>();
	private HashMap<String, FeatureType> featureTypes = new HashMap<String, FeatureType>();

	private String type = BROWSE_TYPE;

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public HashMap<String, RequestType> getRequestTypes() {
		return requestTypes;
	}

	public void setRequestTypes(HashMap<String, RequestType> requestTypes) {
		this.requestTypes = requestTypes;
	}

	public HashMap<String, FeatureType> getFeatureTypes() {
		return featureTypes;
	}

	public void setFeatureTypes(HashMap<String, FeatureType> featureTypes) {
		this.featureTypes = featureTypes;
	}

	public String getRequestForParam(ArrayList<Param> optionalParam) {
		if (optionalParam == null) {
			return getRequestTypes().get(DescribeService.BROWSE_TYPE).getUrl();
		}

		if (optionalParam.size() == 0) {
			return getRequestTypes().get(DescribeService.BROWSE_TYPE).getUrl();
		}

		for (Param optParam : optionalParam) {
			if (optParam.getType() == Param.QUERY) {
				return getRequestTypes().get(DescribeService.SEARCH_TYPE)
						.getUrl();
			}
		}

		return getRequestTypes().get(DescribeService.BROWSE_TYPE).getUrl();
	}
}
