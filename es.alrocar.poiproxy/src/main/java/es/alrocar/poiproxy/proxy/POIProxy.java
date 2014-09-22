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

package es.alrocar.poiproxy.proxy;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import es.alrocar.jpe.parser.JPEParser;
import es.alrocar.jpe.parser.JPEParserManager;
import es.alrocar.jpe.parser.csv.CSVParser;
import es.alrocar.jpe.parser.exceptions.NoParserRegisteredException;
import es.alrocar.jpe.parser.json.JSONJPEParser;
import es.alrocar.jpe.parser.kml.KMLParser;
import es.alrocar.jpe.parser.xml.XMLJPEParser;
import es.alrocar.jpe.writer.GeoJSONWriter;
import es.alrocar.poiproxy.configuration.DescribeService;
import es.alrocar.poiproxy.configuration.DescribeServices;
import es.alrocar.poiproxy.configuration.Param;
import es.alrocar.poiproxy.configuration.ServiceConfigurationManager;
import es.alrocar.poiproxy.configuration.ServiceParams;
import es.alrocar.poiproxy.exceptions.POIProxyException;
import es.alrocar.poiproxy.geotools.GeotoolsUtils;
import es.alrocar.poiproxy.proxy.utiles.Calculator;
import es.alrocar.utils.CompressionEnum;
import es.alrocar.utils.Downloader;
import es.alrocar.utils.PropertyLocator;
import es.alrocar.utils.Unzip;
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

	private static final String MERCATOR_SRS = "EPSG:900913";

	private static final String GEODETIC_SRS = "EPSG:4326";

	private final static Logger logger = Logger.getLogger(POIProxy.class);

	public static String CACHE_DIR = "/var/lib/sp/cache";

	private static POIProxy proxy;
	private ServiceConfigurationManager serviceManager;

	private final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static POIProxy asSingleton() {
		if (proxy == null) {
			proxy = new POIProxy();
			proxy.initialize();
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
		JPEParserManager.getInstance().registerJPEParser(new CSVParser());
		JPEParserManager.getInstance().registerJPEParser(new KMLParser());
	}

	/**
	 * Returns a JSON containing the describe service document of each service
	 * registered into the library
	 * 
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 */
	public String getAvailableServices() throws JsonGenerationException,
			JsonMappingException, IOException {
		DescribeServices services = serviceManager.getAvailableServices();

		return services.asJSON();
	}

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
			List<Param> optionalParams) throws Exception {
		DescribeService describeService = getDescribeServiceByID(id);

		Extent e1 = TileConversor.tileOSMMercatorBounds(x, y, z);

		double[] minXY = ConversionCoords.reproject(e1.getMinX(), e1.getMinY(),
				CRSFactory.getCRS(MERCATOR_SRS),
				CRSFactory.getCRS(GEODETIC_SRS));
		double[] maxXY = ConversionCoords.reproject(e1.getMaxX(), e1.getMaxY(),
				CRSFactory.getCRS(MERCATOR_SRS),
				CRSFactory.getCRS(GEODETIC_SRS));

		String geoJSON = getResponseAsGeoJSON(id, optionalParams,
				describeService, minXY[0], minXY[1], maxXY[0], maxXY[1], 0, 0);

		return geoJSON;
	}

	/**
	 * This method is used to get the pois from a service and return a list of
	 * {@link JTSFeature} document with the data retrieved given a Z/X/Y tile.
	 * 
	 * @param id
	 *            The id of the service
	 * @param z
	 *            The zoom level
	 * @param x
	 *            The x tile
	 * @param y
	 *            The y tile
	 * @return a list of {@link JTSFeature}
	 */
	public ArrayList<JTSFeature> getFeatures(String id, int z, int x, int y,
			List<Param> optionalParams) throws Exception {
		DescribeService describeService = getDescribeServiceByID(id);

		Extent e1 = TileConversor.tileOSMMercatorBounds(x, y, z);

		double[] minXY = ConversionCoords.reproject(e1.getMinX(), e1.getMinY(),
				CRSFactory.getCRS(MERCATOR_SRS),
				CRSFactory.getCRS(GEODETIC_SRS));
		double[] maxXY = ConversionCoords.reproject(e1.getMaxX(), e1.getMaxY(),
				CRSFactory.getCRS(MERCATOR_SRS),
				CRSFactory.getCRS(GEODETIC_SRS));

		return getFeatures(id, optionalParams, describeService, minXY[0],
				minXY[1], maxXY[0], maxXY[1], 0, 0);
	}

	/**
	 * This method is used to get the pois from a category and return a list of
	 * {@link JTSFeature} document with the data retrieved given a Z/X/Y tile.
	 * 
	 * @param id
	 *            The category to request
	 * @see #getAvailableCategories()
	 * @param z
	 *            The zoom level
	 * @param x
	 *            The x tile
	 * @param y
	 *            The y tile
	 * @return a list of {@link JTSFeature}
	 */
	public ArrayList<JTSFeature> getFeaturesByCategory(String category, int z,
			int x, int y, List<Param> optionalParams) throws Exception {
		List<String> ids = serviceManager.getAvailableServices()
				.getServicesIDByCategory(category);
		ArrayList<JTSFeature> features = new ArrayList<JTSFeature>();
		for (String id : ids) {
			try {
				features.addAll(this.getFeatures(id, z, x, y, optionalParams));
			} catch (Exception e) {
				logger.error("POIProxy", e);
			}
		}

		return features;
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
			double distanceInMeters, List<Param> optionalParams)
			throws Exception {
		DescribeService describeService = getDescribeServiceByID(id);

		double[] bbox = Calculator.boundingCoordinates(lon, lat,
				distanceInMeters);

		String geoJSON = getResponseAsGeoJSON(id, optionalParams,
				describeService, bbox[0], bbox[1], bbox[2], bbox[3], lon, lat);

		return geoJSON;
	}

	/**
	 * This method is used to get the pois from a service and return a list of
	 * {@link JTSFeature} document with the data retrieved given a longitude,
	 * latitude and a radius in meters.
	 * 
	 * @param id
	 *            The id of the service
	 * @param lon
	 *            The longitude
	 * @param lat
	 *            The latitude
	 * @param distanceInMeters
	 *            The distance in meters from the lon, lat
	 * @return A list of {@link JTSFeature}
	 */
	public ArrayList<JTSFeature> getFeatures(String id, double lon, double lat,
			double distanceInMeters, List<Param> optionalParams)
			throws Exception {
		DescribeService describeService = getDescribeServiceByID(id);

		double[] bbox = Calculator.boundingCoordinates(lon, lat,
				distanceInMeters);

		return getFeatures(id, optionalParams, describeService, bbox[0],
				bbox[1], bbox[2], bbox[3], lon, lat);
	}

	/**
	 * This method is used to get the pois from a category and return a list of
	 * {@link JTSFeature} document with the data retrieved given a longitude,
	 * latitude and a radius in meters.
	 * 
	 * @param id
	 *            The id of the service
	 * @param lon
	 *            The longitude
	 * @param lat
	 *            The latitude
	 * @param distanceInMeters
	 *            The distance in meters from the lon, lat
	 * @return A list of {@link JTSFeature}
	 */
	public ArrayList<JTSFeature> getFeaturesByCategory(String category,
			double lon, double lat, double distanceInMeters,
			List<Param> optionalParams) throws Exception {
		List<String> ids = serviceManager.getAvailableServices()
				.getServicesIDByCategory(category);
		ArrayList<JTSFeature> features = new ArrayList<JTSFeature>();
		for (String id : ids) {
			try {
				features.addAll(this.getFeatures(id, lon, lat,
						distanceInMeters, optionalParams));
			} catch (Exception e) {
				logger.error("POIProxy", e);
			}
		}

		return features;

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
			double maxY, List<Param> optionalParams) throws Exception {
		DescribeService describeService = getDescribeServiceByID(id);

		String geoJSON = getResponseAsGeoJSON(id, optionalParams,
				describeService, minX, minY, maxX, maxY, 0, 0);

		return geoJSON;
	}

	/**
	 * This method is used to get the pois from a service and return a list of
	 * {@link JTSFeature} document with the data retrieved given a bounding box
	 * corners
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
	 * @return A list of {@link JTSFeature}
	 */
	public ArrayList<JTSFeature> getFeatures(String id, double minX,
			double minY, double maxX, double maxY, List<Param> optionalParams)
			throws Exception {
		DescribeService describeService = getDescribeServiceByID(id);

		return getFeatures(id, optionalParams, describeService, minX, minY,
				maxX, maxY, 0, 0);
	}

	/**
	 * This method is used to get the pois from a category and return a list of
	 * {@link JTSFeature} document with the data retrieved given a bounding box
	 * corners
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
	 * @return A list of {@link JTSFeature}
	 */
	public ArrayList<JTSFeature> getFeaturesByCategory(String category,
			double minX, double minY, double maxX, double maxY,
			List<Param> optionalParams) throws Exception {
		List<String> ids = serviceManager.getAvailableServices()
				.getServicesIDByCategory(category);
		ArrayList<JTSFeature> features = new ArrayList<JTSFeature>();
		for (String id : ids) {
			try {
				features.addAll(getFeatures(id, minX, minY, maxX, maxY,
						optionalParams));
			} catch (Exception e) {
				logger.error("POIProxy", e);
			}
		}

		return features;
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
	 * @throws POIProxyException
	 */
	public String buildRequest(DescribeService describeService, double minX,
			double minY, double maxX, double maxY, List<Param> optionalParams,
			double lon, double lat) throws POIProxyException {
		ServiceParams params = new ServiceParams();

		Extent e1 = new Extent(minX, minY, maxX, maxY);

		double[] minXY = GeotoolsUtils.transform(GEODETIC_SRS,
				describeService.getSRS(),
				new double[] { e1.getMinX(), e1.getMinY() });

		double[] maxXY = GeotoolsUtils.transform(GEODETIC_SRS,
				describeService.getSRS(),
				new double[] { e1.getMaxX(), e1.getMaxY() });

		int distanceMeters = this.getDistanceMeters(e1);

		e1.setMinX(minXY[0]);
		e1.setMinY(minXY[1]);
		e1.setMaxX(maxXY[0]);
		e1.setMaxY(maxXY[1]);

		params.putParam(ServiceParams.MINX, String.valueOf(minXY[0]));
		params.putParam(ServiceParams.MINY, String.valueOf(minXY[1]));
		params.putParam(ServiceParams.MAXX, String.valueOf(maxXY[0]));
		params.putParam(ServiceParams.MAXY, String.valueOf(maxXY[1]));

		double longitude = (lon != 0) ? lon : e1.getCenter().getX();
		double latitude = (lat != 0) ? lat : e1.getCenter().getY();

		params.putParam(ServiceParams.LON, String.valueOf(longitude));
		params.putParam(ServiceParams.LAT, String.valueOf(latitude));

		params.putParam(ServiceParams.FORMAT, "json");

		params.putParam(ServiceParams.DIST,
				String.valueOf((int) distanceMeters));
		params.putParam(ServiceParams.DISTKM,
				String.valueOf((int) distanceMeters / 1000));
		params.putParam(ServiceParams.KEY, describeService.getApiKey());

		if (optionalParams != null) {
			String p;
			for (Param optParam : optionalParams) {
				p = params.getServiceParamFromURLParam(optParam.getType());
				if (p == null) {
					params.putParam(optParam.getType(), optParam.getValue());
				} else {
					params.putParam(p, optParam.getValue());
				}
			}
		}

		String url = describeService.getRequestForParam(optionalParams, params);

		Set<String> keys = params.getParams().keySet();
		Iterator<String> it = keys.iterator();

		String key;
		while (it.hasNext()) {
			key = it.next();
			if (describeService.isSpecialParam(key)) {
				url = url.replaceAll(key,
						getValue(params, key, describeService));
			}
		}

		return url;
	}

	protected String getValue(ServiceParams params, String key,
			DescribeService describeService) throws POIProxyException {
		if (params.isDate(key)) {
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
			SimpleDateFormat outsdf = new SimpleDateFormat(
					describeService.getDateFormat());
			try {
				Date date = sdf.parse(params.getValueForParam(key));
				return outsdf.format(date);
			} catch (ParseException e) {
				logger.warn(e);
				throw new POIProxyException("Error with dates");
			}
		} else {
			return params.getValueForParam(key);
		}
	}

	/**
	 * Calls
	 * {@link Downloader#downloadFromUrl(String, es.prodevelop.gvsig.mini.utiles.Cancellable)}
	 * 
	 * @param url
	 *            The url to request to
	 * @param service
	 *            The {@link DescribeService} object
	 * @param id
	 *            The ID of the {@link DescribeService}
	 * @return The data downloaded
	 * @throws Exception
	 */
	public String doRequest(String url, DescribeService service, String id)
			throws Exception {
		// TODO do it in backgound?
		Downloader d = new Downloader();
		System.out.println(url);
		d.downloadFromUrl(url, id, PropertyLocator.getInstance().tempFolder
				+ id + File.separator, null);

		if (service.getCompression() != null
				&& service.getCompression().equals(CompressionEnum.ZIP.format)) {
			Unzip unzip = new Unzip();
			unzip.unzip(PropertyLocator.getInstance().tempFolder + id
					+ File.separator, PropertyLocator.getInstance().tempFolder
					+ id + File.separator + id, true);

			Downloader opener = new Downloader();
			return opener.openFile(PropertyLocator.getInstance().tempFolder
					+ id + File.separator + service.getContentFile());
		}

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
	public String onResponseReceived(String json, DescribeService service,
			LocalFilter localFilter) throws NoParserRegisteredException {
		final JPEParser jpeParser = JPEParserManager.getInstance()
				.getJPEParser(service.getFormat());
		jpeParser.parse(json, service, localFilter);
		String geoJSON = jpeParser.getGeoJSON();
		// Escribir en cache el geoJSON
		return geoJSON;
	}

	private ArrayList<JTSFeature> parseFeatures(String json,
			DescribeService service, LocalFilter localFilter)
			throws NoParserRegisteredException {
		final JPEParser jpeParser = JPEParserManager.getInstance()
				.getJPEParser(service.getFormat());
		return jpeParser.parse(json, service, localFilter);
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
		return new Double(Math.floor(Calculator.latLonDist(
				boundingBox.getMinX(), boundingBox.getMinY(),
				boundingBox.getMaxX(), boundingBox.getMaxY()))).intValue();
	}

	/**
	 * Frees this instance of {@link POIProxy}
	 */
	public void destroy() {
		logger.debug("Goodbye POIProxy");
	}

	/**
	 * Iterates the {@link DescribeServices} and returns a list of categories
	 * configured
	 * 
	 * @return
	 */
	public List<String> getAvailableCategories() {
		DescribeServices services = serviceManager.getAvailableServices();
		return services.getCategories();
	}

	private DescribeService getDescribeServiceByID(String id)
			throws POIProxyException {
		DescribeService describeService = this.getServiceFromID(id);

		if (describeService == null) {
			throw new POIProxyException(this.getErrorForUnknownService(id));
		}
		return describeService;
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

	private String getResponseAsGeoJSON(String id, List<Param> optionalParams,
			DescribeService describeService, double minX, double minY,
			double maxX, double maxY, double lon, double lat) throws Exception,
			NoParserRegisteredException {
		String url = buildRequest(describeService, minX, minY, maxX, maxY,
				optionalParams, lon, lat);

		String file = doRequest(url, describeService, id);

		String geoJSON = this.onResponseReceived(file, describeService,
				describeService.getLocalFilter(optionalParams));
		return geoJSON;
	}

	private ArrayList<JTSFeature> getFeatures(String id,
			List<Param> optionalParams, DescribeService describeService,
			double minX, double minY, double maxX, double maxY, double lon,
			double lat) throws Exception, NoParserRegisteredException {
		String url = buildRequest(describeService, minX, minY, maxX, maxY,
				optionalParams, lon, lat);

		String file = doRequest(url, describeService, id);

		ArrayList<JTSFeature> features = this
				.parseFeatures(file, describeService,
						LocalFilter.fromOptionalParams(optionalParams));

		return features;
	}

	/**
	 * Interface for registering a new service in POIProxy
	 * 
	 * @param service
	 *            The {@link DescribeService}
	 * @param describeService
	 *            The JSON representation of the DescribeService for hot
	 *            registering
	 * @throws POIProxyException
	 */
	public void register(DescribeService service, String describeService)
			throws POIProxyException {
		serviceManager.registerServiceConfiguration(service.getId(),
				describeService, service);
	}
}
