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
