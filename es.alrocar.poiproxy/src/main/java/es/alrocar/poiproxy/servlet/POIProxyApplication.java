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

package es.alrocar.poiproxy.servlet;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.geojson.FeatureCollection;
import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.representation.StringRepresentation;
import org.restlet.routing.Router;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import es.alrocar.poiproxy.configuration.ServiceConfigurationManager;

@Api(value = "/poiproxy", description = "Operations to browse and search POIs and list configured POI services")
@Produces({ "application/json" })
public class POIProxyApplication extends Application {

	public POIProxyApplication() {
		super();
	}

	public POIProxyApplication(Context parentContext) {
		super(parentContext);
	}

	public Restlet createRoot() {
		Router router = new Router(getContext());
		ServiceConfigurationManager.CONFIGURATION_DIR = "./WEB-INF/services";

		router.attach("/browse", BrowsePOIProxyZXY.class);
		router.attach("/browseByExtent", BrowsePOIProxyBBox.class);
		router.attach("/browseByLonLat", BrowsePOIProxyLonLat.class);
		router.attach("/describeServices", DescribeServices.class);
		router.attach("/registerService", RegisterService.class);

		Restlet mainpage = new Restlet() {
			@Override
			public void handle(Request request, Response response) {
				StringBuilder stringBuilder = new StringBuilder();

				stringBuilder.append("<html>");
				stringBuilder.append("<head><title>Hello Application "
						+ "Servlet Page</title></head>");
				stringBuilder.append("<body bgcolor=white>");
				stringBuilder.append("<table border=\"0\">");
				stringBuilder.append("<tr>");
				stringBuilder.append("<td>");
				stringBuilder.append("<h3>available REST calls</h3>");
				stringBuilder
						.append("<ol><li><a href=\"poiproxy/hello\">hello</a> --> returns hello world message "
								+ "and date string</li>");
				stringBuilder
						.append("<li><a href=\"poiproxy/browse?service=panoramio&z=17&x=65397&y=49868\">browse sample</a> --> returns a test panoramio response"
								+ "</li>");

				stringBuilder.append("</ol>");
				stringBuilder.append("</td>");
				stringBuilder.append("</tr>");
				stringBuilder.append("</table>");
				stringBuilder.append("</body>");
				stringBuilder.append("</html>");

				response.setEntity(new StringRepresentation(stringBuilder
						.toString(), MediaType.TEXT_HTML));
			}
		};
		router.attach("", mainpage);

		return router;
	}

