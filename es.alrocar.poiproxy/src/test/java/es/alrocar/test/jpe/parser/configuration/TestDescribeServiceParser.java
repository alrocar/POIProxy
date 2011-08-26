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

package es.alrocar.test.jpe.parser.configuration;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import org.json.JSONException;

import junit.framework.TestCase;
import es.alrocar.jpe.parser.GeoJSONParser;
import es.alrocar.jpe.parser.JPEParser;
import es.alrocar.jpe.parser.configuration.DescribeServiceParser;
import es.alrocar.jpe.parser.json.JSONJPEParser;
import es.alrocar.jpe.writer.GeoJSONWriter;
import es.alrocar.poiproxy.configuration.DescribeService;
import es.alrocar.poiproxy.configuration.ServiceConfigurationManager;
import es.alrocar.poiproxy.proxy.POIProxy;
import es.prodevelop.gvsig.mini.exceptions.BaseException;
import es.prodevelop.gvsig.mini.geom.impl.jts.JTSFeature;
import es.prodevelop.gvsig.mini.utiles.Constants;

public class TestDescribeServiceParser extends TestCase {

	String json = "{describeService : {    apiKey : \"\",    requestTypes : {         \"browse\":  {\"url\": \"b\", \"params\": []},                    \"search\":  {\"url\": \"a\", \"params\": []}                   },    featureTypes : { \"browse\" : {                    	\"feature\" : \"upload_date\",                    	\"elements\" : [\"photo_url\", \"photo_title\"],                    	\"lon\": \"longitude\",                    	\"lat\": \"latitude\"                    	}                    }}}";

