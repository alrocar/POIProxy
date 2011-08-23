/* POIProxy
 *
 * Copyright (C) 2011 Alberto Romeu.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
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
 *   2011.
 *   author Alberto Romeu aromeu@prodevelop.es  
 *   
 */

package es.alrocar.poiproxy.configuration;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import es.alrocar.jpe.parser.configuration.DescribeServiceParser;
import es.prodevelop.gvsig.mini.utiles.Constants;

public class ServiceConfigurationManager {

	public static String CONFIGURATION_DIR = "/var/lib/services";

	private HashMap<String, String> registeredConfigurations = new HashMap<String, String>();
	private DescribeServiceParser parser = new DescribeServiceParser();

	public HashMap<String, String> getRegisteredConfigurations() {
		return registeredConfigurations;
	}

	public ServiceConfigurationManager() {
		this.loadConfiguration();
	}

	public void loadConfiguration() {
		File f = new File(CONFIGURATION_DIR);

		if (f.isDirectory()) {
			String[] files = f.list();
			for (String s : files) {
				this.registerServiceConfiguration(s.split(".json")[0], s);
			}
		}
	}

	public void registerServiceConfiguration(String id, String configFile) {
		this.registeredConfigurations.put(id, configFile);
	}

	public DescribeService getServiceConfiguration(String id) {
		// Buscar los servicios registrados
		String res = this.getServiceAsJSON(id);

		DescribeService service = parser.parse(res);

		return service;
	}

	public String getServiceAsJSON(String id) {
		String path = this.registeredConfigurations.get(id);

		if (path == null) {
			return null;
		}

		// cargar fichero de disco obtener el json y parsearlo
		File f = new File(CONFIGURATION_DIR + File.separator + path);
		FileInputStream fis = null;
		InputStream in = null;
		OutputStream out = null;
		String res = null;
		try {
			fis = new FileInputStream(f);
			in = new BufferedInputStream(fis, Constants.IO_BUFFER_SIZE);
			final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
			out = new BufferedOutputStream(dataStream, Constants.IO_BUFFER_SIZE);
			byte[] b = new byte[8 * 1024];
			int read;
			int total = 0;
			while ((read = in.read(b)) != -1) {
				total += read;
				out.write(b, 0, read);
			}
			out.flush();
			res = new String(dataStream.toByteArray());

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			Constants.closeStream(fis);
			Constants.closeStream(in);
			Constants.closeStream(out);
		}

		return res;
	}

	public DescribeService getRemoteConfiguration(String url) {
		// Descargar json de la url y parsearlo
		return null;
	}

	/**
	 * list resources available from the classpath
	 * 
	 * @author stoughto
	 * 
	 */
	public static class ResourceList {
		/**
		 * for all elements of java.class.path get a Collection of resources
		 * Pattern pattern = Pattern.compile(".*"); gets all resources
		 * 
		 * @param pattern
		 *            the pattern to match
		 * @return the resources in the order they are found
		 */
		public static Collection<String> getResources(Pattern pattern) {
			ArrayList<String> retval = new ArrayList<String>();
			String classPath = System.getProperty("java.class.path", ".");
			String[] classPathElements = classPath.split(":");
			for (String element : classPathElements) {
				retval.addAll(getResources(element, pattern));
			}
			return retval;
		}

		private static Collection<String> getResources(String element,
				Pattern pattern) {
			ArrayList<String> retval = new ArrayList<String>();
			File file = new File(element);
			if (file.isDirectory()) {
				retval.addAll(getResourcesFromDirectory(file, pattern));
			} else {
				// retval.addAll(getResourcesFromJarFile(file, pattern));
			}
			return retval;
		}

		private static Collection<String> getResourcesFromJarFile(File file,
				Pattern pattern) {
			ArrayList<String> retval = new ArrayList<String>();
			ZipFile zf;
			try {
				zf = new ZipFile(file);
			} catch (ZipException e) {
				throw new Error(e);
			} catch (IOException e) {
				throw new Error(e);
			}
			Enumeration e = zf.entries();
			while (e.hasMoreElements()) {
				ZipEntry ze = (ZipEntry) e.nextElement();
				String fileName = ze.getName();
				boolean accept = pattern.matcher(fileName).matches();
				if (accept) {
					retval.add(fileName);
				}
			}
			try {
				zf.close();
			} catch (IOException e1) {
				throw new Error(e1);
			}
			return retval;
		}

		private static Collection<String> getResourcesFromDirectory(
				File directory, Pattern pattern) {
			ArrayList<String> retval = new ArrayList<String>();
			File[] fileList = directory.listFiles();
			for (File file : fileList) {
				if (file.isDirectory()) {
					retval.addAll(getResourcesFromDirectory(file, pattern));
				} else {
					try {
						String fileName = file.getCanonicalPath();
						boolean accept = pattern.matcher(fileName).matches();
						if (accept) {
							retval.add(fileName);
						}
					} catch (IOException e) {
						throw new Error(e);
					}
				}
			}
			return retval;
		}

		/**
		 * list the resources that match args[0]
		 * 
		 * @param args
		 *            args[0] is the pattern to match, or list all resources if
		 *            there are no args
		 */
		public static void main(String[] args) {
			Pattern pattern;
			if (args.length < 1) {
				pattern = Pattern.compile(".json");
			} else {
				pattern = Pattern.compile(args[0]);
			}
			Collection<String> list = ResourceList.getResources(pattern);
			for (String name : list) {
				System.out.println(name);
			}

		}
	}

}
