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

package es.alrocar.poiproxy.proxy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import es.alrocar.jpe.parser.JPEParser;
import es.alrocar.jpe.parser.JPEParserManager;
import es.alrocar.jpe.parser.exceptions.NoParserRegisteredException;
import es.alrocar.jpe.parser.json.JSONJPEParser;
import es.alrocar.jpe.parser.xml.XMLJPEParser;
import es.alrocar.jpe.writer.GeoJSONWriter;
import es.alrocar.poiproxy.configuration.DescribeService;
import es.alrocar.poiproxy.configuration.Param;
import es.alrocar.poiproxy.configuration.ServiceConfigurationManager;
import es.alrocar.poiproxy.configuration.ServiceParams;
import es.alrocar.poiproxy.proxy.utiles.Calculator;
import es.prodevelop.geodetic.utils.conversion.ConversionCoords;
import es.prodevelop.gvsig.mini.geom.Extent;
import es.prodevelop.gvsig.mini.geom.impl.jts.JTSFeature;
import es.prodevelop.gvsig.mini.projection.TileConversor;
import es.prodevelop.gvsig.mobile.fmap.proj.CRSFactory;

/**
 * The main entry point of the library. This class has methods to make a request
 * to a service, parse the response and return the GeoJSON result
 * 
 * @author albertoromeu
 * 
 */
public class POIProxy {

	public static String CACHE_DIR = "/var/lib/sp/cache";

	private static POIProxy proxy;
	private ServiceConfigurationManager serviceManager;
	private GeoJSONWriter geoJSONWriter;

	/**
	 * A singleton
	 * 
	 * @return The {@link POIProxy} instance
	 */
	public static POIProxy getInstance() {
		if (proxy == null) {
			proxy = new POIProxy();
		}

		return proxy;
	}

	/**
	 * Instantiates the {@link ServiceConfigurationManager} that loads the
	 * services available at
	 * {@link ServiceConfigurationManager#CONFIGURATION_DIR}
	 * 
	 * Registers a {@link JSONJPEParser} and a {@link XMLJPEParser} to allow
	 * parsing either json or xml depending on the format response of the
	 * registered services
	 * 
	 * And finally instantiates a {@link GeoJSONWriter} that will return the
	 * GeoJSON document
	 */
	public void initialize() {
		// Registra todos los servicios disponibles
		serviceManager = new ServiceConfigurationManager();

		JPEParserManager.getInstance().registerJPEParser(new JSONJPEParser());
		JPEParserManager.getInstance().registerJPEParser(new XMLJPEParser());
		geoJSONWriter = new GeoJSONWriter();
	}

	/**
	 * Returns a JSON containing the describe service document of each service
	 * registered into the library
	 * 
	 * @return
	 */
	public String getAvailableServices() {
		StringBuffer services = new StringBuffer();

		Set<String> keys = serviceManager.getRegisteredConfigurations()
				.keySet();

		Iterator<String> it = keys.iterator();

		String id = null;
		while (it.hasNext()) {
			id = it.next();
			services.append("{").append(id).append(":\n");
			services.append(serviceManager.getServiceAsJSON(id)).append("}");
			if (it.hasNext()) {
				services.append(",");
			}
		}

		return services.toString();
	}

	// TODO Allow several strategies
	// LAT LON
	// BBOX
	// TILES

	// AND DIFERENT REQUESTS
	// BROWSE
	// SEARCH WITH QUERY

