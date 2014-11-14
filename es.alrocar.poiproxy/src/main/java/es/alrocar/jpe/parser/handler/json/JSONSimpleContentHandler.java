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

package es.alrocar.jpe.parser.handler.json;

import java.io.IOException;

import org.json.simple.parser.ParseException;

import es.alrocar.jpe.parser.handler.BaseContentHandler;

/**
 * An event driven JSON parser
 * 
 * @author albertoromeu
 * 
 */
public class JSONSimpleContentHandler extends BaseContentHandler implements
		org.json.simple.parser.ContentHandler {

	/**
	 * {@inheritDoc}
	 */
	public boolean endArray() throws ParseException, IOException {
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Internally calls {@link BaseContentHandler#end()}
	 */
	public void endJSON() throws ParseException, IOException {
		this.end();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean endObject() throws ParseException, IOException {
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean endObjectEntry() throws ParseException, IOException {
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Internally calls {@link BaseContentHandler#processValue(String)}
	 */
	public boolean primitive(Object arg0) throws ParseException, IOException {
		if (arg0 == null)
			return true;
		this.processValue(arg0.toString());
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean startArray() throws ParseException, IOException {
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Internally calls {@link BaseContentHandler#start()}
	 */
	public void startJSON() throws ParseException, IOException {
		start();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean startObject() throws ParseException, IOException {
		this.onStartObject();
		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Internally calls {@link BaseContentHandler#startNewElement(String)}
	 */
	public boolean startObjectEntry(String arg0) throws ParseException,
			IOException {
		this.startNewElement(arg0, null);
		return true;
	}
}
