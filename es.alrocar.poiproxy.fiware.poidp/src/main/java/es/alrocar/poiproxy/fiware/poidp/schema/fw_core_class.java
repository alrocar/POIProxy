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

import es.alrocar.poiproxy.fiware.poidp.schema.utils.IntString;
import es.alrocar.poiproxy.fiware.poidp.schema.utils.IntUri;
import es.alrocar.poiproxy.fiware.poidp.schema.utils.Location;
import es.alrocar.poiproxy.fiware.poidp.schema.utils.Source;
import es.alrocar.poiproxy.fiware.poidp.schema.utils.UpdateStamp;

public class fw_core_class {

	private String category = "";
	private Location location = new Location(0, 0);
	private String geometry = "";

	private String short_name = "";
	private String name = "";

	private IntString label = null;
	private IntString description = null;

	private String thumbnail = "";
	private IntUri url = null;

	private Source source = null;
	private UpdateStamp last_update = null;

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getGeometry() {
		return geometry;
	}

	public void setGeometry(String geometry) {
		this.geometry = geometry;
	}

	public String getShort_name() {
		return short_name;
	}

	public void setShort_name(String short_name) {
		this.short_name = short_name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public IntString getLabel() {
		return label;
	}

	public void setLabel(IntString label) {
		this.label = label;
	}

	public IntString getDescription() {
		return description;
	}

	public void setDescription(IntString description) {
		this.description = description;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public IntUri getUrl() {
		return url;
	}

	public void setUrl(IntUri url) {
		this.url = url;
	}

	public Source getSource() {
		return source;
	}

	public void setSource(Source source) {
		this.source = source;
	}

	public UpdateStamp getLast_update() {
		return last_update;
	}

	public void setLast_update(UpdateStamp last_update) {
		this.last_update = last_update;
	}

}
