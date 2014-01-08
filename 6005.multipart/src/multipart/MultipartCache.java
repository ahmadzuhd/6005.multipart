package multipart;

/**
 * An interface for a cache that stores content stored from particular URLs.
 * 
 * The cache must not have duplicates in it, and must be of a finite size <=
 * MAX_CACHE_SIZE.
 * 
 */
public interface MultipartCache {
	/**
	 * A cache should not store data for more than these many links at any given
	 * time.
	 */
	int MAX_CACHE_SIZE = 3;

	/**
	 * Returns true of content for the provided URL is stored in the cache.
	 */
	public boolean isInCache(String url);

	/**
	 * Returns null if there is no cached content for the provided URL.
	 * Otherwise, returns an array of integers representing data read from the
	 * URL.
	 */
	public int[] getCachedData(String url);

	/**
	 * Stores an array of integers representing data read from the URL in the
	 * cache.
	 * 
	 * Evicts some other data previously stored in cache if cache is already
	 * full.
	 */
	public void putDataInCache(String url, int[] data);
}
