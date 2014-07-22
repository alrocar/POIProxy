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
	 * 
	 * @param maxCacheSize
	 *            The cache size in number of elements
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
	 * 
	 * @param key
	 *            The key of the Object stored on the HashMap
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
			log.log(Level.SEVERE, "get", e);
			return null;
		}
	}

	/**
	 * Removes a Object from the cache
	 * 
	 * @param key
	 *            The key of the Bitmap to remove
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
	 * 
	 * @param key
	 *            The key to be updated
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
