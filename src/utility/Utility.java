package utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

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
    public ArrayList<String> findFileNames(String filePath){

        String directory = filePath;
        String regexMatchCondition = ".+\\.txt$";
        ArrayList<String> directoryFilesMatched = new ArrayList<String>();
        File directoryObj =  new File(directory);

        if (directoryObj.exists() && directoryObj.isDirectory()){
            String[] files = directoryObj.list();

            for(String out : files){
                //System.out.println(out);
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
    public ArrayList<String> readSelectedFile(String filePath ,String filename){
    	File openingDirectory = new File(filePath + "/" + filename + ".txt");
    	if (filePath == null) {openingDirectory = new File(filename);}
    	ArrayList<String> fileContents = new ArrayList<String>();
    	try {
			BufferedReader fileStream = new BufferedReader(new FileReader(openingDirectory));
		    String lineRead;
		    while ((lineRead = fileStream.readLine()) != null)
		    {
		      fileContents.add(lineRead);
		    }
		    fileStream.close();
		    return fileContents;
		} catch (Exception e) {
			System.err.printf("File: %s is corrupted or empty.", filename);
			return null;
		}
    }
    
    public String getIndexingString(long startTime, long finishTime, int fileCount){
        String secondsPassed = Long.toString((finishTime-startTime)/1000);
        String filesIndexed = Integer.toString(fileCount);
        String indexTimeText = "Indexing time of " + filesIndexed + "(s) requirements is: " + secondsPassed + " seconds";
    	return indexTimeText;
    }
    
    public String[] tokenize(ArrayList<String> strList){
		String originalStr = "";
		for (String str : strList){
			originalStr += (str + " ");
		}
		//Splits string by all none alpha-numeric values, except underscores and apostrophes
		String[] tokens = originalStr.split("[^a-zA-Z0-9_/']+");
		return tokens;
	}
	
	
	public String[] restoreAcronyms(String[] strings, ArrayList<String> acronyms){
		for (int i=0 ; i < strings.length; i++) {
			for (String acrLine : acronyms){
				//Split the data at the colon character. Remove the trailing period as well
				String[] acronymData = acrLine.split("[\\:\\.]");
				//There should be 2 parts of the acronym data.
				//If you have more than 2 it still runs but the data will be incorrect
				if(!(acronymData.length < 2)){
					if (strings[i].equals(acronymData[0].trim())){
						strings[i] = acronymData[1].trim();
					}
				}
				else{
					//Problem parsing the data.
					//This happens when a user does not format their acronym data correctly
				}
			}
		}
		return strings;
	}
}

