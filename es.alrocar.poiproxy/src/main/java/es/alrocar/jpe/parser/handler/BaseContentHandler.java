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

package es.alrocar.jpe.parser.handler;

import java.util.ArrayList;

import org.xml.sax.Attributes;

import es.alrocar.jpe.parser.JPEParser;
import es.alrocar.poiproxy.configuration.DescribeService;
import es.alrocar.poiproxy.configuration.FeatureType;
import es.alrocar.poiproxy.proxy.LocalFilter;
import es.alrocar.utils.Utils;

/**
 * Base class that can be used by a json or xml content handler to throw the
 * events needed by {@link JPEContentHandler}. The idea is to use an event
 * driven json or xml parser, and through a {@link DescribeService} instance
 * send the proper events to the {@link JPEContentHandler}
 * 
 * @author albertoromeu
 * 
 */
public class BaseContentHandler {

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

	private boolean processedLon = false;
	private boolean processedLat = false;

	private LocalFilter localFilter;
	private boolean hasPassedLocalFilter = false;

	/**
	 * A {@link LocalFilter} instance to apply filters on the features to be
	 * parsed
	 * 
	 * @param localFilter
	 */
	public void setLocalFilter(LocalFilter localFilter) {
		this.localFilter = localFilter;
	}

	/**
	 * Sets an implementation of {@link JPEContentHandler} to load into memory
	 * the document that is going to be parsed
	 * 
	 * @param contentHandler
	 *            The {@link JPEContentHandler} implementation
	 */
	public void setJPEParseContentHandler(JPEContentHandler contentHandler) {
		this.contentHandler = contentHandler;
	}

	/**
	 * Sets an implementation of {@link JPEContentHandler} to write into GeoJSON
	 * format the document to be parsed
	 * 
	 * @param contentHandler
	 *            The {@link JPEContentHandler} that will write the GeoJSON
	 */
	public void setJPEWriteContentHandler(JPEContentHandler contentHandler) {
		this.writerContentHandler = contentHandler;
	}

	/**
	 * The DescribeService instance used to parse the document. The parser will
	 * parse only the attributes in the {@link FeatureType} object of the
	 * {@link DescribeService}
	 * 
	 * @param describeService
	 *            The {@link DescribeService}
	 */
	public void setDescribeService(DescribeService describeService) {
		this.service = describeService;
	}

	/**
	 * Returns the feature collection created by the {@link JPEContentHandler}
	 * set at {@link #setJPEParseContentHandler(JPEContentHandler)}
	 * 
	 * @return An ArrayList of features
	 */
	public ArrayList getResult() {
		return (ArrayList) this.featureCollection;
	}

	/**
	 * Calls the event {@link JPEContentHandler#endFeatureCollection(Object)} of
	 * both {@link JPEContentHandler} set through
	 * {@link #setJPEParseContentHandler(JPEContentHandler)} and
	 * {@link #setJPEWriteContentHandler(JPEContentHandler)}
	 */
	public void end() {
		if (((ArrayList) this.featureCollection).size() == 0) {
			this.endNewElement();
		}
		contentHandler.endFeatureCollection(this.featureCollection);
		if (writerContentHandler != null)
			geoJSON = writerContentHandler.endFeatureCollection(geoJSON);
	}

