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

import java.io.IOException;
import java.util.ArrayList;

import es.alrocar.jpe.parser.handler.JPEContentHandler;
import es.alrocar.jpe.parser.handler.MiniJPEContentHandler;
import es.alrocar.jpe.writer.handler.MiniJPEWriterHandler;
import es.alrocar.poiproxy.configuration.DescribeService;
import es.alrocar.poiproxy.configuration.FeatureType;
import es.prodevelop.gvsig.mini.geom.AttributePoint.Attribute;
import es.prodevelop.gvsig.mini.geom.impl.base.Point;
import es.prodevelop.gvsig.mini.geom.impl.jts.JTSFeature;

public abstract class DefaultJPEParser extends JPEParser {

	protected JPEContentHandler contentHandler = new MiniJPEContentHandler();
	protected MiniJPEWriterHandler writerHandler = new MiniJPEWriterHandler();
	protected FeatureType currentFeatureType;
	protected ArrayList<JTSFeature> fc;

	protected void addAttribute(String value, JTSFeature feature, String element)
			throws IOException {
		contentHandler.addElementToFeature(value, element, feature);
	}

	protected void setLongitude(double lon, Point point) throws IOException {
		point.setX(lon);
	}

	protected void setLatitude(double lat, Point point) throws IOException {
		point.setY(lat);
	}

	protected void setCurrentFeatureType(DescribeService service) {
		this.currentFeatureType = service.getFeatureTypes().get(
				service.getType());
	}

	@Override
	public String getGeoJSON() {
		if (writerHandler != null) {
			return writerHandler.toJSON(fc);
		}

		return null;
	}

	public void fillService(JTSFeature feature, DescribeService service) {
		feature.addField(JPEParser.SERVICE, service.getId(),
				Attribute.TYPE_STRING);
	}

	public void fillCategories(JTSFeature feature, DescribeService service) {
		String categories = service.getCategoriesAsString();

		feature.addField(JPEParser.CATEGORIES, categories,
				Attribute.TYPE_STRING);
	}
}