	String pano = "{\"count\": 2977, \"photos\": [{\"upload_date\": \"02 February 2006\", \"owner_name\": \"heavenearth\", \"photo_id\": 9439, \"longitude\": -151.75, \"height\": 375, \"width\": 500, \"photo_title\": \"Bora Bora\", \"latitude\": -16.5, \"owner_url\": \"http://www.panoramio.com/user/1600\", \"owner_id\": 1600, \"photo_file_url\": \"http://mw2.google.com/mw-panoramio/photos/medium/9439.jpg\", \"photo_url\": \"http://www.panoramio.com/photo/9439\"}, {\"upload_date\": \"18 January 2011\", \"owner_name\": \"\u5b89\u8def   Michael\", \"photo_id\": 46752123, \"longitude\": 120.52718600000003, \"height\": 370, \"width\": 500, \"photo_title\": \" Rising (above SL 1235 m)  \u5d01\u982d\u5c71  Winner--Jan. 2011\", \"latitude\": 23.327833999999999, \"owner_url\": \"http://www.panoramio.com/user/2780232\", \"owner_id\": 2780232, \"photo_file_url\": \"http://mw2.google.com/mw-panoramio/photos/medium/46752123.jpg\", \"photo_url\": \"http://www.panoramio.com/photo/46752123\"}, {\"upload_date\": \"12 March 2007\", \"owner_name\": \"Jean-Michel Raggioli\", \"photo_id\": 1282387, \"longitude\": -61.017049999999998, \"height\": 400, \"width\": 500, \"photo_title\": \"Thunderstorm in Martinique\", \"latitude\": 14.500819, \"owner_url\": \"http://www.panoramio.com/user/49870\", \"owner_id\": 49870, \"photo_file_url\": \"http://mw2.google.com/mw-panoramio/photos/medium/1282387.jpg\", \"photo_url\": \"http://www.panoramio.com/photo/1282387\"}, {\"upload_date\": \"20 January 2011\", \"owner_name\": \"Dominik M. Ram\u00edk\", \"photo_id\": 46817885, \"longitude\": -178.13709299999999, \"height\": 330, \"width\": 500, \"photo_title\": \"Dusk in Tufulega, Futuna Island (dedicated to my friend Oram), homage to the people and islands of Wallis and Futuna\", \"latitude\": -14.310613, \"owner_url\": \"http://www.panoramio.com/user/919358\", \"owner_id\": 919358, \"photo_file_url\": \"http://mw2.google.com/mw-panoramio/photos/medium/46817885.jpg\", \"photo_url\": \"http://www.panoramio.com/photo/46817885\"}, {\"upload_date\": \"04 January 2007\", \"owner_name\": \"Artusi\", \"photo_id\": 298967, \"longitude\": -111.40788999999999, \"height\": 375, \"width\": 500, \"photo_title\": \"Antelope Canyon, Ray of Light\", \"latitude\": 36.894036999999997, \"owner_url\": \"http://www.panoramio.com/user/64388\", \"owner_id\": 64388, \"photo_file_url\": \"http://mw2.google.com/mw-panoramio/photos/medium/298967.jpg\", \"photo_url\": \"http://www.panoramio.com/photo/298967\"}, {\"upload_date\": \"12 March 2007\", \"owner_name\": \"@mabut\", \"photo_id\": 1289207, \"longitude\": -4.0199660000000002, \"height\": 337, \"width\": 500, \"photo_title\": \"Silouette\", \"latitude\": 31.187546000000001, \"owner_url\": \"http://www.panoramio.com/user/232099\", \"owner_id\": 232099, \"photo_file_url\": \"http://mw2.google.com/mw-panoramio/photos/medium/1289207.jpg\", \"photo_url\": \"http://www.panoramio.com/photo/1289207\"}, {\"upload_date\": \"21 January 2007\", \"owner_name\": \"Busa P\u00e9ter\", \"photo_id\": 522084, \"longitude\": 17.470493000000001, \"height\": 350, \"width\": 500, \"photo_title\": \"In Memoriam Antoine de Saint-Exup\u00e9ry\", \"latitude\": 47.867077000000002, \"owner_url\": \"http://www.panoramio.com/user/109117\", \"owner_id\": 109117, \"photo_file_url\": \"http://mw2.google.com/mw-panoramio/photos/medium/522084.jpg\", \"photo_url\": \"http://www.panoramio.com/photo/522084\"}, {\"upload_date\": \"14 April 2008\", \"owner_name\": \"Dejah\", \"photo_id\": 9363990, \"longitude\": -72.607527000000005, \"height\": 375, \"width\": 500, \"photo_title\": \"Marble Cave\", \"latitude\": -46.647137999999998, \"owner_url\": \"http://www.panoramio.com/user/947917\", \"owner_id\": 947917, \"photo_file_url\": \"http://mw2.google.com/mw-panoramio/photos/medium/9363990.jpg\", \"photo_url\": \"http://www.panoramio.com/photo/9363990\"}, {\"upload_date\": \"18 December 2010\", \"owner_name\": \"ZEUS74\", \"photo_id\": 45214668, \"longitude\": -0.6199980000000096, \"height\": 335, \"width\": 500, \"photo_title\": \"MIENTRAS QUEDE UN REFLEJO HABRA UNA FOTO < 1st Prize  Scenery December 2010 >\", \"latitude\": 38.178075999999997, \"owner_url\": \"http://www.panoramio.com/user/3100440\", \"owner_id\": 3100440, \"photo_file_url\": \"http://mw2.google.com/mw-panoramio/photos/medium/45214668.jpg\", \"photo_url\": \"http://www.panoramio.com/photo/45214668\"}, {\"upload_date\": \"21 December 2006\", \"owner_name\": \"mikel ortega\", \"photo_id\": 204924, \"longitude\": -1.806951, \"height\": 346, \"width\": 500, \"photo_title\": \"zaldiak\", \"latitude\": 43.245139999999999, \"owner_url\": \"http://www.panoramio.com/user/2575\", \"owner_id\": 2575, \"photo_file_url\": \"http://mw2.google.com/mw-panoramio/photos/medium/204924.jpg\", \"photo_url\": \"http://www.panoramio.com/photo/204924\"}, {\"upload_date\": \"25 December 2006\", \"owner_name\": \"Andrzej Wierzcho\u0144\", \"photo_id\": 233619, \"longitude\": 21.035727999999999, \"height\": 500, \"width\": 500, \"photo_title\": \"Warsaw Bridge 01 [www.wierzchon.com]\", \"latitude\": 52.242353000000001, \"owner_url\": \"http://www.panoramio.com/user/47836\", \"owner_id\": 47836, \"photo_file_url\": \"http://mw2.google.com/mw-panoramio/photos/medium/233619.jpg\", \"photo_url\": \"http://www.panoramio.com/photo/233619\"}, {\"upload_date\": \"05 February 2011\", \"owner_name\": \"Pozlp\u98ce\u683c\", \"photo_id\": 47594975, \"longitude\": 138.95496000000003, \"height\": 433, \"width\": 500, \"photo_title\": \"\u8fdc\u773a\u5bcc\u58eb\u5c71 Overlooking Mount Fuji\", \"latitude\": 35.285626000000001, \"owner_url\": \"http://www.panoramio.com/user/4826506\", \"owner_id\": 4826506, \"photo_file_url\": \"http://mw2.google.com/mw-panoramio/photos/medium/47594975.jpg\", \"photo_url\": \"http://www.panoramio.com/photo/47594975\"}, {\"upload_date\": \"21 September 2010\", \"owner_name\": \"Mahdi Kalhor\", \"photo_id\": 41085291, \"longitude\": 52.057343000000003, \"height\": 389, \"width\": 500, \"photo_title\": \"\u06a9\u0648\u06cc\u0631\u0645\u0631\u0646\u062c\u0627\u0628\u060c \u067e\u0627\u0631\u06a9 \u0645\u0644\u06cc \u06a9\u0648\u06cc\u0631\u060c  \u0628\u0646\u062f \u0631\u06cc\u06af\u060c \u062a\u067e\u0647 \u0647\u0627\u06cc \u0634\u0646\u06cc\u060c \u0634\u0646\u0647\u0627\u06cc \u0631\u0648\u0627\u0646\u060c \u0631\u0642\u0635 \u0645\u0627\u0633\u0647 \u0647\u0627  Kavir National Park, Maranjab Desert, Band E Rig, Sand dunes, 1st comment  \", \"latitude\": 34.353923999999999, \"owner_url\": \"http://www.panoramio.com/user/1171864\", \"owner_id\": 1171864, \"photo_file_url\": \"http://mw2.google.com/mw-panoramio/photos/medium/41085291.jpg\", \"photo_url\": \"http://www.panoramio.com/photo/41085291\"}, {\"upload_date\": \"21 February 2011\", \"owner_name\": \"MoUzEs\", \"photo_id\": 48482258, \"longitude\": 12.219414999999998, \"height\": 497, \"width\": 500, \"photo_title\": \"Way into the unknown\", \"latitude\": 55.425227, \"owner_url\": \"http://www.panoramio.com/user/4423138\", \"owner_id\": 4423138, \"photo_file_url\": \"http://mw2.google.com/mw-panoramio/photos/medium/48482258.jpg\", \"photo_url\": \"http://www.panoramio.com/photo/48482258\"}, {\"upload_date\": \"17 June 2006\", \"owner_name\": \"Roberto Garcia\", \"photo_id\": 25514, \"longitude\": -84.693431854248004, \"height\": 375, \"width\": 500, \"photo_title\": \"Arenal\", \"latitude\": 10.479372089916, \"owner_url\": \"http://www.panoramio.com/user/4112\", \"owner_id\": 4112, \"photo_file_url\": \"http://mw2.google.com/mw-panoramio/photos/medium/25514.jpg\", \"photo_url\": \"http://www.panoramio.com/photo/25514\"}, {\"upload_date\": \"22 January 2007\", \"owner_name\": \"Snemann\", \"photo_id\": 532693, \"longitude\": 11.272659301758001, \"height\": 333, \"width\": 500, \"photo_title\": \"Wheatfield in afternoon light\", \"latitude\": 59.637471648419002, \"owner_url\": \"http://www.panoramio.com/user/39160\", \"owner_id\": 39160, \"photo_file_url\": \"http://mw2.google.com/mw-panoramio/photos/medium/532693.jpg\", \"photo_url\": \"http://www.panoramio.com/photo/532693\"}, {\"upload_date\": \"25 June 2006\", \"owner_name\": \"Miguel Coranti\", \"photo_id\": 27932, \"longitude\": -64.404944999999998, \"height\": 375, \"width\": 500, \"photo_title\": \"Atardecer en Embalse\", \"latitude\": -32.202924000000003, \"owner_url\": \"http://www.panoramio.com/user/4483\", \"owner_id\": 4483, \"photo_file_url\": \"http://mw2.google.com/mw-panoramio/photos/medium/27932.jpg\", \"photo_url\": \"http://www.panoramio.com/photo/27932\"}, {\"upload_date\": \"09 December 2006\", \"owner_name\": \"Cyrill\", \"photo_id\": 97671, \"longitude\": 30.78603, \"height\": 375, \"width\": 500, \"photo_title\": \"kin-dza-dza\", \"latitude\": 46.638703999999997, \"owner_url\": \"http://www.panoramio.com/user/13058\", \"owner_id\": 13058, \"photo_file_url\": \"http://mw2.google.com/mw-panoramio/photos/medium/97671.jpg\", \"photo_url\": \"http://www.panoramio.com/photo/97671\"}, {\"upload_date\": \"05 October 2006\", \"owner_name\": \"Norbert MAIER\", \"photo_id\": 57823, \"longitude\": 12.900009155273001, \"height\": 333, \"width\": 500, \"photo_title\": \"Maria Alm\", \"latitude\": 47.409967629268003, \"owner_url\": \"http://www.panoramio.com/user/8060\", \"owner_id\": 8060, \"photo_file_url\": \"http://mw2.google.com/mw-panoramio/photos/medium/57823.jpg\", \"photo_url\": \"http://www.panoramio.com/photo/57823\"}, {\"upload_date\": \"25 May 2008\", \"owner_name\": \"VFedele\", \"photo_id\": 10574161, \"longitude\": 148.682098, \"height\": 346, \"width\": 500, \"photo_title\": \"SILHOUETTE\", \"latitude\": -35.299996, \"owner_url\": \"http://www.panoramio.com/user/766550\", \"owner_id\": 766550, \"photo_file_url\": \"http://mw2.google.com/mw-panoramio/photos/medium/10574161.jpg\", \"photo_url\": \"http://www.panoramio.com/photo/10574161\"}], \"has_more\": true}";

