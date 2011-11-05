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

package es.alrocar.map.vector.provider.driver.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import es.alrocar.map.vector.provider.VectorialProvider;
import es.alrocar.map.vector.provider.driver.ProviderDriver;
import es.alrocar.map.vector.provider.filesystem.IVectorFileSystemProvider;
import es.alrocar.map.vector.provider.filesystem.impl.GeoJSONFileSystemProvider;
import es.prodevelop.geodetic.utils.conversion.ConversionCoords;
import es.prodevelop.gvsig.mini.geom.Extent;
import es.prodevelop.gvsig.mini.geom.Point;
import es.prodevelop.gvsig.mini.utiles.Calculator;
import es.prodevelop.gvsig.mini.utiles.Constants;
import es.prodevelop.gvsig.mobile.fmap.proj.CRSFactory;

public abstract class BaseDriver implements ProviderDriver {

	private VectorialProvider provider;
	private IVectorFileSystemProvider fsProvider;
	public final String MINX = "_MINX_";
	public final String MINY = "_MINY_";
	public final String MAXX = "_MAXX_";
	public final String MAXY = "_MAXY_";

	public final String LAT = "_LAT_";
	public final String LON = "_LON_";

	public final String DIST = "_DIST_";

	public final String MAXRESULTS = "_MAXRESULTS_";
	
	private String path;
	
	public BaseDriver() {
		path = "";
	}
	
	public BaseDriver(String fileSystemPath) {
		path = fileSystemPath;
	}

	public String getExtent(int[] tile, Extent extent) {
		if (extent != null) {
			return formatExtent(extent);
		} else {
			return formatExtent(getProvider().getRenderer().getTileExtent(
					tile[0],
					tile[1],
					getProvider().getRenderer().resolutions[getProvider()
							.getRenderer().getZoomLevel()],
					getProvider().getRenderer().getOriginX(),
					getProvider().getRenderer().getOriginY()));
		}
	}

	public String formatExtent(Extent extent) {
		return extent.toString();
	}

	public boolean needsExtentToWork() {
		return true;
	}

	public String getSRS() {
		return "EPSG:4326";
	}

	public VectorialProvider getProvider() {
		return provider;
	}

	public void setProvider(VectorialProvider provider) {
		this.provider = provider;
	}

	public Extent convertExtent(Extent extent) {
		Point minXY = extent.getLefBottomCoordinate();
		Point maxXY = extent.getRightTopCoordinate();

		double[] miXY = ConversionCoords.reproject(minXY.getX(), minXY.getY(),
				CRSFactory.getCRS(getProvider().getRenderer().getSRS()),
				CRSFactory.getCRS(getProvider().getDriver().getSRS()));

		double[] maXY = ConversionCoords.reproject(maxXY.getX(), maxXY.getY(),
				CRSFactory.getCRS(getProvider().getRenderer().getSRS()),
				CRSFactory.getCRS(getProvider().getDriver().getSRS()));

		return new Extent(miXY[0], miXY[1], maXY[0], maXY[1]);
	}

	public String download(String query) {
		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		ByteArrayOutputStream dataStream = null;
		try {
			System.out.println(query);
			in = new BufferedInputStream(openConnection(query),
					Constants.IO_BUFFER_SIZE);
			dataStream = new ByteArrayOutputStream();
			out = new BufferedOutputStream(dataStream, Constants.IO_BUFFER_SIZE);
			byte[] b = new byte[8 * 1024];
			int read;
			int total = 0;
			while ((read = in.read(b)) != -1) {
				total += read;
				out.write(b, 0, read);
			}
			out.flush();
			return dataStream.toString();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			Constants.closeStream(out);
			Constants.closeStream(in);
			Constants.closeStream(dataStream);
		}
	}

	public static InputStream openConnection(String query) throws IOException {
		final URL url = new URL(query.replace(" ", "%20"));
		URLConnection urlconnec = url.openConnection();
		urlconnec.setConnectTimeout(15000);
		urlconnec.setReadTimeout(15000);
		return urlconnec.getInputStream();
	}

	public double getDistanceMeters(Extent boundingBox) {
		return Calculator.latLonDist(boundingBox.getMinX(),
				boundingBox.getMinY(), boundingBox.getMaxX(),
				boundingBox.getMaxY());
	}

	public IVectorFileSystemProvider getFileSystemProvider() {
		if (fsProvider == null) {
			fsProvider = new GeoJSONFileSystemProvider(path);
		}

		return fsProvider;

	}
}
