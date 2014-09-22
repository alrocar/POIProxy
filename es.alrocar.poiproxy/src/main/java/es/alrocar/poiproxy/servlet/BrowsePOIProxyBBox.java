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

import java.util.HashMap;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Parameter;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;

import es.alrocar.poiproxy.configuration.ParamEnum;
import es.alrocar.poiproxy.proxy.POIProxy;

public class BrowsePOIProxyBBox extends BrowseQueryServerResource {

	public BrowsePOIProxyBBox() {
		super();
	}

	public BrowsePOIProxyBBox(Context context, Request request,
			Response response) {
		getVariants().add(new Variant(MediaType.TEXT_PLAIN));
	}

	@Override
	protected Representation get() throws ResourceException {
		Request r = this.getRequest();
		Form form = r.getResourceRef().getQueryAsForm();
		HashMap<String, String> params = new HashMap<String, String>();
		for (Parameter parameter : form) {
			params.put(parameter.getName(), parameter.getValue());
		}

		POIProxy proxy = POIProxy.asSingleton();

		String geoJSON = "";
		try {
			geoJSON = proxy.getPOIs(params.get(ParamEnum.SERVICE.name),
					Double.valueOf(params.get(ParamEnum.MINX.name)),
					Double.valueOf(params.get(ParamEnum.MINY.name)),
					Double.valueOf(params.get(ParamEnum.MAXX.name)),
					Double.valueOf(params.get(ParamEnum.MAXY.name)),
					this.extractParams(params));
		} catch (Exception e) {
			return new StringRepresentation(
					"An unexpected error ocurred, please contact the administrator \n\n. You are accessing the browseByExtent service, check that your URL is of the type '/browseByExtent?service=XXXXX&minX=-180&minY=-90&maxX=180&maxY=90&callback=whatever' "
							+ e.getMessage());
		}

		String callback = params.get(ParamEnum.CALLBACK.name);

		if (callback == null) {
			return new StringRepresentation(geoJSON, MediaType.APPLICATION_JSON);
		} else if (callback.compareTo("?") == 0) {
			return new StringRepresentation("poiproxy(" + geoJSON + ");",
					MediaType.TEXT_JAVASCRIPT);
		} else {
			return new StringRepresentation(callback + "(" + geoJSON + ");",
					MediaType.TEXT_JAVASCRIPT);
		}
	}
}
