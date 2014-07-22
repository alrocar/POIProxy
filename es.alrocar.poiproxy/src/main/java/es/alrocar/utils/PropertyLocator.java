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

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;

import es.alrocar.poiproxy.exceptions.POIProxyException;

public class PropertyLocator {

	@PropertyParam(name = "APP.TEST")
	public String test = null;

	@PropertyParam(name = "APP.ENV")
	public String env = null;

	@PropertyParam(name = "APP.TMP")
	public String tempFolder = "/tmp";

	private final static String PROPERTIES = "poiproxy.properties";
	private Properties poiProxyProperties;

	private static PropertyLocator propertyLocator;

	private PropertyLocator() {
		init();
	}

	public static PropertyLocator getInstance() {
		if (propertyLocator == null) {
			propertyLocator = new PropertyLocator();
		}

		return propertyLocator;
	}

	private void init() {
		Field[] fields = this.getClass().getDeclaredFields();

		for (Field field : fields) {
			field.setAccessible(true);

			PropertyParam pParam = field.getAnnotation(PropertyParam.class);

			if (pParam != null) {
				try {
					field.set(
							this,
							this.getAplicacionProperties().getProperty(
									pParam.name()));

				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (POIProxyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * @NOT generated
	 */
	protected Properties getAplicacionProperties() throws POIProxyException {
		if (poiProxyProperties == null) {
			poiProxyProperties = getProperties(PROPERTIES);
		}
		return poiProxyProperties;
	}

	/**
	 * @NOT generated
	 */
	protected Properties getProperties(String name) throws POIProxyException {
		return PropertyLocator.cargarFicheroPropiedades(name);
	}

	public static Properties cargarFicheroPropiedades(String pStrNombre)
			throws POIProxyException {
		try {
			Properties lObjProps = new Properties();
			lObjProps.load(Thread.currentThread().getContextClassLoader()
					.getResourceAsStream(pStrNombre));
			return lObjProps;
		} catch (IOException e) {
			throw new POIProxyException(e);
		}
	}

}
