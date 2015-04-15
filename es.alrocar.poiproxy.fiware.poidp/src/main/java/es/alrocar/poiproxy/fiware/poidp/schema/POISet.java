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
 * @author Alberto Romeu Carrasco aromeu@prodevelop.es
 */

package es.alrocar.poiproxy.fiware.poidp.schema;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import es.alrocar.poiproxy.fiware.poidp.schema.utils.DateTypeAdapter;
import es.alrocar.poiproxy.fiware.poidp.schema.utils.Source;
import es.alrocar.poiproxy.fiware.poidp.schema.utils.UpdateStamp;

public class POISet {

	private Gson gson = null;
	private Map<String, POI> pois = new LinkedHashMap<String, POI>();

	public static void main(String[] args) {

		POISet poiset = new POISet();
		POI poi = new POI();

		Source sou = new Source();
		sou.setId("dsdasdada");
		sou.setLicense("My license");
		sou.setName("My name");
		sou.setWebsite("http://www.upv.es");

		UpdateStamp usta = new UpdateStamp();
		usta.setTimestamp(18236812876l);
		usta.setResponsible("My resposible");

		// ==========================================
		poi.getCore().setCategory("restaurant");

		// ==========================================
		fw_contact_class fw_contact = new fw_contact_class();

		poi.getContact().setLast_update(usta);
		poi.getContact().setSource(sou);
		poi.getContact().setMailto("mail@fa.com");
		poi.getContact().setPhone(new String[] { "96-81263", "96-1263" });
		poi.getContact().setVisit("How to arrive");

		poi.getContact().setPostal(
				new String[] { "Pepito García", "Prodevelop, SL",
						"Pl. Juan 14, pta 5", "CP 46001 Valencia" });
		// ==========================================
		// ==========================================

		poiset.put("ID-98729346293469236", poi);
		poiset.put("ID-12729346293469236", poi);

		String str = poiset.asJSON();

		System.out.println("OUT = ");
		System.out.println(str);

	}

	protected String asJSON() {
		return gson().toJson(pois);
	}

	// ==================================

	protected Gson gson() {
		if (gson == null) {
			GsonBuilder lObjJson = new GsonBuilder();
			lObjJson.setDateFormat("yyyy/MM/dd HH:mm:ss");
			lObjJson.registerTypeAdapter(Date.class, new DateTypeAdapter());
			lObjJson.serializeNulls();
			gson = lObjJson.create();
		}
		return gson;
	}

	public void put(String poiID, POI poi) {
		this.pois.put(poiID, poi);
	}

	public Map<String, POI> getPOIs() {
		return pois;
	}
}
