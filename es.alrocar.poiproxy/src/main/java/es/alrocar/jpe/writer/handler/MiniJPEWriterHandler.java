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

package es.alrocar.jpe.writer.handler;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.json.simple.JSONArray;

import es.alrocar.jpe.parser.handler.BaseContentHandler;
import es.alrocar.jpe.parser.handler.JPEContentHandler;
import es.alrocar.poiproxy.geotools.GeotoolsUtils;
import es.prodevelop.gvsig.mini.exceptions.BaseException;
import es.prodevelop.gvsig.mini.geom.impl.jts.JTSFeature;

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
	public Object endPoint(Object point, String from, String to) {
		JSONArray coords = ((JSONArray) ((LinkedHashMap) point)
				.get("coordinates"));

		try {
			double[] xy = GeotoolsUtils.transform(
					from,
					to,
					new double[] {
							Double.valueOf(String.valueOf(coords.get(0))),
							Double.valueOf(String.valueOf(coords.get(1))) });
			if (xy != null) {
				coords.set(0, xy[0]);
				coords.set(1, xy[1]);
				((LinkedHashMap) point).put("coordinates", coords);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

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
			return featureCollectionAsJSON(this.featureCollection);
		}

		return null;
	}

	private String featureCollectionAsJSON(org.json.JSONObject featureCollection) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);

		String originalJson = featureCollection.toString();

		JsonNode tree;
		try {
			tree = objectMapper.readTree(originalJson);
			return objectMapper.writeValueAsString(tree);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Creates a GeoJSON from a List of {@link JTSFeature}
	 * 
	 * @param list
	 * @return
	 */
	public String toJSON(List<JTSFeature> list) {
		Object fcJSON = this.startFeatureCollection();
		for (JTSFeature feature : list) {
			Object f = this.startFeature();
			Object p = this.startPoint();
			try {
				this.addXToPoint(feature.getGeometry().getGeometry()
						.getCentroid().getX(), p);
				this.addYToPoint(feature.getGeometry().getGeometry()
						.getCentroid().getY(), p);
			} catch (BaseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Iterator it = feature.getAttributes().keySet().iterator();
			while (it.hasNext()) {
				String key = it.next().toString();
				this.addElementToFeature(feature.getAttribute(key).value, key,
						f);
			}

			p = this.endPoint(p, null, null);
			this.addPointToFeature(f, p);
			this.endFeature(f);
			this.addFeatureToCollection(fcJSON, f);
		}

		fcJSON = this.endFeatureCollection(fcJSON);

		return this.featureCollectionAsJSON((org.json.JSONObject) fcJSON);
	}
}
