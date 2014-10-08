package es.alrocar.poiproxy.request;

import es.alrocar.poiproxy.configuration.Auth;
import es.alrocar.utils.Downloader;

/**
 * Makes plain Http request
 * 
 * @author aromeu
 * 
 */
public class SimpleHttpRequestService implements RequestService {

	private Downloader d;

	@Override
	public byte[] download(String URL, String fileName, String downloadPath,
			Auth authElem) throws Exception {
		d = new Downloader();

		d.downloadFromUrl(URL, fileName, downloadPath, null);

		return d.getData();
	}
}
