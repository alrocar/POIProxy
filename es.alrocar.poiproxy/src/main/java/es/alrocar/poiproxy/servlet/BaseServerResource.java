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

package es.alrocar.poiproxy.servlet;

import java.util.ArrayList;
import java.util.HashMap;

import org.restlet.resource.ServerResource;

import es.alrocar.poiproxy.configuration.DescribeService;
import es.alrocar.poiproxy.configuration.Param;
import es.alrocar.poiproxy.configuration.ParamEnum;

public abstract class BaseServerResource extends ServerResource {

	public ArrayList<Param> extractParams(HashMap<String, String> params) {
		final ArrayList<String> optionalParams = this.getOptionalParamsNames();

		ArrayList<Param> extractedParams = new ArrayList<Param>();

		String p;
		Param optParam;
		for (String paramName : optionalParams) {
			p = params.get(paramName);

			if (p != null) {
				params.remove(paramName);
				optParam = new Param(paramName, p);
				extractedParams.add(optParam);
			}
		}

		addAdditionalParams(params, extractedParams);

		return extractedParams;
	}

	/**
	 * Adds params found in the URL that are not in the {@link DescribeService}
	 * document of the service. Having this, we can add in the URL to POIProxy
	 * params from the original service. This should not be a good option when
	 * we want to have a single interface, but allows anyone to access the
	 * original API adding the original parameters to the POIProxy request
	 * 
	 * @param params
	 * @param extractedParams
	 */
	protected void addAdditionalParams(HashMap<String, String> params,
			ArrayList<Param> extractedParams) {
		for (String key : params.keySet()) {
			if (!ParamEnum.from(key)) {
				extractedParams.add(new Param(key, params.get(key)));
			}
		}
	}

	public abstract ArrayList<String> getOptionalParamsNames();
}
