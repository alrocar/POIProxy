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

package es.alrocar.jpe.parser.configuration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import es.alrocar.poiproxy.configuration.DescribeService;
import es.alrocar.poiproxy.configuration.FeatureType;
import es.alrocar.poiproxy.configuration.RequestType;

/**
 * A JSON parser that uses the org.json package to parse de json document that
 * describes a public POI service supported by POIProxy
 * 
 * An example of document could be something like this:
 * 
 * { describeService : { format: "json", apiKey : "", requestTypes : { "browse":
 * {"url":
 * "https://www.googleapis.com/buzz/v1/activities/search?key=AIzaSyDM7V5F3X0g4_aH6YSwsR4Hbd_uBuQ3QeA&lat=__LAT__&lon=__LON__&radius=__DIST__&alt=json"
 * , "params": []}, "search": {"url": "b", "params": []} }, featureTypes : {
 * "browse" : { "feature" : "kind", "elements" : ["title", "published", "name",
 * "profileUrl", "thumbnailUrl"], "lon": "lng", "lat": "lat", "combinedLonLat":
 * "geocode", "separator": " "
 * 
 * },"search" : { "feature" : "kind", "elements" : ["title", "published",
 * "name", "profileUrl", "thumbnailUrl"], "lon": "lng", "lat": "lat",
 * "combinedLonLat": "geocode", "separator": " "
 * 
 * } } }}
 * 
 * The format attribute specifies the format in which the service returns the
 * response and is stored into {@link DescribeService#getFormat()}. Currently
 * both json and xml are supported and the POIProxy response is always a GeoJSON
 * 
 * apiKey is not used at the moment and is stored into
 * {@link DescribeService#getApiKey()}
 * 
 * There are currently two {@link RequestType} supported,
 * {@link DescribeService#BROWSE_TYPE} simply looks for POIs and
 * {@link DescribeService#BROWSE_TYPE}, allows to specify a search term. Not all
 * services support both operations
 * 
 * {@link FeatureType} specifies the attributes of the response that are going
 * to be parsed and iares stored into {@link DescribeService#getFeatureTypes()}.
 * The parser engine used will take only the attributes specified.
 * 
 * The feature attribute is needed to know which is the first attribute of the
 * response to start parsing a new feature through
 * {@link FeatureType#getFeature()}.
 * 
 * The elements are the attributes that will be parsed and written to the
 * GeoJSON response through {@link FeatureType#getElements()}.
 * 
 * lon, lat specify the attributes of both properties to parse the Point
 * coordinates {@link FeatureType#getLon()}, {@link FeatureType#getLat()}
 * 
 * Finally as some services provide the lon, lat attributes as an array,
 * {@link FeatureType#getCombinedLonLat()} is an optional attribute that is used
 * to specify the name of that array, and
 * {@link FeatureType#getLonLatSeparator()} is the separator character that is
 * used inside the array
 * 
 * @author albertoromeu
 * 
 */
public class DescribeServiceParser {

	/******
	 * TAGS TO BE PARSED
	 */

	public final static String DESCRIBE_SERVICE = "describeService";
	public final static String API_KEY = "apiKey";
	public final static String FORMAT = "format";
	public final static String REQUEST_TYPES = "requestTypes";
	public final static String FEATURE_TYPES = "featureTypes";
	public final static String BROWSE = "browse";
	public final static String SEARCH = "search";
	public final static String FEATURE = "feature";
	public final static String ELEMENTS = "elements";
	public final static String LON = "lon";
	public final static String LAT = "lat";
	public final static String URL = "url";
	public final static String PARAMS = "params";
	public final static String COMBINEDLONLAT = "combinedLonLat";
	public final static String SEPARATOR = "separator";

	/**
	 * Parses a describe service json document passed as a String
	 * 
	 * @param json
	 *            The String containing the json document
	 * @return The {@link DescribeService} instance
	 */
	public DescribeService parse(String json) {
		DescribeService result = new DescribeService();

		JSONObject root;
		try {
			root = new JSONObject(json);
			JSONObject describeService = root.getJSONObject(DESCRIBE_SERVICE);
			String apiKey = describeService.getString(API_KEY);
			String format = describeService.getString(FORMAT);

			JSONObject requestTypes = describeService
					.getJSONObject(REQUEST_TYPES);

			RequestType requestType = parseRequestType(BROWSE, requestTypes);
			if (requestType != null)
				result.getRequestTypes().put(BROWSE, requestType);

			RequestType searchRequestType;
			try {
				searchRequestType = parseRequestType(SEARCH, requestTypes);
				if (searchRequestType != null)
					result.getRequestTypes().put(SEARCH, searchRequestType);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			JSONObject featureTypes = describeService
					.getJSONObject(FEATURE_TYPES);

			FeatureType featureType = parseFeatureType(BROWSE, featureTypes);
			if (featureType != null)
				result.getFeatureTypes().put(BROWSE, featureType);

			FeatureType searchFeatureType;
			try {
				searchFeatureType = parseFeatureType(SEARCH, featureTypes);
				if (searchFeatureType != null)
					result.getFeatureTypes().put(SEARCH, searchFeatureType);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			result.setApiKey(apiKey);
			result.setFormat(format);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			return null;
		}

		return result;
	}

	/**
	 * Parses a {@link FeatureType} object
	 * 
	 * @param type
	 *            one of {@link DescribeServiceParser#BROWSE}
	 *            {@link DescribeServiceParser#SEARCH}
	 * @param featureTypes
	 *            The {@link JSONObject} containing the {@link FeatureType}
	 * @return The {@link FeatureType} parsed
	 * @throws JSONException
	 *             when the org.json library throws an exception
	 */
	public FeatureType parseFeatureType(String type, JSONObject featureTypes)
			throws JSONException {
		FeatureType result = new FeatureType();

		Object f = featureTypes.get(type);
		if (f == null)
			return null;

		JSONObject featureType = (JSONObject) f;
		result.setFeature(featureType.getString(FEATURE));
		result.setLon(featureType.getString(LON));
		result.setLat(featureType.getString(LAT));

		try {
			result.setCombinedLonLat(featureType.getString(COMBINEDLONLAT));
		} catch (JSONException e) {

		}

		try {
			result.setLonLatSeparator(featureType.getString(SEPARATOR));
		} catch (JSONException e) {

		}

		JSONArray elements = featureType.getJSONArray(ELEMENTS);
		final int size = elements.length();
		for (int i = 0; i < size; i++) {
			result.getElements().add(elements.getString(i));
		}

		return result;
	}

	/**
	 * Parses a {@link RequestType} object
	 * 
	 * @param type
	 *            one of {@link DescribeServiceParser#BROWSE}
	 *            {@link DescribeServiceParser#SEARCH}
	 * @param requestTypes
	 *            The {@link JSONObject} containing the {@link RequestType}
	 * @return The parsed {@link RequestType}
	 * @throws JSONException
	 *             when the org.json library throws an exception
	 */
	public RequestType parseRequestType(String type, JSONObject requestTypes)
			throws JSONException {
		RequestType result = new RequestType();

		Object requestType = requestTypes.get(type);

		if (requestType == null)
			return null;

		result.setUrl(((JSONObject) requestType).getString(URL));

		JSONArray elements = ((JSONObject) requestType).getJSONArray(PARAMS);
		final int size = elements.length();
		for (int i = 0; i < size; i++) {
			result.getParams().add(elements.getString(i));
		}

		return result;
	}

}
