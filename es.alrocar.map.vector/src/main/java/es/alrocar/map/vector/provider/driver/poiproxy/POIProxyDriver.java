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

package es.alrocar.map.vector.provider.driver.poiproxy;

import java.util.ArrayList;

import es.alrocar.map.vector.provider.VectorialProvider;
import es.alrocar.map.vector.provider.driver.impl.BaseDriver;
import es.alrocar.map.vector.provider.filesystem.impl.GeoJSONParser;
import es.alrocar.map.vector.provider.observer.VectorialProviderListener;
import es.prodevelop.gvsig.mini.geom.Extent;
import es.prodevelop.gvsig.mini.utiles.Cancellable;

public class POIProxyDriver extends BaseDriver {

	private String name = "";
	private String url = "http://poiproxy.mapps.es/browse?service=__NAME__&z=__Z__&x=__X__&y=__Y__";
	private VectorialProviderListener listener;
	private GeoJSONParser parser = new GeoJSONParser();

	public POIProxyDriver(String name, String fileSystemPath) {
		super(fileSystemPath);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public void setProvider(VectorialProvider provider) {
		super.setProvider(provider);
		this.listener = provider;
	}

	@Override
	public boolean needsExtentToWork() {
		return false;
	}

	public ArrayList getData(int[] tile, Extent booundingBox,
			Cancellable cancellable, int zoomLevel) {

		String json = null;
		String url = null;
		ArrayList result = new ArrayList();

		try {
			if (cancellable != null && cancellable.getCanceled())
				return null;

			if (tile != null) {
				url = this.url.replace("__NAME__", name)
						.replace("__Z__", String.valueOf(zoomLevel))
						.replace("__X__", String.valueOf(tile[0]))
						.replace("__Y__", String.valueOf(tile[1]));

				json = download(url);

				if (listener != null) {
					if (cancellable != null && cancellable.getCanceled())
						return null;
					listener.onRawDataRetrieved(tile, json, cancellable, this,
							zoomLevel);
				}

				if (cancellable != null && cancellable.getCanceled())
					return null;
				result = parser.parse(json);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			json = null;
			url = null;
			return result;
		}
	}
}
