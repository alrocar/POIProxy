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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.alrocar.jpe.parser.JPEParser;
import es.alrocar.jpe.parser.JPEParserFormatEnum;
import es.alrocar.jpe.parser.configuration.DescribeServiceParser;
import es.alrocar.poiproxy.proxy.LocalFilter;
import es.alrocar.poiproxy.proxy.POIProxy;
import es.alrocar.utils.CompressionEnum;

/**
 * An entity where to load into memory a describe service json
 * 
 * @see DescribeServiceParser
 * 
 * @author albertoromeu
 * 
 */
public class DescribeService {

	private final static Logger logger = LoggerFactory
			.getLogger(DescribeService.class);

	public static final String CATEGORY_SEPARATOR = ",";

	public final static String SEARCH_TYPE = "search";
	public final static String BROWSE_TYPE = "browse";
	private static final String DEFAULT_ENCODING = "UTF-8";
	public static final String DEFAULT_SRS = "EPSG:4326";

	public static final String TIMESTAMP = "timestamp";

	@JsonIgnore
	private String apiKey;
	private HashMap<String, RequestType> requestTypes = new HashMap<String, RequestType>();
	private HashMap<String, FeatureType> featureTypes = new HashMap<String, FeatureType>();
	private List<String> categories = new ArrayList<String>();

	@JsonIgnore
	private Auth auth = new Auth();

	private String format;
	private String dateFormat;
	private String csvSeparator;
	private String encoding;
	private String numberSeparator;
	private String decimalSeparator;
	private String compression;
	private String contentFile;
	private boolean useLocalFilter = false;
	private String SRS = DEFAULT_SRS;

	private String type = BROWSE_TYPE;

	private String id;

	/**
	 * 
	 * @return The apiKey
	 */
	@JsonIgnore
	public String getApiKey() {
		return apiKey;
	}

	/**
	 * Sets the apiKey
	 * 
	 * @param apiKey
	 */
	@JsonProperty
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	/**
	 * The current type selected to parse a file, one of {@link #SEARCH_TYPE} or
	 * {@link #BROWSE_TYPE}
	 * 
	 * @return
	 */
	public String getType() {
		return type;
	}

	/**
	 * The current type depending on the request made by {@link POIProxy} one of
	 * {@link #SEARCH_TYPE} or {@link #BROWSE_TYPE}
	 * 
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * The format of the source response of the service to parse.
	 * 
	 * @return {@link JPEParser#FORMAT_JSON} or {@link JPEParser#FORMAT_XML}
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * Sets the format of the source response of the service to parse
	 * 
	 * @param format
	 *            {@link JPEParser#FORMAT_JSON} or {@link JPEParser#FORMAT_XML}
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	/**
	 * A map of {@link #BROWSE_TYPE}, {@link #SEARCH_TYPE} with
	 * {@link RequestType}
	 * 
	 * @return
	 */
	public HashMap<String, RequestType> getRequestTypes() {
		return requestTypes;
	}

	/**
	 * sets the request types
	 * 
	 * @param requestTypes
	 *            A map of {@link #BROWSE_TYPE}, {@link #SEARCH_TYPE} with
	 *            {@link RequestType}
	 */
	public void setRequestTypes(HashMap<String, RequestType> requestTypes) {
		this.requestTypes = requestTypes;
	}

	/**
	 * A map of {@link #BROWSE_TYPE}, {@link #SEARCH_TYPE} with
	 * {@link FeatureType}
	 * 
	 * @return
	 */
	public HashMap<String, FeatureType> getFeatureTypes() {
		return featureTypes;
	}

	/**
	 * sets the feature types
	 * 
	 * @param featureTypes
	 *            A map of {@link #BROWSE_TYPE}, {@link #SEARCH_TYPE} with
	 *            {@link FeatureType}
	 */
	public void setFeatureTypes(HashMap<String, FeatureType> featureTypes) {
		this.featureTypes = featureTypes;
	}

