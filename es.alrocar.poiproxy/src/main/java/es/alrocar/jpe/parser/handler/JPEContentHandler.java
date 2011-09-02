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

/**
 * This interface is used to provide an event driven parser of a json or xml and
 * make a parser independent of the geometry model
 * 
 * @author albertoromeu
 * 
 */
public interface JPEContentHandler {

	/**
	 * Event thrown when a new feature collection has to be started
	 * 
	 * @return A new collection of features
	 */
	public Object startFeatureCollection();

	/**
	 * Event thrown when a feature collection is finished
	 * 
	 * @param featureCollection
	 *            The feature collection created after
	 *            {@link #startFeatureCollection()}
	 * @return The feature collection object
	 */
	public Object endFeatureCollection(Object featureCollection);

	/**
	 * Event thrown when a new feature has to be started
	 * 
	 * @return The feature
	 */
	public Object startFeature();

	/**
	 * Event thrown when a feature is finished
	 * 
	 * @param feature
	 *            The feature created after {@link #startFeature()}
	 * @return The feature object
	 */
	public Object endFeature(Object feature);

	/**
	 * Event thrown when a new point has to be started
	 * 
	 * @return The Point
	 */
	public Object startPoint();

	/**
	 * Adds the x coordinate to the point created after {@link #startPoint()}
	 * 
	 * @param x
	 *            The x coordinate
	 * @param point
	 *            The point object
	 * @return The point object
	 */
	public Object addXToPoint(double x, Object point);

	/**
	 * Adds the y coordinate to the point created after {@link #startPoint()}
	 * 
	 * @param y
	 *            The y coordinate
	 * @param point
	 *            The point object
	 * @return The point object
	 */
	public Object addYToPoint(double y, Object point);

	/**
	 * Event thrown when a point is finished
	 * 
	 * @param point
	 *            The point created after {@link #startPoint()}
	 * @return The point object
	 */
	public Object endPoint(Object point);

	/**
	 * Event thrown when a new attribute has to be added to the current feature
	 * 
	 * @param element
	 *            The attribute
	 * @param key
	 *            The attribute key
	 * @param feature
	 *            The feature
	 * @return The feature
	 */
	public Object addElementToFeature(String element, String key, Object feature);

	/**
	 * Event thrown when a feature has to be added to the current feature
	 * collection
	 * 
	 * @param featureCollection
	 *            The feature collection
	 * @param feature
	 *            The feature
	 * @return The feature collection
	 */
	public Object addFeatureToCollection(Object featureCollection,
			Object feature);

	/**
	 * Event thrown when the point has to be added to the feature
	 * 
	 * @param feature
	 *            The feature
	 * @param point
	 *            The point
	 * @return The feature
	 */
	public Object addPointToFeature(Object feature, Object point);

}
