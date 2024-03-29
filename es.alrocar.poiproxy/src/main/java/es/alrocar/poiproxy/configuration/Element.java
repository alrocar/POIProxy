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

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

/**
 * 
 * @author aromeu
 * 
 */
public class Element {

	private String input;
	private String parent;
	private List<String> avoid;
	private boolean encodeUrl;

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public void setEncodeUrl(boolean encodeUrl) {
		this.encodeUrl = encodeUrl;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public List<String> getAvoid() {
		return avoid;
	}

	public void setAvoid(List<String> avoid) {
		this.avoid = avoid;
	}

	public boolean apply(String currentKey, String currentObjectKey) {
		boolean apply = true;
		if (this.parent != null) {
			if (this.parent.compareTo(currentObjectKey) == 0
					&& this.input.compareTo(currentKey) == 0) {
				apply = true;
			} else {
				apply = false;	
			}
		} else {
			if (getAvoid() != null && !getAvoid().isEmpty()) {
				if (this.input.compareTo(currentKey) == 0) {
					for (String avoidKey : getAvoid()) {
						if (avoidKey.compareTo(currentObjectKey) == 0) {
							apply = false;
							break;
						}
					}
				} else {
					apply = false;
				}
			} else {
				apply = this.input.compareTo(currentKey) == 0;
			}
		}

		return apply;
	}

	public String encode(String val) {
		if (this.encodeUrl){
			 try {
				return URLEncoder.encode(val, java.nio.charset.StandardCharsets.UTF_8.toString());
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return val;
	}

	public boolean getEncodeUrl() {
		return encodeUrl;
	}
}
