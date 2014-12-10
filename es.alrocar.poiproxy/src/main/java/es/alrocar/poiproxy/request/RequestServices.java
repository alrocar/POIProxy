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

package es.alrocar.poiproxy.request;

import java.util.HashMap;
import java.util.Map;

import es.alrocar.poiproxy.configuration.Auth;
import es.alrocar.poiproxy.configuration.AuthTypeEnum;
import es.alrocar.poiproxy.configuration.DescribeService;

/**
 * A manager of available {@link RequestService}
 * 
 * Dependending on the {@link Auth} configuration of a {@link DescribeService}
 * object a {@link RequestService} is used to downlaod data.
 * 
 * @author aromeu
 * 
 */
public class RequestServices {

	private Map<AuthTypeEnum, RequestService> requestServices = new HashMap<AuthTypeEnum, RequestService>();

	private static RequestServices instance;

	public static RequestServices getInstance() {
		if (instance == null) {
			instance = new RequestServices();
		}

		return instance;
	}

	public void addRequestService(AuthTypeEnum authType,
			RequestService requestService) {
		this.requestServices.put(authType, requestService);
	}

	public RequestService getRequestService(AuthTypeEnum authTypeEnum) {
		return this.requestServices.get(authTypeEnum);
	}

	public RequestService getRequestService(String authType) {
		return this.requestServices.get(AuthTypeEnum.valueOf(authType));
	}
}
