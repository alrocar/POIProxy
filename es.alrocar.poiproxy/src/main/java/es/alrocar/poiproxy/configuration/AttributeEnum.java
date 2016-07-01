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
public enum AttributeEnum {

	ID("id"),
	DESCRIPTION("description"),
	USERNAME("username"),
	COUNT("count"),
	NAME("name"),
	LINK("link"),
	IMAGE("image"),
	DATE("date"),
	TIME("time"),
	WEB("web"),
	URL("url"),
	ADDRESS("address"),
	CATEGORY("category"),
	COUNTRY("country"),
	CITY("city"),
	ZIP("zip"),
	STREET("street"),
	LON("lon"),
	LAT("lat"); 
      
	public String name;

	AttributeEnum(String name) {
		this.name = name;
	}

	public static boolean from(String key) {
		for (AttributeEnum pe : values()) {
			if (pe.name.compareTo(key) == 0) {
				return true;
			}
		}

		return false;
	}
}
