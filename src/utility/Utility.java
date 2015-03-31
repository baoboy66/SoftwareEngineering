package utility;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import IRUtilities.Porter;

public class Utility {

	/**
	 * This function takes the filePath string parameter it is passed, ensures that the path
	 * is valid and that it also exists. It then creates a string array of the directory's
	 * contents, which are parsed with regex to ensure that only ".txt" files are added to
	 * the array list that is returned to the calling function. 
	 * 
	 * @param filePath
	 * @return ArrayList<String> directoryFilesMatched
	 */
	
    public ArrayList<String> findFileNames(String directory){
        String regexMatchCondition = ".+\\.txt$";
        ArrayList<String> directoryFilesMatched = new ArrayList<String>();
        File directoryObj =  new File(directory);

        if (directoryObj.exists() && directoryObj.isDirectory()){
            String[] files = directoryObj.list();

            for(String out : files){
                if(out.matches(regexMatchCondition)){
                    directoryFilesMatched.add(out.substring(0, out.lastIndexOf(".")));
                }
            }
        }
        else{
            //Pop-Up for invalid directory
        	try{
            MessageDialog.openInformation(new Shell(),
                                          "Error",
                                          "Invalid File Path, Try Again Please.");}
        	catch(Exception exp){};
        }

        return directoryFilesMatched;
    }
    
    public void findAllFileNames(String directory, ArrayList<String> listIn){
    	String regexExtensionMatch = ".+\\.java";
        File directoryObj =  new File(directory);
        File[] filesFound = directoryObj.listFiles();
        
        for (File out : filesFound){
        	if(out.isDirectory()){
        		findAllFileNames(out.toString(), listIn);
        	}
        	else if(out.toString().matches(regexExtensionMatch)){
        		String filename = out.toString();
        		//listIn.add(filename.substring(filename.lastIndexOf("/") + 1, filename.lastIndexOf(".")));
        		listIn.add(filename);
        	}
        }
    }
    
    /**
     * This function takes the name of the file that is selected from the ComboViewer[Dropdown],
     * adds the file path and extension to it in order to create a file object. Then the file is
     * parsed using a Buffered Reader and FileReader in conjunction to store the lines of the file
     * in the ArrayList that is returned.
     * 
     * @param filename
     * @return fileContents: Array if successful
     * 		   null: if exception is thrown
     */
    public String readSelectedFile(String filePath ,String filename){
        File openingDirectory = new File(filePath + "/" + filename + ".txt");
        if (filePath == null) {openingDirectory = new File(filename);}
        String fileContents = "";
        try {
                    BufferedReader fileStream = new BufferedReader(new FileReader(openingDirectory));
                    int charRead;
                    while ((charRead = fileStream.read()) != -1)
                    {
                      fileContents += (char)charRead;
                    }
                    fileStream.close();
                    return fileContents;
                } catch (Exception e) {
                        System.err.printf("File: %s is corrupted or empty.", filename);
                        return null;
                }
    }
    /***
     * This function takes in the time when the program starts, the time when the program finishes,
     * and the number of files indexed and calculates the time it took, in milliseconds, and 
     * returns it for the combo viewer to display. 
     * @param startTime
     * @param finishTime
     * @param fileCount
     * @return
     */
    public String getIndexingString(long startTime, long finishTime, int fileCount){
        String secondsPassed = Long.toString((finishTime-startTime));
        return "Indexing time of " + fileCount + " requirements is: " + secondsPassed + " milliseconds";
    }
    /***
     * This function takes in an array of Strings and concatenates them with a space inbetween
     * each word to create a single string. This concatenated string is then returned. If there
     * are no strings in the array, a empty string is returned. 
     * @param strings
     * @return
     */
    public static String convertArrayToString(String[] strings)
    {
    	if(strings.length <= 0){
    		return "";
    	}
		StringBuilder stringBuild = new StringBuilder();
		stringBuild.append(strings[0]);
		for(int i = 1; i < strings.length; i++){
			stringBuild.append(" ");
			stringBuild.append(strings[i]);
		}
		return stringBuild.toString();
    }
    
    /***
     * This function takes a string and breaks it into a words as at every character that is not
     * in the .split() list. 
     * @param strList
     * @return
     */
    public String tokenize(String strList){
    	strList = strList.trim();
		String[] tokens = strList.split("[^a-zA-Z0-9_/']+");
		return convertArrayToString(tokens);
	}
    
