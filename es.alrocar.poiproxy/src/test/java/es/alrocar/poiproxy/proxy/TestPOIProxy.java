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

package es.alrocar.poiproxy.proxy;

import java.util.List;

import es.alrocar.jpe.BaseJSONTest;
import es.alrocar.poiproxy.configuration.ServiceConfigurationManager;

public class TestPOIProxy extends BaseJSONTest {

	public void test_zip_service() throws Exception {
		// POIProxy p = new POIProxy();
		// JPEParserManager.getInstance().registerJPEParser(new XMLJPEParser());
		//
		// URL uReport = POIProxy.class.getClassLoader().getResource(
		// this.getResource());
		// System.out.println(uReport.getPath());
		//
		// String json = this.getJSON(uReport.getPath());
		//
		// assertTrue(json.length() > 0);
		//
		// DescribeServiceParser parser = new DescribeServiceParser();
		// DescribeService service = parser.parse(json);
		//
		// String data = p.doRequest(
		// "http://www.bcn.cat/tercerlloc/comercial.zip", service,
		// "comercial");
		//
		// System.out.println(p.onResponseReceived(data, service, null));
	}

	public void test_get_available_categories() {
		POIProxy poiProxy = new POIProxy();

		ServiceConfigurationManager.CONFIGURATION_DIR = "src/test/resources/";

		poiProxy.initialize();

		List<String> categories = poiProxy.getAvailableCategories();
		assertTrue(categories.size() == 5);
	}

	@Override
	public String getResource() {
		return "comercial.json";
	}
}
