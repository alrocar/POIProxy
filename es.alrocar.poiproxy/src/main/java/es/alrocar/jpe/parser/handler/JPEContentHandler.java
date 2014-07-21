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
	 * Event thrown when a point is finished. If from and to parameters are
	 * passed then transforms the coordinates of the point
	 * 
	 * @param point
	 *            The point created after {@link #startPoint()}
	 * @param from
	 *            The EPSG code of the source SRS
	 * @param to
	 *            The EPSG code of the destination SRS
	 * @return The point object
	 */
	public Object endPoint(Object point, String from, String to);

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
