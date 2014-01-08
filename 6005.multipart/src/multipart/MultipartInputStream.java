package multipart;

import java.io.IOException;
import java.io.InputStream;
import java.util.EmptyStackException;

public class MultipartInputStream extends InputStream{

	private MultipartManifestParser parser;
	private InputStream stream;

	public MultipartInputStream(MultipartManifestParser parser) throws EmptyStackException, IOException{
		this.parser = parser;
		stream = parser.nextStream();
	}

	public int read() throws IOException {
		int readValue = stream.read();
		if (!parser.firstManifest){
			//Keep reading from the first stream. Once that ends, keep returning -1.
			return readValue;
		}
		else{
			//This section is if the first inputStream was a manifest.
			try{
				if (readValue == -1){
					while (readValue == -1){
						System.out.println("readValue was -1, so returning new readValue.");
						stream = parser.nextStream();
						System.out.println("Got new stream, reading new number");
						readValue = stream.read();
					}
					return readValue;
				}
				else{
					return readValue;
				}
			}
			catch (EmptyStackException e){
				return -1;
			}
		}
	}
}