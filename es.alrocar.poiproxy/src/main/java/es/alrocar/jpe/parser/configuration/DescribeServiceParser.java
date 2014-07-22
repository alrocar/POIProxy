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

package es.alrocar.jpe.parser.configuration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

	/**
	 * Parses a describe service json document passed as a String
	 * 
	 * @param json
	 *            The String containing the json document
	 * @return The {@link DescribeService} instance
	 */
	public DescribeService parse(String json) {
		Gson gson = createGSON();
		return gson.fromJson(json, DescribeService.class);
	}

	protected Gson createGSON() {
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		return gson;
	}
}
