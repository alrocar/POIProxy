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
