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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author albertoromeu
 *
 */
public class LRUVectorTileCache extends HashMap<String, Object> {

	private static final long serialVersionUID = 6215141;
	
	private final int maxCacheSize;

	private final LinkedList<String> list;
	private final static Logger log = Logger.getLogger("LRUTileCache");

	/**
	 * The Constructor
	 * @param maxCacheSize The cache size in number of elements
	 */
	public LRUVectorTileCache(final int maxCacheSize) {
		super(maxCacheSize);
		this.maxCacheSize = Math.max(0, maxCacheSize);
		this.list = new LinkedList<String>();
	}

	/**
	 * Clears the cache
	 */
	public synchronized void clear() {		
		try {
			super.clear();
			list.clear();
		} catch (Exception e) {
			log.log(Level.SEVERE, "clear cache: ", e);
		}
	}

	/**
	 * Adds an Object to the cache. If the cache is full, removes the last
	 */
	public synchronized Object put(final String key, final Object value) {
		try {
			if (maxCacheSize == 0) {
				return null;
			}

			// if the key isn't in the cache and the cache is full...
			if (!super.containsKey(key) && !list.isEmpty()
					&& list.size() + 1 > maxCacheSize) {
				final Object deadKey = list.removeLast();
				super.remove(deadKey);				
			}

			updateKey(key);			
		} catch (Exception e) {
			log.log(Level.SEVERE, "put", e);
		}
		return super.put(key, value);		
	}

	/**
	 * Returns a cached Object given a key
	 * @param key The key of the Object stored on the HashMap
	 * @return The Object or null if it is not stored in the cache
	 */
	public synchronized Object get(final String key) {
		try {
			final Object value = super.get(key);
			if (value != null) {
				updateKey(key);
			}
			return value;
		} catch (Exception e) {
			log.log(Level.SEVERE, "get",  e);
			return null;
		}		
	} 

	/**
	 * Removes a Object from the cache
	 * @param key The key of the Bitmap to remove
	 */
	public synchronized void remove(final String key) {
		try {
			list.remove(key);			
		} catch (Exception e) {
			log.log(Level.SEVERE, "remove", e);			
		}
		super.remove(key);		
	}

	/**
	 * The key is touched (recent used) and added to the top of the list
	 * @param key The key to be updated
	 */
	private void updateKey(final String key) {
		try {
			list.remove(key);
			list.addFirst(key);
		} catch (Exception e) {
			log.log(Level.SEVERE, "updatekey", e);
		}		
	}
}
