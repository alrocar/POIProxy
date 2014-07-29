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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

import es.alrocar.jpe.parser.configuration.DescribeServiceParser;
import es.alrocar.poiproxy.exceptions.POIProxyException;
import es.prodevelop.gvsig.mini.utiles.Constants;

/**
 * This class is used to register into the library al the json documents
 * available that describe available services to request and parse responses
 * 
 * It takes all the services at
 * {@link ServiceConfigurationManager#CONFIGURATION_DIR} parses the json
 * documents through {@link DescribeServiceParser} and registers each file as a
 * {@link DescribeService} taking as the id the name of the json file
 * 
 * So adding a new service to the library implies write its json document
 * describing its {@link RequestType} and {@link FeatureType} and putting it
 * into the {@link #CONFIGURATION_DIR}
 * 
 * 
 * @author albertoromeu
 * 
 */
public class ServiceConfigurationManager {

	public static String CONFIGURATION_DIR = "/var/lib/services";

	private HashMap<String, String> registeredConfigurations = new HashMap<String, String>();
	private HashMap<String, DescribeService> parsedConfigurations = new HashMap<String, DescribeService>();
	private DescribeServiceParser parser = new DescribeServiceParser();
	private DescribeServices services;

	/**
	 * A map of ids of the registered services
	 * 
	 * @return
	 */
	public HashMap<String, String> getRegisteredConfigurations() {
		return registeredConfigurations;
	}

	/**
	 * The constructor.
	 * 
	 * Internally calls {@link #loadConfiguration()}
	 */
	public ServiceConfigurationManager() {
		this.loadConfiguration();
	}

	/**
	 * Iterates the files at {@link #CONFIGURATION_DIR} and calls
	 * {@link #registerServiceConfiguration(String, String)} for each file found
	 * setting as the id of the service the file name
	 */
	public void loadConfiguration() {
		System.out.println("CONFIGURATION_DIR: " + CONFIGURATION_DIR);
		File f = new File(CONFIGURATION_DIR);

		if (f.isDirectory()) {
			String[] files = f.list();
			for (String s : files) {
				if (s.endsWith(".json")) {
					System.out.println("Registering: " + s.toLowerCase());
					this.registerServiceConfiguration(s.split(".json")[0], s);
				}
			}
		}
	}

	/**
	 * Registers a new service into the library
	 * 
	 * @param id
	 *            The id of the service
	 * @param configFile
	 *            The content of the json document describing the service
	 */
	public void registerServiceConfiguration(String id, String configFile) {
		this.registeredConfigurations.put(id, configFile);
	}

	/**
	 * Registers a new service into the library
	 * 
	 * @param id
	 *            The id of the service
	 * @param configFile
	 *            The content of the json document describing the service
	 * @param describeService
	 *            The parsed {@link DescribeService}
	 * @throws POIProxyException
	 *             When any of the parameters is null
	 */
	public void registerServiceConfiguration(String id, String configFile,
			DescribeService service) throws POIProxyException {
		if (service == null || configFile == null || service.getId() == null) {
			throw new POIProxyException("Null service configuration");
		}

		this.registeredConfigurations.put(id, configFile);
		this.parsedConfigurations.put(id, service);

		try {
			this.save(id, configFile);
		} catch (IOException e) {
			throw new POIProxyException("Unable to write service configuration");
		}
	}

	private void save(String id, String configFile) throws IOException {
		FileUtils.writeStringToFile(new File(
				ServiceConfigurationManager.CONFIGURATION_DIR + File.separator
						+ id + ".json"), configFile);
	}

	/**
	 * Returns a {@link DescribeService} given an id. If the service has not
	 * been used previously, this method parses the json document describing the
	 * service
	 * 
	 * @param id
	 *            The id of the service. Usually the name of the json document
	 * @return The {@link DescribeService}
	 */
	public DescribeService getServiceConfiguration(String id) {
		// Buscar los servicios registrados
		DescribeService service = this.parsedConfigurations.get(id);

		if (service == null) {
			String res = this.getServiceAsJSON(id);
			service = parser.parse(res);
			service.setId(id);
			this.parsedConfigurations.put(id, service);
		}

		return service;
	}

	/**
	 * Returns the content of the json document describing a service given its
	 * id
	 * 
	 * @param id
	 *            The id of the service. Usually the name of the json document
	 * @return The content of the json document
	 */
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
			while ((read = in.read(b)) != -1) {
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

	/**
	 * not used at the moment
	 * 
	 * @param url
	 * @return
	 */
	public DescribeService getRemoteConfiguration(String url) {
		// Descargar json de la url y parsearlo
		return null;
	}

	/**
	 * Iterates the registered configurations and returns a
	 * {@link DescribeServices} instance
	 * 
	 * @return
	 */
	public DescribeServices getAvailableServices() {
		if (services == null) {
			Set<String> keys = this.getRegisteredConfigurations().keySet();

			Iterator<String> it = keys.iterator();

			String id = null;
			services = new DescribeServices();
			while (it.hasNext()) {
				id = it.next();
				services.put(id, this.getServiceConfiguration(id));
			}
		}

		return services;
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
