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
	 * @return if returns false, then the request is cancelled
	 */
	public boolean beforeRequest(POIProxyEvent poiProxyEvent);

	/**
	 * Called when {@link POIProxy} is destroyed. Free resources please.
	 */
	public void destroy();
}
