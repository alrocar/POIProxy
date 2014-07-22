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

package es.alrocar.jpe.parser.csv;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import es.alrocar.jpe.parser.DefaultJPEParser;
import es.alrocar.jpe.parser.JPEParser;
import es.alrocar.jpe.parser.JPEParserFormatEnum;
import es.alrocar.poiproxy.configuration.DescribeService;
import es.alrocar.poiproxy.proxy.LocalFilter;
import es.alrocar.utils.Utils;
import es.prodevelop.csv.CsvReader;
import es.prodevelop.gvsig.mini.geom.AttributePoint.Attribute;
import es.prodevelop.gvsig.mini.geom.impl.base.Point;
import es.prodevelop.gvsig.mini.geom.impl.jts.JTSFeature;

public class CSVParser extends DefaultJPEParser {

	@Override
	public ArrayList<JTSFeature> parse(String contentFile,
			DescribeService service, LocalFilter filter) {

		CsvReader csvReader = createReader(contentFile, service);
		boolean hasPassedFilter = false;
		try {
			csvReader.readHeaders();
			setCurrentFeatureType(service);
			fc = (ArrayList<JTSFeature>) contentHandler
					.startFeatureCollection();
			JTSFeature feature;
			Point point;
			String element;
			while (csvReader.readRecord()) {
				hasPassedFilter = false;
				feature = (JTSFeature) contentHandler.startFeature();
				point = (Point) contentHandler.startPoint();

				for (String destProp : this.currentFeatureType.getElements()
						.keySet()) {
					element = csvReader.get(this.currentFeatureType
							.getElements().get(destProp).getInput());
					if (filter == null) {
						hasPassedFilter = true;
					} else {
						hasPassedFilter = hasPassedFilter
								|| filter.apply(element);
					}

					addAttribute(element, feature, destProp);
				}

				setLatitude(Utils.formatNumber(
						csvReader.get(this.currentFeatureType.getLat()),
						service.getDecimalSeparator(),
						service.getNumberSeparator()), point);
				setLongitude(Utils.formatNumber(
						csvReader.get(this.currentFeatureType.getLon()),
						service.getDecimalSeparator(),
						service.getNumberSeparator()), point);

				contentHandler.addPointToFeature(feature, contentHandler
						.endPoint(point, service.getSRS(),
								DescribeService.DEFAULT_SRS));
				if (hasPassedFilter) {
					fillCategories(feature, service);
					fillService(feature, service);
					contentHandler.addFeatureToCollection(fc, feature);
				}
			}

			return (ArrayList) fc;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	private CsvReader createReader(String contentFile, DescribeService service) {
		InputStream is = new ByteArrayInputStream(contentFile.getBytes());

		CsvReader csvReader = new CsvReader(is, service.getCsvSeparator()
				.toCharArray()[0], Charset.forName(service.getEncoding()));
		return csvReader;
	}

	@Override
	public String getFormat() {
		return JPEParserFormatEnum.CSV.format;
	}

}
