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

package es.alrocar.jpe.parser.kml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.geotools.kml.v22.KML;
import org.geotools.kml.v22.KMLConfiguration;
import org.geotools.xml.StreamingParser;
import org.opengis.feature.simple.SimpleFeature;
import org.xml.sax.SAXException;

import com.vividsolutions.jts.geom.Geometry;

import es.alrocar.jpe.parser.DefaultJPEParser;
import es.alrocar.jpe.parser.JPEParserFormatEnum;
import es.alrocar.poiproxy.configuration.DescribeService;
import es.alrocar.poiproxy.proxy.LocalFilter;
import es.prodevelop.gvsig.mini.geom.impl.base.Point;
import es.prodevelop.gvsig.mini.geom.impl.jts.JTSFeature;

@SuppressWarnings("deprecation")
public class KMLParser extends DefaultJPEParser {

	@SuppressWarnings({ "deprecation" })
	@Override
	public ArrayList<JTSFeature> parse(String contentFile,
			DescribeService service, LocalFilter filter) {
		InputStream inputStream = new ByteArrayInputStream(
				contentFile.getBytes());

		fc = (ArrayList<JTSFeature>) contentHandler.startFeatureCollection();
		this.setCurrentFeatureType(service);

		JTSFeature feature;
		Point point;

		StreamingParser parser;

		try {
			KMLConfiguration configuration = new KMLConfiguration();
			parser = new StreamingParser(configuration, inputStream,
					KML.Placemark);

			@SuppressWarnings("unused")
			SimpleFeature f = null;
			boolean hasPassedFilter = false;
			String element;
			while ((f = (SimpleFeature) parser.parse()) != null) {
				hasPassedFilter = false;
				feature = (JTSFeature) contentHandler.startFeature();
				point = (Point) contentHandler.startPoint();

				for (String destProp : this.currentFeatureType.getElements()
						.keySet()) {
					element = this.currentFeatureType.getElements()
							.get(destProp).getInput();
					String attribute = (String) f.getAttribute(element);
					if (attribute == null) {
						Set<Entry<Object, Object>> data = f.getUserData()
								.entrySet();
						Iterator<Entry<Object, Object>> it = data.iterator();

						while (it.hasNext()) {
							try {
								Entry<Object, Object> extendedData = it.next();
								if (extendedData.getKey().equals(element)) {
									attribute = extendedData.getValue()
											.toString();
									break;
								} else {
									String[] parts = extendedData.getValue()
											.toString().split(",");
									for (String part : parts) {
										String[] aa = part.trim().split("=");
										if (aa != null && aa.length == 2) {
											if (aa[0].trim().indexOf(element) != -1) {
												attribute = aa[1].trim();
											}
										}
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}

					if (attribute != null) {
						try {
							if (filter == null) {
								hasPassedFilter = true;
							} else {
								hasPassedFilter = hasPassedFilter
										|| filter.apply(attribute);
							}
							addAttribute(attribute, feature, destProp);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				Geometry geom = (Geometry) f.getDefaultGeometry();
				try {
					this.setLatitude(geom.getCentroid().getY(), point);
					this.setLongitude(geom.getCentroid().getX(), point);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				contentHandler.addPointToFeature(feature, contentHandler
						.endPoint(point, service.getSRS(),
								DescribeService.DEFAULT_SRS));
				if (hasPassedFilter) {
					fillCategories(feature, service);
					fillService(feature, service);
					contentHandler.addFeatureToCollection(fc, feature);
				}
			}
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return (ArrayList) fc;
	}

	@Override
	public String getFormat() {
		return JPEParserFormatEnum.KML.format;
	}
}
