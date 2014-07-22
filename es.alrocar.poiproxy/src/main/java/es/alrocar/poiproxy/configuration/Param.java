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

import es.alrocar.poiproxy.proxy.POIProxy;

/**
 * Optional parameters that comes from a request to {@link POIProxy}
 * 
 * @author albertoromeu
 * 
 */
public class Param {

	public static String QUERY = "query";
	public static String APIKEY = "apiKey";
	public static String SERVICE = "service";
	public static String X = "x";
	public static String Y = "y";
	public static String Z = "z";
	public static String MINX = "minX";
	public static String MINY = "minY";
	public static String MAXX = "maxX";
	public static String MAXY = "maxY";
	public static String LON = "lon";
	public static String LAT = "lat";
	public static String DIST = "dist";
	public static String CALLBACK = "callback";

	private String type;
	private String value;

	/**
	 * Constructor
	 * 
	 * @param type
	 * 
	 *            {@value #QUERY} {@value #SERVICE} {@value #X} {@value #Y}
	 *            {@value #Z} {@value #MINX} {@value #MINY} {@value #MAXX}
	 *            {@value #MAXY} {@value #LON} {@value #LAT} {@value #DIST}
	 *            {@value #CALLBACK} {@value #APIKEY}
	 * @param value
	 */
	public Param(String type, String value) {
		this.type = type;
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