	/**
	 * returns the URL to request given an array of optional params. Usually if
	 * optionalParam contains a {@link Param} of type {@link Param#QUERY} then
	 * this method will return the url of the {@link RequestType} of type
	 * {@link DescribeService#SEARCH_TYPE} otherwise will return the url of
	 * {@link DescribeService#BROWSE_TYPE}
	 * 
	 * @param optionalParam
	 *            An array of {@link Param}
	 * @param params
	 *            A {@link ServiceParams} instance
	 * @return The url to request
	 */
	public String getRequestForParam(List<Param> optionalParam,
			ServiceParams params) {
		String url;
		ArrayList<String> requestParams;
		RequestType requestType;

		this.setType(DescribeService.BROWSE_TYPE);
		requestType = getRequestTypes().get(DescribeService.BROWSE_TYPE);

		if (optionalParam == null) {
			this.setType(DescribeService.BROWSE_TYPE);
			requestType = getRequestTypes().get(DescribeService.BROWSE_TYPE);
		}

		if (optionalParam.size() == 0) {
			this.setType(DescribeService.BROWSE_TYPE);
			requestType = getRequestTypes().get(DescribeService.BROWSE_TYPE);
		}

		for (Param optParam : optionalParam) {
			if (optParam.getType() == ParamEnum.QUERY.name) {
				this.setType(DescribeService.SEARCH_TYPE);
				requestType = getRequestTypes()
						.get(DescribeService.SEARCH_TYPE);
			}
		}

		url = requestType.getUrl();
		requestParams = requestType.getParams();

		String optionalUrl = processRequestParams(requestParams, params);
		return url + optionalUrl;
	}

	private String processRequestParams(ArrayList<String> requestParams,
			ServiceParams params) {
		String p;
		String value;
		StringBuffer optionalUrl = new StringBuffer();
		for (String requestParam : requestParams) {
			try {
				p = requestParam.split("=")[1];
				value = params.getValueForParam(p);
				if (value != null) {
					optionalUrl.append("&" + requestParam);
				}
			} catch (Exception ignore) {
				ignore.printStackTrace();
			}
		}

		addOriginalParams(params, optionalUrl);

		return optionalUrl.toString();
	}

	protected void addOriginalParams(ServiceParams params,
			StringBuffer optionalUrl) {
		for (String key : params.getParams().keySet()) {
			if (!ParamEnum.from(key) && !isSpecialParam(key)) {
				optionalUrl.append("&" + key + "="
						+ params.getParams().get(key));
			}
		}
	}

	public boolean isSpecialParam(String key) {
		return key.startsWith("__");
	}

	/**
	 * Gets the column separator if the format is
	 * {@link JPEParserFormatEnum#CSV}
	 * 
	 * @return
	 */
	public String getCsvSeparator() {
		return csvSeparator;
	}

	/**
	 * Sets the column separator if the format is
	 * {@link JPEParserFormatEnum#CSV}
	 * 
	 * @return
	 */
	public void setCsvSeparator(String csvSeparator) {
		this.csvSeparator = csvSeparator;
	}

	/**
	 * Gets the encoding of the result document of the service. This is
	 * specially usefull for CSV
	 * 
	 * @return
	 */
	public String getEncoding() {
		if (encoding == null) {
			return DEFAULT_ENCODING;
		}
		return encoding;
	}

	/**
	 * Sets the encoding of the result document of the service. This is
	 * specially usefull for CSV
	 * 
	 * @param encoding
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * Gets the number separator. This is useful for CSV resources
	 * 
	 * @return
	 */
	public String getNumberSeparator() {
		return numberSeparator;
	}

	/**
	 * Sets the number separator. This is useful for CSV resources
	 * 
	 * @param numberSeparator
	 */
	public void setNumberSeparator(String numberSeparator) {
		this.numberSeparator = numberSeparator;
	}

	/**
	 * Gets the decimal separator. This is useful for CSV resources
	 * 
	 * @return
	 */
	public String getDecimalSeparator() {
		return decimalSeparator;
	}

	/**
	 * Sets the decimal separator. This is useful for CSV resources
	 * 
	 * @param decimalSeparator
	 */
	public void setDecimalSeparator(String decimalSeparator) {
		this.decimalSeparator = decimalSeparator;
	}

