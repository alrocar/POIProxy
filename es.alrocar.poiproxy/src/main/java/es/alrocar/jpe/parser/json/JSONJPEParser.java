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

package es.alrocar.jpe.parser.json;

import java.util.ArrayList;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import es.alrocar.jpe.parser.JPEParser;
import es.alrocar.jpe.parser.handler.JPEContentHandler;
import es.alrocar.jpe.parser.handler.MiniJPEContentHandler;
import es.alrocar.jpe.parser.handler.json.JSONSimpleContentHandler;
import es.alrocar.jpe.parser.handler.xml.XMLSimpleContentHandler;
import es.alrocar.jpe.writer.handler.MiniJPEWriterHandler;
import es.alrocar.poiproxy.configuration.DescribeService;
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
	public ArrayList<JTSFeature> parse(String json, DescribeService service) {
		simpleContentHandler.setJPEParseContentHandler(contentHandler);
		simpleContentHandler.setJPEWriteContentHandler(writerHandler);
		simpleContentHandler.setDescribeService(service);

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
		return JPEParser.FORMAT_JSON;
	}

}
