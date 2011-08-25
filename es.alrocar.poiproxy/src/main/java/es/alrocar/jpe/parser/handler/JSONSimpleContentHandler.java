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

package es.alrocar.jpe.parser.handler;

import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.parser.ParseException;

import es.alrocar.poiproxy.configuration.DescribeService;
import es.alrocar.poiproxy.configuration.FeatureType;

public class JSONSimpleContentHandler implements
		org.json.simple.parser.ContentHandler {

	private JPEContentHandler contentHandler;
	private JPEContentHandler writerContentHandler;
	private DescribeService service;

	private Object featureCollection;

	private Object currentFeature;
	private Object currentPoint;

	private FeatureType currentFeatureType;

	private String currentKey;

	private Object geoJSON;
	private Object currentFeatureGeoJSON;
	private Object currentGeometryGeoJSON;

	public void setJPEParseContentHandler(JPEContentHandler contentHandler) {
		this.contentHandler = contentHandler;
	}

	public void setJPEWriteContentHandler(JPEContentHandler contentHandler) {
		this.writerContentHandler = contentHandler;
	}

	public void setDescribeService(DescribeService describeService) {
		this.service = describeService;
	}

	public ArrayList getResult() {
		return (ArrayList) this.featureCollection;
	}

	public boolean endArray() throws ParseException, IOException {
		// TODO Auto-generated method stub
		return true;
	}

	public void endJSON() throws ParseException, IOException {
		contentHandler.endFeatureCollection(this.featureCollection);
		if (writerContentHandler != null)
			geoJSON = writerContentHandler.endFeatureCollection(geoJSON);
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
		FeatureType fType = this.currentFeatureType;
		final int size = fType.getElements().size();

		for (int i = 0; i < size; i++) {
			if (fType.getElements().get(i)
					.compareToIgnoreCase(this.currentKey.toString()) == 0) {
				if (writerContentHandler != null)
					writerContentHandler.addElementToFeature(arg0.toString(),
							this.currentKey, this.currentFeatureGeoJSON);
				contentHandler.addElementToFeature(arg0.toString(),
						this.currentKey, this.currentFeature);
				return true;
			}
		}

		if (currentKey.toString().compareToIgnoreCase(fType.getLon()) == 0) {
			if (writerContentHandler != null)
				writerContentHandler.addXToPoint(new Double(arg0.toString()),
						this.currentGeometryGeoJSON);
			contentHandler.addXToPoint(new Double(arg0.toString()),
					this.currentPoint);
			return true;
		}

		if (currentKey.toString().compareToIgnoreCase(fType.getLat()) == 0) {
			if (writerContentHandler != null)
				writerContentHandler.addYToPoint(new Double(arg0.toString()),
						this.currentGeometryGeoJSON);
			contentHandler.addYToPoint(new Double(arg0.toString()),
					this.currentPoint);
			return true;
		}

		try {
			if (fType.getCombinedLonLat() != null
					&& currentKey.toString().compareToIgnoreCase(
							fType.getCombinedLonLat()) == 0) {
				String[] latLon = arg0.toString().split(
						fType.getLonLatSeparator());

				double lat = Double.valueOf(latLon[0]);
				double lon = Double.valueOf(latLon[1]);
				if (writerContentHandler != null) {
					writerContentHandler.addYToPoint(lat,
							this.currentGeometryGeoJSON);
					writerContentHandler.addXToPoint(lon,
							this.currentGeometryGeoJSON);
				}

				contentHandler.addYToPoint(lat, this.currentPoint);
				contentHandler.addXToPoint(lon, this.currentPoint);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	public boolean startArray() throws ParseException, IOException {
		// TODO Auto-generated method stub
		return true;
	}

	public void startJSON() throws ParseException, IOException {
		featureCollection = null;
		currentFeature = null;
		currentPoint = null;

		geoJSON = null;
		currentFeatureGeoJSON = null;
		currentGeometryGeoJSON = null;

		if (writerContentHandler != null)
			this.geoJSON = writerContentHandler.startFeatureCollection();
		featureCollection = contentHandler.startFeatureCollection();
		this.currentFeatureType = this.service.getFeatureTypes().get(
				this.service.getType());
	}

	public boolean startObject() throws ParseException, IOException {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean startObjectEntry(String arg0) throws ParseException,
			IOException {
		this.currentKey = arg0;
		if (arg0.compareToIgnoreCase(this.currentFeatureType.getFeature()) == 0) {
			if (this.currentFeature != null) {

				if (writerContentHandler != null) {
					this.currentFeatureGeoJSON = writerContentHandler
							.addPointToFeature(
									this.currentFeatureGeoJSON,
									writerContentHandler
											.endPoint(this.currentGeometryGeoJSON));
					this.currentFeatureGeoJSON = writerContentHandler
							.endFeature(this.currentFeatureGeoJSON);
					writerContentHandler.addFeatureToCollection(this.geoJSON,
							this.currentFeatureGeoJSON);
				}
				this.currentFeature = contentHandler.addPointToFeature(
						this.currentFeature,
						contentHandler.endPoint(this.currentPoint));
				this.currentFeature = contentHandler
						.endFeature(this.currentFeature);
				contentHandler.addFeatureToCollection(this.featureCollection,
						this.currentFeature);
			}
			if (writerContentHandler != null) {
				this.currentFeatureGeoJSON = writerContentHandler
						.startFeature();
				this.currentGeometryGeoJSON = writerContentHandler.startPoint();
			}

			this.currentFeature = contentHandler.startFeature();
			this.currentPoint = contentHandler.startPoint();
		}
		return true;
	}
}
