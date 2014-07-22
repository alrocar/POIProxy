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

package es.alrocar.jpe.writer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.JSONArray;

import es.prodevelop.gvsig.mini.exceptions.BaseException;
import es.prodevelop.gvsig.mini.geom.impl.jts.JTSFeature;

/**
 * An utility class that writes into a GeoJSON document an array of
 * {@link JTSFeature} that cotains only points as geometry type
 * 
 * @author albertoromeu
 * 
 */
public class GeoJSONWriter {

	public String write(ArrayList<JTSFeature> features) throws JSONException,
			BaseException {

		String result = null;
		JSONObject featureCollection = new JSONObject();
		featureCollection.put("type", "featureCollection");

		JSONArray featuresJSON = new JSONArray();
		JSONObject featureJSON;
		JSONObject point;
		JSONObject properties;
		JSONArray coords;

		for (JTSFeature feature : features) {
			featureJSON = new JSONObject();
			point = new JSONObject();
			properties = new JSONObject();
			coords = new JSONArray();

			featureJSON.put("type", "Feature");

			point.put("type", "Point");
			coords.add(feature.getGeometry().getGeometry().getCoordinate().x);
			coords.add(feature.getGeometry().getGeometry().getCoordinate().y);
			point.put("coordinates", coords);

			Set keys = feature.getAttributes().keySet();

			Iterator it = keys.iterator();

			String key;
			while (it.hasNext()) {
				key = it.next().toString();
				properties.put(key, feature.getAttribute(key).value);
			}

			featureJSON.put("geometry", point);
			featureJSON.put("properties", properties);

			featuresJSON.add(featureJSON);
		}

		featureCollection.put("features", featuresJSON);

		result = featureCollection.toString();
		return result;
	}

}
