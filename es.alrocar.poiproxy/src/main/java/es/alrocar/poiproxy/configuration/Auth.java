package es.alrocar.poiproxy.configuration;

/**
 * Configuration object for Authentication methods
 * 
 * @author aromeu
 * 
 */
public class Auth {

	/**
	 * @see AuthTypeEnum. Default value is {@link AuthTypeEnum#none}
	 */
	private String type = AuthTypeEnum.none.toString();
	private String apiKey;
	private String apiSecret;
	private String accessToken;
	private String accessTokenSecret;

	/**
	 * @see AuthTypeEnum
	 * @return
	 */
	public String getType() {
		return type;
	}

	/**
	 * 
	 * @param type
	 *            One of {@link AuthTypeEnum}
	 */
	public void setType(String type) {
		this.type = type;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getApiSecret() {
		return apiSecret;
	}

	public void setApiSecret(String apiSecret) {
		this.apiSecret = apiSecret;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getAccessTokenSecret() {
		return accessTokenSecret;
	}

	public void setAccessTokenSecret(String accessTokenSecret) {
		this.accessTokenSecret = accessTokenSecret;
	}

}
