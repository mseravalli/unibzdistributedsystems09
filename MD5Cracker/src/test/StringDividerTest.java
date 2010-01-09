package test;

import static org.junit.Assert.*;

import org.junit.Test;

import Cracker.StringDivider;

public class StringDividerTest {
	
	StringDivider sd = new StringDivider();

	@Test
	public void testStringDivider() {
		
		assertEquals(true, "AAA***".equals(sd.getTestedStrings()[7].str));
	}

	@Test
	public void testGetTestedStrings() {
		assertEquals(true, "***".equals(sd.getTestedStrings()[0].str));
	}

	@Test
	public void testCreateNextString() {
		assertEquals(true, "AAB***".equals(sd.createNextString("AAA***")));
	}

	@Test
	public void testRecurseLastChar() {
		assertEquals(true, "AAAA***".equals(sd.createNextString("BBB***")));
	}

	@Test
	public void testReconstructParsedString() {
		fail("Not yet implemented");
	}

}
