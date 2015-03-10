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
		fileObj.delete();
		
		
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
		fileObj.delete();
	}
	
    @Test
    public void testTokenizeCode() {
            String a = "this is a test $100.00 + $200 = $300 % 3 = 0 label this @s ^100";
            String expected = "this is a test 100 00 200 300 3 0 label this s 100";
            String actual = utl.tokenizeCode(a);
            assertEquals(expected, actual);
    }
   
    @Test
    public void testCamelCaseTokenizeCode2() {
            String a = "CamelCase Test This is a MethodToTest(String myString)";
            String expected = "Camel Case Test This is a Method To Test String my String";
            String actual = utl.tokenizeCode(a);
            assertEquals(expected, actual);
    }
   
    @Test
    public void testCamelCaseTokenizeCode3() {
            String a = "DBException";
            String expected = "DB Exception";
            String actual = utl.tokenizeCode(a);
            assertEquals(expected, actual);
    }
    @Test
    public void testCamelCaseTokenizeCode5() {
            String b = "ALLCAP";          
            String actual = utl.tokenizeCode(b);
            assertEquals(b, actual);
    }
   
    @Test
    public void testUnderScoreTokenizeCode4() {
            String a = "DB_Exception";
            String expected = "DB Exception";
            String actual = utl.tokenizeCode(a);
            assertEquals(expected, actual);
    }
    @Test
    public void testUnderScoreTokenizeCode6() {
            String a = "bd_exception";
            String expected = "bd exception";
            String actual = utl.tokenizeCode(a);
            assertEquals(expected, actual);
    }
    
    @Test
    public void testProcessCode() {
            String a = "CamelCase Test This is a MethodToTest(String myString)";
            String expected = "Camel Case Test This is a Method To Test String my String";
            String actual = utl.processCode(a);
            assertEquals(expected, actual);
    }
   
    @Test
    public void testProcessCode3() {
            String a = utl.readSelectedFile(null,"C:/Users/User/workspace/Lab2_SE/src/utility/Utility.java");
            //String a = utl.readSelectedFile(null, "/Users/lxdavidxl/Downloads/iTrust/src/edu/ncsu/csc/itrust/EmailUtil.java");
            String expected = "/*test this*/";
            String actual = utl.processCode(a);
            System.out.println("Expected: " +expected);
            System.out.println("Actual: " +actual);
            //assertEquals(expected, actual);
    }
}
