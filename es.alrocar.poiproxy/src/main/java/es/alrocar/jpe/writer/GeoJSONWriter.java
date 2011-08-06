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

package es.alrocar.jpe.writer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.JSONArray;

import com.vividsolutions.jts.geom.Geometry;

import es.prodevelop.gvsig.mini.exceptions.BaseException;
import es.prodevelop.gvsig.mini.geom.api.IGeometry;
import es.prodevelop.gvsig.mini.geom.impl.jts.JTSFeature;

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
