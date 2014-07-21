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

package es.alrocar.map.vector.provider.driver.minube;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import es.alrocar.map.vector.provider.driver.impl.JSONDriver;
import es.prodevelop.gvsig.mini.geom.Extent;
import es.prodevelop.gvsig.mini.geom.Point;

public class MinubeDriver extends JSONDriver {

	private String url = "http://api.minube.com/places/coordinates.json%3Fapi_key%3D8d1622416ac9ddd97c5a86bd6c280d41%26latitude%3D_LAT_%26longitude%3D_LON_%26distance%3D_DIST_%26limit%3D20%26order%3Ddate";

	@Override
	public String buildQuery(Extent boundingBox) {
		return url.replace("_LAT_", "" + boundingBox.getCenter().getY())
				.replace("_LON_", "" + boundingBox.getCenter().getX())
				.replace("_DIST_", getDistanceMeters(boundingBox) + "");
	}

	@Override
	public JSONArray getJSONArray(JSONObject o) throws JSONException {
		return o.getJSONObject("response").getJSONArray("pois");
	}

	@Override
	public Point processResult(JSONObject object) {
		String name = null;
		double distance = 0, lat = 0, lon = 0;
		String date = null;
		String address = null;
		String zip = null;
		String telephone = null;
		String web = null, url = null;
		String city = null;
		String country = null;
		String category = null;

		try {
			name = object.getString("name");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		try {
			distance = object.getDouble("distance");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		try {
			date = object.getString("date");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		try {
			address = object.getString("address");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		try {
			zip = object.getString("zip_code");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		try {
			telephone = object.getString("telephone");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		try {
			lat = object.getDouble("latitude");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		try {
			lon = object.getDouble("longitude");
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
			city = object.getJSONObject("city").getString("name");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		try {
			country = object.getJSONObject("country").getString("name");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		try {
			category = object.getJSONObject("category").getString("name");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		MinubePoint p = new MinubePoint();

		if (lat == 0 || lon == 0)
			return null;

		p.setAddress(address);
		p.setName(name);
		p.setDate(date);
		p.setDistance(distance);
		p.setZip(zip);
		p.setTelephone(telephone);
		p.setWeb(web);
		p.setUrl(url);
		p.setCity(city);
		p.setCategory(category);
		p.setCountry(country);
		p.setX(lon);
		p.setY(lat);
		return p;
	}
	
	public String getName() {
		return "minube";
	}

}
