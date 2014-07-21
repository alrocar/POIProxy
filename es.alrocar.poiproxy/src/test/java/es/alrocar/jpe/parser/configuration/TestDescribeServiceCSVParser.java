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

package es.alrocar.jpe.parser.configuration;

import java.net.URL;

import es.alrocar.jpe.BaseJSONTest;
import es.alrocar.poiproxy.configuration.DescribeService;
import es.alrocar.poiproxy.proxy.POIProxy;

public class TestDescribeServiceCSVParser extends BaseJSONTest {

	public void testParseDescribeService() {
		System.out.println("=======================================");
		System.out
				.println("TEST PARSE DESCRIBE SERVICE CSV: bcn_estacionsbus.json");
		System.out.println("=======================================");

		URL uReport = POIProxy.class.getClassLoader().getResource(
				this.getResource());
		System.out.println(uReport.getPath());

		String json = this.getJSON(uReport.getPath());

		assertTrue(json.length() > 0);

		DescribeServiceParser parser = new DescribeServiceParser();
		DescribeService service = parser.parse(json);

		assertEquals(service.getFormat(), "csv");
		System.out.println("bcn_estacionsubs.json format is csv");

		System.out.println("Checking csvSeparator");
		assertEquals(service.getCsvSeparator(), ";");

		System.out.println("Checking encoding");
		assertEquals(service.getEncoding(), "UTF-8");

		System.out.println("Checking decimal");
		assertEquals(service.getDecimalSeparator(), ",");

		System.out.println("=======================================");
		System.out.println("TEST SUCCESSFULL!!!");
		System.out.println("=======================================");
	}

	public String getResource() {
		return "bcn_estacionsbus.json";
	}
}
