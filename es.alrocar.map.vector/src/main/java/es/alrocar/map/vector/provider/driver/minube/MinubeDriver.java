/* Copyright (C) 2011 Alberto Romeu Carrasco
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 *
 *   
 *   author: Alberto Romeu Carrasco (alberto@alrocar.es)
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
