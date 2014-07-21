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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author albertoromeu
 *
 */
public class VectorTileCache {

	protected LRUVectorTileCache mCachedTiles;
	private final static Logger log = Logger.getLogger("TileCache");
	public final static int DEFAULT_CACHE_SIZE = 15;

	public VectorTileCache(final int cacheSize) {
		try {
			if (cacheSize != 0)
				this.mCachedTiles = new LRUVectorTileCache(cacheSize);
			else
				this.mCachedTiles = new LRUVectorTileCache(DEFAULT_CACHE_SIZE);
		} catch (Exception e) {
			log.log(Level.SEVERE, "constructor", e);
		}
	}

	public synchronized ArrayList getTile(final String aTileURLString) {
		return (ArrayList)this.mCachedTiles.get(aTileURLString);
	}

	public synchronized void putTile(final String aTileURLString,
			final Object aTile) {
		this.mCachedTiles.put(aTileURLString, aTile);
	}

	/**
	 * This method clears the memory cache
	 */
	public synchronized void onLowMemory() {
		try {
			this.mCachedTiles.clear();
		} catch (Exception e) {
			log.log(Level.SEVERE, "onLowMemory", e);
		}
	}

	public void destroy() {
		try {
			onLowMemory();
			this.mCachedTiles = null;
		} catch (Exception e) {
			log.log(Level.SEVERE, "destroy", e);
		}
	}
}
