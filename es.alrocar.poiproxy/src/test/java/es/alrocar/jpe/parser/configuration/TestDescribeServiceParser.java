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
import es.alrocar.poiproxy.configuration.FeatureType;
import es.alrocar.poiproxy.proxy.POIProxy;

public class TestDescribeServiceParser extends BaseJSONTest {

	public void testParseDescribeService() {
		System.out.println("=======================================");
		System.out.println("TEST PARSE DESCRIBE SERVICE: test.json");
		System.out.println("=======================================");

		URL uReport = POIProxy.class.getClassLoader().getResource(
				this.getResource());
		System.out.println(uReport.getPath());

		String json = this.getJSON(uReport.getPath());

		assertTrue(json.length() > 0);

		DescribeServiceParser parser = new DescribeServiceParser();
		DescribeService service = parser.parse(json);

		assertEquals(service.getFormat(), "json");
		System.out.println("Buzz.json format is json");

		System.out.println("Checking featureType to browse");
		FeatureType browseType = service.getFeatureTypes().get(
				DescribeService.BROWSE_TYPE);

		assertEquals(browseType.getFeature(), "kind");
		System.out.println("Buzz.json feature is kind");

		assertEquals(browseType.getLat(), "lat");
		System.out.println("Buzz.json latitude is lat");

		assertEquals(browseType.getLon(), "lng");
		System.out.println("Buzz.json longitude is lng");

		assertEquals(browseType.getCombinedLonLat(), "geocode");
		System.out.println("Buzz.json combinedLonLat is geocode");

		assertEquals(browseType.getLonLatSeparator(), " ");
		System.out.println("Buzz.json lonLatSeparator is ' '");

		assertEquals(browseType.getElements().size(), 5);
		System.out.println("Buzz.json elements size is 5");

		System.out.println("Checking featureType to search");
		browseType = service.getFeatureTypes().get(DescribeService.SEARCH_TYPE);

		assertEquals(browseType.getFeature(), "kind");
		System.out.println("Buzz.json feature is kind");

		assertEquals(browseType.getLat(), "lat");
		System.out.println("Buzz.json latitude is lat");

		assertEquals(browseType.getLon(), "lng");
		System.out.println("Buzz.json longitude is lng");

		assertEquals(browseType.getCombinedLonLat(), "geocode");
		System.out.println("Buzz.json combinedLonLat is geocode");

		assertEquals(browseType.getLonLatSeparator(), " ");
		System.out.println("Buzz.json lonLatSeparator is ' '");

		assertEquals(browseType.getElements().size(), 5);
		System.out.println("Buzz.json elements size is 5");

		System.out.println("Check requestType to browse");

		assertEquals(
				service.getRequestTypes().get(DescribeService.BROWSE_TYPE)
						.getUrl(),
				"https://www.googleapis.com/buzz/v1/activities/search?key=AIzaSyDM7V5F3X0g4_aH6YSwsR4Hbd_uBuQ3QeA&lat=__LAT__&lon=__LON__&radius=__DIST__&alt=json");

		System.out.println("=======================================");
		System.out.println("TEST SUCCESSFULL!!!");
		System.out.println("=======================================");
	}

	public void testParseDescribeServiceCategories() {
		System.out.println("=======================================");
		System.out.println("TEST PARSE DESCRIBE SERVICE CATEOGRIES: test.json");
		System.out.println("=======================================");

		URL uReport = POIProxy.class.getClassLoader().getResource(
				this.getResource());
		System.out.println(uReport.getPath());

		String json = this.getJSON(uReport.getPath());

		assertTrue(json.length() > 0);

		DescribeServiceParser parser = new DescribeServiceParser();
		DescribeService service = parser.parse(json);

		assertEquals(service.getSRS(), "EPSG:23030");

		assertEquals(service.getCategories().size(), 3);
		assertEquals(service.getCategories().get(0), "social");
		assertEquals(service.getCategories().get(2), "location");

		System.out.println("=======================================");
		System.out.println("TEST SUCCESSFULL!!!");
		System.out.println("=======================================");
	}

	public void testParseDescribeServiceSRS() {
		System.out.println("=======================================");
		System.out.println("TEST PARSE DESCRIBE SERVICE SRS: test.json");
		System.out.println("=======================================");

		URL uReport = POIProxy.class.getClassLoader().getResource(
				this.getResource());
		System.out.println(uReport.getPath());

		String json = this.getJSON(uReport.getPath());

		assertTrue(json.length() > 0);

		DescribeServiceParser parser = new DescribeServiceParser();
		DescribeService service = parser.parse(json);

		assertEquals(service.getSRS(), "EPSG:23030");

		System.out.println("=======================================");
		System.out.println("TEST SUCCESSFULL!!!");
		System.out.println("=======================================");
	}

	public String getResource() {
		return "test.json";
	}
}
