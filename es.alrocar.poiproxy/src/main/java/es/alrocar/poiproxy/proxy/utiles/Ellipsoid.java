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

/**
 * Ellipsoide ED50 or WGS84
 * 
 * @author vsanjaime
 * 
 */
public class Ellipsoid {

	public static final Ellipsoid WGS84 = new Ellipsoid(6378137.0,
			1 / 298.257223563);
	public static final Ellipsoid ED50 = new Ellipsoid(6378388.0, 1 / 297.0);
	public static final Ellipsoid INTL_1924 = new Ellipsoid(6378388.0,
			1.0 / 297.0);

	/**
	 * Semimayor Axis
	 */
	private double a;
	/**
	 * Flattening
	 */
	private double f;
	/**
	 * Semiminor Axis
	 */
	private double b;
	/**
	 * First Excentricity
	 */
	private double pe;
	/**
	 * First Excentricity ^2
	 */
	private double pe2;
	/**
	 * Second Excentricity
	 */
	private double se;
	/**
	 * Second Excentricity ^2
	 */
	private double se2;
	/**
	 * Radio de curvatura polar
	 */
	private double c;

	/**
	 * Constructor
	 * 
	 * @param _a
	 *            Semimayor Axis elipsoide
	 * @param _f
	 *            Flattening elipsoide
	 */
	public Ellipsoid(double _a, double _f) {
		this.a = _a;
		this.f = _f;

		b = a * (1 - f);
		pe = Math.sqrt(((Math.pow(a, 2)) - (Math.pow(b, 2)))
				/ ((Math.pow(a, 2))));
		se = Math.sqrt(((Math.pow(a, 2)) - (Math.pow(b, 2)))
				/ ((Math.pow(b, 2))));
		pe2 = Math.pow(pe, 2);
		se2 = Math.pow(se, 2);
		c = (Math.pow(a, 2)) / b;
	}

	/**
	 * 
	 * @param lat
	 *            = latitude
	 * @return double[] = {rm, rn, rg}
	 */
	public double[] radios(double lat) {

		double rlat = lat * Math.PI / 180;
		double denomin = (1 - pe2 * (Math.pow(Math.sin(rlat), 2)));
		/* radio meridiano */
		double rm = (a * (1 - pe2)) / (Math.pow(denomin, 1.5));
		/* radio primer vertical */
		double rn = a / (Math.sqrt(denomin));
		/* radio gaussiano */
		double rg = Math.sqrt(rm * rn);

		double[] lRadios = { rm, rn, rg };
		return lRadios;

	}

	/**
	 * Get Semimayor Axis
	 */
	public double getA() {
		return a;
	}

	/**
	 * Get Semiminor Axis
	 */
	public double getB() {
		return b;
	}

	/**
	 * Get Flattening
	 */
	public double getF() {
		return f;
	}

	/**
	 * Get First Excentricity
	 */
	public double getPe() {
		return pe;
	}

	/**
	 * Get First Excentricity ^2
	 */
	public double getPe2() {
		return pe2;
	}

	/**
	 * Get Second Excentricity
	 */
	public double getSe() {
		return se;
	}

	/**
	 * Get Second Excentricity ^2
	 */
	public double getSe2() {
		return se2;
	}

	/**
	 * Get Radio de curvatura polar
	 */
	public double getC() {
		return c;
	}

}