	/**
	 * Indicates the compression format of the resource
	 * 
	 * @return @see {@link CompressionEnum}
	 */
	public String getCompression() {
		return compression;
	}

	/**
	 * Sets the compression format of the resource
	 * 
	 * @see {@link CompressionEnum}
	 */
	public void setCompression(String compression) {
		this.compression = compression;
	}

	/**
	 * This is used when the {@link #compression} is used. Gets the file name of
	 * the file compressed
	 * 
	 * @return
	 */
	public String getContentFile() {
		return contentFile;
	}

	/**
	 * This is used when the {@link #compression} is used. Sets the file name of
	 * the file compressed
	 * 
	 * @param contentFile
	 */
	public void setContentFile(String contentFile) {
		this.contentFile = contentFile;
	}

	/**
	 * Gets the array of categories configured in the {@link DescribeService}
	 * document
	 * 
	 * @return
	 */
	public List<String> getCategories() {
		return categories;
	}

	/**
	 * Sets the array of categories configured in the {@link DescribeService}
	 * document
	 * 
	 * @param categories
	 */
	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	/**
	 * Check if the category is in the {@link #categories} supported by the
	 * service
	 * 
	 * @param category
	 * @return
	 */
	public boolean containsCategory(String category) {
		if (this.categories == null || this.categories.isEmpty()) {
			return false;
		}

		for (String cat : categories) {
			if (category.compareToIgnoreCase(cat) == 0) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Gets the SRS (EPSG code) of the origin service
	 * 
	 * @return
	 */
	public String getSRS() {
		if (SRS == null) {
			return DEFAULT_SRS;
		}
		return SRS;
	}

	/**
	 * Sets the SRS (EPSG code) of the origin service
	 * 
	 * @param sRS
	 */
	public void setSRS(String sRS) {
		SRS = sRS;
	}

	/**
	 * Concatenates the categories as a String separated by
	 * {@link DescribeService#CATEGORY_SEPARATOR}
	 * 
	 * @return
	 */
	public String getCategoriesAsString() {
		List<String> categories = getCategories();
		String categoriesStr = "";
		if (categories != null && !categories.isEmpty()) {
			categoriesStr = StringUtils.join(categories.toArray(),
					CATEGORY_SEPARATOR);
		}

		return categoriesStr;
	}

	/**
	 * Sets the identifier of the service
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the identifier of the service
	 * 
	 * @param id
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Converts a comma separated list of categories into a {@link List}
	 * 
	 * @param categoriesList
	 * @return
	 */
	public static List<String> categoriesFromString(String categoriesList) {
		List<String> result = new ArrayList<String>();
		try {
			if (categoriesList != null) {
				String[] categories = categoriesList
						.split(DescribeService.CATEGORY_SEPARATOR);
				if (categories != null && categories.length != 0) {
					for (String cat : categories) {
						result.add(cat);
					}
				}
			}
		} catch (Exception ignore) {
			logger.warn("DescribeService", ignore);
		}

		return result;
	}

	/**
	 * When the data comes from a static file such a CSV, if this is set to True
	 * then the {@link Param#QUERY} is used to filter the data in memory
	 * 
	 * @return
	 */
	public boolean isUseLocalFilter() {
		return useLocalFilter;
	}

	/**
	 * When the data comes from a static file such a CSV, if this is set to True
	 * then the {@link Param#QUERY} is used to filter the data in memory
	 * 
	 * @param useLocalFilter
	 */
	public void setUseLocalFilter(boolean useLocalFilter) {
		this.useLocalFilter = useLocalFilter;
	}

	/**
	 * 
	 * @param optionalParams
	 * @return
	 */
	public LocalFilter getLocalFilter(List<Param> optionalParams) {
		LocalFilter localFilter = null;
		if (isUseLocalFilter()) {
			localFilter = LocalFilter.fromOptionalParams(optionalParams);
		}
		return localFilter;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getAuthType() {
		return auth.getType();
	}

	@JsonIgnore
	public Auth getAuth() {
		return auth;
	}

	@JsonProperty
	public void setAuth(Auth auth) {
		this.auth = auth;
	}
}
