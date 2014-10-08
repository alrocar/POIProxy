package es.alrocar.poiproxy.request;

import es.alrocar.poiproxy.configuration.Auth;
import es.alrocar.poiproxy.configuration.AuthTypeEnum;

public interface RequestService {

	/**
	 * 
	 * @param URL
	 *            The URL where download data from
	 * @param fileName
	 *            The name of the file to store the data in
	 * @param downloadPath
	 *            The path where to store the file
	 * @param authElement
	 *            The {@link Auth} configuration that can have, apiKeys,
	 *            credentials, etc.
	 * @return An array of bytes with the data
	 * @throws Exception
	 */
	public byte[] download(String URL, String fileName, String downloadPath,
			Auth authElement) throws Exception;
}
