package es.alrocar.jpe.parser.handler;

import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.parser.ParseException;

import es.alrocar.poiproxy.configuration.DescribeService;
import es.alrocar.poiproxy.configuration.FeatureType;

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

	public void end() {
		if (((ArrayList) this.featureCollection).size() == 0) {
			this.endNewElement();
		}
		contentHandler.endFeatureCollection(this.featureCollection);
		if (writerContentHandler != null)
			geoJSON = writerContentHandler.endFeatureCollection(geoJSON);
	}

	public void processValue(String arg0) {
		if (arg0 == null)
			return;
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
				return;
			}
		}

		if (currentKey.toString().compareToIgnoreCase(fType.getLon()) == 0) {
			if (writerContentHandler != null)
				writerContentHandler.addXToPoint(new Double(arg0.toString()),
						this.currentGeometryGeoJSON);
			contentHandler.addXToPoint(new Double(arg0.toString()),
					this.currentPoint);
			return;
		}

		if (currentKey.toString().compareToIgnoreCase(fType.getLat()) == 0) {
			if (writerContentHandler != null)
				writerContentHandler.addYToPoint(new Double(arg0.toString()),
						this.currentGeometryGeoJSON);
			contentHandler.addYToPoint(new Double(arg0.toString()),
					this.currentPoint);
			return;
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
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return;
	}

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

	public void startNewElement(String localName) {
		String arg0 = localName;
		this.currentKey = arg0;
		if (arg0.compareToIgnoreCase(this.currentFeatureType.getFeature()) == 0) {
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
