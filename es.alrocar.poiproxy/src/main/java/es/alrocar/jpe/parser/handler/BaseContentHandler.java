package es.alrocar.jpe.parser.handler;

import java.util.ArrayList;

import es.alrocar.poiproxy.configuration.DescribeService;
import es.alrocar.poiproxy.configuration.FeatureType;

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
		if (arg0 == null || this.currentFeatureGeoJSON == null)
			return;
		FeatureType fType = this.currentFeatureType;
		final int size = fType.getElements().size();

		// HACK for the special case when lon and lat came in an array
		if (processedLat && processedLon) {
			processedLat = false;
			processedLon = false;
		}

		for (int i = 0; i < size; i++) {
			if (fType.getElements().get(i)
					.compareTo(this.currentKey.toString()) == 0) {
				if (writerContentHandler != null)
					writerContentHandler.addElementToFeature(arg0.toString(),
							this.currentKey, this.currentFeatureGeoJSON);
				contentHandler.addElementToFeature(arg0.toString(),
						this.currentKey, this.currentFeature);
				return;
			}
		}

		if (currentKey.toString().compareTo(fType.getLon()) == 0
				&& !processedLon) {
			if (this.currentGeometryGeoJSON == null)
				this.currentGeometryGeoJSON = writerContentHandler.startPoint();
			if (writerContentHandler != null)
				writerContentHandler.addXToPoint(new Double(arg0.toString()),
						this.currentGeometryGeoJSON);
			contentHandler.addXToPoint(new Double(arg0.toString()),
					this.currentPoint);
			processedLon = true;
			return;
		}

		if (currentKey.toString().compareTo(fType.getLat()) == 0
				&& !processedLat) {
			if (this.currentGeometryGeoJSON == null)
				this.currentGeometryGeoJSON = writerContentHandler.startPoint();
			if (writerContentHandler != null)
				writerContentHandler.addYToPoint(new Double(arg0.toString()),
						this.currentGeometryGeoJSON);
			contentHandler.addYToPoint(new Double(arg0.toString()),
					this.currentPoint);
			processedLat = true;
			return;
		}

		try {
			if (fType.getCombinedLonLat() != null
					&& currentKey.toString().compareTo(
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
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return;
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
	 */
	public void startNewElement(String localName) {
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
	}

	/**
	 * Ends the current feature parsed, its point associated and adds the
	 * feature to the current feature collection by throwing the proper events
	 * of {@link JPEContentHandler}
	 */
	public void endNewElement() {
		if (this.currentFeature != null) {

			if (writerContentHandler != null) {
				this.currentFeatureGeoJSON = writerContentHandler
						.addPointToFeature(this.currentFeatureGeoJSON,
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
	}

}
