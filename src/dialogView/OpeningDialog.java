package dialogView;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;

import java.io.File;

public class OpeningDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text txtRequirementsSourceFolder;
	private Text textRestoreAcronyms;
	private Text txtRemoveStopWords;
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public OpeningDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(450, 300);
		shell.setText(getText());
		
		
		// input for root folder
		Label lblRequirementsSourceFolder = new Label(shell, SWT.NONE);
		lblRequirementsSourceFolder.setBounds(10, 10, 168, 15);
		lblRequirementsSourceFolder.setText("Requirements source folder:");
		
		txtRequirementsSourceFolder = new Text(shell, SWT.BORDER);
		txtRequirementsSourceFolder.setText("Insert path here!");
		txtRequirementsSourceFolder.setBounds(184, 10, 147, 21);
		Button btnBrowse = new Button(shell, SWT.NONE);
		btnBrowse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try{
				    DirectoryDialog dialog = new DirectoryDialog(shell);
				    dialog.setFilterPath("c:\\");	    
				    txtRequirementsSourceFolder.setText(dialog.open());
				}
				catch(Exception e1){}

			}
		});
		btnBrowse.setBounds(347, 8, 75, 25);
		btnBrowse.setText("Browse");
		
		//tokenizing button
		Button btnTokenizing = new Button(shell, SWT.CHECK);
		btnTokenizing.setBounds(10, 58, 93, 16);
		btnTokenizing.setText("Tokenizing");
		
		//restoring acronyms checkbox, browse, and input for file name
		Button btnRestoringAcronyms = new Button(shell, SWT.CHECK);

		btnRestoringAcronyms.setBounds(10, 93, 126, 16);
		btnRestoringAcronyms.setText("Restoring acronyms");
		
		textRestoreAcronyms = new Text(shell, SWT.BORDER);
		textRestoreAcronyms.setText("Insert file name");
		textRestoreAcronyms.setBounds(301, 97, 97, 21);
		
		Button btnRestoreAcronyms = new Button(shell, SWT.NONE);
		btnRestoreAcronyms.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try{
					FileDialog fileDialog = new FileDialog(shell);
					fileDialog.setFilterPath(txtRequirementsSourceFolder.getText());	        
			        fileDialog.setFilterExtensions(new String[]{"*.txt"});	        
			        textRestoreAcronyms.setText(fileDialog.open());	
				}
				catch(Exception e1){}
			}
		});
		btnRestoreAcronyms.setBounds(184, 93, 75, 25);
		btnRestoreAcronyms.setText("Browse");
		btnRestoringAcronyms.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!btnRestoringAcronyms.getSelection()){
					textRestoreAcronyms.setEnabled(false);
					btnRestoreAcronyms.setEnabled(false);
				}
				else{
					textRestoreAcronyms.setEnabled(true);
					btnRestoreAcronyms.setEnabled(true);
				}
			}
		});

		if(!btnRestoringAcronyms.getSelection()){
			textRestoreAcronyms.setEnabled(false);
			btnRestoreAcronyms.setEnabled(false);
		}		
		//removing stop words checkbox, browse and input for file name
		Button btnRemovingStopWords = new Button(shell, SWT.CHECK);
		btnRemovingStopWords.setBounds(10, 128, 136, 16);
		btnRemovingStopWords.setText("Removing stop words");
		
		Button btnRemoveStopWords = new Button(shell, SWT.NONE);
		btnRemoveStopWords.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try{
					FileDialog fileDialog = new FileDialog(shell);
					fileDialog.setFilterPath(txtRequirementsSourceFolder.getText());	        
			        fileDialog.setFilterExtensions(new String[]{"*.txt"});	        
			        txtRemoveStopWords.setText(fileDialog.open());	
				}
				catch(Exception e1){}
			}
		});
		btnRemoveStopWords.setBounds(184, 128, 75, 25);
		btnRemoveStopWords.setText("Browse");
		
		txtRemoveStopWords = new Text(shell, SWT.BORDER);
		txtRemoveStopWords.setText("Insert file name");
		txtRemoveStopWords.setBounds(301, 130, 97, 21);
		
		btnRemovingStopWords.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(!btnRemovingStopWords.getSelection()){
					txtRemoveStopWords.setEnabled(false);
					btnRemoveStopWords.setEnabled(false);
				}
				else{
					txtRemoveStopWords.setEnabled(true);
					btnRemoveStopWords.setEnabled(true);
				}
			}
		});

		if(!btnRemovingStopWords.getSelection()){
			txtRemoveStopWords.setEnabled(false);
			btnRemoveStopWords.setEnabled(false);
		}	
		//stemming checkbox
		Button btnStemming = new Button(shell, SWT.CHECK);
		btnStemming.setBounds(10, 162, 93, 16);
		btnStemming.setText("Stemming");
		
		Button btnSubmit = new Button(shell, SWT.NONE);
		btnSubmit.setBounds(301, 208, 75, 25);
		btnSubmit.setText("Submit");
	}

}
