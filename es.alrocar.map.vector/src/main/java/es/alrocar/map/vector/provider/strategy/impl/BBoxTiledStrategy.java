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
