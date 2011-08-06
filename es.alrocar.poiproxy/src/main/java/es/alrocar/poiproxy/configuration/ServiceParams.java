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
