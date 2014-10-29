package es.alrocar.poiproxy.proxy.event;

import es.alrocar.poiproxy.configuration.DescribeService;
import es.alrocar.poiproxy.proxy.POIProxy;
import es.alrocar.poiproxy.proxy.POIProxyListener;
import es.prodevelop.gvsig.mini.geom.Extent;

/**
 * An Event that {@link POIProxy} notifies to {@link POIProxyListener}
 * 
 * @author aromeu
 * 
 */
public class POIProxyEvent {

	private Extent extent;
	private POIProxyEventEnum type;
	private Integer z;
	private Integer y;
	private Integer x;
	private Double lon;
	private Double lat;
	private Double distance;
	private String query;

	private DescribeService describeService;

	private String parsedData;

	public POIProxyEvent(POIProxyEventEnum type, DescribeService service,
			Extent extent, Integer z, Integer y, Integer x, Double lon,
			Double lat, Double distance, String query, String parsedData) {
		super();
		this.extent = extent;
		this.describeService = service;
		this.type = type;
		this.z = z;
		this.y = y;
		this.x = x;
		this.lon = lon;
		this.lat = lat;
		this.distance = distance;
		this.query = query;
		this.parsedData = parsedData;
	}

	public DescribeService getDescribeService() {
		return describeService;
	}

	public Extent getExtent() {
		return extent;
	}

	public POIProxyEventEnum getType() {
		return type;
	}

	public Integer getZ() {
		return z;
	}

	public Integer getY() {
		return y;
	}

	public Integer getX() {
		return x;
	}

	public Double getLon() {
		return lon;
	}

	public Double getLat() {
		return lat;
	}

	public Double getDistance() {
		return distance;
	}

	public String getQuery() {
		return query;
	}

	public String getParsedData() {
		return parsedData;
	}
}
