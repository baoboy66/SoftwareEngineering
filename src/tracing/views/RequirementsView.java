package tracing.views;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.part.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.*;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;

import dialogView.OpeningDialog;


/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */

public class RequirementsView extends ViewPart implements ISelectionProvider{
	
	private ISelection selection;
	private ComboViewer comboViewer;
	
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "tracing.views.RequirementsView";
	
	/**
	 * The constructor.
	 */
	public RequirementsView() {
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	@Override
	public void createPartControl(Composite parent) {
		//Set layout forum of parent composite
		parent.setLayout(new FormLayout());
		//Create a drop box
		comboViewer = new ComboViewer(parent,SWT.NONE|SWT.DROP_DOWN);
		Combo combo = comboViewer.getCombo();
		combo.add("Choose Use Case");
		
		
		long startTime = System.currentTimeMillis();
		
		// Call the findFileName method and assign the result to the combo viewer.
        ArrayList<String> directoryFiles = findFileNames(OpeningDialog.rootFolderPath);
        for(String itr: directoryFiles){
            combo.add(itr.toUpperCase());
        }
        
        long finishTime = System.currentTimeMillis();
        getIndexingString(startTime,finishTime,directoryFiles.size());

        
        // Set the default to index 0 of the drop down.
		combo.select(0);
		
		//Set combo position
		FormData formdata = new FormData();
		formdata.top=new FormAttachment(0,5);
		formdata.left = new FormAttachment(0,10);
		formdata.right = new FormAttachment(0,290);
		combo.setLayoutData(formdata);
		
		//Set text position
		Text text = new Text(parent,SWT.MULTI|SWT.V_SCROLL|SWT.READ_ONLY);
		formdata = new FormData();
		formdata.top=new FormAttachment(combo,10);
		formdata.bottom = new FormAttachment(combo,600);
		formdata.left = new FormAttachment(0,5);
		formdata.right = new FormAttachment(0,355);
		text.setLayoutData(formdata);
		//set text content
		text.setText(getIndexingString(startTime,finishTime,directoryFiles.size()));
		 
		combo.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetSelected(SelectionEvent e) {
		        
				if(combo.getSelectionIndex()==0)
					text.setText(getIndexingString(startTime,finishTime,directoryFiles.size()));
				else
					// Try to set the text panel to the raw contents of the selected text file.
					// If the file is empty, unreadable, or there is an error we will default to
					// a blank panel after catching the exception.
					try{
						text.setText("");
						// The '-1' is needed in the call below becasue the first index of the dropdown
						// is set by default causing an offset of 1.
						ArrayList<String> fileStream = readSelectedFile(directoryFiles.get(combo.getSelectionIndex() - 1));
						for(String line: fileStream){
							// Newline character needed to ensure the proper display format.
				            text.append(line + "\n");
				        }
					}
					catch(Exception exp2){
						// Set the text panel to blank when an exception is encountered.
						text.setText("");
					}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		comboViewer.addSelectionChangedListener(new ISelectionChangedListener(){

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				ISelection comboSelection = event.getSelection();
				setSelection(comboSelection);
			}
			
		});
		
	}
	
	@Override
	public void setSelection(ISelection selection) {
		this.selection = selection;
		SelectionChangedEvent event = new SelectionChangedEvent(comboViewer,selection);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
	}

	@Override
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ISelection getSelection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeSelectionChangedListener(
			ISelectionChangedListener listener) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * This function takes the filePath string parameter it is passed, ensures that the path
	 * is valid and that it also exists. It then creates a string array of the directory's
	 * contents, which are parsed with regex to ensure that only ".txt" files are added to
	 * the array list that is returned to the calling function. 
	 * 
	 * @param filePath
	 * @return ArrayList<String> directoryFilesMatched
	 */
    public static ArrayList<String> findFileNames(String filePath){

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
    public static ArrayList<String> readSelectedFile(String filename){
    	File openingDirectory = new File(OpeningDialog.rootFolderPath + "/" + filename + ".txt");
    	ArrayList<String> fileContents = new ArrayList<String>();
    	System.out.println(openingDirectory.toString());
    	try {
			BufferedReader fileStream = new BufferedReader(new FileReader(openingDirectory));
		    String lineRead;
		    while ((lineRead = fileStream.readLine()) != null)
		    {
		      fileContents.add(lineRead);
		    }
		    fileStream.close();
	        for(String line: fileContents){
	            System.out.println(line);
	        }
		    return fileContents;
		} catch (Exception e) {
			System.err.printf("File: %s is corrupted or empty.", filename);
			return null;
		}
    }
    
    private String getIndexingString(long startTime, long finishTime, int fileCount){
        String secondsPassed = Long.toString((finishTime-startTime)/1000);
        String filesIndexed = Integer.toString(fileCount);
        String indexTimeText = "Indexing time of " + filesIndexed + "(s) requirements is: " + secondsPassed + " seconds";
    	return indexTimeText;
    }
}