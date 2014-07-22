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

package es.alrocar.map.vector.provider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.alrocar.map.vector.provider.driver.ProviderDriver;
import es.alrocar.map.vector.provider.observer.VectorialProviderListener;
import es.alrocar.map.vector.provider.strategy.IVectorProviderStrategy;
import es.prodevelop.gvsig.mini.geom.Extent;
import es.prodevelop.gvsig.mini.utiles.Cancellable;
import es.prodevelop.tilecache.renderer.MapRenderer;

/**
 * 
 * @author albertoromeu
 * 
 */
public class VectorialProvider implements VectorialProviderListener {

	private MapRenderer renderer;
	private IVectorProviderStrategy strategy;
	private ProviderDriver driver;
	private VectorTileCache cache;
	private VectorialProviderListener observer;
	private int lastZoomLevel = -1;
	private Cancellable currentCancellable;
	public HashSet<String> mPending = new HashSet<String>();
	private ExecutorService executor = Executors.newCachedThreadPool();

	public VectorialProvider(MapRenderer renderer,
			IVectorProviderStrategy strategy, ProviderDriver driver,
			VectorialProviderListener observer) {
		this.renderer = renderer;
		this.strategy = strategy;
		this.strategy.setProvider(this);
		cache = new VectorTileCache(VectorTileCache.DEFAULT_CACHE_SIZE * 2);
		this.observer = observer;
		this.driver = driver;
		this.driver.setProvider(this);
	}

	public void getDataAsynch(final int[][] tiles, final int zoomLevel,
			final Extent viewExtent, final Cancellable cancellable) {
		try {
			if (zoomLevel < 2)
				return;
//			WorkQueue.getExclusiveInstance().clearPendingTasks();
			executor.execute(new Runnable() {

				public void run() {
					if (lastZoomLevel != zoomLevel) {
						lastZoomLevel = zoomLevel;
						if (currentCancellable != null) {
							currentCancellable.setCanceled(true);
						}
						// cache.mCachedTiles.clear();
						observer.onVectorDataRetrieved(null, null, null,
								zoomLevel);
					}

					currentCancellable = cancellable;

					ArrayList tilesToRetrieve = new ArrayList();
					final int size = tiles.length;

					boolean found = false;
					for (int i = 0; i < size; i++) {
						if (!mPending.contains(format(tiles[i]))) {
							mPending.add(format(tiles[i]));
							found = false;
							if (tiles[i] != null) {
								ArrayList values = cache.getTile(format(tiles[i]));
								if (values == null) {
									values = driver.getFileSystemProvider()
											.load(tiles[i], zoomLevel,
													driver.getName(),
													cancellable);
									if (values == null) {
										tilesToRetrieve.add(tiles[i]);
									} else {
										found = true;
									}
								} else {
									found = true;
								}

								if (found) {
									mPending.remove(format(tiles[i]));
									observer.onVectorDataRetrieved(tiles[i],
											values, cancellable, zoomLevel);
								}
							}
						}
					}
					
					final int length = tilesToRetrieve.size();
					if (length > 0) {
						int[][] t = new int[length][2];

						for (int i = 0; i < length; i++) {
							t[i] = (int[]) tilesToRetrieve.get(i);
						}
						strategy.getVectorialData(t, zoomLevel, viewExtent,
								VectorialProvider.this, cancellable);
					}
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public MapRenderer getRenderer() {
		return this.renderer;
	}

	public ProviderDriver getDriver() {
		return this.driver;
	}

	public void onVectorDataRetrieved(int[] tile, ArrayList data,
			Cancellable cancellable, int zoomLevel) {
		try {
			if (data != null) {
				mPending.remove(format(tile));
				cache.putTile(format(tile), data);

				observer.onVectorDataRetrieved(tile, data, cancellable,
						zoomLevel);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String format(int[] tile) {
		return new StringBuffer().append(tile[0]).append("_").append(tile[1])
				.toString();
	}

	public void onRawDataRetrieved(int[] tile, Object data,
			Cancellable cancellable, ProviderDriver driver, int zoomLevel) {
		if (driver.getFileSystemProvider() != null) {
			if (cancellable != null && cancellable.getCanceled())
				return;
			driver.getFileSystemProvider().save(tile, zoomLevel,
					driver.getName(), cancellable, data);
		}

	}
}
