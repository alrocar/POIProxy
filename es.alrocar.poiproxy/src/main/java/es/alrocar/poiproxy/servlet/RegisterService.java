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

import java.io.IOException;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import es.alrocar.poiproxy.configuration.DescribeService;
import es.alrocar.poiproxy.exceptions.POIProxyException;
import es.alrocar.poiproxy.proxy.POIProxy;

public class RegisterService extends ServerResource implements
		DescribeServiceResource {

	public RegisterService() {
		super();
	}

	public RegisterService(Context context, Request request, Response response) {
		getVariants().add(new Variant(MediaType.TEXT_PLAIN));
	}

	@Override
	protected Representation post(Representation entity)
			throws ResourceException {
		// try {
		//
		// FormConverter converter = new FormConverter(new FormDeserializer(
		// new ObjectMapper()));
		// DescribeService service = converter.toObject(entity,
		// DescribeService.class, this);
		// int i = 0;
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		try {
			String text = entity.getText();
			Gson gson = new GsonBuilder().create();
			String data = java.net.URLDecoder.decode(text.split("=")[1],
					"UTF-8");
			DescribeService service = gson
					.fromJson(data, DescribeService.class);
			POIProxy proxy = POIProxy.asSingleton();
			proxy.register(service, data);
			int i = 0;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (POIProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		return super.post(entity);
	}

	@Override
	protected Representation post(Representation entity, Variant variant)
			throws ResourceException {
		// TODO Auto-generated method stub
		return super.post(entity, variant);
	}

	@Override
	public DescribeService getDescribeService() {
		// TODO Auto-generated method stub
		return null;
	}

}
