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
			Cancellable cancellable, int zoom) {

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
