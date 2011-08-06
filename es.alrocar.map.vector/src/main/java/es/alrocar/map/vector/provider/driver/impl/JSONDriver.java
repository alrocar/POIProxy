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

package es.alrocar.map.vector.provider.driver.impl;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import es.prodevelop.gvsig.mini.geom.Extent;
import es.prodevelop.gvsig.mini.geom.Point;
import es.prodevelop.gvsig.mini.utiles.Cancellable;

public abstract class JSONDriver extends BaseDriver {
	
	protected String[] keys;

	public ArrayList getData(int[] tile, Extent boundingBox,
			Cancellable cancellable) {

		String query = null;
		String jsonTweets = null;
		JSONTokener tokener = null;
		JSONObject o = null;

		ArrayList points = new ArrayList();
		try {
			if (cancellable != null && cancellable.getCanceled())
				return null;

			query = buildQuery(boundingBox);

			jsonTweets = download(query);

			tokener = new JSONTokener(jsonTweets);

			o = new JSONObject(tokener);

			points = processJSON(o, cancellable);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return points;
		} finally {
			tokener = null;
			jsonTweets = null;
			query = null;
			o = null;
		}
		return points;
	}

	public ArrayList processJSON(JSONObject o, Cancellable cancellable)
			throws JSONException {
		ArrayList points = new ArrayList();
		JSONArray results = getJSONArray(o);

		final int size = results.length();

		JSONObject object;
		for (int i = 0; i < size; i++) {
			try {
				if (cancellable != null && cancellable.getCanceled())
					return null;
				object = results.getJSONObject(i);
				Point f = processResult(object);
				if (f != null)
					points.add(f);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return points;
	}

	public abstract String buildQuery(Extent boundingBox);

	public abstract JSONArray getJSONArray(JSONObject o) throws JSONException;

	public abstract Point processResult(JSONObject object) throws JSONException;
}
