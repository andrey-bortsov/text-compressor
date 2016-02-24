package compressor;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class proj1Test {
	proj1 p;

	@Before
	public void setUp() throws Exception {
		p = new proj1();
	}

	@Test
	public void testCompress() {
		assertEquals("a , 1. 1 b 1 2 1 c,,, 1   ;1 3 3 +1 2 1 1 2 1", p.compress("a , a. a b b a a c,,, c   ;c b a +a b b b a a"));
	}
	
	@Test
	public void testDecompress() {
		//assertEquals("", proj1.compress("a , a.. a b b a b b a   "));
		assertEquals("a , a. a b b a a c,,, c   ;c b a +a b b b a a", p.decompress("a , 1. 1 b 1 2 1 c,,, 1   ;1 3 3 +1 2 1 1 2 1"));
	}

}
