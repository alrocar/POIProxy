/* Copyright (C) 2011 Alberto Romeu Carrasco
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 *
 *   
 *   author: Alberto Romeu Carrasco (alberto@alrocar.es)
 */

package es.alrocar.map.vector.provider.driver.panoramio;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import es.alrocar.map.vector.provider.driver.impl.JSONDriver;
import es.alrocar.map.vector.provider.geom.FeatureAttribute;
import es.alrocar.map.vector.provider.geom.FeatureAttribute.Attribute;
import es.prodevelop.gvsig.mini.geom.Extent;
import es.prodevelop.gvsig.mini.geom.Point;

public class PanoramioDriver extends JSONDriver {

	private String url = "http://www.panoramio.com/map/get_panoramas.php?set=public&from=0&to=50&minx=_MINX_&miny=_MINY_&maxx=_MAXX_&maxy=_MAXY_&size=small&mapfilter=true";

	public final static String UPLOAD_DATE = "upload_date";
	public final static String OWNER_NAME = "owner_name";
	public final static String HEIGHT = "height";
	public final static String WIDTH = "width";
	public final static String PHOTO_FILE_URL = "photo_file_url";
	public final static String PHOTO_URL = "photo_url";
	public final static String LONGITUDE = "longitude";
	public final static String LATITUDE = "latitude";
	public final static String PHOTO_TITLE = "photo_title";

	public final static HashMap<String, Integer> fieldTypes = new HashMap<String, Integer>();

	static {
		fieldTypes.put(UPLOAD_DATE, Attribute.TYPE_DATE);
		fieldTypes.put(OWNER_NAME, Attribute.TYPE_STRING);
		fieldTypes.put(HEIGHT, Attribute.TYPE_INT);
		fieldTypes.put(WIDTH, Attribute.TYPE_INT);
		fieldTypes.put(PHOTO_FILE_URL, Attribute.TYPE_IMG_URL);
		fieldTypes.put(PHOTO_URL, Attribute.TYPE_IMG_URL);
	}
		
	public PanoramioDriver() {
		keys = new String[6];
		int i = 0;		
		keys[i++] = UPLOAD_DATE;		
		keys[i++] = OWNER_NAME;		
		keys[i++] = HEIGHT;		
		keys[i++] = WIDTH;		
		keys[i++] = PHOTO_FILE_URL;		
		keys[i++] = PHOTO_URL;
	}

	@Override
	public String buildQuery(Extent boundingBox) {
		
		return url.replace(MINX, "" + boundingBox.getMinX())
				.replace(MINY, "" + boundingBox.getMinY())
				.replace(MAXX, "" + boundingBox.getMaxX())
				.replace(MAXY, "" + boundingBox.getMaxY());
	}

	@Override
	public JSONArray getJSONArray(JSONObject o) throws JSONException {
		return o.getJSONArray("photos");
	}

	@Override
	public Point processResult(JSONObject object) {
		String uploadDate = null;
		String ownerName = null;
		int height = 0;
		int width = 0;
		String previewUrl = null;
		String photoUrl = null;
		double lat = 0, lon = 0;
		String photoTitle = null;

		JSONArray names = object.names();

		try {
			uploadDate = object.getString(UPLOAD_DATE);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		try {
			ownerName = object.getString(OWNER_NAME);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		try {
			height = object.getInt(HEIGHT);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		try {
			width = object.getInt(WIDTH);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		try {
			previewUrl = object.getString(PHOTO_FILE_URL);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		try {
			photoUrl = object.getString(PHOTO_URL);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		try {
			lat = object.getDouble(LATITUDE);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		try {
			lon = object.getDouble(LONGITUDE);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		try {
			photoTitle = object.getString(PHOTO_TITLE);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		if (lat == 0 || lon == 0)
			return null;

		Point point = new Point(lon, lat);
		FeatureAttribute t = new FeatureAttribute(point);
		t.setText(photoTitle);

		String[] keys = this.keys;
		String[] values = new String[6];

		int i = 0;
		values[i++] = uploadDate;		
		values[i++] = ownerName;		
		values[i++] = String.valueOf(height);		
		values[i++] = String.valueOf(width);		
		values[i++] = previewUrl;		
		values[i++] = photoUrl;		

		t.addFields(keys, values, fieldTypes);
		// PanoramioPoint p = new PanoramioPoint();
		// p.setUploadDate(uploadDate);
		// p.setOwnerName(ownerName);
		// p.setHeight(height);
		// p.setWidth(width);
		// p.setPreviewUrl(previewUrl);
		// p.setPhotoUrl(photoUrl);
		// p.setX(lon);
		// p.setY(lat);
		return point;
	}

	public String getName() {
		return "panoramio";
	}
}
