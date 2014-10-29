package es.alrocar.poiproxy.proxy.event;

import es.alrocar.poiproxy.proxy.POIProxyListener;

/**
 * Type of {@link POIProxyEvent} notified to {@link POIProxyListener} 
 * @author aromeu
 *
 */
public enum POIProxyEventEnum {
	
	BeforeBrowseZXY(10),
	AfterBrowseZXY(11),
	BeforeBrowseExtent(20),
	AfterBrowseExtent(21),
	BeforeBrowseLonLat(30),
	AfterBrowseLonLat(31),
	BeforeSearchZXY(40),
	AfterSearchZXY(41),
	BeforeSearchExtent(50),
	AfterSearchExtent(51),
	BeforeSearchLonLat(60),
	AfterSearchLonLat(61),
	BeforeDescribeService(70),
	AfterDescribeService(71);
	
	public int id;
	
	private POIProxyEventEnum(int id) {
		this.id = id;
	}
}
