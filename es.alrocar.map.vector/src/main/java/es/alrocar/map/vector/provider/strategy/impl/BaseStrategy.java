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
