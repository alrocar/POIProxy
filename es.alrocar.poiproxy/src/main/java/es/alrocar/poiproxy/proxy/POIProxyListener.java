package es.alrocar.poiproxy.proxy;

import es.alrocar.poiproxy.proxy.event.POIProxyEvent;

/**
 * An Observer interface for {@link POIProxyEvent}
 * 
 * @author aromeu
 * 
 */
public interface POIProxyListener {

	/**
	 * Sends a {@link POIProxyEvent} after having requested a service and parsed
	 * the data
	 * 
	 * @param event
	 *            The {@link POIProxyEvent}
	 */
	public void afterParseResponse(POIProxyEvent event);

	/**
	 * Sends a {@link POIProxyEvent} before requesting a service
	 * 
	 * @param event
	 *            The {@link POIProxyEvent}
	 */
	public void beforeRequest(POIProxyEvent poiProxyEvent);
}
