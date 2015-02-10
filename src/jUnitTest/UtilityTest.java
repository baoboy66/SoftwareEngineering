package jUnitTest;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import org.junit.Test;

import utility.Utility;
public class UtilityTest {

	Utility utl = new Utility();
	@Test
	public void test() {
		
	}
	
	@Test
	public void testGetIndexingString(){
		long startTime = 0;
		long endTime = 1000;
		int fileCount = 5;
		String result = utl.getIndexingString(startTime, endTime, fileCount);
		String expectedResult = "Indexing time of 5 requirements is: 1000 milliseconds";
		assertEquals(expectedResult, result);
	}
	
	@Test
	public void testConvertArrayToString() {
		String[] arr = {"this", "is", "a", "test"};
		String expectedResult = "this is a test";
		assertEquals(expectedResult, utl.convertArrayToString(arr));
	}
	
	@Test
	public void testTokenize(){
		String str = "'Test (a) != {b} and c > d";
		String expectedResult = "'Test a b and c d";
		assertEquals(expectedResult, utl.tokenize(str));
	}
	
	@Test
	public void testGetStems(){
		String str = "The best and most beautiful things in the world cannot be seen or even touched - they must be felt with the heart.";
		String expectedResult = "the best and most beauti thing in the world cannot be seen or even touch  thei must be felt with the heart";
		System.out.println(utl.getStems(str));
	}
}
