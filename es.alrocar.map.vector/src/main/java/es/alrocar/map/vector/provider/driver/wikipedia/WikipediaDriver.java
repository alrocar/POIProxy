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

package es.alrocar.map.vector.provider.driver.wikipedia;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import es.alrocar.map.vector.provider.driver.impl.JSONDriver;
import es.prodevelop.gvsig.mini.geom.Extent;
import es.prodevelop.gvsig.mini.geom.Point;

public class WikipediaDriver extends JSONDriver {

	private String url = "http://api.wikilocation.org/articles?lat=_LAT_&lng=_LON_&limit=20";

	@Override
	public String buildQuery(Extent boundingBox) {
		return url.replace("_LAT_", boundingBox.getCenter().getY() + "")
				.replace("_LON_", boundingBox.getCenter().getX() + "");
	}

	@Override
	public JSONArray getJSONArray(JSONObject o) throws JSONException {
		return o.getJSONArray("articles");
	}

	@Override
	public Point processResult(JSONObject object) {
		String title = null, url = null, distance = null;
		double lat = 0, lon = 0;

		try {
			lat = object.getDouble("lat");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		try {
			lon = object.getDouble("lng");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		try {
			title = object.getString("title");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		try {
			url = object.getString("url");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		try {
			distance = object.getString("distance");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		if (lat == 0 || lon == 0)
			return null;

		WikipediaPoint p = new WikipediaPoint();
		p.setTitle(title);
		p.setUrl(url);
		p.setDistance(distance);
		p.setX(lon);
		p.setY(lat);
		return p;
	}

	public String getName() {
		return "wikipedia";
	}

}
