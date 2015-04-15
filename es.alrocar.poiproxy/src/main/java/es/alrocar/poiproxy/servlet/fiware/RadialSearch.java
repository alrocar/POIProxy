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
 * @author Alberto Romeu Carrasco aromeu@prodevelop.es
 */

package es.alrocar.poiproxy.servlet.fiware;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Parameter;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Geometry;

import es.alrocar.poiproxy.configuration.ParamEnum;
import es.alrocar.poiproxy.fiware.poidp.schema.POI;
import es.alrocar.poiproxy.fiware.poidp.schema.POISet;
import es.alrocar.poiproxy.fiware.poidp.schema.fw_media_entity;
import es.alrocar.poiproxy.fiware.poidp.schema.utils.IntString;
import es.alrocar.poiproxy.fiware.poidp.schema.utils.IntUri;
import es.alrocar.poiproxy.fiware.poidp.schema.utils.Location;
import es.alrocar.poiproxy.fiware.poidp.schema.utils.Source;
import es.alrocar.poiproxy.proxy.POIProxy;
import es.alrocar.poiproxy.servlet.BrowseQueryServerResource;
import es.prodevelop.gvsig.mini.geom.impl.jts.JTSFeature;

public class RadialSearch extends BrowseQueryServerResource {

	private final static Logger log = LoggerFactory
			.getLogger(RadialSearch.class);

	public RadialSearch() {
		super();
	}

	public RadialSearch(Context context, Request request, Response response) {
		getVariants().add(new Variant(MediaType.TEXT_PLAIN));
	}

	@Override
	protected Representation get() throws ResourceException {
		Request r = this.getRequest();
		Form form = r.getResourceRef().getQueryAsForm();
		HashMap<String, String> params = new HashMap<String, String>();
		for (Parameter parameter : form) {
			params.put(parameter.getName(), parameter.getValue());
		}

		POIProxy proxy = POIProxy.asSingleton();

		List<JTSFeature> features;
		String geoJSON = "";
		try {
			features = proxy.getFeatures(params.get(ParamEnum.SERVICE.name),
					Double.valueOf(params.get(ParamEnum.LON.name)),
					Double.valueOf(params.get(ParamEnum.LAT.name)),
					Double.valueOf(params.get(ParamEnum.RADIUS.name)),
					this.extractParams(params));

			geoJSON = this.adapt(features);
		} catch (Exception e) {
			throw new ResourceException(
					Status.SERVER_ERROR_INTERNAL,
					"An unexpected error ocurred, please contact the administrator \n\n. Probably the service parameter you provided is not a valid service. You are accessing the radial_search service, check that your URL is of the type '/radial_search?service=panoramio&lon=1&lat=1&radius=10&callback=whatever'"
							+ " - " + e.getMessage());
		}

		String callback = params.get(ParamEnum.CALLBACK.name);

		if (callback == null) {
			return new StringRepresentation(geoJSON, MediaType.APPLICATION_JSON);
		} else if (callback.compareTo("?") == 0) {
			return new StringRepresentation("poiproxy(" + geoJSON + ");",
					MediaType.TEXT_JAVASCRIPT);
		} else {
			return new StringRepresentation(callback + "(" + geoJSON + ");",
					MediaType.TEXT_JAVASCRIPT);
		}
	}