	/**
	 * This method is called after {@link #startNewElement(localName)} if the
	 * localName of {@link #startNewElement(localName)} is equals to any of the
	 * {@link FeatureType#getElements()}, {@link FeatureType#getLat())},
	 * {@link FeatureType#getLon())} or {@link FeatureType#getCombinedLonLat())}
	 * then the correspondant event of {@link JPEContentHandler} is thrownn
	 * 
	 * @param arg0
	 */
	public void processValue(String arg0) {
		if (arg0 == null || this.currentFeatureGeoJSON == null
				|| arg0.trim().isEmpty())
			return;
		FeatureType fType = this.currentFeatureType;
		final int size = fType.getElements().size();

		// HACK for the special case when lon and lat came in an array
		if (processedLat && processedLon) {
			processedLat = false;
			processedLon = false;
		}

		for (String destProp : fType.getElements().keySet()) {
			if (fType.getElements().get(destProp).getInput()
					.compareTo(this.currentKey.toString()) == 0) {
				checkValidAttribute(localFilter, arg0.toString());
				if (writerContentHandler != null)
					writerContentHandler.addElementToFeature(arg0.toString(),
							destProp, this.currentFeatureGeoJSON);
				contentHandler.addElementToFeature(arg0.toString(), destProp,
						this.currentFeature);
				return;
			}
		}

		if (currentKey.toString().compareTo(fType.getLon()) == 0
				&& !processedLon) {
			double lon = Utils
					.formatNumber(arg0.toString(),
							service.getDecimalSeparator(),
							service.getNumberSeparator());
			if (this.currentGeometryGeoJSON == null)
				this.currentGeometryGeoJSON = writerContentHandler.startPoint();
			if (writerContentHandler != null)
				writerContentHandler.addXToPoint(new Double(lon),
						this.currentGeometryGeoJSON);
			contentHandler.addXToPoint(new Double(lon), this.currentPoint);
			processedLon = true;
			return;
		}

		if (currentKey.toString().compareTo(fType.getLat()) == 0
				&& !processedLat) {
			double lat = Utils
					.formatNumber(arg0.toString(),
							service.getDecimalSeparator(),
							service.getNumberSeparator());
			if (this.currentGeometryGeoJSON == null)
				this.currentGeometryGeoJSON = writerContentHandler.startPoint();
			if (writerContentHandler != null)
				writerContentHandler.addYToPoint(new Double(lat),
						this.currentGeometryGeoJSON);
			contentHandler.addYToPoint(new Double(lat), this.currentPoint);
			processedLat = true;
			return;
		}

		try {
			if (fType.getCombinedLonLat() != null
					&& currentKey.toString().compareTo(
							fType.getCombinedLonLat()) == 0) {
				String[] latLon = arg0.toString().split(
						fType.getLonLatSeparator());

				double lon = Utils.formatNumber(latLon[0].toString(),
						service.getDecimalSeparator(),
						service.getNumberSeparator());
				double lat = Utils.formatNumber(latLon[1].toString(),
						service.getDecimalSeparator(),
						service.getNumberSeparator());

				if (writerContentHandler != null) {
					writerContentHandler.addYToPoint(lat,
							this.currentGeometryGeoJSON);
					writerContentHandler.addXToPoint(lon,
							this.currentGeometryGeoJSON);
				}

				contentHandler.addYToPoint(lat, this.currentPoint);
				contentHandler.addXToPoint(lon, this.currentPoint);
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return;
	}

	private boolean checkValidAttribute(LocalFilter localFilter,
			String attribute) {
		if (localFilter == null) {
			hasPassedLocalFilter = true;
		} else {
			hasPassedLocalFilter = localFilter.apply(attribute)
					|| hasPassedLocalFilter;
		}

		return hasPassedLocalFilter;
	}

	/**
	 * Calls {@link JPEContentHandler#startFeatureCollection()} of both
	 * {@link JPEContentHandler} registered
	 */
	public void start() {
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

	/**
	 * Compares the localname with the {@link FeatureType#getFeature()}
	 * attribute of the current {@link FeatureType} of the
	 * {@link DescribeService}
	 * 
	 * If the current tag being parsed is equals to
	 * {@link FeatureType#getFeature()} then the
	 * {@link JPEContentHandler#startFeature()} and
	 * {@link JPEContentHandler#startPoint()} are thrown
	 * 
	 * @param localName
	 *            The current tag name being parsed
	 * @param atts
	 *            Additional attributes
	 * @param atts
	 */
	public void startNewElement(String localName, Attributes atts) {
		String arg0 = localName;
		this.currentKey = arg0;
		if (arg0.compareTo(this.currentFeatureType.getFeature()) == 0) {
			endNewElement();
			if (writerContentHandler != null) {
				this.currentFeatureGeoJSON = writerContentHandler
						.startFeature();
				this.currentGeometryGeoJSON = writerContentHandler.startPoint();
			}

			this.currentFeature = contentHandler.startFeature();
			this.currentPoint = contentHandler.startPoint();
		}

		// FIXME improve the support for attributes
		if (atts != null) {
			int length = atts.getLength();
			for (int i = 0; i < length; i++) {
				String key = atts.getQName(i);
				String value = atts.getValue(i);
				this.currentKey = key;
				this.processValue(value);
			}
		}
	}

	/**
	 * Ends the current feature parsed, its point associated and adds the
	 * feature to the current feature collection by throwing the proper events
	 * of {@link JPEContentHandler}
	 */
	public void endNewElement() {
		if (this.currentFeature != null) {
			if (!hasPassedLocalFilter) {
				hasPassedLocalFilter = false;
				return;
			}

			if (writerContentHandler != null) {
				this.currentFeatureGeoJSON = this.fillCategories(
						writerContentHandler, this.currentFeatureGeoJSON,
						service);
				this.currentFeatureGeoJSON = this.fillService(
						writerContentHandler, this.currentFeatureGeoJSON,
						service);
				this.currentFeatureGeoJSON = writerContentHandler
						.addPointToFeature(this.currentFeatureGeoJSON,
								writerContentHandler.endPoint(
										this.currentGeometryGeoJSON,
										this.service.getSRS(),
										DescribeService.DEFAULT_SRS));
				this.currentFeatureGeoJSON = writerContentHandler
						.endFeature(this.currentFeatureGeoJSON);
				writerContentHandler.addFeatureToCollection(this.geoJSON,
						this.currentFeatureGeoJSON);
			}
			this.currentFeature = this.fillCategories(contentHandler,
					this.currentFeature, service);
			this.currentFeature = this.fillService(contentHandler,
					this.currentFeature, service);

			this.currentFeature = contentHandler
					.addPointToFeature(this.currentFeature, contentHandler
							.endPoint(this.currentPoint, this.service.getSRS(),
									DescribeService.DEFAULT_SRS));
			this.currentFeature = contentHandler
					.endFeature(this.currentFeature);
			contentHandler.addFeatureToCollection(this.featureCollection,
					this.currentFeature);
			hasPassedLocalFilter = false;
		}
	}

	public Object fillService(JPEContentHandler handler, Object feature,
			DescribeService service) {
		return handler.addElementToFeature(service.getId(), JPEParser.SERVICE,
				feature);
	}

	public Object fillCategories(JPEContentHandler handler, Object feature,
			DescribeService service) {
		String categories = service.getCategoriesAsString();

		return handler.addElementToFeature(categories, JPEParser.CATEGORIES,
				feature);
	}
}
