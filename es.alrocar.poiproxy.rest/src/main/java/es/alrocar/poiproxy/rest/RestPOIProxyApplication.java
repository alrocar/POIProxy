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

package es.alrocar.poiproxy.rest;

import java.io.File;
import java.net.URL;

import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import es.alrocar.poiproxy.configuration.ServiceConfigurationManager;
import es.alrocar.poiproxy.servlet.POIProxyApplication;

public class RestPOIProxyApplication extends POIProxyApplication {

	public RestPOIProxyApplication() {
		super();
	}

	public RestPOIProxyApplication(Context parentContext) {
		super(parentContext);
	}

	public Restlet createRoot() {
		Router router = (Router) super.createRoot();

		URL servicesPath = RestPOIProxyApplication.class.getResource("RestPOIProxyApplication.class");

		ServiceConfigurationManager.CONFIGURATION_DIR = preparePathServices(servicesPath);

		return router;
	}

	private String preparePathServices(URL servicesPath) {
		String path = servicesPath.getPath();
		if (System.getProperty("os.name").toLowerCase().indexOf("windows") != -1) {
			if (path.startsWith("/")) {
				path = path.substring(1);
			}
		}
		return path.substring(0, path.indexOf("es/alrocar/poiproxy/rest/RestPOIProxyApplication.class"));
	}
}
