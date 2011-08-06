package es.alrocar.map.vector.provider.filesystem;

import java.util.ArrayList;

import es.prodevelop.gvsig.mini.utiles.Cancellable;

public interface IVectorFileSystemProvider {

	public ArrayList load(int[] tile, int zoomLevel, String driverName,
			Cancellable cancellable);

	public void save(int[] tile, int zoomLevel, String driverName,
			Cancellable cancellable, ArrayList data);
}
