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

package es.alrocar.map.vector.provider.driver;

import java.util.ArrayList;

import es.alrocar.map.vector.provider.VectorialProvider;
import es.alrocar.map.vector.provider.filesystem.IVectorFileSystemProvider;
import es.prodevelop.gvsig.mini.geom.Extent;
import es.prodevelop.gvsig.mini.utiles.Cancellable;

public interface ProviderDriver {

	public ArrayList getData(int[] tile, Extent booundingBox,
			Cancellable cancellable, int zoom);

	public boolean needsExtentToWork();

	public String getSRS();

	public VectorialProvider getProvider();

	public void setProvider(VectorialProvider provider);

	public String getName();
	
	public IVectorFileSystemProvider getFileSystemProvider();

//	public void write(int[] tile, int zoomLevel, ArrayList data,
//			Cancellable cancellable);
//
//	public void write(int[] tile, int zoomLevel, InputStream in,
//			Cancellable cancellable);
//
//	public ArrayList loadFromDisk(int[] tile, Extent boundingBox,
//			Cancellable cancellable);
//
//	public boolean supportsLocalCache();
//
//	public boolean isOverwriteLocalCache();
//
//	public boolean isLocalCacheEnable();
}
