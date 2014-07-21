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

package es.alrocar.poiproxy.geotools;

import org.geotools.geometry.DirectPosition2D;
import org.geotools.referencing.CRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

public class GeotoolsUtils {

	static {
		System.setProperty("org.geotools.referencing.forceXY", "true");
	}

	public static double[] transform(String from, String to, double[] xy) {
		try {
			if (from == null || to == null) {
				return xy;
			}
			
			if (from.compareToIgnoreCase("EPSG:4326") == 0) {
				// E6 support
				if (xy[0] > 181 || xy[0] < -181) {
					xy[0] /= 1000000;
				}
				if (xy[1] > 91 || xy[1] < -91) {
					xy[1] /= 1000000;
				}
			}

			if (from.equals(to)) {
				return xy;
			}

			CoordinateReferenceSystem from_crs = CRS.decode(from);

			// The 'true' means:
			// "I'm going to use the order (longitude, latitude)"
			CoordinateReferenceSystem to_crs = CRS.decode(to, true);

			// The 'false' means:
			// "There will be an exception if Geotools doesn't know how to do it"
			MathTransform transform1 = CRS.findMathTransform(from_crs, to_crs,
					false);

			DirectPosition2D from_point = new DirectPosition2D(xy[0], xy[1]);
			DirectPosition2D to_point = new DirectPosition2D(0, 0);

			transform1.transform(from_point, to_point);

			return new double[] { to_point.x, to_point.y };

		} catch (Exception exc) {
			exc.printStackTrace();
			return null;
		}
	}

}
