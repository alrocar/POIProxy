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

package es.alrocar.map.vector.provider.filesystem.impl;

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
