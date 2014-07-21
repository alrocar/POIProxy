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

package es.alrocar.jpe.parser;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import es.prodevelop.gvsig.mini.geom.impl.jts.JTSFeature;
import es.prodevelop.gvsig.mini.geom.impl.jts.JTSGeometry;

/**
 * Utility class to parse a GeoJSON Document of Points into an array of
 * {@link JTSFeature}
 * 
 * @author albertoromeu
 * 
 */
public class GeoJSONParser {

	/**
	 * Parses a GeoJSON document into an array of {@link JTSFeature}
	 * 
	 * @param geoJSON
	 *            The GeoJSON content
	 * @return An array of {@link JTSFeature}
	 * @throws JSONException
	 */
	public ArrayList<JTSFeature> parse(String geoJSON) throws JSONException {

		ArrayList<JTSFeature> features = new ArrayList<JTSFeature>();

		JSONObject json = new JSONObject(geoJSON);

		JSONArray feats = json.getJSONArray("features");

		JSONObject feature;
		JTSFeature feat;

		final int size = feats.length();

		for (int i = 0; i < size; i++) {
			feature = feats.getJSONObject(i);
			feat = parseFeature(feature);
			features.add(feat);
		}

		return features;
	}

	/**
	 * Parses a single feature with a Point
	 * 
	 * @param feature
	 *            The feature {@link JSONObject}
	 * @return A {@link JTSFeature}
	 * @throws JSONException
	 */
	public JTSFeature parseFeature(JSONObject feature) throws JSONException {
		JTSFeature feat = new JTSFeature(null);

		JSONObject geometry = (JSONObject) feature.get("geometry");

		String geomType = geometry.get("type").toString();
		if (geomType.compareTo("Point") == 0) {
			JSONArray coords = geometry.getJSONArray("coordinates");
			double x = coords.getDouble(0);
			double y = coords.getDouble(1);

			GeometryFactory factory = new GeometryFactory();
			Coordinate c = new Coordinate();
			c.x = x;
			c.y = y;
			Point p = factory.createPoint(c);
			feat.setGeometry(new JTSGeometry(p));
		} // TODO process other geometry types

		JSONObject props = feature.getJSONObject("properties");
		Iterator it = props.keys();

		String key;

		while (it.hasNext()) {
			key = it.next().toString();
			feat.addField(key, props.getString(key), 0);
		}

		return feat;
	}

}
