package es.alrocar.map.vector.provider.filesystem.impl;

import java.util.ArrayList;

import es.alrocar.map.vector.provider.filesystem.IVectorFileSystemProvider;
import es.prodevelop.gvsig.mini.utiles.Cancellable;

public class JSONFileSystemProvider implements IVectorFileSystemProvider {

	public ArrayList load(int[] tile, int zoomLevel, String driverName,
			Cancellable cancellable) {
		// TODO Auto-generated method stub
		return null;
	}

	public void save(int[] tile, int zoomLevel, String driverName,
			Cancellable cancellable, ArrayList data) {
		// TODO Auto-generated method stub

		//Al persistir hay que guardar tambien el tipo
		//bastaría con guardar el Attribute.TYPE_*
		//para que al cargar se pueda saber de qué tipo es cada cosa
		
//		{ "type": "FeatureCollection",
//			  "features": [
//			    { "type": "Feature",
//			      "geometry": {"type": "Point", "coordinates": [102.0, 0.5]},
//			      "properties": {
//				        "prop0": "value0",
//				        "prop1": 0.0
//				   }, 
//				   "data_types": {
//				        "prop0": "0", //TYPE_STRING
//				        "prop1": "2"  // TYPE_DOUBLE
//				   }
//				 }
//			   ]
//		}
	}

}
