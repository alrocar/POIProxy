/* POIProxy
 *
 * Copyright (C) 2011 Alberto Romeu.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
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
 *   2011.
 *   author Alberto Romeu aromeu@prodevelop.es  
 *   
 */

package es.alrocar.poiproxy.configuration;

import java.util.HashMap;

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
}
