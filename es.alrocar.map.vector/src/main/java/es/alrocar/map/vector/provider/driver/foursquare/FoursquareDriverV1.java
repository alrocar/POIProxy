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
