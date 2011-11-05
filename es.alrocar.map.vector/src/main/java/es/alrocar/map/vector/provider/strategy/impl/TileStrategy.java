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

import es.alrocar.map.vector.provider.driver.ProviderDriver;
import es.alrocar.map.vector.provider.observer.VectorialProviderListener;
import es.prodevelop.gvsig.mini.geom.Extent;
import es.prodevelop.gvsig.mini.utiles.Cancellable;
import es.prodevelop.tilecache.renderer.MapRenderer;

/**
 * 
 * @author albertoromeu
 * 
 */
public class TileStrategy extends BaseStrategy {

	public void getVectorialData(int[][] tiles, int zoomLevel,
			Extent viewExtent, VectorialProviderListener observer,
			Cancellable cancellable) {
		try {
			final int size = tiles.length;

			final ProviderDriver driver = getProvider().getDriver();
			final MapRenderer renderer = getProvider().getRenderer();
			for (int i = 0; i < size; i++) {
				if (cancellable != null && cancellable.getCanceled())
					return;
				if (driver.needsExtentToWork()) {
					if (tiles[i][0] != 0 && tiles[i][1] != 0) {
						Extent bbox = renderer.getTileExtent(tiles[i][0],
								tiles[i][1], renderer.resolutions[zoomLevel],
								-renderer.getOriginX(), -renderer.getOriginY());
						bbox = convertExtent(bbox);
						notifyObserver(observer, tiles[i], cancellable,
								driver.getData(tiles[i], bbox, cancellable, zoomLevel), zoomLevel);
					}
				} else {
					notifyObserver(observer, tiles[i], cancellable,
							driver.getData(tiles[i], null, cancellable, zoomLevel), zoomLevel);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
