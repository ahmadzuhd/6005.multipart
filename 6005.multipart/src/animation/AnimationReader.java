package animation;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Static class for reading from a file animation stream.
 * <p>
 * The format for file animation streams can be found
 * <a href="http://mit.edu/6.005/www/fa10/projects/multipart/lab.html">here</a>.
 *
 */
public class AnimationReader {
	/**
	 * Returns the data from the next frame in the given file animation stream.
	 * <p>
	 * If no frames remain, returns null. If the stream ends prematurely,
	 * throws an EOFException.
	 */
	public static byte[] readOneFrame(InputStream sequence)
		throws IOException, EOFException {
		// animations consist of a (4-byte) int giving the size of the frame,
		// followed by the frame, followed by another size, followed by the frame,
		// and so on until EOF
		int size;
		// note that this hides errors involving less than 4 trailing bytes:
		try {
			size = new DataInputStream(sequence).readInt();
		} catch(EOFException e) { // no more frames
			return null;
		}

		byte[] data = new byte[size];
		int read = 0;
		while(read<size) {
			int justRead = sequence.read(data, read, size-read);
			if(justRead==-1)
				throw new EOFException("stream ended after only "+read+" bytes of "+size+"-byte frame!");
			read += justRead;
		}
		return data;
	}
}
