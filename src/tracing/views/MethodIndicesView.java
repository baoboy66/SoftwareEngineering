package tracing.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.jar.JarException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.widgets.*;

import dialogView.OpeningDialog;
import utility.Utility;

public class MethodIndicesView extends ViewPart implements ISelectionProvider{
	
	public static Text indicesText;
	public Utility UTL = new Utility();
	public static Label titleLabel;
	private void testIndex(){	
		ArrayList<String> Test = new ArrayList<String>();
		UTL.findAllFileNames(OpeningDialog.rootFolderPath, Test);   
		HashMap<String, String> originalContents = new HashMap<String, String>();
        for (String test: Test){
        	System.out.println(test);
        	originalContents.put(test.substring(test.lastIndexOf("/") + 1, test.lastIndexOf(".")),
        							 UTL.readSelectedFile(null,test));
        }
        
        for (Entry<String, String> testAll : originalContents.entrySet()){
        	System.out.println(testAll.getKey());
        	System.out.println(testAll.getValue());
        }
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

	@Override
	public void setSelection(ISelection selection) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createPartControl(Composite parent) {

		//Set layout forum of parent composite
		parent.setLayout(new FormLayout());
		
		FormData formdata = new FormData();
		formdata.top=new FormAttachment(0,5);
		formdata.left = new FormAttachment(0,10);
		formdata.right = new FormAttachment(0,200);
		
		//Create title label
		titleLabel = new Label(parent,SWT.SINGLE);
		titleLabel.setText("Method Indices:");
		titleLabel.setLayoutData(formdata);
		
		//Create text area
		indicesText = new Text(parent,SWT.MULTI|SWT.V_SCROLL|SWT.READ_ONLY);
		indicesText.setText("This is a sample result.");
		formdata = new FormData();
		formdata.top = new FormAttachment(titleLabel,10);
		formdata.bottom = new FormAttachment(titleLabel,230);
		formdata.left = new FormAttachment(0,10);
		formdata.right = new FormAttachment(0,800);
		indicesText.setLayoutData(formdata);
		
		Button manageButton = new Button(parent,SWT.PUSH);
		manageButton.setText("TEST");
		formdata = new FormData();
		formdata.top = new FormAttachment(indicesText,10);
		formdata.left = new FormAttachment(10,10);
		manageButton.setLayoutData(formdata);
		
		manageButton.addSelectionListener(new SelectionListener(){

			@Override
			public void widgetSelected(SelectionEvent e) {
				testIndex();		
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

}
