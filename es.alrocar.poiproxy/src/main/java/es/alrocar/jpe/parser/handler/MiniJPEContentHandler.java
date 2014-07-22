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

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

import es.alrocar.poiproxy.geotools.GeotoolsUtils;
import es.prodevelop.gvsig.mini.exceptions.BaseException;
import es.prodevelop.gvsig.mini.geom.impl.base.Point;
import es.prodevelop.gvsig.mini.geom.impl.jts.JTSFeature;
import es.prodevelop.gvsig.mini.geom.impl.jts.JTSGeometry;

/**
 * An implementation of {@link JPEContentHandler} that works with the geometries
 * model of gvSIG Mini that is based on the JTS library. The class used as a
 * feature is {@link JTSFeature}
 * 
 * @author albertoromeu
 * 
 */
public class MiniJPEContentHandler implements JPEContentHandler {

	ArrayList featureCollections = new ArrayList();

	/**
	 * {@inheritDoc}
	 */
	public Object startFeatureCollection() {
		featureCollections = new ArrayList();
		return new ArrayList();
	}

	/**
	 * {@inheritDoc}
	 */
	public Object endFeatureCollection(Object featureCollection) {
		this.featureCollections.add(featureCollection);
		return featureCollection;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object startFeature() {
		return new JTSFeature(null);
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
		Point p = new Point();
		return p;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object addXToPoint(double x, Object point) {
		((Point) point).setX(x);
		return point;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object addYToPoint(double y, Object point) {
		((Point) point).setY(y);
		return point;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object endPoint(Object point, String from, String to) {
		GeometryFactory factory = new GeometryFactory();
		Coordinate coord = new Coordinate();
		coord.x = ((Point) point).getX();
		coord.y = ((Point) point).getY();

		transform(from, to, coord);

		return factory.createPoint(coord);
	}

	private void transform(String from, String to, Coordinate coord) {
		double[] xy = GeotoolsUtils.transform(from, to, new double[] { coord.x,
				coord.y });
		if (xy != null && xy.length == 2) {
			coord.x = xy[0];
			coord.y = xy[1];
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Object addPointToFeature(Object feature, Object point) {
		((JTSFeature) feature).setGeometry(new JTSGeometry(
				(com.vividsolutions.jts.geom.Point) point));
		return feature;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object addElementToFeature(String element, String key, Object feature) {
		((JTSFeature) feature).addField(key, element, 0);
		return feature;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object addFeatureToCollection(Object featureCollection,
			Object feature) {
		JTSFeature feat = (JTSFeature) feature;
		try {
			if (feat.getGeometry().getGeometry().getCoordinate().x == 0
					&& feat.getGeometry().getGeometry().getCoordinate().y == 0) {
				return featureCollection;
			}
		} catch (BaseException e) {
			e.printStackTrace();
			return featureCollection;
		}
		((ArrayList) featureCollection).add(feature);
		return featureCollection;
	}
}