	public void testParse() {
		String json = this.json;

		if (json != null && json.length() > 0) {
			DescribeServiceParser parser = new DescribeServiceParser();
			DescribeService service = parser.parse(json);

			JPEParser jpeParser = new JSONJPEParser();
			ArrayList results = jpeParser.parse(pano, service);

			String geoJSON = jpeParser.getGeoJSON();

			GeoJSONParser geoJSONParser = new GeoJSONParser();
			try {
				ArrayList<JTSFeature> features = geoJSONParser.parse(geoJSON);

				GeoJSONWriter writer = new GeoJSONWriter();
				geoJSON = writer.write(features);
				int i = 0;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BaseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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
		return "panoramio.json";
	}

	public void testProxy() {
		ServiceConfigurationManager.CONFIGURATION_DIR = "/var/lib/sp/services";
		POIProxy proxy = POIProxy.getInstance();

		proxy.initialize();

		try {
			String geoJSON = proxy.getPOIs("panoramio", 0, 0, 0, null);
			System.out.println(geoJSON);
			//
			// geoJSON = proxy.getPOIs("wikipedia", 14, 8174, 6233, null);
			// System.out.println(geoJSON);
			//
			// geoJSON = proxy.getPOIs("minube", 17, 65397, 49868, null);
			// System.out.println(geoJSON);
			//
			// geoJSON = proxy.getPOIs("twitter", 17, 65397, 49868, null);
			// System.out.println(geoJSON);

			geoJSON = proxy.getPOIs("buzz", 17, 65397, 49868, null);
			System.out.println(geoJSON);
			//
			// geoJSON = proxy.getPOIs("foursquare", 17, 65397, 49868, null);
			// System.out.println(geoJSON);
			//
			// geoJSON = proxy.getPOIs("flickr", 17, 65397, 49868, null);
			// System.out.println(geoJSON);
			int i = 0;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
