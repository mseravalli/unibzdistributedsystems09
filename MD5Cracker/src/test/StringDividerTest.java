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
		
		for(int i = 0; i < sd.PARSED_STRING_LENGTH; i++){
			//System.out.println(testedStrings[i].str);
		}	
		
		sd.setStringInArray("", 1);	
		sd.setStringInArray("", 2);	
		sd.setStringInArray("", 3);	
		sd.setStringInArray("", 4);	
		sd.setStringInArray("", 5);	
		sd.reconstructParsedString();
		
		
		for(int i = 0; i < sd.PARSED_STRING_LENGTH; i++){
			//System.out.println(testedStrings[i].str);
		}		
		
		
		
		assertEquals(true, "BBB***".equals(sd.getTestedStrings()[9].str));
	}

}
