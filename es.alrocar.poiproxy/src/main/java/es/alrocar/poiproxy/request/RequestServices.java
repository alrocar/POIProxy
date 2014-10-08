package es.alrocar.poiproxy.request;

import java.util.HashMap;
import java.util.Map;

import es.alrocar.poiproxy.configuration.Auth;
import es.alrocar.poiproxy.configuration.AuthTypeEnum;
import es.alrocar.poiproxy.configuration.DescribeService;

/**
 * A manager of available {@link RequestService}
 * 
 * Dependending on the {@link Auth} configuration of a {@link DescribeService}
 * object a {@link RequestService} is used to downlaod data.
 * 
 * @author aromeu
 * 
 */
public class RequestServices {

	private Map<AuthTypeEnum, RequestService> requestServices = new HashMap<AuthTypeEnum, RequestService>();

	private static RequestServices instance;

	public static RequestServices getInstance() {
		if (instance == null) {
			instance = new RequestServices();
		}

		return instance;
	}

	public void addRequestService(AuthTypeEnum authType,
			RequestService requestService) {
		this.requestServices.put(authType, requestService);
	}

	public RequestService getRequestService(AuthTypeEnum authTypeEnum) {
		return this.requestServices.get(authTypeEnum);
	}

	public RequestService getRequestService(String authType) {
		return this.requestServices.get(AuthTypeEnum.valueOf(authType));
	}
}
