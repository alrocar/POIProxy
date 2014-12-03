package es.alrocar.poiproxy.proxy;

import es.alrocar.poiproxy.proxy.event.POIProxyEvent;

public interface CacheListener extends POIProxyListener {

	/**
	 * Reads data from cache
	 * 
	 * @param event
	 * @return The cached data
	 */
	public String read(POIProxyEvent event);

	/**
	 * Writes data to cache
	 * 
	 * @param event
	 * 
	 */
	public void write(POIProxyEvent event);

	/**
	 * Features have been parsed
	 * 
	 * @param event
	 * @return
	 * 
	 */
	public String onFeaturesParsed(POIProxyEvent event);
}