	/**
	 * This method is used to get the pois from a service and return a GeoJSON
	 * document with the data retrieved given a Z/X/Y tile.
	 * 
	 * @param id
	 *            The id of the service
	 * @param z
	 *            The zoom level
	 * @param x
	 *            The x tile
	 * @param y
	 *            The y tile
	 * @return The GeoJSON response from the original service response
	 */
	public String getPOIs(String id, int z, int x, int y,
			ArrayList<Param> optionalParams) throws Exception {
		DescribeService describeService = this.getServiceFromID(id);

		if (describeService == null) {
			return this.getErrorForUnknownService(id);
		}

		// Buscar en cache el geoJSON y si est� llamar a onResponseReceived

		// si no est� en cache

		// construir la url
		Extent e1 = TileConversor.tileOSMMercatorBounds(x, y, z);

		double[] minXY = ConversionCoords.reproject(e1.getMinX(), e1.getMinY(),
				CRSFactory.getCRS("EPSG:900913"),
				CRSFactory.getCRS("EPSG:4326"));
		double[] maxXY = ConversionCoords.reproject(e1.getMaxX(), e1.getMaxY(),
				CRSFactory.getCRS("EPSG:900913"),
				CRSFactory.getCRS("EPSG:4326"));

		String url = buildRequest(describeService, minXY[0], minXY[1],
				maxXY[0], maxXY[1], optionalParams, 0, 0);

		// hacer petici�n al servicio
		String file = doRequest(url);

		String geoJSON = this.onResponseReceived(file, describeService);

		return geoJSON;
	}

	/**
	 * Calls the
	 * {@link ServiceConfigurationManager#getServiceConfiguration(String)}
	 * 
	 * @param id
	 *            The id of the service
	 * @return The {@link DescribeService}
	 */
	public DescribeService getServiceFromID(String id) {
		return serviceManager.getServiceConfiguration(id);
	}

