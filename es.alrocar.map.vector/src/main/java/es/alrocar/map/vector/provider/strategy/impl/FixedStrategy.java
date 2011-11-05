package es.alrocar.map.vector.provider.strategy.impl;

import es.alrocar.map.vector.provider.observer.VectorialProviderListener;
import es.prodevelop.gvsig.mini.geom.Extent;
import es.prodevelop.gvsig.mini.utiles.Cancellable;

public class FixedStrategy extends BBoxTiledStrategy {

	private Extent extent;
	private int zoomLevel;

	public FixedStrategy(Extent extent, int zoomLevel) {
		this.extent = extent;
		this.zoomLevel = zoomLevel;
	}

	public void getVectorialData(int[][] tiles, int zoomlevel,
			Extent viewExtent, VectorialProviderListener observer,
			Cancellable cancellable) {
		cacheValues(tiles, this.zoomLevel, extent);
		Extent convertedExtent = convertExtent(extent);
		notifyObserver(observer, tiles[0], cancellable, getProvider()
				.getDriver().getData(null, convertedExtent, cancellable, zoomLevel), zoomLevel);
	}

}
