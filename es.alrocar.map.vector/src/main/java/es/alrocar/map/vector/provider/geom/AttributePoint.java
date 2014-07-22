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

package es.alrocar.map.vector.provider.geom;

import java.util.HashMap;

import es.prodevelop.gvsig.mini.geom.Point;

public class AttributePoint extends Point {

	private HashMap fields = new HashMap();

	public void addField(String key, Object value, int type) {
		fields.put(key, new Attribute(value, type));
	}

	public Attribute getAttribute(String key) {
		return (Attribute) fields.get(key);
	}

	public HashMap getAttributes() {
		return fields;
	}

	public static class Attribute {

		public final static int TYPE_UNKNOWN = -1;
		public final static int TYPE_STRING = 0;
		public final static int TYPE_INT = 1;
		public final static int TYPE_DOUBLE = 2;
		public final static int TYPE_LIST_STRING = 3;
		public final static int TYPE_LIST_INT = 4;
		public final static int TYPE_LIST_DOUBLE = 5;
		public final static int TYPE_URL = 6;
		public final static int TYPE_PHONE = 7;
		public final static int TYPE_DATE = 8;

		public int type = TYPE_UNKNOWN;
		public Object value;

		public Attribute(Object value, int type) {
			this.value = value;
			this.type = type;
		}
	}
}
