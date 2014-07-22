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

package es.alrocar.jpe.parser.csv;

import java.net.URL;
import java.util.ArrayList;

import es.alrocar.jpe.BaseJSONTest;
import es.alrocar.jpe.parser.configuration.DescribeServiceParser;
import es.alrocar.poiproxy.configuration.DescribeService;
import es.alrocar.poiproxy.proxy.LocalFilter;
import es.alrocar.poiproxy.proxy.POIProxy;
import es.prodevelop.gvsig.mini.geom.impl.jts.JTSFeature;

public class TestCSVParser extends BaseJSONTest {

	public void testParseCSV() {
		System.out.println("=======================================");
		System.out.println("TEST PARSE CSV: bcn_estacionsbus.csv");
		System.out.println("=======================================");

		URL uReport = POIProxy.class.getClassLoader().getResource(
				"bcn_estacionsbus.json");
		System.out.println(uReport.getPath());

		String json = this.getJSON(uReport.getPath());

		// FIXME Not an atomic test
		assertTrue(json.length() > 0);

		DescribeServiceParser parser = new DescribeServiceParser();
		DescribeService service = parser.parse(json);

		String geoJSON = this.getJSON();
		CSVParser csvParser = new CSVParser();

		ArrayList<JTSFeature> features = csvParser
				.parse(geoJSON, service, null);
		assertEquals(features.size(), 3225);

		System.out.println(csvParser.getGeoJSON());

		System.out.println("=======================================");
		System.out.println("TEST SUCCESSFULL!!!");
		System.out.println("=======================================");
	}

	public void testParseCSVLocalFilter() {
		System.out.println("=======================================");
		System.out
				.println("TEST PARSE CSV with LocalFilter: bcn_estacionsbus.csv");
		System.out.println("=======================================");

		URL uReport = POIProxy.class.getClassLoader().getResource(
				"bcn_estacionsbus.json");
		System.out.println(uReport.getPath());

		String json = this.getJSON(uReport.getPath());

		// FIXME Not an atomic test
		assertTrue(json.length() > 0);

		DescribeServiceParser parser = new DescribeServiceParser();
		DescribeService service = parser.parse(json);

		String geoJSON = this.getJSON();
		CSVParser csvParser = new CSVParser();

		LocalFilter filter = new LocalFilter();
		filter.value = "NITBUS";
		ArrayList<JTSFeature> features = csvParser.parse(geoJSON, service,
				filter);
		assertEquals(features.size(), 869);

		System.out.println(csvParser.getGeoJSON());

		System.out.println("=======================================");
		System.out.println("TEST SUCCESSFULL!!!");
		System.out.println("=======================================");
	}

	@Override
	public String getResource() {
		return "bcn_estacionsbus.csv";
	}

}
