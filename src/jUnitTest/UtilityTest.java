package jUnitTest;

import static org.junit.Assert.*;

import java.io.*;
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
		assertEquals(expectedResult, utl.getStems(str));
	}
	
	@Test
	public void testRemoveStopWords(){
		String originalString ="The big brown dog had a bone that was white ";
		File fileObj = new File("stoptest.txt");
		try {
			fileObj.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Writer writer = null;
		try {
		    writer = new BufferedWriter(new OutputStreamWriter(
		          new FileOutputStream("stoptest.txt"), "utf-8"));
		    writer.write("horse,potato,the,was,a,green,that");
		} catch (IOException ex) {
		  // report
		} finally {
		   try {writer.close();} catch (Exception ex) {}
		}
		String expectedResult = "big brown dog had bone white";
		String result = utl.removeStopWords(originalString, "stoptest.txt");
		assertEquals(expectedResult, result);
		
		
	}
	
	@Test
	public void testReadSelectFile() {

		File fileObj = new File("test.txt");
		try {
			fileObj.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Writer writer = null;

		try {
		    writer = new BufferedWriter(new OutputStreamWriter(
		          new FileOutputStream("test.txt"), "utf-8"));
		    writer.write("Something to test LCHP +++ hopefully it passes!!![]]]}{");
		} catch (IOException ex) {
		  // report
		} finally {
		   try {writer.close();} catch (Exception ex) {}
		}
		String readTest = utl.readSelectedFile(".", "test");
		assertEquals("Something to test LCHP +++ hopefully it passes!!![]]]}{", readTest);
	}
}
