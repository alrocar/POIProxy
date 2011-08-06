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

package es.alrocar.map.vector.provider;

import java.util.ArrayList;
import java.util.HashMap;
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
