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

import java.text.DecimalFormat;

import junit.framework.TestCase;
import es.prodevelop.geodetic.utils.conversion.ConversionCoords;
import es.prodevelop.gvsig.mobile.fmap.proj.CRSFactory;

public class TestGeotools extends TestCase {

	public void test_transform() throws Exception {

		double[] point = new double[] { 123234.23, 2433455.34 };
		DecimalFormat format = new DecimalFormat("#.####");

		double[] minXY = GeotoolsUtils.transform("EPSG:900913", "EPSG:4326",
				point);

		double[] anotherMinXY = ConversionCoords.reproject(point[0], point[1],
				CRSFactory.getCRS("EPSG:900913"),
				CRSFactory.getCRS("EPSG:4326"));

		assertEquals(format.format(minXY[0]), format.format(anotherMinXY[0]));
		assertEquals(format.format(minXY[1]), format.format(anotherMinXY[1]));

		System.out.println("");

	}

	public void test_transform2() throws Exception {

		double[] point = new double[] { 2.1641534, 41.3829945 };

		double[] minXY = GeotoolsUtils.transform("EPSG:4326", "EPSG:23031",
				point);

		double[] anotherMinXY = ConversionCoords
				.reproject(point[0], point[1], CRSFactory.getCRS("EPSG:4326"),
						CRSFactory.getCRS("EPSG:23031"));

		assertTrue(Math.abs(minXY[0] - anotherMinXY[0]) < 10);
		assertTrue(Math.abs(minXY[1] - anotherMinXY[1]) < 10);

		System.out.println("");

	}
}
