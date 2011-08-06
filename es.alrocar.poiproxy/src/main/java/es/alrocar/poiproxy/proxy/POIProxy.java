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

package es.alrocar.poiproxy.proxy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import es.alrocar.jpe.parser.JPEParser;
import es.alrocar.jpe.writer.GeoJSONWriter;
import es.alrocar.poiproxy.configuration.DescribeService;
import es.alrocar.poiproxy.configuration.ServiceConfigurationManager;
import es.alrocar.poiproxy.configuration.ServiceParams;
import es.prodevelop.geodetic.utils.conversion.ConversionCoords;
import es.prodevelop.gvsig.mini.geom.Extent;
import es.prodevelop.gvsig.mini.geom.impl.base.Point;
import es.prodevelop.gvsig.mini.geom.impl.jts.JTSFeature;
import es.prodevelop.gvsig.mini.projection.TileConversor;
import es.prodevelop.gvsig.mini.utiles.Calculator;
import es.prodevelop.gvsig.mobile.fmap.proj.CRSFactory;

public class POIProxy {

	public static String CACHE_DIR = "/var/lib/sp/cache";

	private static POIProxy proxy;
	private ServiceConfigurationManager serviceManager;
	private JPEParser jpeParser;
	private GeoJSONWriter geoJSONWriter;

	public static POIProxy getInstance() {
		if (proxy == null) {
			proxy = new POIProxy();
		}

		return proxy;
	}

	public void initialize() {
		// Registra todos los servicios disponibles
		serviceManager = new ServiceConfigurationManager();

		jpeParser = new JPEParser();
		geoJSONWriter = new GeoJSONWriter();
	}

	// TODO Allow several strategies
	// LAT LON
	// BBOX
	// TILES

	// AND DIFERENT REQUESTS
	// BROWSE
	// SEARCH WITH QUERY

	public String getPOIs(String id, int z, int x, int y,
			ArrayList optionalParams) throws Exception {
		DescribeService describeService = serviceManager
				.getServiceConfiguration(id);

		if (describeService == null) {
			StringBuffer error = new StringBuffer();
			error.append("The service with id: " + id + " is not registered");

			error.append("\n Available services are: ");

			Set<String> keys = serviceManager.getRegisteredConfigurations()
					.keySet();

			Iterator<String> it = keys.iterator();

			while (it.hasNext()) {
				error.append(it.next()).append(" ");
			}

			return error.toString();
		}

		// Buscar en cache el geoJSON y si est‡ llamar a onResponseReceived

		// si no est‡ en cache

		// construir la url
		String url = buildRequest(describeService, z, x, y, optionalParams);

		// hacer petici—n al servicio
		String json = doRequest(url);

		String geoJSON = this.onResponseReceived(json, describeService);

		return geoJSON;
	}

	public String buildRequest(DescribeService describeService, int z, int x,
			int y, ArrayList optionalParams) {
		ServiceParams params = new ServiceParams();

		Extent e1 = TileConversor.tileOSMMercatorBounds(x, y, z);		

		double[] minXY = ConversionCoords.reproject(e1.getMinX(), e1.getMinY(),
				CRSFactory.getCRS("EPSG:900913"),
				CRSFactory.getCRS("EPSG:4326"));
		double[] maxXY = ConversionCoords.reproject(e1.getMaxX(), e1.getMaxY(),
				CRSFactory.getCRS("EPSG:900913"),
				CRSFactory.getCRS("EPSG:4326"));

		e1.setMinX(minXY[0]);
		e1.setMinY(minXY[1]);
		e1.setMaxX(maxXY[0]);
		e1.setMaxY(maxXY[1]);
		
		double distanceMeters = this.getDistanceMeters(e1);

		params.putParam(ServiceParams.MINX, String.valueOf(minXY[0]));
		params.putParam(ServiceParams.MINY, String.valueOf(minXY[1]));
		params.putParam(ServiceParams.MAXX, String.valueOf(maxXY[0]));
		params.putParam(ServiceParams.MAXY, String.valueOf(maxXY[1]));

		params.putParam(ServiceParams.LON,
				String.valueOf(e1.getCenter().getX()));
		params.putParam(ServiceParams.LAT,
				String.valueOf(e1.getCenter().getY()));

		params.putParam(ServiceParams.FORMAT, "json");

		params.putParam(ServiceParams.DIST, String.valueOf(distanceMeters));
		params.putParam(ServiceParams.DISTKM, String.valueOf(distanceMeters/1000));
		params.putParam(ServiceParams.KEY, describeService.getApiKey());

		String url = describeService.getRequestTypes()
				.get(DescribeService.BROWSE_TYPE).getUrl();

		Set<String> keys = params.getParams().keySet();
		Iterator<String> it = keys.iterator();

		String key;
		while (it.hasNext()) {
			key = it.next();
			url = url.replaceAll(key, params.getValueForParam(key));
		}

		return url;
	}

	public String doRequest(String url) throws Exception {
		// hacer peticion en segundo plano
		Downloader d = new Downloader();
		System.out.println(url);
		d.downloadFromUrl(url, null);
		return new String(d.getData());
	}

	public String onResponseReceived(String json, DescribeService service) {
		ArrayList<JTSFeature> features = jpeParser.parse(json, service);
		String geoJSON = jpeParser.getGeoJSON();
		// Escribir en cache el geoJSON
		return geoJSON;
	}
	
	public double getDistanceMeters(Extent boundingBox) {
		return Calculator.latLonDist(boundingBox.getMinX(),
				boundingBox.getMinY(), boundingBox.getMaxX(),
				boundingBox.getMaxY());
	}

}
