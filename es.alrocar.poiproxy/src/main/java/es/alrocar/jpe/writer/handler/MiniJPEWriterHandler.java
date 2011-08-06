/* POIProxy
 *
 * Copyright (C) 2011 Alberto Romeu.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
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
 *   2011.
 *   author Alberto Romeu aromeu@prodevelop.es  
 *   
 */

package es.alrocar.jpe.writer.handler;

import java.io.IOException;
import java.util.Map;
import java.util.LinkedHashMap;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import es.alrocar.jpe.parser.handler.JPEContentHandler;

public class MiniJPEWriterHandler implements JPEContentHandler {

	private org.json.JSONObject featureCollection;
	private int featureCount = 0;
	private JSONArray features;
	

	public Object startFeatureCollection() {
		featureCount = 0;
		featureCollection = new org.json.JSONObject();
		
		features = new JSONArray();
//		featureCollection.put("features", new JSONArray());

		return featureCollection;
	}

	public Object endFeatureCollection(Object featureCollection) {
//		((JSONObject)featureCollection).put("type", "FeatureCollection");
		Map a = new LinkedHashMap();
		a.put("type", "FeatureCollection");
		a.put("features", features);
//		((JSONObject)featureCollection).put("features", features);
//		
//		((JSONObject) featureCollection).put("type", "FeatureCollection");
		
		this.featureCollection = new org.json.JSONObject(a);

		return this.featureCollection;
	}

	public Object startFeature() {
		Map feature = new LinkedHashMap();
//		JSONObject feature = new JSONObject();

		feature.put("type", "Feature");		
		feature.put("properties", new LinkedHashMap());

		return feature;
	}

	public Object endFeature(Object feature) {
		return feature;
	}

	public Object startPoint() {
		Map geometry = new LinkedHashMap();
		JSONArray coords = new JSONArray();
		coords.add(0);
		coords.add(0);

		geometry.put("type", "Point");
		geometry.put("coordinates", coords);

		return geometry;
	}

	public Object addXToPoint(double x, Object point) {

		((JSONArray) ((LinkedHashMap) point).get("coordinates")).set(0, x);

		return point;
	}

	public Object addYToPoint(double y, Object point) {

		((JSONArray) ((LinkedHashMap) point).get("coordinates")).set(1, y);

		return point;
	}

	public Object endPoint(Object point) {		
		return point;
	}

	public Object addElementToFeature(String element, String key, Object feature) {

		((Map) ((Map) feature).get("properties")).put(key,
				element);

		return feature;
	}

	public Object addFeatureToCollection(Object featureCollection,
			Object feature) {
		features.add(
				featureCount++, feature);

		return featureCollection;
	}

	public Object addPointToFeature(Object feature, Object point) {

		((Map) feature).put("geometry", point);

		return feature;
	}

	public String getGeoJSON() {
		if (this.featureCollection != null){
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
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
			
		return null;
	}

}
