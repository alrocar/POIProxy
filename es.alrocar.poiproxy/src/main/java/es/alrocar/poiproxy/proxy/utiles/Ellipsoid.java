package es.alrocar.poiproxy.proxy.utiles;

/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
*
* Copyright (C) 2006 Prodevelop and Generalitat Valenciana.
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
*   Generalitat Valenciana
*   Conselleria d'Infraestructures i Transport
*   Av. Blasco Ib��ez, 50
*   46010 VALENCIA
*   SPAIN
*
*   +34 963862235
*   gvsig@gva.es
*   http://www.gvsig.gva.es
*
*    or
*
*   Prodevelop Integraci�n de Tecnolog�as SL
*   Conde Salvatierra de �lava , 34-10
*   46004 Valencia
*   Spain
*
*   +34 963 510 612
*   +34 963 510 968
*   gis@prodevelop.es
*   http://www.prodevelop.es
*
*    or
*
*   Instituto de Rob�tica
*   Apartado de correos 2085
*   46071 Valencia
*   (Spain)
*   
*   +34 963 543 577
*   jjordan@robotica.uv.es
*   http://robotica.uv.es
*   
*/


/**
* Ellipsoide ED50 or WGS84
* @author vsanjaime
*
*/
public class Ellipsoid {
	
	public static final Ellipsoid WGS84 = new Ellipsoid(6378137.0,1/298.257223563);
	public static final Ellipsoid ED50 = new Ellipsoid(6378388.0,1/297.0);
	public static final Ellipsoid INTL_1924 = new Ellipsoid(6378388.0,1.0/297.0);

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
	 * @param _a  Semimayor Axis elipsoide
	 * @param _f  Flattening elipsoide
	 */
	public Ellipsoid(double _a, double _f){
		this.a = _a;
		this.f = _f;

		b = a*(1-f);
		pe = Math.sqrt(((Math.pow(a,2))-(Math.pow(b,2)))/((Math.pow(a,2))));
		se = Math.sqrt(((Math.pow(a,2))-(Math.pow(b,2)))/((Math.pow(b,2))));		
		pe2 = Math.pow(pe,2);
		se2 = Math.pow(se,2);
		c = (Math.pow(a,2))/b;
	}

	
	/**
	 * 
	 * @param lat = latitude
	 * @return double[] = {rm, rn, rg}
	 */
	public double[] radios (double lat){
		
		double rlat = lat*Math.PI/180;
		double denomin = (1-pe2*(Math.pow(Math.sin(rlat),2)));
		/* radio meridiano*/
		double rm = (a*(1-pe2))/(Math.pow(denomin, 1.5));
		/* radio primer vertical*/
		double rn = a/(Math.sqrt(denomin));
		/* radio gaussiano */
		double rg = Math.sqrt(rm*rn);
		
		double[] lRadios = {rm, rn, rg}; 
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
