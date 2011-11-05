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

package es.alrocar.map.vector.provider.strategy.impl;

import java.util.ArrayList;

import es.alrocar.map.vector.provider.VectorialProvider;
import es.alrocar.map.vector.provider.driver.ProviderDriver;
import es.alrocar.map.vector.provider.observer.VectorialProviderListener;
import es.alrocar.map.vector.provider.strategy.IVectorProviderStrategy;
import es.prodevelop.geodetic.utils.conversion.ConversionCoords;
import es.prodevelop.gvsig.mini.geom.Extent;
import es.prodevelop.gvsig.mini.geom.Point;
import es.prodevelop.gvsig.mini.utiles.Cancellable;
import es.prodevelop.gvsig.mobile.fmap.proj.CRSFactory;
import es.prodevelop.tilecache.renderer.MapRenderer;

/**
 * 
 * @author albertoromeu
 * 
 */
public abstract class BaseStrategy implements IVectorProviderStrategy {

	private VectorialProvider provider;

	public VectorialProvider getProvider() {
		return provider;
	}

	public void setProvider(VectorialProvider provider) {
		this.provider = provider;
	}

	public Extent convertExtent(Extent extent) {
		return convertExtent(extent, getProvider().getRenderer().getSRS());
	}

	public Extent convertExtent(Extent extent, String fromSRS) {
		Point minXY = extent.getLefBottomCoordinate();
		Point maxXY = extent.getRightTopCoordinate();

		double[] miXY = ConversionCoords.reproject(minXY.getX(), minXY.getY(),
				CRSFactory.getCRS(fromSRS),
				CRSFactory.getCRS(getProvider().getDriver().getSRS()));

		double[] maXY = ConversionCoords.reproject(maxXY.getX(), maxXY.getY(),
				CRSFactory.getCRS(fromSRS),
				CRSFactory.getCRS(getProvider().getDriver().getSRS()));

		return new Extent(miXY[0], miXY[1], maXY[0], maXY[1]);
	}

	public void convertCoordinates(ArrayList data) {
		if (data == null) return;
		MapRenderer renderer = getProvider().getRenderer();
		ProviderDriver driver = getProvider().getDriver();

		final int size = data.size();
		Point p;
		Point temp;
		for (int i = 0; i < size; i++) {
			// if (cancellable != null && cancellable.getCanceled())
			// return;
			p = (Point) data.get(i);
			final double[] xy = ConversionCoords.reproject(p.getX(), p.getY(),
					CRSFactory.getCRS(driver.getSRS()),
					CRSFactory.getCRS(renderer.getSRS()));
			p.setX(xy[0]);
			p.setY(xy[1]);
			// pois.set(i, temp);
		}
	}

	public void notifyObserver(VectorialProviderListener observer, int[] tile,
			Cancellable cancellable, ArrayList data, int zoomLevel) {		
		observer.onVectorDataRetrieved(tile, data, cancellable, zoomLevel);
	}
}
