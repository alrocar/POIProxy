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

import es.alrocar.jpe.parser.JPEParser;
import es.alrocar.jpe.parser.configuration.DescribeServiceParser;
import es.alrocar.poiproxy.proxy.POIProxy;

/**
 * An entity where to load into memory a describe service json
 * 
 * @see DescribeServiceParser
 * 
 * @author albertoromeu
 * 
 */
public class DescribeService {

	public final static String SEARCH_TYPE = "search";
	public final static String BROWSE_TYPE = "browse";

	private String apiKey;
	private HashMap<String, RequestType> requestTypes = new HashMap<String, RequestType>();
	private HashMap<String, FeatureType> featureTypes = new HashMap<String, FeatureType>();

	private String format;

	private String type = BROWSE_TYPE;

	/**
	 * 
	 * @return The apiKey
	 */
	public String getApiKey() {
		return apiKey;
	}

	/**
	 * Sets the apiKey
	 * 
	 * @param apiKey
	 */
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	/**
	 * The current type selected to parse a file, one of {@link #SEARCH_TYPE} or
	 * {@link #BROWSE_TYPE}
	 * 
	 * @return
	 */
	public String getType() {
		return type;
	}

	/**
	 * The current type depending on the request made by {@link POIProxy} one of
	 * {@link #SEARCH_TYPE} or {@link #BROWSE_TYPE}
	 * 
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * The format of the source response of the service to parse.
	 * 
	 * @return {@link JPEParser#FORMAT_JSON} or {@link JPEParser#FORMAT_XML}
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * Sets the format of the source response of the service to parse
	 * 
	 * @param format
	 *            {@link JPEParser#FORMAT_JSON} or {@link JPEParser#FORMAT_XML}
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	/**
	 * A map of {@link #BROWSE_TYPE}, {@link #SEARCH_TYPE} with
	 * {@link RequestType}
	 * 
	 * @return
	 */
	public HashMap<String, RequestType> getRequestTypes() {
		return requestTypes;
	}

	/**
	 * sets the request types
	 * 
	 * @param requestTypes
	 *            A map of {@link #BROWSE_TYPE}, {@link #SEARCH_TYPE} with
	 *            {@link RequestType}
	 */
	public void setRequestTypes(HashMap<String, RequestType> requestTypes) {
		this.requestTypes = requestTypes;
	}

	/**
	 * A map of {@link #BROWSE_TYPE}, {@link #SEARCH_TYPE} with
	 * {@link FeatureType}
	 * 
	 * @return
	 */
	public HashMap<String, FeatureType> getFeatureTypes() {
		return featureTypes;
	}

	/**
	 * sets the feature types
	 * 
	 * @param featureTypes
	 *            A map of {@link #BROWSE_TYPE}, {@link #SEARCH_TYPE} with
	 *            {@link FeatureType}
	 */
	public void setFeatureTypes(HashMap<String, FeatureType> featureTypes) {
		this.featureTypes = featureTypes;
	}

	/**
	 * returns the URL to request given an array of optional params. Usually if
	 * optionalParam contains a {@link Param} of type {@link Param#QUERY} then
	 * this method will return the url of the {@link RequestType} of type
	 * {@link DescribeService#SEARCH_TYPE} otherwise will return the url of
	 * {@link DescribeService#BROWSE_TYPE}
	 * 
	 * @param optionalParam
	 *            An array of {@link Param}
	 * @return The url to request
	 */
	public String getRequestForParam(ArrayList<Param> optionalParam) {
		if (optionalParam == null) {
			this.setType(DescribeService.BROWSE_TYPE);
			return getRequestTypes().get(DescribeService.BROWSE_TYPE).getUrl();
		}

		if (optionalParam.size() == 0) {
			this.setType(DescribeService.BROWSE_TYPE);
			return getRequestTypes().get(DescribeService.BROWSE_TYPE).getUrl();
		}

		for (Param optParam : optionalParam) {
			if (optParam.getType() == Param.QUERY) {
				this.setType(DescribeService.SEARCH_TYPE);
				return getRequestTypes().get(DescribeService.SEARCH_TYPE)
						.getUrl();
			}
		}

		this.setType(DescribeService.BROWSE_TYPE);
		return getRequestTypes().get(DescribeService.BROWSE_TYPE).getUrl();
	}
}
