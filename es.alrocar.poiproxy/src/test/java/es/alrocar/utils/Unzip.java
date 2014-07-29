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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import es.alrocar.poiproxy.exceptions.POIProxyException;

public class Unzip {

	public void unzip(String unzipFolder, String filePath, boolean deleteZip)
			throws POIProxyException {

		File file = new File(filePath);
		if (!file.exists()) {
			throw new POIProxyException("The file does not exist: " + filePath);
		}

		try {
			ZipFile zipFile = new ZipFile(file);
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				if (entry.isDirectory()) {
					continue;
				}

				String fileName = unzipFolder + entry.getName();
				File zf = new File(fileName);
				// int append = 1;
				if (zf.exists()) {
					zf.delete();
					zf.createNewFile();
				}
				FileOutputStream output = new FileOutputStream(zf);
				BufferedInputStream input = new BufferedInputStream(
						zipFile.getInputStream(entry));
				int bytesRead;
				while ((bytesRead = input.read()) != -1) {
					output.write(bytesRead);
				}

				output.flush();
				output.close();
				input.close();
			}
			zipFile.close();
		} catch (ZipException e) {
			throw new POIProxyException(e);
		} catch (IOException e) {
			throw new POIProxyException(e);
		}

		// delete zip file
		if (deleteZip) {
			file.delete();
		}
	}
}
