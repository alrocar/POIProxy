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

import java.util.HashMap;

import es.alrocar.jpe.parser.exceptions.NoParserRegisteredException;

/**
 * A manager to register {@link JPEParser} implementations. The manager is a
 * singleton
 * 
 * @author albertoromeu
 * 
 */
public class JPEParserManager {

	private static JPEParserManager instance;
	private HashMap<String, JPEParser> registeredFormats = new HashMap<String, JPEParser>();

	/**
	 * A singleton
	 * 
	 * @return The {@link JPEParserManager} instance
	 */
	public static JPEParserManager getInstance() {
		if (instance == null) {
			instance = new JPEParserManager();
		}

		return instance;
	}

	/**
	 * Registers a {@link JPEParser}
	 * 
	 * @param parser
	 *            The {@link JPEParser}
	 */
	public void registerJPEParser(JPEParser parser) {
		this.registeredFormats.put(parser.getFormat(), parser);
	}

	/**
	 * Gets a {@link JPEParser} which {@link JPEParser#getFormat()} is equals to
	 * the format
	 * 
	 * @param format
	 *            The format to compare with {@link JPEParser#FORMAT_JSON} or
	 *            {@link JPEParser#FORMAT_XML}
	 * @return The {@link JPEParser}
	 * @throws NoParserRegisteredException
	 *             if no {@link JPEParser} with the format specified is
	 *             registerd into the {@link JPEParserManager}
	 */
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
