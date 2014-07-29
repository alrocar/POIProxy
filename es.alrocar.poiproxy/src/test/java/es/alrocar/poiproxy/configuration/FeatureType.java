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
import java.util.Map;

import es.alrocar.jpe.parser.configuration.DescribeServiceParser;

/**
 * An Entity to where to parse featureType object of the describe service json
 * document.
 * 
 * This class is used to know which attributes of the source response of a
 * service should be parsed
 * 
 * @see DescribeServiceParser
 * @see DescribeService
 * 
 * @author albertoromeu
 * 
 */
public class FeatureType {

	private String lon;
	private String lat;
	private Map<String, Element> elements = new HashMap<String, Element>();
	private String feature;
	private String combinedLonLat;
	private String separator;
	private Boolean reverseLonLat = false;

	/**
	 * An attribute that specify the lon lat attributes when they are on an
	 * array
	 * 
	 * @return
	 */
	public String getCombinedLonLat() {
		return combinedLonLat;
	}

	/**
	 * 
	 * @param combinedLonLat
	 */
	public void setCombinedLonLat(String combinedLonLat) {
		this.combinedLonLat = combinedLonLat;
	}

	/**
	 * The separator used to separate lon lat values, when they are on an array
	 * 
	 * @return
	 */
	public String getLonLatSeparator() {
		return separator;
	}

	public void setLonLatSeparator(String lonLatSeparator) {
		this.separator = lonLatSeparator;
	}

	/**
	 * The attribute where longitude is stored in the document response of a
	 * service
	 * 
	 * @return
	 */
	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	/**
	 * The attribute where latitude is stored in the document response of a
	 * service
	 * 
	 * @return
	 */
	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	/**
	 * The name of the attribute to start parsing a new feature from the source
	 * response
	 * 
	 * @return
	 */
	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public Map<String, Element> getElements() {
		return elements;
	}

	public void setElements(Map<String, Element> elements) {
		this.elements = elements;
	}

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	public Boolean getReverseLonLat() {
		return reverseLonLat;
	}

	public void setReverseLonLat(Boolean reverseLonLat) {
		this.reverseLonLat = reverseLonLat;
	}
}
