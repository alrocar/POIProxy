package es.alrocar.map.vector.provider.driver.buzz;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import es.alrocar.map.vector.provider.driver.impl.JSONDriver;
import es.alrocar.map.vector.provider.geom.AttributePoint;
import es.prodevelop.gvsig.mini.geom.Extent;
import es.prodevelop.gvsig.mini.geom.Point;

public class BuzzDriverV1 extends JSONDriver {

	private String API_KEY = "AIzaSyDM7V5F3X0g4_aH6YSwsR4Hbd_uBuQ3QeA";

	private String url = "https://www.googleapis.com/buzz/v1/activities/search?key="
			+ API_KEY + "&lat=_LAT_&lon=_LON_&radius=_DIST_&alt=json";

	@Override
	public String buildQuery(Extent boundingBox) {
		Point center = boundingBox.getCenter();
		String url = this.url.replaceAll("_LAT_", "" + center.getY());
		url = url.replaceAll("_LON_", "" + center.getX());
		url = url.replace("_DIST_", getDistanceMeters(boundingBox) + "");
		return url;
	}

	@Override
	public JSONArray getJSONArray(JSONObject o) throws JSONException {
		return o.getJSONObject("data").getJSONArray("items");
	}

	@Override
	public Point processResult(JSONObject object) {
		AttributePoint p = new AttributePoint();
		String title = null;
		String published = null;
		String id = null;
		String actorName = null;
		String profileUrl = null;
		String actorId = null;
		String thumbUrl = null;
		String geocode = null;

		try {
			title = object.getString("title");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		try {
			published = object.getString("published");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		try {
			id = object.getString("id");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		try {
			actorName = object.getJSONObject("actor").getString("name");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		try {
			profileUrl = object.getJSONObject("actor").getString("profileUrl");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		try {
			thumbUrl = object.getJSONObject("actor").getString("thumbUrl");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		try {
			actorId = object.getJSONObject("actor").getString("id");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		try {
			geocode = object.getString("geocode");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		if (geocode == null)
			return null;

		if (!geocode.contains(" "))
			return null;

		String[] coords = geocode.split(" ");

		p.setX(Double.valueOf(coords[1]));
		p.setY(Double.valueOf(coords[0]));
		p.addField("title", title, AttributePoint.Attribute.TYPE_STRING);
		p.addField("published", published, AttributePoint.Attribute.TYPE_DATE);
		p.addField("id", id, AttributePoint.Attribute.TYPE_STRING);
		p.addField("actor_name", actorName,
				AttributePoint.Attribute.TYPE_STRING);
		p.addField("actor_id", actorId, AttributePoint.Attribute.TYPE_STRING);
		p.addField("actor_profile_url", profileUrl,
				AttributePoint.Attribute.TYPE_URL);
		p.addField("actor_thumb_url", thumbUrl,
				AttributePoint.Attribute.TYPE_URL);

		return p;
	}

	public String getName() {
		return "buzz";
	}

}
