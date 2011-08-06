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

package es.alrocar.jpe.parser.configuration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import es.alrocar.poiproxy.configuration.DescribeService;
import es.alrocar.poiproxy.configuration.FeatureType;
import es.alrocar.poiproxy.configuration.RequestType;

public class DescribeServiceParser {

	public final static String DESCRIBE_SERVICE = "describeService";
	public final static String API_KEY = "apiKey";
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

	public DescribeService parse(String json) {
		DescribeService result = new DescribeService();
		try {
			JSONObject root = new JSONObject(json);
			JSONObject describeService = root.getJSONObject(DESCRIBE_SERVICE);
			String apiKey = describeService.getString(API_KEY);

			JSONObject requestTypes = describeService
					.getJSONObject(REQUEST_TYPES);

			RequestType requestType = parseRequestType(BROWSE, requestTypes);
			if (requestType != null)
				result.getRequestTypes().put(BROWSE, requestType);

			RequestType searchRequestType = parseRequestType(SEARCH,
					requestTypes);
			if (searchRequestType != null)
				result.getRequestTypes().put(SEARCH, searchRequestType);

			JSONObject featureTypes = describeService
					.getJSONObject(FEATURE_TYPES);

			FeatureType featureType = parseFeatureType(BROWSE, featureTypes);
			if (featureType != null)
				result.getFeatureTypes().put(BROWSE, featureType);

			FeatureType searchFeatureType = parseFeatureType(SEARCH,
					featureTypes);
			if (searchFeatureType != null)
				result.getFeatureTypes().put(SEARCH, searchFeatureType);

			result.setApiKey(apiKey);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return result;
	}

	public FeatureType parseFeatureType(String type, JSONObject featureTypes)
			throws JSONException {
		FeatureType result = new FeatureType();

		JSONObject featureType = featureTypes.getJSONObject(BROWSE);
		if (featureType == null)
			return null;
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

	public RequestType parseRequestType(String type, JSONObject requestTypes)
			throws JSONException {
		RequestType result = new RequestType();

		JSONObject requestType = requestTypes.getJSONObject(BROWSE);

		if (requestType == null)
			return null;

		result.setUrl(requestType.getString(URL));

		JSONArray elements = requestType.getJSONArray(PARAMS);
		final int size = elements.length();
		for (int i = 0; i < size; i++) {
			result.getParams().add(elements.getString(i));
		}

		return result;
	}

}
