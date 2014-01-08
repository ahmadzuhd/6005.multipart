package multipart;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

public class MultipartManifestParser{

	public SingleCache cache;

	public List<List<String>> parts = new ArrayList<List<String>>();
	public int partIndex;
	public Stack<InputStream> streamStack = new Stack<InputStream>();
	public boolean firstManifest = false;
	private static final int DELIMITER = (int) '\n';

	public MultipartManifestParser(SingleCache cache, String url) throws IOException{
		this.cache = cache;
		partIndex = 0;
		ArrayList<String> manifest = new ArrayList<String>();
		manifest.add(url);
		parts.add(manifest);
		URLConnection connection = new URL(url).openConnection();
		streamStack.push(connection.getInputStream());
		String mimeType = connection.getContentType();
		if (url.endsWith("parts") || mimeType.equals("text/parts-manifest")){
			firstManifest = true;
		}
	}

	/** Pops the InputStream at the top of the stack and returns the next InputStream in the process.
	 * @return next InputStream in the manifest process.
	 * @throws EmptyStackException
	 * @throws IOException 
	 */
	public InputStream nextStream() throws EmptyStackException, IOException{
		System.out.println("nextStream starting");
		manifestRead();
		System.out.println("nextStream returning new stream");
		return streamStack.pop();
	}

	/**Reads a manifest file stream at the top of the stack
	 * and places the next part in the streamStack.
	 * @throws IOException 
	 */
	private void manifestRead() throws IOException{
		System.out.println("ManifestRead starting");
		InputStream stream = streamStack.peek();
		List<Integer> iList = new ArrayList<Integer>();
		int value = 0;
		while (value != -1){
			value = stream.read();
			while (value != DELIMITER){
				iList.add(value);
				value = stream.read();
				if (value == -1){
					streamStack.pop();
					manifestRead();
					return;
				}
			}
			ByteArrayOutputStream dest = new ByteArrayOutputStream();
			for (int i : iList){
				dest.write(i);
			}
			iList = new ArrayList<Integer>();
			String url = new String(dest.toByteArray());
			url = url.trim();
			System.out.println(url);
			boolean cached = false;
			boolean toCache = false;
			if (url.startsWith("(*)")){
				toCache = true;
				url = url.substring(3);
				if (cache.isInCache(url)){
					int[] intArray = cache.getCachedData(url);
					byte[] byteArray = new byte[intArray.length];
					for (int i = 0; i < intArray.length; i++){
						byteArray[i] = (byte) intArray[i];
					}
					streamStack.push(new ByteArrayInputStream(byteArray));
					System.out.println("Successfully pushed cached stream to stack.");
					cached = true;
				}
			}
			try {
				if (!cached){
					InputStream newStream = new URL(url).openStream();
					streamStack.push(newStream);
					System.out.println("Successfully pushed new stream to stack.");
				}
				URLConnection connection = new URL(url).openConnection();
				String mimeType = connection.getContentType();
				System.out.println("Checking if file is a manifest.");
				if (url.endsWith("parts") || mimeType.equals("text/parts-manifest")){
					System.out.println("Ended with parts or was manifest, running manifestRead()");
					manifestRead();
					return;
				}
				else{
					System.out.println(url + " is not a manifest.");
				}
				List<Integer> iList2 = new ArrayList<Integer>();
				String line = "";
				while (!line.equals("--")){
					int value2 = stream.read();
					while (value2 != DELIMITER){
						iList2.add(value2);
						value2 = stream.read();
						if (value2 == -1){
							System.out.println("Stream ended, return from manifestRead.");
							return;
						}
					}
					ByteArrayOutputStream dest2 = new ByteArrayOutputStream();
					for (int i : iList2){
						dest2.write(i);
					}
					line = new String(dest2.toByteArray());
				}
				if (toCache && !cached){
					InputStream newStream = new URL(url).openStream();
					List<Integer> iListi = new ArrayList<Integer>();
					int valuei = newStream.read();
					while (valuei != -1){
						iListi.add(valuei);
						valuei = newStream.read();
					}
					int[] iData = new int[iListi.size()];
					for (int i = 0; i < iListi.size(); i++){
						iData[i] = iListi.get(i);
					}
					cache.putDataInCache(url, iData);
				}
				System.out.println("Found --, returning from manifestRead()");
				return;
			} catch (IOException e) {
				System.out.println("Bad URL!");
			}
		}
		streamStack.pop();
		manifestRead();
	}
	
}