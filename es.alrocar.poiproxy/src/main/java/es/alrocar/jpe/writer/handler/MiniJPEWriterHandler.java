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

package es.alrocar.jpe.writer.handler;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.json.simple.JSONArray;

import es.alrocar.jpe.parser.handler.BaseContentHandler;
import es.alrocar.jpe.parser.handler.JPEContentHandler;

/**
 * A {@link JPEContentHandler} used to write a GeoJSON document on the fly while
 * an xml or json document is being parsed. As the source file is being parsed a
 * {@link BaseContentHandler} that is sending the events can have several
 * {@link JPEContentHandler}, one of them can load the document into an array of
 * features, and other (this) can write a GeoJSON on the fly as the events are
 * flowing
 * 
 * @author albertoromeu
 * 
 */
public class MiniJPEWriterHandler implements JPEContentHandler {

	private org.json.JSONObject featureCollection;
	private int featureCount = 0;
	private JSONArray features;

	/**
	 * {@inheritDoc}
	 */
	public Object startFeatureCollection() {
		featureCount = 0;
		featureCollection = new org.json.JSONObject();

		features = new JSONArray();
		// featureCollection.put("features", new JSONArray());

		return featureCollection;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object endFeatureCollection(Object featureCollection) {
		// ((JSONObject)featureCollection).put("type", "FeatureCollection");
		Map a = new LinkedHashMap();
		a.put("type", "FeatureCollection");
		a.put("features", features);
		// ((JSONObject)featureCollection).put("features", features);
		//
		// ((JSONObject) featureCollection).put("type", "FeatureCollection");

		this.featureCollection = new org.json.JSONObject(a);

		return this.featureCollection;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object startFeature() {
		Map feature = new LinkedHashMap();
		// JSONObject feature = new JSONObject();

		feature.put("type", "Feature");
		feature.put("properties", new LinkedHashMap());

		return feature;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object endFeature(Object feature) {
		return feature;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object startPoint() {
		Map geometry = new LinkedHashMap();
		JSONArray coords = new JSONArray();
		coords.add(0);
		coords.add(0);

		geometry.put("type", "Point");
		geometry.put("coordinates", coords);

		return geometry;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object addXToPoint(double x, Object point) {

		((JSONArray) ((LinkedHashMap) point).get("coordinates")).set(0, x);

		return point;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object addYToPoint(double y, Object point) {

		((JSONArray) ((LinkedHashMap) point).get("coordinates")).set(1, y);

		return point;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object endPoint(Object point) {
		return point;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object addElementToFeature(String element, String key, Object feature) {

		((Map) ((Map) feature).get("properties")).put(key, element);

		return feature;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object addFeatureToCollection(Object featureCollection,
			Object feature) {
		features.add(featureCount++, feature);

		return featureCollection;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object addPointToFeature(Object feature, Object point) {

		((Map) feature).put("geometry", point);

		return feature;
	}

	/**
	 * Indents and returns the GeoJSON document built
	 * 
	 * @return The GeoJSON document
	 */
	public String getGeoJSON() {
		if (this.featureCollection != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(SerializationConfig.Feature.INDENT_OUTPUT,
					true);

			String originalJson = featureCollection.toString();
			JsonNode tree;
			try {
				tree = objectMapper.readTree(originalJson);
				return objectMapper.writeValueAsString(tree);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return null;
	}

}
