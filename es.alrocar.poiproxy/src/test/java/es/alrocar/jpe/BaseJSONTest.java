package es.alrocar.jpe;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;

import es.alrocar.poiproxy.proxy.POIProxy;
import es.prodevelop.gvsig.mini.utiles.Constants;
import junit.framework.TestCase;

public abstract class BaseJSONTest extends TestCase {

	public String getJSON(String resource) {
		File f = new File(resource);
		FileInputStream fis = null;
		InputStream in = null;
		OutputStream out = null;
		String res = null;
		try {
			fis = new FileInputStream(f);
			in = new BufferedInputStream(fis, Constants.IO_BUFFER_SIZE);
			final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
			out = new BufferedOutputStream(dataStream, Constants.IO_BUFFER_SIZE);
			byte[] b = new byte[8 * 1024];
			int read;
			int total = 0;
			while ((read = in.read(b)) != -1) {
				total += read;
				out.write(b, 0, read);
			}
			out.flush();
			res = new String(dataStream.toByteArray());

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			Constants.closeStream(fis);
			Constants.closeStream(in);
			Constants.closeStream(out);
		}

		return res;
	}

	public String getJSON() {
		URL uReport = POIProxy.class.getClassLoader().getResource(
				this.getResource());
		File f = new File(uReport.getPath());
		FileInputStream fis = null;
		InputStream in = null;
		OutputStream out = null;
		String res = null;
		try {
			fis = new FileInputStream(f);
			in = new BufferedInputStream(fis, Constants.IO_BUFFER_SIZE);
			final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
			out = new BufferedOutputStream(dataStream, Constants.IO_BUFFER_SIZE);
			byte[] b = new byte[8 * 1024];
			int read;
			int total = 0;
			while ((read = in.read(b)) != -1) {
				total += read;
				out.write(b, 0, read);
			}
			out.flush();
			res = new String(dataStream.toByteArray());

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			Constants.closeStream(fis);
			Constants.closeStream(in);
			Constants.closeStream(out);
		}

		return res;
	}

	public abstract String getResource();

	public String getTestFilePath(String testFile) {
		URL uReport = POIProxy.class.getClassLoader().getResource(testFile);

		if (uReport == null)
			return null;

		return uReport.getPath();
	}
}
