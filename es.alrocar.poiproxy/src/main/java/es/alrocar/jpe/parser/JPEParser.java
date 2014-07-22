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

package es.alrocar.jpe.parser;

import java.util.ArrayList;

import org.geotools.geometry.jts.JTS;

import es.alrocar.poiproxy.configuration.DescribeService;
import es.alrocar.poiproxy.geotools.GeotoolsUtils;
import es.alrocar.poiproxy.proxy.LocalFilter;
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
	
	public static final String CATEGORIES = "px_categories";
	public static final String SERVICE = "px_service";

	/**
	 * Parses a contentFile using a {@link DescribeService} instance
	 * 
	 * @param contentFile
	 *            The content of the file to parse into a String
	 * @param service
	 *            The DescribeService instance. THe
	 *            {@link DescribeService#getFeatureTypes()} will be used to get
	 *            some attributes to be parsed
	 * @param localFilter
	 *            A LocalFilter instance to perform a filter operation in the
	 *            contentFile
	 * @return An array of {@link JTSFeature}
	 */
	public abstract ArrayList<JTSFeature> parse(String contentFile,
			DescribeService service, LocalFilter localFilter);

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

	/**
	 * Transform a pair of coordinates from a SRS to another expressed as EPSG
	 * codes
	 * 
	 * @param from
	 *            The EPSG code of the source SRS
	 * @param to
	 *            The EPSG code of the destination SRS
	 * @param xy
	 *            The pair of coordinates (x,y) or (lon,lat)
	 * @return
	 */
	public double[] transform(String from, String to, double[] xy) {
		return GeotoolsUtils.transform(from, to, xy);
	}
}
