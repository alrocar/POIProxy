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

package es.alrocar.poiproxy.fiware.poidp.schema.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class DateTypeAdapter extends TypeAdapter<Date> {
	/**
	 * yyyy/MM/dd HH:mm:ss
	 */
	public static final String DEFAULT_FORMAT = "yyyy/MM/dd HH:mm:ss";

	private String format;

	/**
	 * Default format {@link DateTypeAdapter#DEFAULT_FORMAT}
	 */
	public DateTypeAdapter() {
		this(DEFAULT_FORMAT);
	}

	public DateTypeAdapter(String format) {
		this.format = format;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	@Override
	public void write(JsonWriter out, Date value) throws IOException {
		if (value == null) {
			out.value((String) null);
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			String val = sdf.format(value);
			out.value(val);
		}
	}

	@Override
	public Date read(JsonReader in) throws IOException {
		if (in.peek() == JsonToken.NULL) {
			in.nextNull();
			return null;
		}

		String result = in.nextString();
		if (result.isEmpty()) {
			return null;
		}
		Date date = DateUtil.stringToDate(result);
		return date;
	}

}
