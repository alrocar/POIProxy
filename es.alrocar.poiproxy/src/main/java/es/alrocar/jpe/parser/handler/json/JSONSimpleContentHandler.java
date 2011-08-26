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

package es.alrocar.jpe.parser.handler.json;

import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.parser.ParseException;

import es.alrocar.jpe.parser.handler.BaseContentHandler;
import es.alrocar.jpe.parser.handler.JPEContentHandler;
import es.alrocar.poiproxy.configuration.DescribeService;
import es.alrocar.poiproxy.configuration.FeatureType;

public class JSONSimpleContentHandler extends BaseContentHandler implements
		org.json.simple.parser.ContentHandler {

	public boolean endArray() throws ParseException, IOException {
		// TODO Auto-generated method stub
		return true;
	}

	public void endJSON() throws ParseException, IOException {
		this.end();
	}

	public boolean endObject() throws ParseException, IOException {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean endObjectEntry() throws ParseException, IOException {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean primitive(Object arg0) throws ParseException, IOException {
		if (arg0 == null)
			return true;
		this.processValue(arg0.toString());
		return true;
	}

	public boolean startArray() throws ParseException, IOException {
		// TODO Auto-generated method stub
		return true;
	}

	public void startJSON() throws ParseException, IOException {
		start();
	}

	public boolean startObject() throws ParseException, IOException {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean startObjectEntry(String arg0) throws ParseException,
			IOException {
		this.startNewElement(arg0);
		return true;
	}
}
