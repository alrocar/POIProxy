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

package es.alrocar.poiproxy.proxy.utiles;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class Calculator {

	private static final double DEFAULT_THRESHOLD = Math.pow(10, (-10));

	public static final double DEGREES_PER_RADIAN = 180.0 / Math.PI;
	public static final double MERCATOR_EARTH_RADIUS = 6378137.0;
	public static final double MERCATOR_METERS_PER_EQUATOR_DEGREE = Math.PI
			* MERCATOR_EARTH_RADIUS / 180.0;

	// public static Rectangle2D WORLD_MERCATOR = new Rectangle2D.Double(
	// -Math.PI*Calculator.MERCATOR_EARTH_RADIUS,
	// -Math.PI*Calculator.MERCATOR_EARTH_RADIUS,
	// 2 * Math.PI*Calculator.MERCATOR_EARTH_RADIUS,
	// 2 * Math.PI*Calculator.MERCATOR_EARTH_RADIUS);

	public static double latLonDist(double lon1, double lat1, double lon2,
			double lat2) {

		double[] aux;
		try {
			aux = PIGbsl(lon1, lat1, lon2, lat2, Ellipsoid.WGS84);
		} catch (Exception e) {
			// Calculator.debugThis(true, "Error while getting dist: " +
			// e.getMessage(), logger);
			return Double.MAX_VALUE;
		}
		return aux[2];
	}

	public static double[] PIGbsl(double lon1_, double lat1_, double lon2_,
			double lat2_, Ellipsoid elip) throws Exception {
		return PIGbsl(lon1_, lat1_, lon2_, lat2_, elip, DEFAULT_THRESHOLD);
	}

	/**
	 * geodetic Direct Problem. This method returns the distance between two
	 * points on the elipsoide. Calculate the geodetic line between the two
	 * points.
	 * 
	 * @param lon1_
	 *            longitude of the initial point
	 * @param lat1_
	 *            latitude of the initial point
	 * @param lon2_
	 *            longitude of the final point
	 * @param lat2_
	 *            latitude of the final point
	 * @param elip
	 *            Elipsoide
	 * @return azimut1, azimut2, distance in meters
	 * @throws Exception
	 */
	public static double[] PIGbsl(double lon1_, double lat1_, double lon2_,
			double lat2_, Ellipsoid elip, double threshold) throws Exception {

		double _lon1 = lon1_;
		double _lon2 = lon2_;
		double _lat1 = lat1_;
		double _lat2 = lat2_;

		if (lon1_ == 0)
			_lon1 = 0.0000001;
		if (lon2_ == 0)
			_lon2 = 0.0000001;
		if (lat1_ == 0)
			_lat1 = 0.0000001;
		if (lat2_ == 0)
			_lat2 = 0.0000001;

		// ------------------------

		_lon1 = _lon1 * Math.PI / 180.0;
		_lat1 = _lat1 * Math.PI / 180.0;
		_lon2 = _lon2 * Math.PI / 180.0;
		_lat2 = _lat2 * Math.PI / 180.0;

		double a = elip.getA();
		double b = elip.getB();
		double e2 = elip.getPe2();
		double e4 = Math.pow(e2, b);
		double se = elip.getSe();

		/* sphere latitudes */
		double u1 = Math.atan(b / a * Math.tan(_lat1));
		double u2 = Math.atan(b / a * Math.tan(_lat2));

		/* initial data */
		double il = _lon2 - _lon1;
		double wt = il;
		double mas = (u1 + u2) / 2;
		double men = (u1 - u2) / 2;
		int flag = 0;

		double w2;
		double g1;
		double g2;
		double a12 = 0.0;
		double a21 = 0.0;

		double M = 0.0;
		double m = 0.0;
		double sig = 0.0;
		double t11;
		double t12;
		double t1;
		double t2;
		double w;

		/* iteration process */
		int ITERATIONS = 100;

		for (int i = 0; i < ITERATIONS; i++) {
			w2 = wt / 2;
			g1 = Math.cos(men) / (Math.sin(mas) * Math.tan(w2));
			g2 = Math.sin(men) / (Math.cos(mas) * Math.tan(w2));
			a12 = Math.atan(g1) + Math.atan(g2);
			a21 = Math.atan(g2) - Math.atan(g1) + Math.PI;

			while (a12 <= 0) {
				a12 += 2 * Math.PI;
			}
			while (a12 >= 2 * Math.PI) {
				a12 -= 2 * Math.PI;
			}
			while (a21 <= 0) {
				a21 += 2 * Math.PI;
			}
			while (a21 >= 2 * Math.PI) {
				a21 -= 2 * Math.PI;
			}

			M = Math.atan((Math.tan(u1)) / (Math.cos(a12)));
			m = Math.acos((Math.sin(u1)) / (Math.sin(M)));
			sig = Math.acos(Math.sin(u1) * Math.sin(u2) + Math.cos(u1)
					* Math.cos(u2) * Math.cos(wt));
			t11 = e2 / 8;
			t12 = e2 * Math.pow(Math.cos(m), 2) / 16;
			t1 = (e2 * Math.sin(m) * sig * (0.5 + t11 - t12));
			t2 = e4
					* Math.sin(m)
					* (Math.pow(Math.cos(m), 2) * Math.sin(sig) * Math.cos(2
							* M + sig)) / 16;

			/* Control del dominio del incremento de lon */

			w = (il > 0) ? il + t1 + t2 : il - (t1 - t2);

			/* Precision */
			double condi = Math.abs(wt - w);
			if (condi <= threshold) {
				a21 = a21 + Math.PI;

				/* Control dominio azimut */
				if (a21 >= 2 * Math.PI) {
					a21 -= 2 * Math.PI;
				}

				/* Calculo de la geod�sica */

				double k = se * Math.cos(m);
				double tA = 1 + (Math.pow(k, 2) / 4 - 3 * Math.pow(k, 4) / 64);
				double tB = Math.pow(k, 2) / 4 - Math.pow(k, 4) / 16;
				double tC = Math.pow(k, 4) / 128;
				double s = tA * b * sig - tB * b * Math.sin(sig)
						* Math.cos(2 * M + sig) - tC * b * Math.sin(2 * sig)
						* Math.cos(4 * M + 2 * sig);

				double[] result = { a12, a21, s };
				return result;
			} else {
				wt = w;
			}
		}

		// No convergence. It may be because coordinate points
		// are equals or because they are at antipodes.
		final double LEPS = 1E-10;
		if (Math.abs(lon1_ - lon2_) <= LEPS && Math.abs(lat1_ - lat2_) <= LEPS) {
			return new double[] { 0, 0, 0 }; // Coordinate points are equals
		}
		if (Math.abs(lat1_) <= LEPS && Math.abs(lat2_) <= LEPS) {
			return new double[] { 0, Math.PI,
					Math.abs(lon1_ - lon2_) * elip.getA() }; // Points are on
																// the equator.
		}

		throw new Exception("No distance oould be calculated");
	}

	public static double[] mercator2geo(double x, double y) {

		double false_lat_rad = y
				/ (MERCATOR_METERS_PER_EQUATOR_DEGREE * DEGREES_PER_RADIAN);
		double lat_rad = 2.0 * Math.atan(Math.exp(false_lat_rad)) - 0.5
				* Math.PI;
		double lat_deg = DEGREES_PER_RADIAN * lat_rad;
		// double y = 0.5 *
		// Math.atan(sinh(rlat));
		// double y = Math.log(Math.tan(phi) + sec(phi));

		double geo[] = { x / MERCATOR_METERS_PER_EQUATOR_DEGREE, lat_deg };
		return geo;
	}

	// public static Rectangle2D mercator2geo(Rectangle2D r) {
	//
	// double minx = r.getMinX();
	// double miny = r.getMinY();
	// double[] geomin = mercator2geo(minx, miny);
	// double maxx = r.getMaxX();
	// double maxy = r.getMaxY();
	// double[] geomax = mercator2geo(maxx, maxy);
	//
	// Rectangle2D resp = new Rectangle2D.Double(
	// geomin[0],
	// geomin[1],
	// geomax[0] - geomin[0],
	// geomax[1] - geomin[1]);
	//
	// return resp;
	// }

	public static double[] geo2mercator(double lon, double lat) {

		double rlat = lat / DEGREES_PER_RADIAN;

		double y = 0.5 * Math.log((1 + Math.sin(rlat)) / (1 - Math.sin(rlat)));
		// double y = 0.5 *
		// Math.atan(sinh(rlat));
		// double y = Math.log(Math.tan(phi) + sec(phi));

		double mer[] = { MERCATOR_METERS_PER_EQUATOR_DEGREE * lon,
				MERCATOR_METERS_PER_EQUATOR_DEGREE * DEGREES_PER_RADIAN * y };
		return mer;
	}

	// public static Rectangle2D mercatorExtent(int _x, int _y, int _z) {
	//
	// long nt = (long) Math.pow(2, _z);
	// double tw = WORLD_MERCATOR.getWidth() / nt;
	// double tminx = _x * tw - WORLD_MERCATOR.getWidth()/2.0;
	// double tminy = WORLD_MERCATOR.getWidth()/2.0 - (_y+1) * tw;
	// Rectangle2D ext = new Rectangle2D.Double(tminx, tminy, tw, tw);
	// return ext;
	// }
	//
	// public static String[] getTilePaths(Rectangle2D geoview, int z) {
	//
	// long nt = (long) Math.pow(2, z);
	// double tw = WORLD_MERCATOR.getWidth() / nt;
	//
	// double[] merview_minxy = geo2mercator(geoview.getMinX(),
	// geoview.getMinY());
	// double[] merview_maxxy = geo2mercator(geoview.getMaxX(),
	// geoview.getMaxY());
	//
	// double minxmargin = merview_minxy[0] + WORLD_MERCATOR.getWidth()/2.0;
	// if (minxmargin < 0) minxmargin = 0;
	// if (minxmargin > WORLD_MERCATOR.getWidth())
	// minxmargin = WORLD_MERCATOR.getWidth();
	//
	// double maxxmargin = merview_maxxy[0] + WORLD_MERCATOR.getWidth()/2.0;
	// if (maxxmargin < 0) maxxmargin = 0;
	// if (maxxmargin > WORLD_MERCATOR.getWidth())
	// maxxmargin = WORLD_MERCATOR.getWidth();
	//
	// double minyfromtop = WORLD_MERCATOR.getWidth()/2.0 - merview_maxxy[1];
	// if (minyfromtop < 0) minyfromtop = 0;
	// if (minyfromtop > WORLD_MERCATOR.getWidth())
	// minyfromtop = WORLD_MERCATOR.getWidth();
	//
	// double maxyfromtop = WORLD_MERCATOR.getWidth()/2.0 - merview_minxy[0];
	// if (maxyfromtop < 0) maxyfromtop = 0;
	// if (maxyfromtop > WORLD_MERCATOR.getWidth())
	// maxyfromtop = WORLD_MERCATOR.getWidth();
	//
	// long ntile_minx = (long) Math.floor(minxmargin / tw);
	// long ntile_maxx = (long) Math.floor(maxxmargin / tw);
	// long ntile_miny = (long) Math.floor(minyfromtop / tw);
	// long ntile_maxy = (long) Math.floor(maxyfromtop / tw);
	//
	// long nx = 1 + ntile_maxx - ntile_minx;
	// long ny = 1 + ntile_maxy - ntile_miny;
	// String[] resp = new String[(int) (nx*ny)];
	//
	// String zstr = "" + z;
	// int ind = 0;
	// for (long j=ntile_miny; j<=ntile_maxy; j++) {
	// for (long i=ntile_minx; i<=ntile_maxx; i++) {
	// resp[ind++] = zstr + "/" + i + "/" + j;
	// }
	// }
	// return resp;
	// }
	//
	//
	// public static void debugThis(boolean iserr, String str, Logger _logger) {
	//
	// if (!CreateTiles.debugMode) {
	// return;
	// }
	//
	// if (iserr) {
	// System.err.println(str);
	// _logger.error(str);
	// } else {
	// System.out.println(str);
	// _logger.debug(str);
	// }
	//
	//
	// }
	//
	//
	// public static Rectangle2D geo2mercator(Rectangle2D r) {
	//
	// if (r == null) {
	// return null;
	// }
	//
	// double minx = r.getMinX();
	// double miny = r.getMinY();
	// double[] geomin = geo2mercator(minx, miny);
	// double maxx = r.getMaxX();
	// double maxy = r.getMaxY();
	// double[] geomax = geo2mercator(maxx, maxy);
	//
	// Rectangle2D resp = new Rectangle2D.Double(
	// geomin[0],
	// geomin[1],
	// geomax[0] - geomin[0],
	// geomax[1] - geomin[1]);
	//
	// return resp;
	// }

	public static String arrayToString(String[] arr) {
		return arrayToString(arr, true);
	}

	public static String arrayToString(String[] arr, boolean brackets) {

		int sz = arr.length;
		if (sz == 0) {
			return "";
		}

		String resp = brackets ? "[" : "";
		for (int i = 0; i < (sz - 1); i++) {
			resp = resp + arr[i] + ",";
		}

		resp = resp + arr[sz - 1] + (brackets ? "]" : "");
		return resp;
	}

	private static DecimalFormat decimalFormat = new DecimalFormat();

	static {
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		decimalFormat.setDecimalFormatSymbols(dfs);
	}

	public static String format(double v, int decpos) {
		decimalFormat.setMaximumFractionDigits(decpos);
		return decimalFormat.format(v);
	}

	// public static String getMinMax(Rectangle2D r, int decpos, String sepa) {
	//
	// if (r == null) {
	// return "0" + sepa +"0" + sepa +"0" + sepa +"0";
	// }
	//
	// String resp = format(r.getMinX(), decpos)
	// + sepa
	// + format(r.getMinY(), decpos)
	// + sepa
	// + format(r.getMaxX(), decpos)
	// + sepa
	// + format(r.getMaxY(), decpos);
	// return resp;
	// }

	public static int getIndex(String[] arr, String str) {

		if (arr == null || arr.length == 0 || str == null) {
			return -1;
		}

		int len = arr.length;
		for (int i = 0; i < len; i++) {
			if (str.compareToIgnoreCase(arr[i]) == 0) {
				return i;
			}
		}
		return -1;
	}

	// public static boolean hasFilesWithExtension(File folder, String ext) {
	//
	// if (folder.isDirectory()) {
	// String[] ff = folder.list();
	// for (int i=0; i<ff.length; i++) {
	// if (ff[i].indexOf("." + ext) != -1) {
	// return true;
	// }
	// }
	// }
	// return false;
	//
	// }

	public static String replaceBadChars(String str, boolean b) {

		if (str == null) {
			return str;
		}

		String resp = str.replace("\"", "\\\"");
		return resp;
	}

	// public static int whichZExplodesGeogDist(double d, double allowed_pixels)
	// {
	//
	// if (d > (2*allowed_pixels)) {
	// // fast ... d is BIG
	// return 0;
	// }
	//
	// int testz = 0;
	// try {
	// while ((TreeBuilder.TILE_SIZE * d * Math.pow(2.0, testz) / 360.0) <
	// allowed_pixels) {
	// testz++;
	// }
	// } catch (Throwable th) {
	// Calculator.debugThis(true, "While doing WHICH Z: " + th.getMessage(),
	// logger);
	// Calculator.debugThis(false, "Z set to 20", logger);
	// testz = 20;
	// }
	// return testz;
	// }

	public static String toString(Object v) {

		if (v instanceof String) {
			return (String) v;
		} else {
			return v.toString();
		}
	}

	private static final double MIN_LAT = Math.toRadians(-90d); // -PI/2
	private static final double MAX_LAT = Math.toRadians(90d); // PI/2
	private static final double MIN_LON = Math.toRadians(-180d); // -PI
	private static final double MAX_LON = Math.toRadians(180d); // PI

	/**
	 * 
	 * @param lat
	 *            latitude in degrees
	 * @param lon
	 *            longitude in degrees
	 * @param distance
	 *            Distance in meters
	 * @return
	 */
	public static double[] boundingCoordinates(double lon, double lat,
			double distance) {

		double[] lonlat = fromDegrees(lon, lat);

		if (distance < 0d)
			throw new IllegalArgumentException();

		// angular distance in radians on a great circle
		double radDist = distance / MERCATOR_EARTH_RADIUS;

		double minLat = lonlat[1] - radDist;
		double maxLat = lonlat[1] + radDist;

		double minLon, maxLon;
		if (minLat > MIN_LAT && maxLat < MAX_LAT) {
			double deltaLon = Math
					.asin(Math.sin(radDist) / Math.cos(lonlat[1]));
			minLon = lonlat[0] - deltaLon;
			if (minLon < MIN_LON)
				minLon += 2d * Math.PI;
			maxLon = lonlat[0] + deltaLon;
			if (maxLon > MAX_LON)
				maxLon -= 2d * Math.PI;
		} else {
			// a pole is within the distance
			minLat = Math.max(minLat, MIN_LAT);
			maxLat = Math.min(maxLat, MAX_LAT);
			minLon = MIN_LON;
			maxLon = MAX_LON;
		}

		double[] minLonLat = fromRadians(minLon, minLat);
		double[] maxLonLat = fromRadians(maxLon, maxLat);
		return new double[] { minLonLat[0], minLonLat[1], maxLonLat[0],
				maxLonLat[1] };
	}

	/**
	 * @param latitude
	 *            the latitude, in degrees.
	 * @param longitude
	 *            the longitude, in degrees.
	 */
	public static double[] fromDegrees(double longitude, double latitude) {
		double radLat = Math.toRadians(latitude);
		double radLon = Math.toRadians(longitude);

		return new double[] { radLon, radLat };
	}

	/**
	 * @param latitude
	 *            the latitude, in radians.
	 * @param longitude
	 *            the longitude, in radians.
	 */
	public static double[] fromRadians(double longitude, double latitude) {
		double degLat = Math.toDegrees(latitude);
		double degLon = Math.toDegrees(longitude);

		return new double[] { degLon, degLat };
	}

}
