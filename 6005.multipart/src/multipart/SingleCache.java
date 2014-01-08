package multipart;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lownes
 *
 */
public class SingleCache implements MultipartCache{

	private Map<String, int[]> cacheMap = new HashMap<String, int[]>();
	
	/**
	 * Checks if a url is in the cacheMap
	 * @param url - url string
	 * @return true if the url is in the cache, false otherwise
	 */
	public boolean isInCache(String url) {
		return cacheMap.containsKey(url);
	}
	
	/**
	 * Retrieves the cached data associated with the url.
	 * @param url - url string of data
	 * @return cached data
	 */
	public int[] getCachedData(String url) {
		return cacheMap.get(url);
	}

	/**
	 * Puts data into the cache
	 * @param url - url associated with the data
	 * @param data - data
	 */
	public void putDataInCache(String url, int[] data) {
		if (cacheMap.size() == MAX_CACHE_SIZE){
			cacheMap.remove(cacheMap.keySet().toArray()[0]);
		}
		cacheMap.put(url, data);
	}
	
}