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

package es.alrocar.map.vector.provider.driver;

import java.io.InputStream;
import java.util.ArrayList;

import es.alrocar.map.vector.provider.VectorialProvider;
import es.prodevelop.gvsig.mini.geom.Extent;
import es.prodevelop.gvsig.mini.utiles.Cancellable;

public interface ProviderDriver {

	public ArrayList getData(int[] tile, Extent booundingBox,
			Cancellable cancellable);

	public boolean needsExtentToWork();

	public String getSRS();

	public VectorialProvider getProvider();

	public void setProvider(VectorialProvider provider);

	public String getName();

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
