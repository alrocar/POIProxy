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

package es.alrocar.map.vector.provider.driver.foursquare;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import es.alrocar.map.vector.provider.driver.impl.JSONDriver;
import es.prodevelop.gvsig.mini.geom.Extent;
import es.prodevelop.gvsig.mini.geom.Point;

public class FoursquareDriverV1 extends JSONDriver {

	private String url = "http://query.yahooapis.com/v1/public/yql?format=json&q=Select%20*%20from%20json%20where%20url%3D%22http://api.foursquare.com/v1/venues.json%3Fgeolat%3D_LAT_%26geolong%3D_LON_%26l%3D50%22";

	@Override
	public String buildQuery(Extent boundingBox) {
		Point center = boundingBox.getCenter();
		String url = this.url.replaceAll("_LAT_", "" + center.getY());
		url = url.replaceAll("_LON_", "" + center.getX());
		return url;
	}

	@Override
	public JSONArray getJSONArray(JSONObject o) throws JSONException {
		return o.getJSONObject("query").getJSONObject("results")
		.getJSONObject("json").getJSONObject("groups")
		.getJSONArray("venues");
	}

	public Point processResult(JSONObject object) {
		FoursquarePoint p = new FoursquarePoint();

		String name = null;
		String fullPathName = null;
		String iconUrl = null;
		String address = null;
		String city = null;
		String zip = null;
		String state = null;
		double distance = 0;
		double lon = 0;
		double lat = 0;

		try {
			name = object.getString("name");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			JSONObject pc = object.getJSONObject("primarycategory");
			fullPathName = pc.getString("fullpathname");
			iconUrl = pc.getString("iconurl");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}

		try {
			address = object.getString("address");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
		try {
			city = object.getString("city");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
		try {
			zip = object.getString("zip");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
		try {
			state = object.getString("state");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
		try {
			distance = object.getDouble("distance");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
		try {
			lon = object.getDouble("geolong");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			lat = object.getDouble("geolat");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (lat == 0 || lon == 0)
			return null;

		p.setName(name);
		p.setFullPathName(fullPathName);
		p.setIconUrl(iconUrl);
		p.setAddress(address);
		p.setCity(city);
		p.setZip(zip);
		p.setState(state);
		p.setDistance(distance);
		p.setX(lon);
		p.setY(lat);
		return p;
	}
	
	public String getName() {
		return "foursquare";
	}
}
