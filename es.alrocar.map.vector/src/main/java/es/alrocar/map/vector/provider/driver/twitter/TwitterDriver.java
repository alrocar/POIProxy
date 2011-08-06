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

package es.alrocar.map.vector.provider.driver.twitter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import es.alrocar.map.vector.provider.driver.impl.JSONDriver;
import es.prodevelop.gvsig.mini.geom.Extent;
import es.prodevelop.gvsig.mini.geom.Point;

public class TwitterDriver extends JSONDriver {

	private String url = "http://search.twitter.com/search.json?result_type=recent&callback=?&geocode=";

	public String buildQuery(Extent boundingBox) {
		double distance = getDistanceMeters(boundingBox) / 1000;

		String query = new StringBuffer()
				.append(boundingBox.getCenter().getY()).append(",")
				.append(boundingBox.getCenter().getX()).append(",")
				.append(distance).append("km").toString();

		return url + query;
	}

	@Override
	public JSONArray getJSONArray(JSONObject o) throws JSONException {
		return o.getJSONArray("results");
	}

	public Point processResult(JSONObject object) throws JSONException {
		TwitterPoint p = new TwitterPoint();

		String profileImg = object.getString("profile_image_url");
		String createdAt = object.getString("created_at");
		String fromUser = object.getString("from_user");
		String text = object.getString("text");
		String geoLoc = null;
		String location = null;
		try {
			JSONObject geo = object.getJSONObject("geo");
			JSONArray coords = geo.getJSONArray("coordinates");
			geoLoc = new StringBuffer().append(coords.get(0)).append(",")
					.append(coords.get(1)).toString();
		} catch (JSONException e) {
			// e.printStackTrace();
			location = object.getString("location");
			if (location.contains(":")) {
				String[] parsedLoc = location.split(":");
				if (parsedLoc.length > 1) {
					location = parsedLoc[1];
				}
			}
		}
		String toUser = object.getString("to_user_id");

		if (location == null && geoLoc == null)
			return null;
		p.setLocation(location);
		p.setImageURL(profileImg);
		p.setCreatedAt(createdAt);
		p.setFromUser(fromUser);
		p.setText(text);
		p.setGeo(geoLoc);
		p.setToUserId(toUser);
		return p;
	}
	
	public String getName() {
		return "twitter";
	}
}
