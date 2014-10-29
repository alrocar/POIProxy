package es.alrocar.poiproxy.proxy.event;

import es.alrocar.poiproxy.proxy.POIProxyListener;

/**
 * Type of {@link POIProxyEvent} notified to {@link POIProxyListener} 
 * @author aromeu
 *
 */
public enum POIProxyEventEnum {
	
	BeforeBrowseZXY(10, "BeforeBrowseZXY"),
	AfterBrowseZXY(11, "AfterBrowseZXY"),
	BeforeBrowseExtent(20, "BeforeBrowseExtent"),
	AfterBrowseExtent(21, "AfterBrowseExtent"),
	BeforeBrowseLonLat(30, "BeforeBrowseLonLat"),
	AfterBrowseLonLat(31, "AfterBrowseLonLat"),
	BeforeSearchZXY(40, "BeforeSearchZXY"),
	AfterSearchZXY(41, "AfterSearchZXY"),
	BeforeSearchExtent(50, "BeforeSearchExtent"),
	AfterSearchExtent(51, "AfterSearchExtent"),
	BeforeSearchLonLat(60, "BeforeSearchLonLat"),
	AfterSearchLonLat(61, "AfterSearchLonLat"),
	BeforeDescribeService(70, "BeforeDescribeService"),
	AfterDescribeService(71, "AfterDescribeService");
	
	public int id;
	public String description;
	
	private POIProxyEventEnum(int id, String description) {
		this.id = id;
		this.description = description;
	}
}
