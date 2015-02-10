package utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import IRUtilities.Porter;
import dialogView.OpeningDialog;

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
    
    public String getIndexingString(long startTime, long finishTime, int fileCount){
        String secondsPassed = Long.toString((finishTime-startTime));
        return "Indexing time of " + fileCount + " requirements is: " + secondsPassed + " milliseconds";
    }
    
    public String convertArrayToString(String[] strings)
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
    public String tokenize(String strList){
		String[] tokens = strList.split("[^a-zA-Z0-9_/']+");
		return convertArrayToString(tokens);
	}
	
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
    
    public String getStems(String data){
    	String[] individualWords = data.split(" ");
    	Porter p = new Porter();
    	if(individualWords.length <= 1 && individualWords[0].isEmpty()) return "";
    	for(int i =0; i<individualWords.length; i++){
    		individualWords[i] = p.stripAffixes(individualWords[i]);
    	}	
    	String allStems = "";
    	allStems = convertArrayToString(individualWords);
    	return allStems;
}


}