	/**
	 * Exception text when a service requested is not registered into the
	 * library
	 * 
	 * @param id
	 *            The id of the service not found
	 * @return The Exception text to show to the user
	 */
	private String getErrorForUnknownService(String id) {
		StringBuffer error = new StringBuffer();
		error.append("Services path: "
				+ ServiceConfigurationManager.CONFIGURATION_DIR);
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

	/**
	 * This method is used to get the pois from a service and return a GeoJSON
	 * document with the data retrieved given a longitude, latitude and a radius
	 * in meters.
	 * 
	 * @param id
	 *            The id of the service
	 * @param lon
	 *            The longitude
	 * @param lat
	 *            The latitude
	 * @param distanceInMeters
	 *            The distance in meters from the lon, lat
	 * @return The GeoJSON response from the original service response
	 */
	public String getPOIs(String id, double lon, double lat,
			double distanceInMeters, ArrayList<Param> optionalParams)
			throws Exception {
		DescribeService describeService = this.getServiceFromID(id);

		if (describeService == null) {
			return this.getErrorForUnknownService(id);
		}

		// Buscar en cache el geoJSON y si est� llamar a onResponseReceived

		// si no est� en cache

		// construir la url
		double[] bbox = Calculator.boundingCoordinates(lon, lat,
				distanceInMeters);

		String url = buildRequest(describeService, bbox[0], bbox[1], bbox[2],
				bbox[3], optionalParams, lon, lat);

		// hacer petici�n al servicio
		String file = doRequest(url);

		String geoJSON = this.onResponseReceived(file, describeService);

		return geoJSON;
	}

	/**
	 * This method is used to get the pois from a service and return a GeoJSON
	 * document with the data retrieved given a bounding box corners
	 * 
	 * @param id
	 *            The id of the service
	 * @param minX
	 * 
	 * @param minY
	 * 
	 * @param maxX
	 * 
	 * @param maxY
	 * @return The GeoJSON response from the original service response
	 */
	public String getPOIs(String id, double minX, double minY, double maxX,
			double maxY, ArrayList<Param> optionalParams) throws Exception {
		DescribeService describeService = this.getServiceFromID(id);

		if (describeService == null) {
			return this.getErrorForUnknownService(id);
		}

		// Buscar en cache el geoJSON y si est� llamar a onResponseReceived

		// si no est� en cache

		// construir la url
		String url = buildRequest(describeService, minX, minY, maxX, maxY,
				optionalParams, 0, 0);

		// hacer petici�n al servicio
		String file = doRequest(url);

		String geoJSON = this.onResponseReceived(file, describeService);

		return geoJSON;
	}

	/**
	 * Given a DescribeService and some mandatory parameters, builds a valid
	 * request for the POI service
	 * 
	 * @param describeService
	 *            The DescribeService
	 * @param minX
	 * @param minY
	 * @param maxX
	 * @param maxY
	 * @param optionalParams
	 *            A list of {@link Param}
	 * @param lon
	 * @param lat
	 * @return The url string to request to
	 */
	public String buildRequest(DescribeService describeService, double minX,
			double minY, double maxX, double maxY,
			ArrayList<Param> optionalParams, double lon, double lat) {
		ServiceParams params = new ServiceParams();

		Extent e1 = new Extent(minX, minY, maxX, maxY);

		int distanceMeters = this.getDistanceMeters(e1);

		params.putParam(ServiceParams.MINX, String.valueOf(minX));
		params.putParam(ServiceParams.MINY, String.valueOf(minY));
		params.putParam(ServiceParams.MAXX, String.valueOf(maxX));
		params.putParam(ServiceParams.MAXY, String.valueOf(maxY));

		double longitude = (lon != 0) ? lon : e1.getCenter().getX();
		double latitude = (lat != 0) ? lat : e1.getCenter().getY();

		params.putParam(ServiceParams.LON, String.valueOf(longitude));
		params.putParam(ServiceParams.LAT, String.valueOf(latitude));

		params.putParam(ServiceParams.FORMAT, "json");

		params.putParam(ServiceParams.DIST, String.valueOf((int) distanceMeters));
		params.putParam(ServiceParams.DISTKM,
				String.valueOf((int)distanceMeters / 1000));
		params.putParam(ServiceParams.KEY, describeService.getApiKey());

		String url = describeService.getRequestForParam(optionalParams);

		if (optionalParams != null) {
			String p;
			for (Param optParam : optionalParams) {
				p = params.getServiceParamFromURLParam(optParam.getType());
				params.putParam(p, optParam.getValue());
			}
		}

		Set<String> keys = params.getParams().keySet();
		Iterator<String> it = keys.iterator();

		String key;
		while (it.hasNext()) {
			key = it.next();
			url = url.replaceAll(key, params.getValueForParam(key));
		}

		return url;
	}

	/**
	 * Calls
	 * {@link Downloader#downloadFromUrl(String, es.prodevelop.gvsig.mini.utiles.Cancellable)}
	 * 
	 * @param url
	 *            The url to request to
	 * @return The data downloaded
	 * @throws Exception
	 */
	public String doRequest(String url) throws Exception {
		// hacer peticion en segundo plano
		Downloader d = new Downloader();
		System.out.println(url);
		d.downloadFromUrl(url, null);
		return new String(d.getData());
	}

	/**
	 * Called after {@link #doRequest(String)} succesfully downlaods the data.
	 * 
	 * This method gets the {@link JPEParser} implementation given the
	 * {@link DescribeService#getFormat()} and calls
	 * {@link JPEParser#parse(String, DescribeService)}
	 * 
	 * Once the response is parsed succesfully returns the GeoJSON response
	 * 
	 * @param json
	 *            The json document retrieved from the source service
	 * @param service
	 *            The {@link DescribeService} used to parse the json
	 * @return A GeoJSON
	 * @throws NoParserRegisteredException
	 */
	public String onResponseReceived(String json, DescribeService service)
			throws NoParserRegisteredException {
		final JPEParser jpeParser = JPEParserManager.getInstance()
				.getJPEParser(service.getFormat());
		ArrayList<JTSFeature> features = jpeParser.parse(json, service);
		String geoJSON = jpeParser.getGeoJSON();
		// Escribir en cache el geoJSON
		return geoJSON;
	}

	/**
	 * Utility method to get the distance from the bottom left corner of the
	 * bounding box to the upper right corner
	 * 
	 * @param boundingBox
	 *            The bounding box
	 * @return The distance in meters
	 */
	public int getDistanceMeters(Extent boundingBox) {		
		return new Double(Math.floor(Calculator.latLonDist(boundingBox.getMinX(),
				boundingBox.getMinY(), boundingBox.getMaxX(),
				boundingBox.getMaxY()))).intValue();
	}

}