	@SuppressWarnings("finally")
	private String adapt(List<JTSFeature> features) {
		POISet poiset = new POISet();

		POI poi;
		for (JTSFeature feature : features) {
			poi = new POI();
			try {
				poi.fw_core.setName(feature.getAttribute("name").value);
			} catch (Exception e) {
				log.warn("adapt name", e);
				poi.fw_core.setName(null);
			}

			try {
				poi.fw_core
						.setCategory(feature.getAttribute("px_categories").value);
			} catch (Exception e) {
				log.warn("adapt category", e);
				poi.fw_core.setCategory(null);
			}

			try {
				poi.fw_core.setDescription(new IntString(feature
						.getAttribute("description").value));
			} catch (Exception e) {
				log.warn("adapt description", e);
				poi.fw_core.setDescription(null);
			}

			try {
				poi.fw_core
						.setThumbnail(feature.getAttribute("thumbnail").value);
			} catch (Exception e) {
				log.warn("adapt thumbnail", e);
				poi.fw_core.setThumbnail(null);
			}

			try {
				poi.fw_core.setGeometry(feature.getGeometry().getGeometry()
						.toString());
			} catch (Exception e) {
				log.warn("adapt geometry", e);
			}

			try {
				poi.fw_core
						.setUrl(new IntUri(feature.getAttribute("url").value));
			} catch (Exception e) {
				log.warn("adapt url", e);
				poi.fw_core.setUrl(null);
			}

			try {
				Geometry geom = feature.getGeometry().getGeometry();
				double lon = geom.getCentroid().getX();
				double lat = geom.getCentroid().getY();

				Location location = new Location(lon, lat, 0);

				poi.fw_core.setLocation(location);
			} catch (Exception e) {
				log.warn("adapt location", e);
			}

			try {
				Source source = new Source();
				source.setName(feature.getAttribute("px_service").value);
				poi.fw_core.setSource(source);
			} catch (Exception e) {
				log.warn("adapt source", e);
				poi.fw_core.setSource(null);
			}

			try {
				fw_media_entity mediaEntity = new fw_media_entity();
				mediaEntity.setThumbnail(feature.getAttribute("image").value);
				mediaEntity.setShort_label(null);
				mediaEntity.setCaption(null);
				mediaEntity.setDescription(null);
				mediaEntity.setCopyright(null);
				mediaEntity.setType("photo");
				poi.fw_media.getEntities().add(mediaEntity);
			} catch (Exception e) {
				log.warn("adapt media photo", e);
			}

			try {
				fw_media_entity mediaEntity = new fw_media_entity();
				mediaEntity.setThumbnail(feature.getAttribute("video").value);
				mediaEntity.setShort_label(null);
				mediaEntity.setCaption(null);
				mediaEntity.setDescription(null);
				mediaEntity.setCopyright(null);
				mediaEntity.setType("video");
				poi.fw_media.getEntities().add(mediaEntity);
			} catch (Exception e) {
				log.warn("adapt media video", e);
			}

			String ID = new Date().getTime() + "";
			try {
				ID = feature.getAttribute("id").value;
			} catch (Exception e) {
				log.warn("adapt ID", e);
			}

			addProperties(poi, feature);

			poi.fw_contact = null;
			poi.fw_relationships = null;
			poi.fw_time = null;
			poi.fw_xml3d = null;
			poi.fw_marker = null;
			poi.fw_core.setLabel(null);
			poi.fw_core.setShort_name(null);
			poi.fw_core.setLast_update(null);

			poiset.put(ID, poi);
		}

		ObjectMapper mapper = new ObjectMapper();
		String out = "";
		try {
			out = poiset.asJSON();
			mapper.defaultPrettyPrintingWriter().writeValueAsString(poiset);
		} catch (JsonGenerationException e) {
			log.warn("error while encoding response", e);
			out = poiset.asJSON();
		} catch (JsonMappingException e) {
			log.warn("error while encoding response", e);
			out = poiset.asJSON();
		} catch (IOException e) {
			log.warn("error while encoding response", e);
			out = poiset.asJSON();
		} finally {
			return out;
		}
	}

	private void addProperties(POI poi, JTSFeature feature) {
		boolean added = false;

		Iterator it = feature.getAttributes().keySet().iterator();

		String key;
		while (it.hasNext()) {
			try {
				key = it.next().toString();
				if (this.isAdditional(key)) {
					poi.fw_core.properties.put(key,
							feature.getAttribute(key).value);
					added = true;
				}
			} catch (Exception ignore) {

			}
		}

		if (!added) {
			poi.fw_core.properties = null;
		}
	}

	private boolean isAdditional(String key) {
		return key.compareToIgnoreCase("name") != 0
				&& key.compareToIgnoreCase("id") != 0
				&& key.compareToIgnoreCase("px_categories") != 0
				&& key.compareToIgnoreCase("description") != 0
				&& key.compareToIgnoreCase("thumbnail") != 0
				&& key.compareToIgnoreCase("url") != 0
				&& key.compareToIgnoreCase("px_service") != 0
				&& key.compareToIgnoreCase("image") != 0;
	}
}