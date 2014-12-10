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

import es.alrocar.poiproxy.proxy.event.POIProxyEvent;
import es.prodevelop.gvsig.mini.geom.impl.jts.JTSFeature;

/**
 * An Observer interface for {@link POIProxyEvent}
 * 
 * @author aromeu
 * 
 */
public interface POIProxyListener {

	/**
	 * Sends a {@link POIProxyEvent} after having requested a service and parsed
	 * the data
	 * 
	 * @param event
	 *            The {@link POIProxyEvent}
	 */
	public void afterParseResponse(POIProxyEvent event);

	/**
	 * Sends a {@link POIProxyEvent} before requesting a service
	 * 
	 * @param event
	 *            The {@link POIProxyEvent}
	 * @return if returns false, then the request is cancelled
	 */
	public boolean beforeRequest(POIProxyEvent poiProxyEvent);

	/**
	 * Called when {@link POIProxy} is destroyed. Free resources please.
	 */
	public void destroy();

	/**
	 * Called when new {@link JTSFeature}s have been parsed
	 * 
	 * @param poiProxyEvent
	 *            The {@link POIProxyEvent}
	 * @return
	 */
	public void onNewFeatures(POIProxyEvent poiProxyEvent);
}
