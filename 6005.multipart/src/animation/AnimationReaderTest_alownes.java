package animation;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for AnimationReader from Alec Lownes.
 */
public class AnimationReaderTest_alownes{
	
	@Test
	public void readOneFrame() throws IOException {
		InputStream animation = new ByteArrayInputStream(new 
				byte[]{0,0,0,5,0x36,0x2E,0x30,0x30,0x35});
		
		Assert.assertArrayEquals("returns the right frame",
				AnimationReader.readOneFrame(animation), new byte[]{0x36,0x2E,0x30,0x30,0x35});
		
		Assert.assertEquals("didn't read all four bytes",
				animation.read(), -1);
		
		Assert.assertNull( "didn't detect end of animation",
				AnimationReader.readOneFrame(animation) );
	}
	
	@Test(expected = EOFException.class)
	public void throwEOF() throws EOFException, IOException{
		InputStream animation = new ByteArrayInputStream(new 
				byte[]{0,0,0,5,0x36,0x2E,0x30,0x30});
		
		AnimationReader.readOneFrame(animation);
	}
}