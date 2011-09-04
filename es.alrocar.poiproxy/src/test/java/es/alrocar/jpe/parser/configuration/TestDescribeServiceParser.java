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

package es.alrocar.jpe.parser.configuration;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.restlet.engine.http.connector.BaseHelper;

import junit.framework.TestCase;
import es.alrocar.jpe.BaseJSONTest;
import es.alrocar.jpe.parser.configuration.DescribeServiceParser;
import es.alrocar.poiproxy.configuration.DescribeService;
import es.alrocar.poiproxy.configuration.FeatureType;
import es.alrocar.poiproxy.proxy.POIProxy;
import es.prodevelop.gvsig.mini.utiles.Constants;

public class TestDescribeServiceParser extends BaseJSONTest {

	public void testParseDescribeService() {
		System.out.println("=======================================");
		System.out.println("TEST PARSE DESCRIBE SERVICE: buzz.json");
		System.out.println("=======================================");

		URL uReport = POIProxy.class.getClassLoader().getResource(
				this.getResource());
		System.out.println(uReport.getPath());

		String json = this.getJSON(uReport.getPath());

		assertTrue(json.length() > 0);

		DescribeServiceParser parser = new DescribeServiceParser();
		DescribeService service = parser.parse(json);

		assertEquals(service.getFormat(), "json");
		System.out.println("Buzz.json format is json");

		System.out.println("Checking featureType to browse");
		FeatureType browseType = service.getFeatureTypes().get(
				DescribeService.BROWSE_TYPE);

		assertEquals(browseType.getFeature(), "kind");
		System.out.println("Buzz.json feature is kind");

		assertEquals(browseType.getLat(), "lat");
		System.out.println("Buzz.json latitude is lat");

		assertEquals(browseType.getLon(), "lng");
		System.out.println("Buzz.json longitude is lng");

		assertEquals(browseType.getCombinedLonLat(), "geocode");
		System.out.println("Buzz.json combinedLonLat is geocode");

		assertEquals(browseType.getLonLatSeparator(), " ");
		System.out.println("Buzz.json lonLatSeparator is ' '");

		assertEquals(browseType.getElements().size(), 5);
		System.out.println("Buzz.json elements size is 5");

		System.out.println("Checking featureType to search");
		browseType = service.getFeatureTypes().get(DescribeService.SEARCH_TYPE);

		assertEquals(browseType.getFeature(), "kind");
		System.out.println("Buzz.json feature is kind");

		assertEquals(browseType.getLat(), "lat");
		System.out.println("Buzz.json latitude is lat");

		assertEquals(browseType.getLon(), "lng");
		System.out.println("Buzz.json longitude is lng");

		assertEquals(browseType.getCombinedLonLat(), "geocode");
		System.out.println("Buzz.json combinedLonLat is geocode");

		assertEquals(browseType.getLonLatSeparator(), " ");
		System.out.println("Buzz.json lonLatSeparator is ' '");

		assertEquals(browseType.getElements().size(), 5);
		System.out.println("Buzz.json elements size is 5");

		System.out.println("Check requestType to browse");

		assertEquals(
				service.getRequestTypes().get(DescribeService.BROWSE_TYPE)
						.getUrl(),
				"https://www.googleapis.com/buzz/v1/activities/search?key=AIzaSyDM7V5F3X0g4_aH6YSwsR4Hbd_uBuQ3QeA&lat=__LAT__&lon=__LON__&radius=__DIST__&alt=json");

		System.out.println("=======================================");
		System.out.println("TEST SUCCESSFULL!!!");
		System.out.println("=======================================");
	}

	public String getJSON(String resource) {
		File f = new File(resource);
		FileInputStream fis = null;
		InputStream in = null;
		OutputStream out = null;
		String res = null;
		try {
			fis = new FileInputStream(f);
			in = new BufferedInputStream(fis, Constants.IO_BUFFER_SIZE);
			final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
			out = new BufferedOutputStream(dataStream, Constants.IO_BUFFER_SIZE);
			byte[] b = new byte[8 * 1024];
			int read;
			int total = 0;
			while ((read = in.read(b)) != -1) {
				total += read;
				out.write(b, 0, read);
			}
			out.flush();
			res = new String(dataStream.toByteArray());

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			Constants.closeStream(fis);
			Constants.closeStream(in);
			Constants.closeStream(out);
		}

		return res;
	}

	public String getResource() {
		return "buzz.json";
	}
}
