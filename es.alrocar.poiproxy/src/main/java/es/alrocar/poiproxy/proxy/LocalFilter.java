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

package es.alrocar.poiproxy.proxy;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import es.alrocar.jpe.parser.JPEParser;
import es.alrocar.poiproxy.configuration.DescribeService;
import es.alrocar.poiproxy.configuration.Param;
import es.alrocar.poiproxy.configuration.ParamEnum;

/**
 * A class that contains properties to perform a LocalFilter while a
 * {@link JPEParser} is parsing the data.
 * 
 * Filtering should consist on a like operation by {@link LocalFilter#value} on
 * all the attributes of the response
 * 
 * @author aromeu
 * 
 */
public class LocalFilter {

	public String value;

	/**
	 * Builds the LocalFilter from the array of optional Params of the
	 * {@link DescribeService}
	 * 
	 * @param optionalParams
	 * @return
	 */
	public static LocalFilter fromOptionalParams(List<Param> optionalParams) {
		LocalFilter filter = new LocalFilter();
		Param p = filter.getQueryParam(optionalParams);

		if (p == null) {
			return null;
		}

		filter.value = p.getValue();
		return filter;
	}

	// FIXME Extract the ArrayList to a Class
	private Param getQueryParam(List<Param> optionalParams) {
		for (Param p : optionalParams) {
			if (p.getType().equals(ParamEnum.QUERY.name)) {
				return p;
			}
		}

		return null;
	}

	/**
	 * Performs a contains operation between the {@link LocalFilter#value}
	 * property and the attribute parameter
	 * 
	 * @param attribute
	 * @return True if passes the filter
	 */
	public boolean apply(String attribute) {
		if (attribute == null || value == null) {
			return true;
		}

		String attNoAccents = StringUtils.stripAccents(attribute);
		String valueNoAccents = StringUtils.stripAccents(value);
		return StringUtils.containsIgnoreCase(attNoAccents, valueNoAccents);
	}
}
