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
