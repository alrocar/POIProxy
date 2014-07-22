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

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.HashMap;
import java.util.Iterator;

import es.alrocar.jpe.BaseJSONTest;
import es.alrocar.jpe.parser.json.JSONJPEParser;
import es.alrocar.jpe.parser.xml.XMLJPEParser;
import es.alrocar.poiproxy.configuration.DescribeService;
import es.alrocar.poiproxy.configuration.FeatureType;
import es.alrocar.poiproxy.configuration.ServiceConfigurationManager;

public class TestJPEParser extends BaseJSONTest {

	private ServiceConfigurationManager serviceManager;

	public void testParseAllServices() {
		System.out.println("=======================================");
		System.out.println("TEST PARSE ALL REGISTERED SERVICES");
		System.out.println("=======================================");

		this.registerAllTestServices();

		HashMap<String, String> registeredConfigurations = this.serviceManager
				.getRegisteredConfigurations();

		Iterator<String> keys = registeredConfigurations.keySet().iterator();
		while (keys.hasNext()) {
			parse(keys.next());
		}
	}

	public void parse(String serviceId) {
		System.out.println("=======================================");
		System.out.println("TEST PARSE " + serviceId + "");
		System.out.println("=======================================");

		DescribeService service = this.serviceManager
				.getServiceConfiguration(serviceId);

		JPEParser parser;

		if (service.getFormat().compareTo("json") == 0) {
			parser = new JSONJPEParser();
		} else {
			parser = new XMLJPEParser();
		}

		HashMap<String, FeatureType> featureTypes = service.getFeatureTypes();

		Iterator<String> fTypes = featureTypes.keySet().iterator();

		String type;
		while (fTypes.hasNext()) {
			type = fTypes.next();
			System.out.println("=======================================");
			System.out.println("TEST PARSE " + serviceId + " " + type + " ");
			System.out.println("=======================================");
			service.setType(type);
			String testFile = this.getTestFilePath(serviceId + "_" + type
					+ ".jsontest");
			if (testFile == null) {
				System.out.println("WARNING: Cannot find: " + serviceId + "_"
						+ type + ".jsontest. Test cannot be executed");

			} else {
				parser.parse(this.getJSON(testFile), service, null);

				System.out.println("Parsed " + serviceId + " " + type);

				Charset charset = Charset.forName("UTF-8");
				CharsetEncoder encoder = charset.newEncoder();

				try {
					String geoJSONParsed = parser.getGeoJSON();
					String geoJSONTestFile = this.getJSON(this
							.getTestFilePath(serviceId + "_" + type
									+ ".geojson"));

					geoJSONParsed = new String(geoJSONParsed.getBytes(),
							"UTF-8");
					geoJSONTestFile = new String(geoJSONTestFile.getBytes(),
							"UTF-8");

					System.out.println(geoJSONParsed);
					System.out.println("::::::::::::::::::::::::::::::::::::");
					System.out.println(geoJSONTestFile);

					// assertTrue(geoJSONParsed.length() ==
					// geoJSONTestFile.length());

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		System.out.println("=======================================");
		System.out.println("TEST " + serviceId + " SUCCESSFULL!!!");
		System.out.println("=======================================");

	}

	public void registerAllTestServices() {
		String path = this.getTestFilePath("panoramio.json").substring(
				0,
				this.getTestFilePath("panoramio.json")
						.indexOf("panoramio.json"));
		ServiceConfigurationManager.CONFIGURATION_DIR = path;
		serviceManager = new ServiceConfigurationManager();
	}

	@Override
	public String getResource() {
		return "panoramio_browse.geojson";
	}

	// final JPEParser jpeParser = JPEParserManager.getInstance()
	// .getJPEParser(service.getFormat());
	// ArrayList<JTSFeature> features = jpeParser.parse(json, service);
	// String geoJSON = jpeParser.getGeoJSON();
	// // Escribir en cache el geoJSON
	// return geoJSON;

}
