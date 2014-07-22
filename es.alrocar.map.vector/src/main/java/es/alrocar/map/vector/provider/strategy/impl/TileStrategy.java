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
						if (cancellable != null && cancellable.getCanceled())
							return;
						Extent bbox = renderer.getTileExtent(tiles[i][0],
								tiles[i][1], renderer.resolutions[zoomLevel],
								-renderer.getOriginX(), -renderer.getOriginY());
						bbox = convertExtent(bbox);
						notifyObserver(observer, tiles[i], cancellable,
								driver.getData(tiles[i], bbox, cancellable,
										zoomLevel), zoomLevel);
					}
				} else {
					if (cancellable != null && cancellable.getCanceled())
						return;
					notifyObserver(observer, tiles[i], cancellable,
							driver.getData(tiles[i], null, cancellable,
									zoomLevel), zoomLevel);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
