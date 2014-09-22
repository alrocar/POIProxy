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

package es.alrocar.jpe.parser.handler.xml;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import es.alrocar.jpe.parser.handler.BaseContentHandler;

/**
 * An event driven XML parser using {@link org.xml.sax.ContentHandler}
 * 
 * @author albertoromeu
 * 
 */
public class XMLSimpleContentHandler extends BaseContentHandler implements
		ContentHandler {

	/**
	 * {@inheritDoc}
	 */
	public void setDocumentLocator(Locator locator) {
//		System.out.println("Hello from setDocumentLocator()!");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Internally calls {@link BaseContentHandler#start()}
	 */
	public void startDocument() throws SAXException {
		start();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Internally calls {@link BaseContentHandler#end()}
	 */
	public void endDocument() throws SAXException {
		this.end();
	}

	/**
	 * {@inheritDoc}
	 */
	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {
//		System.out.println("Hello from startPrefixMapping()!" + prefix + ", "
//				+ uri);
	}

	/**
	 * {@inheritDoc}
	 */
	public void endPrefixMapping(String prefix) throws SAXException {
//		System.out.println("Hello from endPrefixMapping()!" + prefix);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Internally calls {@link BaseContentHandler#startNewElement(String)}
	 */
	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {
//		System.out.println("Hello from startElement()!" + uri + ", "
//				+ localName + ", " + qName + ", " + atts.toString());
		this.startNewElement(localName, atts);
	}

	/**
	 * {@inheritDoc}
	 */
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
//		System.out.println("Hello from endElement()!" + uri + ", "
//				+ localName + ", " + qName);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Internally calls {@link BaseContentHandler#processValue(String)}
	 */
	public void characters(char ch[], int start, int length)
			throws SAXException {
		String arg0 = new String(ch);
		arg0 = arg0.substring(start, start + length);
//		System.out.println(arg0);

		if (arg0.startsWith("\n"))
			return;

		this.processValue(arg0);
	}

	public void ignorableWhitespace(char ch[], int start, int length)
			throws SAXException {
//		System.out.println("Hello from ignorableWhitespace()!" + ch.toString());
	}

	/**
	 * {@inheritDoc}
	 */
	public void processingInstruction(String target, String data)
			throws SAXException {
//		System.out.println("Hello from processingInstruction()!" + target
//				+ ", " + data);
	}

	/**
	 * {@inheritDoc}
	 */
	public void skippedEntity(String name) throws SAXException {
//		System.out.println("Hello from skippedEntity()!" + name);
	}
}

