package es.alrocar.poiproxy.configuration;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

public enum AuthTypeEnum {

	none("simple", null),

	twitter_oauth("twitter_oauth", TwitterApi.class);

	public String type;
	public Class<? extends Api> _class;

	private AuthTypeEnum(String type, Class<? extends Api> _class) {
		this.type = type;
		this._class = _class;
	}

	public boolean isOauth() {
		return type.indexOf("oauth") != -1;
	}
}
