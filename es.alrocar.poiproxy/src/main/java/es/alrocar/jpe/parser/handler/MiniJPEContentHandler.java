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

import java.util.ArrayList;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

import es.prodevelop.gvsig.mini.exceptions.BaseException;
import es.prodevelop.gvsig.mini.geom.impl.base.Point;
import es.prodevelop.gvsig.mini.geom.impl.jts.JTSFeature;
import es.prodevelop.gvsig.mini.geom.impl.jts.JTSGeometry;

public class MiniJPEContentHandler implements JPEContentHandler {

	ArrayList featureCollections = new ArrayList();

	public Object startFeatureCollection() {
		featureCollections = new ArrayList();
		return new ArrayList();
	}

	public Object endFeatureCollection(Object featureCollection) {
		this.featureCollections.add(featureCollection);
		return featureCollection;
	}

	public Object startFeature() {
		return new JTSFeature(null);
	}

	public Object endFeature(Object feature) {
		return feature;
	}

	public Object startPoint() {
		Point p = new Point();
		return p;
	}

	public Object addXToPoint(double x, Object point) {
		((Point) point).setX(x);
		return point;
	}

	public Object addYToPoint(double y, Object point) {
		((Point) point).setY(y);
		return point;
	}

	public Object endPoint(Object point) {
		GeometryFactory factory = new GeometryFactory();
		Coordinate coord = new Coordinate();
		coord.x = ((Point) point).getX();
		coord.y = ((Point) point).getY();
		return factory.createPoint(coord);
	}

	public Object addPointToFeature(Object feature, Object point) {
		((JTSFeature) feature).setGeometry(new JTSGeometry(
				(com.vividsolutions.jts.geom.Point) point));
		return feature;
	}

	public Object addElementToFeature(String element, String key, Object feature) {
		((JTSFeature) feature).addField(key, element, 0);
		return feature;
	}

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
