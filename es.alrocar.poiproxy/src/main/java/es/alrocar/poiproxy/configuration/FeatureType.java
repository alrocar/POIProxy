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

import java.util.ArrayList;

public class FeatureType {

	private String lon;
	private String lat;
	private ArrayList<String> elements = new ArrayList<String>();
	private String feature;
	private String combinedLonLat;
	private String lonLatSeparator;

	public String getCombinedLonLat() {
		return combinedLonLat;
	}

	public void setCombinedLonLat(String combinedLonLat) {
		this.combinedLonLat = combinedLonLat;
	}

	public String getLonLatSeparator() {
		return lonLatSeparator;
	}

	public void setLonLatSeparator(String lonLatSeparator) {
		this.lonLatSeparator = lonLatSeparator;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public ArrayList<String> getElements() {
		return elements;
	}

	public void setElements(ArrayList<String> elements) {
		this.elements = elements;
	}

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

}
