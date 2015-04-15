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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This utility class supports formating multiple date format
 */
public class DateUtil {
	private static final String[] timeFormats = { "HH:mm:ss", "HH:mm" };
	private static final String[] dateSeparators = { "/", "-", " " };

	private static final String DMY_FORMAT = "dd{sep}MM{sep}yyyy";
	private static final String YMD_FORMAT = "yyyy{sep}MM{sep}dd";

	private static final String ymd_template = "\\d{4}{sep}\\d{2}{sep}\\d{2}.*";
	private static final String dmy_template = "\\d{2}{sep}\\d{2}{sep}\\d{4}.*";

	public static Date stringToDate(String input) {
		Date date = null;
		String dateFormat = getDateFormat(input);
		if (dateFormat == null) {
			throw new IllegalArgumentException(
					"Date is not in an accepted format " + input);
		}

		for (String sep : dateSeparators) {
			String actualDateFormat = patternForSeparator(dateFormat, sep);
			// try first with the time
			for (String time : timeFormats) {
				date = tryParse(input, actualDateFormat + " " + time);
				if (date != null) {
					return date;
				}
			}
			// didn't work, try without the time formats
			date = tryParse(input, actualDateFormat);
			if (date != null) {
				return date;
			}
		}

		return date;
	}

	private static String getDateFormat(String date) {
		for (String sep : dateSeparators) {
			String ymdPattern = patternForSeparator(ymd_template, sep);
			if (date.matches(ymdPattern)) {
				return YMD_FORMAT;
			}
			String dmyPattern = patternForSeparator(dmy_template, sep);
			if (date.matches(dmyPattern)) {
				return DMY_FORMAT;
			}
		}

		return null;
	}

	private static String patternForSeparator(String template, String sep) {
		return template.replace("{sep}", sep);
	}

	private static Date tryParse(String input, String pattern) {
		try {
			return new SimpleDateFormat(pattern).parse(input);
		} catch (ParseException e) {
			return null;
		}
	}

}