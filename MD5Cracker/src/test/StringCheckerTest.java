package test;

import static org.junit.Assert.*;

import org.junit.Test;

import cracker.*;

public class StringCheckerTest {

	@Test
	public void testCompute() {
		
		fail("Not yet implemented");
	}

	@Test
	public void testCheckStrings() {
		fail("Not yet implemented");
	}

	@Test
	public void testCompare() {
		System.out.println(StringChecker.compare( "ciao mamma!!!","2317c3f5359466bbee2e0ff62d468b7c"));	
		assertEquals("ciao mamma!!!", StringChecker.compare( "ciao mamma!!!","2317c3f5359466bbee2e0ff62d468b7c"));
	}

	@Test
	public void testEncode() {
		
		System.out.println(StringChecker.encode("ciao mamma!!!"));
		
		assertEquals("2317c3f5359466bbee2e0ff62d468b7c", StringChecker.encode("ciao mamma!!!"));
	}

}
