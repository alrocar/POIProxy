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

package es.alrocar.jpe.parser;

import java.util.ArrayList;

import org.geotools.geometry.jts.JTS;

import es.alrocar.poiproxy.configuration.DescribeService;
import es.prodevelop.gvsig.mini.geom.impl.jts.JTSFeature;

/**
 * An abstract class to parse an xml or json document and return a GeoJSON given
 * a {@link DescribeService} instance.
 * 
 * The idea is having a single parser that is configurable with a json document
 * that is parsed into a {@link DescribeService} instance
 * 
 * @author albertoromeu
 * 
 */
public abstract class JPEParser {

	/**
	 * The format of the document to parse is json
	 */
	public static String FORMAT_JSON = "json";

	/**
	 * The format of the document to parse is xml
	 */
	public static String FORMAT_XML = "xml";

	/**
	 * Parses a contentFile using a {@link DescribeService} instance
	 * 
	 * @param contentFile
	 *            The content of the file to parse into a String
	 * @param service
	 *            The DescribeService instance. THe
	 *            {@link DescribeService#getFeatureTypes()} will be used to get
	 *            some attributes to be parsed
	 * @return An array of {@link JTSFeature}
	 */
	public abstract ArrayList<JTSFeature> parse(String contentFile,
			DescribeService service);

	/**
	 * The GeoJSON document corresponding to the array of {@link JTS} returned
	 * by the {@link JPEParser#parse(String, DescribeService)} method
	 * 
	 * @return
	 */
	public abstract String getGeoJSON();

	/**
	 * The format that supports the parser
	 * 
	 * @return either {@link JPEParser#FORMAT_JSON} or
	 *         {@link JPEParser#FORMAT_XML}
	 */
	public abstract String getFormat();
}