	@GET
	@Path("/describeServices")
	@ApiOperation(value = "List the available services in POIProxy with their operations, configuration and expected output", notes = "Prints every single service configured in POIProxy in a key, value object, where the key that defines a service can be used to other API requests as the service parameter", response = es.alrocar.poiproxy.configuration.DescribeServices.class)
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "OK", response = es.alrocar.poiproxy.configuration.DescribeServices.class) })
	public void describeServices() {
	}

	@GET
	@Path("/browse")
	@ApiOperation(value = "Get POIs using a tile vector scheme", notes = "This API operation is used to get POIs from a POIProxy service contained in a vector tile. See http://www.maptiler.org/google-maps-coordinates-tile-bounds-projection/ to understand the tilling scheme used. The accuracy of the results obtained depends on the origin service and mainly the zoom level. This API operation works well at street level", response = FeatureCollection.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "service", value = "A valid POIProxy service (see /poiproxy/describeServices)", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "z", value = "Zoom level", required = true, dataType = "integer", paramType = "query"),
			@ApiImplicitParam(name = "y", value = "Y tile", required = true, dataType = "integer", paramType = "query"),
			@ApiImplicitParam(name = "x", value = "X tile", required = true, dataType = "integer", paramType = "query"),
			@ApiImplicitParam(name = "query", value = "A keyword to search. The search operation depends on the service. To know which services allow the search param see /poiproxy/describeServices", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "apiKey", value = "Your apiKey to make requests to the origin service. Most services already have an apiKey configured or don't need one, but it is highly recommended to use your own apiKeys in order to avoid Rate Limits. Please refer to the origin service documentation in order to register for an apiKey", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "callback", value = "Provide a callback name for JSONP purposes", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "fromDate", value = "A date from which get POIs. Use this date_format: yyyy-MM-dd HH:mm:ss. This parameter has to be configured for the service you are requesting, please refer to the /poiproxy/describeServices operation for more info", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "toDate", value = "A date to which get POIs. Use this date_format: yyyy-MM-dd HH:mm:ss. This parameter has to be configured for the service you are requesting, please refer to the /poiproxy/describeServices operation for more info", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "offset", value = "The page from where to start to get results. Use it together with the limit parameter. The origin service has to support pagination, please refer to /poiproxy/describeServices for more info", required = false, dataType = "integer", paramType = "query"),
			@ApiImplicitParam(name = "limit", value = "The number of results. Use it together with the offset parameter. The origin service has to support pagination, please refer to /poiproxy/describeServices for more info", required = false, dataType = "integer", paramType = "query") })
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "OK", response = FeatureCollection.class) })
	public void zxy() {
	}

	@GET
	@Path("/browseByLonLat")
	@ApiOperation(value = "Get POIs using a point and radius", notes = "This API operation is used to get POIs from a POIProxy service at a distance of a point. All coordinates use a geodetic reference system. The accuracy of the results obtained depends on the origin service and the radius distance. This API operation works well at street level", response = FeatureCollection.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "service", value = "A valid POIProxy service (see /poiproxy/describeServices)", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "lon", value = "Longitude", required = true, dataType = "double", paramType = "query"),
			@ApiImplicitParam(name = "lat", value = "Latitude", required = true, dataType = "double", paramType = "query"),
			@ApiImplicitParam(name = "dist", value = "Radius distance in meters to browse or search POIs", required = true, dataType = "integer", paramType = "query"),
			@ApiImplicitParam(name = "query", value = "A keyword to search. The search operation depends on the service. To know which services allow the search param see /poiproxy/describeServices", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "apiKey", value = "Your apiKey to make requests to the origin service. Most services already have an apiKey configured or don't need one, but it is highly recommended to use your own apiKeys in order to avoid Rate Limits. Please refer to the origin service documentation in order to register for an apiKey", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "callback", value = "Provide a callback name for JSONP purposes", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "fromDate", value = "A date from which get POIs. Use this date_format: yyyy-MM-dd HH:mm:ss. This parameter has to be configured for the service you are requesting, please refer to the /poiproxy/describeServices operation for more info", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "toDate", value = "A date to which get POIs. Use this date_format: yyyy-MM-dd HH:mm:ss. This parameter has to be configured for the service you are requesting, please refer to the /poiproxy/describeServices operation for more info", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "offset", value = "The page from where to start to get results. Use it together with the limit parameter. The origin service has to support pagination, please refer to /poiproxy/describeServices for more info", required = false, dataType = "integer", paramType = "query"),
			@ApiImplicitParam(name = "limit", value = "The number of results. Use it together with the offset parameter. The origin service has to support pagination, please refer to /poiproxy/describeServices for more info", required = false, dataType = "integer", paramType = "query") })
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "OK", response = FeatureCollection.class) })
	public void lonlat() {
	}

	@GET
	@Path("/browseByExtent")
	@ApiOperation(value = "Get POIs using a bounding box", notes = "This API operation is used to get POIs from a POIProxy service contained in a bounding box. All coordinates use a geodetic reference system. The accuracy of the results obtained depends on the origin service and the radius distance. This API operation works well at street level", response = FeatureCollection.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "service", value = "A valid POIProxy service (see /poiproxy/describeServices)", required = true, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "minX", value = "The minimum X coordinate of the bounding box", required = true, dataType = "double", paramType = "query"),
			@ApiImplicitParam(name = "minY", value = "The minimum Y coordinate of the bounding box", required = true, dataType = "double", paramType = "query"),
			@ApiImplicitParam(name = "maxX", value = "The maximum X coordinate of the bounding box", required = true, dataType = "double", paramType = "query"),
			@ApiImplicitParam(name = "maxY", value = "The maximum Y coordinate of the bounding box", required = true, dataType = "double", paramType = "query"),
			@ApiImplicitParam(name = "query", value = "A keyword to search. The search operation depends on the service. To know which services allow the search param see /poiproxy/describeServices", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "apiKey", value = "Your apiKey to make requests to the origin service. Most services already have an apiKey configured or don't need one, but it is highly recommended to use your own apiKeys in order to avoid Rate Limits. Please refer to the origin service documentation in order to register for an apiKey", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "callback", value = "Provide a callback name for JSONP purposes", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "fromDate", value = "A date from which get POIs. Use this date_format: yyyy-MM-dd HH:mm:ss. This parameter has to be configured for the service you are requesting, please refer to the /poiproxy/describeServices operation for more info", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "toDate", value = "A date to which get POIs. Use this date_format: yyyy-MM-dd HH:mm:ss. This parameter has to be configured for the service you are requesting, please refer to the /poiproxy/describeServices operation for more info", required = false, dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "offset", value = "The page from where to start to get results. Use it together with the limit parameter. The origin service has to support pagination, please refer to /poiproxy/describeServices for more info", required = false, dataType = "integer", paramType = "query"),
			@ApiImplicitParam(name = "limit", value = "The number of results. Use it together with the offset parameter. The origin service has to support pagination, please refer to /poiproxy/describeServices for more info", required = false, dataType = "integer", paramType = "query") })
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "OK", response = FeatureCollection.class) })
	public void bbox() {
	}

}
