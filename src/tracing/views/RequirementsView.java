package tracing.views;
import java.util.ArrayList;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.SWT;
import dialogView.OpeningDialog;
import utility.Utility;


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
	private static ComboViewer comboViewer;
	private static Combo combo;
	private Utility utl = new Utility(); 
	private static Text requirementViewText;
	private static ArrayList<String> displayString = new ArrayList<String>();
	private static boolean isView = false;
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "tracing.views.RequirementsView";
	
	/**
	 * The constructor.
	 */
	public RequirementsView() {

	}

	public void runPlugIn(){
		combo.removeAll();
		combo.add("Choose Use Case");
		long startTime = System.currentTimeMillis();
		
		// Call the findFileName method and assign the result to the combo viewer.
        ArrayList<String> directoryFiles = utl.findFileNames(OpeningDialog.getRootFolderPath());
        for(String itr: directoryFiles){
        	String result = "";
            combo.add(itr);
            String file = utl.readSelectedFile(OpeningDialog.getRootFolderPath(),itr);
            String tokens = utl.tokenize(file);
            if (OpeningDialog.getIsTokenizing()) {
            	result = tokens;	
            }
        	if(OpeningDialog.getIsRestoringAcronyms()){       		
        		tokens = result.isEmpty() ? tokens : result;     		
        		result = utl.restoreAcronyms(tokens, OpeningDialog.getRestoringAcronymsFile());
        	} 
        	if(OpeningDialog.getIsRemovingStopWords()){
        		tokens = result.isEmpty() ? tokens : result;    		
        		result = utl.removeStopWords(tokens, OpeningDialog.getRemovingStopWordsFile());
        	}
        	if(OpeningDialog.getIsStemming()){
                tokens = result.isEmpty() ? tokens : result;
               	result = utl.getStems(tokens);
        	}
        	// print the original content if no feature is selected
        	if(!(OpeningDialog.getIsRemovingStopWords() || OpeningDialog.getIsRestoringAcronyms() || OpeningDialog.getIsTokenizing() || OpeningDialog.getIsStemming())) result = file;
        	displayString.add(result);
        	utl.storeStringIntoFiles(OpeningDialog.getRootFolderPath(), itr, result);
        }
        long finishTime = System.currentTimeMillis();
        utl.getIndexingString(startTime,finishTime,directoryFiles.size());

        
        // Set the default to index 0 of the drop down.
		combo.select(0);
		requirementViewText.setText(utl.getIndexingString(startTime,finishTime,directoryFiles.size()));
		combo.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetSelected(SelectionEvent e) {
		        
				if(combo.getSelectionIndex()==0){
					requirementViewText.setText(utl.getIndexingString(startTime,finishTime,directoryFiles.size()));
					RequirementsIndicesView.setIndicesText("This is a sample result.");
				}
				else
					// Try to set the text panel to the raw contents of the selected text file.
					// If the file is empty, unreadable, or there is an error we will default to
					// a blank panel after catching the exception.
					try{
						requirementViewText.setText("");
						// The '-1' is needed in the call below becasue the first index of the dropdown
						// is set by default causing an offset of 1.
						int index = combo.getSelectionIndex() -1;
						String fileStream = utl.readSelectedFile(OpeningDialog.getRootFolderPath(), directoryFiles.get(index));
						requirementViewText.setText(fileStream);
						RequirementsIndicesView.setIndicesText(displayString.get(index));
					}
					catch(Exception exp2){
						// Set the text panel to blank when an exception is encountered.
						requirementViewText.setText("");
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
	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	@Override
	public void createPartControl(Composite parent) {
		//Set layout forum of parent composite
		isView = true;
		parent.setLayout(new FormLayout());
		//Create a drop box
		comboViewer = new ComboViewer(parent,SWT.NONE|SWT.DROP_DOWN);
		combo = comboViewer.getCombo();
		requirementViewText = new Text(parent,SWT.MULTI|SWT.V_SCROLL|SWT.READ_ONLY);
		if( !OpeningDialog.getRootFolderPath().isEmpty()) runPlugIn();
		
		//Set combo position
		FormData formdata = new FormData();
		formdata.top=new FormAttachment(0,5);
		formdata.left = new FormAttachment(0,10);
		formdata.right = new FormAttachment(0,290);
		combo.setLayoutData(formdata);
		
		//Set text position
		
		formdata = new FormData();
		formdata.top=new FormAttachment(combo,10);
		formdata.bottom = new FormAttachment(combo,600);
		formdata.left = new FormAttachment(0,5);
		formdata.right = new FormAttachment(0,355);
		requirementViewText.setLayoutData(formdata);			
	}
	
	@Override
	public void setSelection(ISelection selection) {
		this.selection = selection;
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
        public static boolean getIsView(){
                return isView;
        }
}