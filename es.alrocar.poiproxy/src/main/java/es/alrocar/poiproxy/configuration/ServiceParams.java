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

package es.alrocar.poiproxy.configuration;

import java.util.HashMap;

public class ServiceParams {

	public static final String MINX = "__MINX__";
	public static final String MAXX = "__MAXX__";
	public static final String MINY = "__MINY__";
	public static final String MAXY = "__MAXY__";

	public static final String LON = "__LON__";
	public static final String LAT = "__LAT__";
	public static final String DIST = "__DIST__";
	public static final String DISTKM = "__DISTKM__";

	public static final String KEY = "__KEY__";
	public static final String FORMAT = "__FORMAT__";

	private HashMap<String, String> params = new HashMap<String, String>();

	public void putParam(String param, String value) {
		this.params.put(param, value);
	}

	public String getValueForParam(String param) {
		return this.params.get(param);
	}

	public HashMap<String, String> getParams() {
		return this.params;
	}
}
