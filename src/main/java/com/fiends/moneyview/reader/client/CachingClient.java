package com.fiends.moneyview.reader.client;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.collections4.map.PassiveExpiringMap;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This is wrapped over the client in the AbstractPipeImpl class.
 * This adds caching to client requests.  The cache map will contain URLs from ALL clients.
 */
public class CachingClient implements ClientIf {

	static final String STORE_CACHE_PATH =  "docs/ClientResultsCache.dat";
	static final Long ONE_WEEK_MILLIS = 7L * 24 * 3600 * 1000L;

	static final public AtomicReference<PassiveExpiringMap<String,JsonNode>> CACHE_REF =
			new AtomicReference<>( new PassiveExpiringMap<String,JsonNode>( ONE_WEEK_MILLIS ) );

	ClientIf client;

	public CachingClient(ClientIf client ) {
		this.client = client;
	}

	public String getFullPath( String relPath ){return client.getFullPath(relPath);}

	static PassiveExpiringMap<String,JsonNode> getCache(){return CACHE_REF.get();}

	/**
	 * Simple exception handling for loading cache.
	 */
	void load() {
		try { synchronized (this) {preLoad();} }
		catch (Exception ex){throw new RuntimeException(ex);}
	}
	static void preLoad() throws IOException, ClassNotFoundException {

		PassiveExpiringMap<String,JsonNode> cache = getCache();
		if (!cache.isEmpty()) return;

		File file = new File(STORE_CACHE_PATH);
		if (!file.exists()) return;

		FileInputStream fileIn = new FileInputStream( file );
		ObjectInputStream objectIn = new ObjectInputStream(fileIn);
		cache = (PassiveExpiringMap)objectIn.readObject();

		CACHE_REF.set( cache );
	}

	/**
	 * Simple exception handling for storing cache.
	 */
	void store() {
		try { synchronized(this){postStore();} }
		catch (Exception ex){throw new RuntimeException(ex);}
	}
	static public void postStore() throws IOException {

		PassiveExpiringMap<String,JsonNode> cache = getCache();
		if (cache.isEmpty()) return;

		File file = new File(STORE_CACHE_PATH);
		if (!file.exists()) {
			FileUtils.forceMkdirParent(file);
		}

		FileOutputStream fileOut = new FileOutputStream(file);
		ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
		objectOut.writeObject(cache);
		objectOut.close();
	}

	/**
	 * Retrieve the JSON response from the relative path for the service implementation.
	 * @param relPath
	 * @return
	 */
	public JsonNode get(String relPath ) {

		load();

		String fullPath = getFullPath( relPath );
		PassiveExpiringMap<String,JsonNode> cache = getCache();

		// has path results ?
		JsonNode node = cache.get( fullPath );

		// need value
		if (node==null) {
			node = client.get(relPath);
			cache.put(fullPath, node);
			store();
		}

		// result
		return node;
	}
}
