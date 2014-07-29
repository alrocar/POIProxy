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

package es.alrocar.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.log4j.Logger;

import es.prodevelop.gvsig.mini.utiles.Cancellable;
import es.prodevelop.gvsig.mini.utiles.Constants;

public class Downloader {

	private final static Logger logger = Logger.getLogger(Downloader.class);

	private byte[] xmldata;
	private DownloadListener listener;

	public void downloadFromUrl(String mapURL, Cancellable cancellable)
			throws Exception {
		xmldata = null;
		InputStream in = null;
		OutputStream out = null;
		BufferedInputStream bis = null;
		if (mapURL == null)
			throw new Exception("Error null url");
		try {

			long startTime = System.currentTimeMillis();
			logger.debug("download starts");
			logger.debug("download url:" + (mapURL));

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
					logger.debug("Download cancelled");

					return;
				}

				// System.gc();
			}
			out.flush();
			xmldata = dataStream.toByteArray();

		} catch (IOException e) {
			logger.debug(e);
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
			logger.debug("download starts");
			logger.debug("download url:" + (mapURL));
			logger.debug("downloading file:" + fileName);

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
					logger.debug("Download cancelled");
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
			logger.debug("download ready in"
					+ ((System.currentTimeMillis() - startTime) / 1000)
					+ " sec");

		} catch (IOException e) {
			logger.debug(e);
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
			logger.debug("download starts");
			logger.debug("download url:" + mapURL);
			logger.debug("downloading file:" + fileName);

			in = new BufferedInputStream(openConnection(mapURL),
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
					logger.debug("Download cancelled");
					file.delete();
					return;
				}

				// System.gc();
			}
			// out.flush();
			// xmldata = dataStream.toByteArray();

			// fileout.write(xmldata);
			fileout.close();
			logger.debug("download ready in"
					+ ((System.currentTimeMillis() - startTime) / 1000)
					+ " sec");

		} catch (IOException e) {
			logger.debug(e);
			throw new Exception(e);
		} finally {
			Constants.closeStream(in);
			// Constants.closeStream(out);
		}
	}

	public String openFile(String filePath) throws Exception {
		if (filePath == null)
			throw new Exception("Error null url");

		try {
			File file = new File(filePath);

			if (!file.exists()) {
				throw new Exception("File not exists");
			}

			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = null;
			StringBuilder stringBuilder = new StringBuilder();
			String ls = System.getProperty("line.separator");

			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line);
				stringBuilder.append(ls);
			}

			return stringBuilder.toString();
		} catch (IOException e) {
			logger.debug(e);
			throw new Exception(e);
		} finally {

		}
	}

	public byte[] getData() {
		return xmldata;
	}

	public static InputStream openConnection(String query) throws IOException {
		final URL url = new URL(query.replace(" ", "%20"));
		URLConnection urlconnec = url.openConnection();
		urlconnec.setConnectTimeout(15000);
		urlconnec.setReadTimeout(15000);
		return urlconnec.getInputStream();
	}

	public void setDownloadListener(DownloadListener listener) {
		this.listener = listener;
	}
}
