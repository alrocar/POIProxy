package es.alrocar.map.vector.provider.filesystem.impl;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import org.json.JSONException;

import es.alrocar.map.vector.provider.filesystem.IVectorFileSystemProvider;
import es.prodevelop.gvsig.mini.exceptions.BaseException;
import es.prodevelop.gvsig.mini.geom.impl.jts.JTSFeature;
import es.prodevelop.gvsig.mini.utiles.Cancellable;
import es.prodevelop.gvsig.mini.utiles.Constants;

public class GeoJSONFileSystemProvider implements IVectorFileSystemProvider {

	private String baseDir;
	private GeoJSONParser parser = new GeoJSONParser();
	private GeoJSONWriter writer = new GeoJSONWriter();

	public GeoJSONFileSystemProvider(String baseDir) {
		if (baseDir != null) {
			if (!baseDir.endsWith(File.separator)) {
				baseDir += File.separator;
			}
		}

		this.baseDir = baseDir;
	}

	public ArrayList load(int[] tile, int zoomLevel, String driverName,
			Cancellable cancellable) {
		if (cancellable != null && cancellable.getCanceled()) {
			return null;
		}

		StringBuffer dir = new StringBuffer(baseDir).append(driverName)
				.append(File.separator).append(zoomLevel)
				.append(File.separator).append(tile[0]).append(File.separator)
				.append(tile[1]).append(".geojson");
		File f = new File(dir.toString());

		if (!f.exists()) {
			return null;
		}

		if (f.length() <= 0) {
			f.delete();
			return null;
		}

		FileInputStream in = null;
		OutputStream out = null;
		ByteArrayOutputStream dataStream = null;
		try {
			in = new FileInputStream(f);

			boolean succeed = false;
			int retryCount = 0;

			dataStream = new ByteArrayOutputStream();

			out = new BufferedOutputStream(dataStream, Constants.IO_BUFFER_SIZE);

			copy(in, out);
			out.flush();

			final byte[] data = dataStream.toByteArray();

			String json = new String(data, "UTF-8");
			return parser.parse(json);
		} catch (IOException e) {
			return null;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			return null;
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (dataStream != null) {
				try {
					dataStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void save(int[] tile, int zoomLevel, String driverName,
			Cancellable cancellable, Object data) {
		StringBuffer file = new StringBuffer(baseDir).append(driverName)
				.append(File.separator).append(zoomLevel)
				.append(File.separator).append(tile[0]).append(File.separator)
				.append(tile[1]).append(".geojson");
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		try {
			File f = new File(file.toString());

			if (f.exists()) {
				if (f.length() <= 0) {
					// log.log(Level.FINE, "Deleting invalid file");
					f.delete();
					f.createNewFile();
				}
			} else {

				File dir = new File(f.getParent());
				if (dir.mkdirs()) {

				} else {
					if (f.getParent() == null) {
						// log.log(Level.FINE, "parent = null");
					}
					// log.log(Level.FINE, "directories failed " + aURLString);
				}

			}

			fos = new FileOutputStream(f);

			bos = new BufferedOutputStream(fos, Constants.IO_BUFFER_SIZE);			

			// log.log(Level.FINE, "prepared to write " + aURLString);
			bos.write(data.toString().getBytes());
			bos.flush();
			// log.log(Level.FINE, "Tile stored " + aURLString);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			Constants.closeStream(fos);
			Constants.closeStream(bos);
		}
	}

	public static void copy(InputStream in, OutputStream out)
			throws IOException {
		byte[] b = new byte[1024 * 8];
		int read;
		while ((read = in.read(b)) != -1) {
			out.write(b, 0, read);
		}
	}

}
