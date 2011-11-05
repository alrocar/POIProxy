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

import es.alrocar.map.vector.provider.driver.ProviderDriver;
import es.alrocar.map.vector.provider.observer.VectorialProviderListener;
import es.prodevelop.gvsig.mini.geom.Extent;
import es.prodevelop.gvsig.mini.geom.Point;
import es.prodevelop.gvsig.mini.utiles.Cancellable;
import es.prodevelop.tilecache.renderer.MapRenderer;

/**
 * 
 * @author albertoromeu
 * 
 */
public class BBoxTiledStrategy extends BaseStrategy {

	private Extent viewExtent;
	private int[][] tiles;
	private int zoomLevel;

	public void getVectorialData(int[][] tiles, int zoomLevel,
			Extent viewExtent, VectorialProviderListener observer,
			Cancellable cancellable) {
		try {
			cacheValues(tiles, zoomLevel, viewExtent);
			final int size = tiles.length;

			final ProviderDriver driver = getProvider().getDriver();
			// final MapRenderer renderer = getProvider().getRenderer();

			if (driver.needsExtentToWork()) {
				// FIXME calc extent from tiles
				Extent convertedExtent = convertExtent(viewExtent);
				notifyObserver(observer, tiles[0], cancellable,
						driver.getData(null, convertedExtent, cancellable, zoomLevel), zoomLevel);
				// observer.onVectorDataRetrieved(tiles[0],
				// driver.getData(null, extent, cancellable), cancellable);
			} else {
				for (int i = 0; i < size; i++) {
					if (cancellable != null && cancellable.getCanceled())
						return;
					notifyObserver(observer, tiles[i], cancellable,
							driver.getData(tiles[i], null, cancellable, zoomLevel), zoomLevel);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void cacheValues(int[][] tiles, int zoomLevel, Extent viewExtent) {
		this.tiles = tiles;
		this.viewExtent = viewExtent;
		this.zoomLevel = zoomLevel;
	}

	public void notifyObserver(VectorialProviderListener observer, int[] tile,
			Cancellable cancellable, ArrayList data, int zoomLevel) {

		convertCoordinates(data);

		ArrayList<TileExtent> extents = new ArrayList<TileExtent>();
		int length = tiles.length;

		int[] t;
		for (int i = 0; i < length; i++) {
			t = tiles[i];
			TileExtent te = new TileExtent(t, getProvider().getRenderer(),
					zoomLevel);
			extents.add(te);
		}

		final int size = data.size();
		Point p;
		TileExtent te;
		for (int i = 0; i < size; i++) {
			p = (Point) data.get(i);
			for (int j = 0; j < length; j++) {
				te = extents.get(j);
				if (te.extent.contains(p)) {
					te.points.add(p);
					break;
				}
			}
		}

		for (int i = 0; i < length; i++) {
			te = extents.get(i);
			observer.onVectorDataRetrieved(te.tile, te.points, cancellable, zoomLevel);
		}
	}

	public static class TileExtent {
		public int[] tile;
		public Extent extent;
		public ArrayList points = new ArrayList();

		public TileExtent(int[] tile, MapRenderer renderer, int zoomLevel) {
			this.tile = tile;
			extent = renderer.getTileExtent(tile[0], tile[1],
					renderer.resolutions[zoomLevel], -renderer.getOriginX(),
					-renderer.getOriginY());
		}
	}
}
