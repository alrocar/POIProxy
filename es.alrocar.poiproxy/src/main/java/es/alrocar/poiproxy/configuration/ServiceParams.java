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

package es.alrocar.poiproxy.configuration;

import java.util.HashMap;

import es.alrocar.poiproxy.proxy.POIProxy;

/**
 * Mandatory params that are used by {@link POIProxy} to build a valid request
 * to a service
 * 
 * @author albertoromeu
 * 
 */
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

	public static final String QUERY = "__QUERY__";

	public static final String FROMDATE = "__FROMDATE__";
	public static final String TODATE = "__TODATE__";

	public static final String LIMIT = "__LIMIT__";
	public static final String OFFSET = "__OFFSET__";

	private HashMap<String, String> params = new HashMap<String, String>();
	private static HashMap<String, String> optParams = new HashMap<String, String>();

	static {
		optParams.put(ParamEnum.QUERY.name, QUERY);
		optParams.put(ParamEnum.APIKEY.name, KEY);
		optParams.put(ParamEnum.FROMDATE.name, FROMDATE);
		optParams.put(ParamEnum.TODATE.name, TODATE);
		optParams.put(ParamEnum.LIMIT.name, LIMIT);
		optParams.put(ParamEnum.OFFSET.name, OFFSET);
	}

	public void putParam(String param, String value) {
		this.params.put(param, value);
	}

	public String getValueForParam(String param) {
		return this.params.get(param);
	}

	public HashMap<String, String> getParams() {
		return this.params;
	}

	public String getServiceParamFromURLParam(String urlParam) {
		return optParams.get(urlParam);
	}

	/**
	 * Decides if the parameter name is of date type
	 * 
	 * @param key
	 * @return
	 */
	public boolean isDate(String key) {
		return key.toLowerCase().indexOf("date") != -1;
	}
}
