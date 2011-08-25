/* POIProxy. A proxy service to retrieve POIs from public services
 *
 * Copyright (C) 2011 Alberto Romeu.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
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
 *   aromeu@prodevelop.es
 *   http://www.prodevelop.es
 *   
 *   2011.
 *   author Alberto Romeu aromeu@prodevelop.es  
 *   
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

import es.alrocar.poiproxy.configuration.Param;
import es.alrocar.poiproxy.proxy.POIProxy;

public class BrowsePOIProxyZXY extends BrowseQueryServerResource {

	public BrowsePOIProxyZXY() {
		super();
	}

	public BrowsePOIProxyZXY(Context context, Request request, Response response) {
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

		POIProxy proxy = POIProxy.getInstance();

		proxy.initialize();

		String geoJSON = "";
		try {
			geoJSON = proxy.getPOIs(params.get(Param.SERVICE),
					Integer.valueOf(params.get(Param.Z)),
					Integer.valueOf(params.get(Param.X)),
					Integer.valueOf(params.get(Param.Y)),
					this.extractParams(params));
			System.out.println(geoJSON);
		} catch (Exception e) {
			return new StringRepresentation(
					"An unexpected error ocurred, please contact the administrator \n\n. You are accessing the browse service, check that your URL is of the type '/browse?service=panoramio&z=0&x=0&y=0&callback=whatever'");
		}

		String callback = params.get(Param.CALLBACK);

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
