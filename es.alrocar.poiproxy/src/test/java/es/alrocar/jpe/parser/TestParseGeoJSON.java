package es.alrocar.jpe.parser;

import java.util.ArrayList;

import org.json.JSONException;

import es.alrocar.jpe.BaseJSONTest;
import es.prodevelop.gvsig.mini.exceptions.BaseException;
import es.prodevelop.gvsig.mini.geom.impl.jts.JTSFeature;

public class TestParseGeoJSON extends BaseJSONTest {

	public void testGeoJSON() {
		System.out.println("=======================================");
		System.out.println("TEST PARSE GEOJSON: panoramio.geojson");
		System.out.println("=======================================");

		String geoJSON = this.getJSON();
		GeoJSONParser parser = new GeoJSONParser();
		try {
			ArrayList<JTSFeature> features = parser.parse(geoJSON);
			assertEquals(features.size(), 49);
			System.out.println("Total features: " + features.size());

			assertEquals(features.get(0).getAttribute("photo_title").value,
					"Valencia, Catedral");
			assertEquals(features.get(0).getAttribute("photo_file_url").value,
					"http://mw2.google.com/mw-panoramio/photos/square/36639728.jpg");
			assertEquals(features.get(0).getAttribute("photo_url").value,
					"http://www.panoramio.com/photo/36639728");
			System.out.println("Checked attributes OK");

			assertEquals(features.get(0).getGeometry().getGeometry()
					.getCoordinate().x, -0.3784170000000131);

			assertEquals(features.get(0).getGeometry().getGeometry()
					.getCoordinate().y, 39.457585);
			System.out.println("Checked geometry OK");

			System.out.println("=======================================");
			System.out.println("TEST SUCCESSFULL!!!");
			System.out.println("=======================================");
		} catch (JSONException e) {
			System.out.println("JSONException: " + e.getCause());
			assertEquals(true, false);
		} catch (BaseException e) {
			System.out.println("BaseException: " + e.getCause());
			assertEquals(true, false);
		}
	}

	@Override
	public String getResource() {
		return "panoramio_browse.geojson";
	}

}
