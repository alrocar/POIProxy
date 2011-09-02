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
		System.out.println("Hello from setDocumentLocator()!");
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
		System.out.println("Hello from startPrefixMapping()!" + prefix + ", "
				+ uri);
	}

	/**
	 * {@inheritDoc}
	 */
	public void endPrefixMapping(String prefix) throws SAXException {
		System.out.println("Hello from endPrefixMapping()!" + prefix);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Internally calls {@link BaseContentHandler#startNewElement(String)}
	 */
	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {
		this.startNewElement(localName);
	}

	/**
	 * {@inheritDoc}
	 */
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// this.endElement(localName);
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

		if (arg0.startsWith("\n"))
			return;

		this.processValue(arg0);
	}

	public void ignorableWhitespace(char ch[], int start, int length)
			throws SAXException {
		System.out.println("Hello from ignorableWhitespace()!" + ch.toString());
	}

	/**
	 * {@inheritDoc}
	 */
	public void processingInstruction(String target, String data)
			throws SAXException {
		System.out.println("Hello from processingInstruction()!" + target
				+ ", " + data);
	}

	/**
	 * {@inheritDoc}
	 */
	public void skippedEntity(String name) throws SAXException {
		System.out.println("Hello from skippedEntity()!" + name);
	}
}