    /***
     * This function takes a string and breaks it into a words as at every character that is not
     * in the .split() list. This also splits the words by camel case.
     * @param strList
     * @return tokens as string
     */
    public static String tokenizeCode(String strList){
		strList = strList.trim();
		
		//Remove underscores _
		//strList = strList.replace("_", "");
		
		String[] tokens = strList.split("[^a-zA-Z0-9]+");
		strList = convertArrayToString(tokens);
		tokens = strList.split("(?<=[a-z])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])");
		return convertArrayToString(tokens);
	}
	
   
    
    /***
     * This function takes in a file and splits each line into an acronym and the words that it stand
     * for based on the delimiter of a colon. It returns a HashMap where the first string is the acronym 
     * and the second string is the expanded words. 
     * @param filePath
     * @return
     */
    public HashMap<String,String> getAcronymsList(String filePath){
    	HashMap<String, String> dictionary = new HashMap<String,String>();
        File openingDirectory = new File(filePath);
        try {
            BufferedReader fileStream = new BufferedReader(new FileReader(openingDirectory));
            String lineRead;
            while ((lineRead = fileStream.readLine()) != null)
            {
            	String[] acronymData = lineRead.split("[\\:\\.]");
            	dictionary.put(acronymData[0].trim(), acronymData[1].trim());
            }
            fileStream.close();
            return dictionary;
        } catch (Exception e) {
            System.err.printf("File: %s is corrupted or empty.", openingDirectory.getName());
            return dictionary;
        }
    }

    /***
     * This function takes in two strings, the first contains the information to be parsed, and 
     * the second is file of the acronyms. It then creates a HashMap of the acronym data and runs
     * through each word in the inputed string and checks if it matches any of the keys in the
     * hashMap. It then replaces them with the expanded acronym.
     * @param data
     * @param restoringAcronymsFile
     * @return
     */
    public String restoringAcronyms(String data, String restoringAcronymsFile ){
    	HashMap<String,String>  dictionary = getAcronymsList(restoringAcronymsFile);
    	if(dictionary.isEmpty()){
    		return "";
    	}
    	String[] words = data.split(" ");
    	for(String w : words){
    		if(dictionary.containsKey(w)){
    			data = data.replace(w, dictionary.get(w));
    		}
    	}
    	return data;
    }
    /***
     * This function takes a String of data and the path of a file that contains the words to
     * be taken out. It breaks both strings into tokens and compares each token in the inputted string 
     * to make sure it is not in the list of words to be removed. If it is not to be removed, it gets 
     * appended to a new string, which is then returned. 
     * @param data
     * @param filePath
     * @return
     */
    public String removeStopWords(String data, String filePath){
    	String[] stopWords = tokenize(readSelectedFile(null, filePath)).split(" ");
    	String[] strings = data.split(" ");
    	ArrayList<String> result = new ArrayList<String>();
        for (int i=0 ; i < strings.length; i++){
            boolean notstopped = true;
            for (int j=0; j< stopWords.length; j++){
                if ((strings[i].toLowerCase()).equals(stopWords[j].toLowerCase())){
                    notstopped = false;
                    break;
                }
            }
            if (notstopped){
            	result.add(strings[i]);
            }
        }
        String[] ResultAsArray = new String[result.size()];
        result.toArray(ResultAsArray);
        return convertArrayToString(ResultAsArray);
    }
    
    /***
     * This function takes in a string of information and breaks the string into words. It
     * then takes each word in the array of words and removes the affixes from the end of
     * the words. 
     * @param data
     * @return
     */
    public String getStems(String data){
    	String[] individualWords = data.split(" ");
    	Porter p = new Porter();
    	if(data.isEmpty() ||individualWords[0].isEmpty()) return "";
    	for(int i =0; i<individualWords.length; i++){
    		individualWords[i] = p.stripAffixes(individualWords[i]);
    	}	
    	String allStems = "";
    	allStems = convertArrayToString(individualWords);
    	return allStems;
    }
    
    public void storeStringIntoFiles(String rootFolder, String fileName, String data){
    	File folder = new File(rootFolder + "\\Reqs_Indices");
    	if (!folder.exists()) {
    		folder.mkdir();
    	}
    	try{
	    	FileWriter fw = new FileWriter(folder + "\\" + fileName + "_Indices.txt");
	    		fw.write(data);
	    	fw.close();
    	}catch(IOException e){
    		System.err.println(e);
    	}
    }
}

