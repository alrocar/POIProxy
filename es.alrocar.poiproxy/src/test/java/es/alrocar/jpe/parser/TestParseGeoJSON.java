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

package es.alrocar.jpe.parser;

import java.util.ArrayList;

import org.json.JSONException;

import es.alrocar.jpe.BaseJSONTest;
import es.prodevelop.gvsig.mini.exceptions.BaseException;
import es.prodevelop.gvsig.mini.geom.impl.jts.JTSFeature;

public class TestParseGeoJSON extends BaseJSONTest {

	public void testGeoJSON() {
		System.out.println("=======================================");
		System.out.println("TEST PARSE GEOJSON: panoramio.geojson");
		System.out.println("=======================================");

		String geoJSON = this.getJSON();
		GeoJSONParser parser = new GeoJSONParser();
		try {
			ArrayList<JTSFeature> features = parser.parse(geoJSON);
			assertEquals(features.size(), 49);
			System.out.println("Total features: " + features.size());

			assertEquals(features.get(0).getAttribute("photo_title").value,
					"Valencia, Catedral");
			assertEquals(features.get(0).getAttribute("photo_file_url").value,
					"http://mw2.google.com/mw-panoramio/photos/square/36639728.jpg");
			assertEquals(features.get(0).getAttribute("photo_url").value,
					"http://www.panoramio.com/photo/36639728");
			System.out.println("Checked attributes OK");

			assertEquals(features.get(0).getGeometry().getGeometry()
					.getCoordinate().x, -0.3784170000000131);

			assertEquals(features.get(0).getGeometry().getGeometry()
					.getCoordinate().y, 39.457585);
			System.out.println("Checked geometry OK");

			System.out.println("=======================================");
			System.out.println("TEST SUCCESSFULL!!!");
			System.out.println("=======================================");
		} catch (JSONException e) {
			System.out.println("JSONException: " + e.getCause());
			assertEquals(true, false);
		} catch (BaseException e) {
			System.out.println("BaseException: " + e.getCause());
			assertEquals(true, false);
		}
	}

	@Override
	public String getResource() {
		return "panoramio_browse.geojson";
	}

}
