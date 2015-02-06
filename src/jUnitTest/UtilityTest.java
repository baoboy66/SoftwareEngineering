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

		/*String folder = "C:\\Users\\User\\Desktop\\School\\test\\insidetest folder";
		String file = utl.readSelectedFile(folder,"abc");
		//System.out.println("result \n" + file);
		
		//String a = "this is a test!";
		String b = utl.tokenize(file);
		//System.out.println(b);
		assertEquals("this is a test", b);
		*/
		//test getAcronymsList 
		String acronymsList = "C:\\Users\\User\\Desktop\\School\\test\\insidetest folder\\Acronym_List.txt";
		HashMap<String,String> hash = utl.getAcronymsList(acronymsList);
		assertEquals("Emergency Responder", hash.get("ER"));
		
		String inputString = utl.tokenize("An admin creates a LHCP, an ER, a LT, or a PHA.");
	    System.out.println(utl.restoringAcronyms(inputString, acronymsList));
		
	}
	
	public void testReadSelectFile() {

	}

}
