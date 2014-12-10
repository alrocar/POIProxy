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

package es.alrocar.poiproxy.proxy.event;

import java.util.List;

import es.alrocar.poiproxy.configuration.DescribeService;
import es.alrocar.poiproxy.proxy.POIProxy;
import es.alrocar.poiproxy.proxy.POIProxyListener;
import es.prodevelop.gvsig.mini.geom.Extent;
import es.prodevelop.gvsig.mini.geom.impl.jts.JTSFeature;

/**
 * An Event that {@link POIProxy} notifies to {@link POIProxyListener}
 * 
 * @author aromeu
 * 
 */
public class POIProxyEvent {

	private Extent extent;
	private POIProxyEventEnum type;
	private Integer z;
	private Integer y;
	private Integer x;
	private Double lon;
	private Double lat;
	private Double distance;
	private String query;

	private DescribeService describeService;

	private String parsedData;
	private List<JTSFeature> features;

	public POIProxyEvent(POIProxyEventEnum type, DescribeService service,
			Extent extent, Integer z, Integer y, Integer x, Double lon,
			Double lat, Double distance, String query, String parsedData,
			List<JTSFeature> features) {
		super();
		this.extent = extent;
		this.describeService = service;
		this.type = type;
		this.z = z;
		this.y = y;
		this.x = x;
		this.lon = lon;
		this.lat = lat;
		this.distance = distance;
		this.query = query;
		this.parsedData = parsedData;
		this.setFeatures(features);
	}

	public DescribeService getDescribeService() {
		return describeService;
	}

	public Extent getExtent() {
		return extent;
	}

	public POIProxyEventEnum getType() {
		return type;
	}

	public Integer getZ() {
		return z;
	}

	public Integer getY() {
		return y;
	}

	public Integer getX() {
		return x;
	}

	public Double getLon() {
		return lon;
	}

	public Double getLat() {
		return lat;
	}

	public Double getDistance() {
		return distance;
	}

	public String getQuery() {
		return query;
	}

	public String getParsedData() {
		return parsedData;
	}

	public List<JTSFeature> getFeatures() {
		return features;
	}

	public void setFeatures(List<JTSFeature> features) {
		this.features = features;
	}
}
