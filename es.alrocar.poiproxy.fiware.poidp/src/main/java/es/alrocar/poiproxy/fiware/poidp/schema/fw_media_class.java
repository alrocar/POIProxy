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
 * @author Alberto Romeu Carrasco aromeu@prodevelop.es
 */

package es.alrocar.poiproxy.fiware.poidp.schema;

import java.util.ArrayList;
import java.util.List;

import es.alrocar.poiproxy.fiware.poidp.schema.utils.UpdateStamp;

public class fw_media_class {

	private List<fw_media_entity> entities = new ArrayList<fw_media_entity>();
	private UpdateStamp last_update = null;

	public UpdateStamp getLast_update() {
		return last_update;
	}

	public void setLast_update(UpdateStamp last_update) {
		this.last_update = last_update;
	}

	public List<fw_media_entity> getEntities() {
		return entities;
	}

	public void setEntities(List<fw_media_entity> entities) {
		this.entities = entities;
	}
}
