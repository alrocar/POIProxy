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

/**
 * The different parameter names that can be provided to a POIProxy request
 * 
 * @author aromeu
 * 
 */
public enum ParamEnum {

	QUERY("query"), APIKEY("apiKey"), SERVICE("service"), X("x"), Y("y"), Z("z"), MINX(
			"minX"), MINY("minY"), MAXX("maxX"), MAXY("maxY"), LON("lon"), LAT(
			"lat"), DIST("dist"), CALLBACK("callback"), FROMDATE("fromDate"), TODATE(
			"toDate"), LIMIT("limit"), OFFSET("offset"), SEARCH("search");

	public String name;

	ParamEnum(String name) {
		this.name = name;
	}

	public static boolean from(String key) {
		for (ParamEnum pe : values()) {
			if (pe.name.compareTo(key) == 0) {
				return true;
			}
		}

		return false;
	}
}
