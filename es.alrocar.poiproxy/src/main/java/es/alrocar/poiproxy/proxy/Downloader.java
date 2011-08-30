/* POIProxy. A proxy service to retrieve POIs from public services
 *
 * Copyright (C) 2011 Alberto Romeu.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
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
 *   aromeu@prodevelop.es
 *   http://www.prodevelop.es
 *   
 *   2011.
 *   author Alberto Romeu aromeu@prodevelop.es  
 *   
 */

package es.alrocar.poiproxy.proxy;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import es.prodevelop.gvsig.mini.utiles.Cancellable;
import es.prodevelop.gvsig.mini.utiles.Constants;

public class Downloader {

	private byte[] xmldata;
	private DownloadListener listener;
	
	public void downloadFromUrl(String mapURL, Cancellable cancellable) throws Exception {
		xmldata = null;
		InputStream in = null;
		OutputStream out = null;
		BufferedInputStream bis = null;
		if (mapURL == null)
			throw new Exception("Error null url");
		try {

						long startTime = System.currentTimeMillis();
			

			in = new BufferedInputStream(openConnection(mapURL),
					Constants.IO_BUFFER_SIZE);
			final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
			out = new BufferedOutputStream(dataStream, Constants.IO_BUFFER_SIZE);
			byte[] b = new byte[8 * 1024];
			int read;
			int total = 0;
			while ((read = in.read(b)) != -1) {
				total += read;				
				out.write(b, 0, read);
				if (listener != null) {
					listener.onBytesDownloaded(read);
				}
				if (cancellable != null && cancellable.getCanceled()) {
					
					
					return;
				}

				// System.gc();
			}
			out.flush();
			xmldata = dataStream.toByteArray();

			
		} catch (IOException e) {
			
			throw new Exception(e);
		} finally {
			Constants.closeStream(in);
			Constants.closeStream(out);
		}
	}

	public void downloadFromUrl(String mapURL, String fileName,
			String downloadPath, Cancellable cancellable) throws Exception {
		xmldata = null;
		InputStream in = null;
		OutputStream out = null;
		BufferedInputStream bis = null;
		if (mapURL == null)
			throw new Exception("Error null url");
		if (fileName == null)
			throw new Exception("Error null name");
		try {

			File directory = new File(downloadPath);
			if (!directory.exists()) {
				directory.mkdirs();
			}

			File file = new File(downloadPath + fileName);
			file.createNewFile();
			long startTime = System.currentTimeMillis();
			
			in = new BufferedInputStream(openConnection(mapURL + fileName),
					Constants.IO_BUFFER_SIZE);
			final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
			out = new BufferedOutputStream(dataStream, Constants.IO_BUFFER_SIZE);
			byte[] b = new byte[8 * 1024];
			int read;
			int total = 0;
			while ((read = in.read(b)) != -1) {
				total += read;
				out.write(b, 0, read);
				if (listener != null) {
					listener.onBytesDownloaded(read);
				}
				if (cancellable != null && cancellable.getCanceled()) {
					
					file.delete();
					return;
				}

				// System.gc();
			}
			out.flush();
			xmldata = dataStream.toByteArray();

			FileOutputStream fileout = new FileOutputStream(file);
			fileout.write(xmldata);
			fileout.close();
		

		} catch (IOException e) {
//			Log.d("DownloadLog", "Error: " + e);
			throw new Exception(e);
		} finally {
			Constants.closeStream(in);
			Constants.closeStream(out);
		}
	}

	public void downloadFromUrlBigFile(String mapURL, String fileName,
			String downloadPath, Cancellable cancellable) throws Exception {
		InputStream in = null;
		// OutputStream out = null;
		BufferedInputStream bis = null;
		if (mapURL == null)
			throw new Exception("Error null url");
		if (fileName == null)
			throw new Exception("Error null name");
		try {

			File directory = new File(downloadPath);
			if (!directory.exists()) {
				directory.mkdirs();
			}

			File file = new File(downloadPath + fileName);
			file.createNewFile();
			long startTime = System.currentTimeMillis();
			
			in = new BufferedInputStream(openConnection(mapURL + fileName),
					Constants.IO_BUFFER_SIZE);
			// final ByteArrayOutputStream dataStream = new
			// ByteArrayOutputStream();
			// out = new BufferedOutputStream(dataStream,
			// Constants.IO_BUFFER_SIZE);
			FileOutputStream fileout = new FileOutputStream(file);
			byte[] b = new byte[8 * 1024];
			int read;
			int total = 0;
			while ((read = in.read(b)) != -1) {
				total += read;
				fileout.write(b, 0, read);
				fileout.flush();
				if (listener != null) {
					listener.onBytesDownloaded(read);
				}
				if (cancellable != null && cancellable.getCanceled()) {
					
					file.delete();
					return;
				}

				// System.gc();
			}
			// out.flush();
			// xmldata = dataStream.toByteArray();

			// fileout.write(xmldata);
			fileout.close();
			

		} catch (IOException e) {
			
			throw new Exception(e);
		} finally {
			Constants.closeStream(in);
			// Constants.closeStream(out);
		}
	}

	public byte[] getData() {
		return xmldata;
	}

	public static InputStream openConnection(String query) throws IOException {
		final URL url = new URL(query.replace(" ", "%20"));
		URLConnection urlconnec = url.openConnection();
		urlconnec.setRequestProperty("Accept", "application/json");
		urlconnec.setConnectTimeout(15000);
		urlconnec.setReadTimeout(15000);
		return urlconnec.getInputStream();
	}

	public void setDownloadListener(DownloadListener listener) {
		this.listener = listener;
	}
}