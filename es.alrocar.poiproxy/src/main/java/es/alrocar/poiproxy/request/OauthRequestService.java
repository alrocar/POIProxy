package es.alrocar.poiproxy.request;

import java.net.URLEncoder;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import es.alrocar.poiproxy.configuration.Auth;
import es.alrocar.poiproxy.configuration.AuthTypeEnum;

/**
 * Makes Oauth requests using Scribe
 * https://github.com/fernandezpablo85/scribe-java
 * 
 * @author aromeu
 * 
 */
public class OauthRequestService implements RequestService {

	@Override
	public byte[] download(String URL, String fileName, String downloadPath,
			Auth authElem) throws Exception {
		AuthTypeEnum authType = AuthTypeEnum.valueOf(authElem.getType());

		OAuthService service = new ServiceBuilder().provider(authType._class)
				.apiKey(authElem.getApiKey())
				.apiSecret(authElem.getApiSecret()).build();

		OAuthRequest request = new OAuthRequest(Verb.GET, URL);
		Token myToken = new Token(authElem.getAccessToken(),
				authElem.getAccessTokenSecret());

		service.signRequest(myToken, request);
		Response response = request.send();

		return response.getBody().getBytes();
	}
}
