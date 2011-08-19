/* POIProxy
 *
 * Copyright (C) 2011 Alberto Romeu.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
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
import org.restlet.resource.ServerResource;

import es.alrocar.poiproxy.proxy.POIProxy;

public class BrowsePOIProxyZXY extends ServerResource {

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
			geoJSON = proxy.getPOIs(params.get("service"),
					Integer.valueOf(params.get("z")),
					Integer.valueOf(params.get("x")),
					Integer.valueOf(params.get("y")), null);
			System.out.println(geoJSON);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String callback = params.get("callback");

		if (callback == null) {
			return new StringRepresentation(geoJSON, MediaType.APPLICATION_JSON);
		} else if (callback.compareTo("?") == 0) {
			return new StringRepresentation("poiproxy ( " + geoJSON + " );",
					MediaType.TEXT_JAVASCRIPT);
		} else {
			return new StringRepresentation(callback + " ( " + geoJSON + " );",
					MediaType.TEXT_JAVASCRIPT);
		}
	}
}
