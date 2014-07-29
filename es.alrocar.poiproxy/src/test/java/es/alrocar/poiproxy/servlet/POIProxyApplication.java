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

package es.alrocar.poiproxy.servlet;

import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.representation.StringRepresentation;
import org.restlet.routing.Router;

import es.alrocar.poiproxy.configuration.ServiceConfigurationManager;

public class POIProxyApplication extends Application {

	public POIProxyApplication() {
		super();
	}

	public POIProxyApplication(Context parentContext) {
		super(parentContext);
	}

	public Restlet createRoot() {
		Router router = new Router(getContext());
		ServiceConfigurationManager.CONFIGURATION_DIR = "./WEB-INF/services";

		router.attach("/browse", BrowsePOIProxyZXY.class);
		router.attach("/browseByExtent", BrowsePOIProxyBBox.class);
		router.attach("/browseByLonLat", BrowsePOIProxyLonLat.class);
		router.attach("/describeServices", DescribeServices.class);
		router.attach("/registerService", RegisterService.class);

		Restlet mainpage = new Restlet() {
			@Override
			public void handle(Request request, Response response) {
				StringBuilder stringBuilder = new StringBuilder();

				stringBuilder.append("<html>");
				stringBuilder.append("<head><title>Hello Application "
						+ "Servlet Page</title></head>");
				stringBuilder.append("<body bgcolor=white>");
				stringBuilder.append("<table border=\"0\">");
				stringBuilder.append("<tr>");
				stringBuilder.append("<td>");
				stringBuilder.append("<h3>available REST calls</h3>");
				stringBuilder
						.append("<ol><li><a href=\"poiproxy/hello\">hello</a> --> returns hello world message "
								+ "and date string</li>");
				stringBuilder
						.append("<li><a href=\"poiproxy/browse?service=panoramio&z=17&x=65397&y=49868\">browse sample</a> --> returns a test panoramio response"
								+ "</li>");

				stringBuilder.append("</ol>");
				stringBuilder.append("</td>");
				stringBuilder.append("</tr>");
				stringBuilder.append("</table>");
				stringBuilder.append("</body>");
				stringBuilder.append("</html>");

				response.setEntity(new StringRepresentation(stringBuilder
						.toString(), MediaType.TEXT_HTML));
			}
		};
		router.attach("", mainpage);

		return router;
	}

}
