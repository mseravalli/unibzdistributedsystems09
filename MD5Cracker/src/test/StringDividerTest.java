package test;

import static org.junit.Assert.*;

import org.junit.Test;

import cracker.ParsedString;
import cracker.StringDivider;


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
		
		
		
		ParsedString[] testedStrings = sd.getTestedStrings();
		
		for(int i = 0; i < 6; i++){
			sd.setStartedInArray(true, i);
			sd.setFinishedInArray(true, i);
		}	
		
		

		
		sd.reconstructParsedString(6);
		
		
		for(int i = 0; i < StringDivider.PARSED_STRING_LENGTH; i++){
			System.out.printf("%s - %b - %b\n",testedStrings[i].str, testedStrings[i].isStarted, testedStrings[i].isFinished);
		}		
		
		
		
		assertEquals(true, "AAAA***".equals(sd.getTestedStrings()[9].str));
	}

}
