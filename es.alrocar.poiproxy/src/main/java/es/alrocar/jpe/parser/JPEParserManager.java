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

import java.util.HashMap;

import es.alrocar.jpe.parser.exceptions.NoParserRegisteredException;

public class JPEParserManager {

	private static JPEParserManager instance;
	private HashMap<String, JPEParser> registeredFormats = new HashMap<String, JPEParser>();

	public static JPEParserManager getInstance() {
		if (instance == null) {
			instance = new JPEParserManager();
		}

		return instance;
	}

	public void registerJPEParser(JPEParser parser) {
		this.registeredFormats.put(parser.getFormat(), parser);
	}

	public JPEParser getJPEParser(String format)
			throws NoParserRegisteredException {
		JPEParser parser = this.registeredFormats.get(format);

		if (parser == null) {
			throw new NoParserRegisteredException(
					"No parser registered for format: " + format);
		}

		return parser;
	}

}
