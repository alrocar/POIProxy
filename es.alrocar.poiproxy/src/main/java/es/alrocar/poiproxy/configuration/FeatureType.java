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

import java.util.ArrayList;

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
	private ArrayList<String> elements = new ArrayList<String>();
	private String feature;
	private String combinedLonLat;
	private String lonLatSeparator;

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
		return lonLatSeparator;
	}

	public void setLonLatSeparator(String lonLatSeparator) {
		this.lonLatSeparator = lonLatSeparator;
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
	 * An array of atributtes that need to be parsed from the source response
	 * 
	 * @return
	 */
	public ArrayList<String> getElements() {
		return elements;
	}

	public void setElements(ArrayList<String> elements) {
		this.elements = elements;
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

}
