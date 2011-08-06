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

public class GeoJSONParser {

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
