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

package es.alrocar.jpe.parser.json;

import java.util.ArrayList;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import es.alrocar.jpe.parser.JPEParser;
import es.alrocar.jpe.parser.JPEParserFormatEnum;
import es.alrocar.jpe.parser.handler.JPEContentHandler;
import es.alrocar.jpe.parser.handler.MiniJPEContentHandler;
import es.alrocar.jpe.parser.handler.json.JSONSimpleContentHandler;
import es.alrocar.jpe.writer.handler.MiniJPEWriterHandler;
import es.alrocar.poiproxy.configuration.DescribeService;
import es.alrocar.poiproxy.proxy.LocalFilter;
import es.prodevelop.gvsig.mini.geom.impl.jts.JTSFeature;

/**
 * A JPEParser that parses a json document using
 * {@link JSONSimpleContentHandler} and converts the xml into GeoJSON through
 * {@link MiniJPEWriterHandler}
 * 
 * @author albertoromeu
 * 
 */
public class JSONJPEParser extends JPEParser {

	private JPEContentHandler contentHandler = new MiniJPEContentHandler();
	private MiniJPEWriterHandler writerHandler = new MiniJPEWriterHandler();
	private JSONParser parser = new JSONParser();
	private JSONSimpleContentHandler simpleContentHandler = new JSONSimpleContentHandler();

	/**
	 * {@inheritDoc}
	 */
	public ArrayList<JTSFeature> parse(String json, DescribeService service,
			LocalFilter filter) {
		simpleContentHandler.setJPEParseContentHandler(contentHandler);
		simpleContentHandler.setJPEWriteContentHandler(writerHandler);
		simpleContentHandler.setDescribeService(service);
		simpleContentHandler.setLocalFilter(filter);

		try {
			parser.parse(json, simpleContentHandler);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return simpleContentHandler.getResult();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getGeoJSON() {
		if (writerHandler != null) {
			return writerHandler.getGeoJSON();
		}

		return null;
	}

	@Override
	public String getFormat() {
		return JPEParserFormatEnum.JSON.format;
	}

}
