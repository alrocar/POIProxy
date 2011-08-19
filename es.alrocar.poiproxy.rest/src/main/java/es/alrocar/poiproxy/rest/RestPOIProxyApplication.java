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

package es.alrocar.poiproxy.rest;

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

		ServiceConfigurationManager.CONFIGURATION_DIR = "/var/lib/sp/services";

		return router;
	}

}
