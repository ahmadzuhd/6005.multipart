package animation;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for AnimationReader.
 */
public class AnimationReaderTest {
	
	/**
	 * Constructs an animation consisting of one zero-byte
	 * frame (not the same as zero frames!) and tests behavior on the
	 * animation.
	 */
	@Test public void oneEmptyFrame() throws IOException {
		InputStream animation = new ByteArrayInputStream(new byte[]{0,0,0,0});
		
		Assert.assertArrayEquals("doesn't return zero-length frame",
				AnimationReader.readOneFrame(animation), new byte[]{});
		
		Assert.assertEquals("didn't read all four bytes",
				animation.read(), -1);
		
		Assert.assertNull( "didn't detect end of animation",
				AnimationReader.readOneFrame(animation) );
	}
}
