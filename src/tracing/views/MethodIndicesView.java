package tracing.views;

import java.util.jar.JarException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.ui.IPackagesViewPart;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import utility.Utility;

public class MethodIndicesView extends ViewPart implements ISelectionProvider{
	
	public static Text indicesText;
	public Utility UTL = new Utility();
	public static Label titleLabel;
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
		formdata.right = new FormAttachment(0,300);
		
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
		long startTime = System.currentTimeMillis();
		UTL.openProject();
		int methodCount = 0;
		try {
			methodCount = Utility.processRootDirectory();
		} catch (JarException | CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long finishTime = System.currentTimeMillis() - startTime;
		MethodIndicesView.titleLabel.setText("Indexing time of " + methodCount + " methods is " + finishTime/1000 + " seconds.");
		
		doubleClickOnMethod();
	}

	public static void doubleClickOnMethod(){		
		IPackagesViewPart nav = (IPackagesViewPart) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                .findView("org.eclipse.jdt.ui.PackageExplorer");
		TreeViewer tree =  nav.getTreeViewer();
		tree.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
	            try{
	            IStructuredSelection selection = (IStructuredSelection)event.getSelection();
                    if(!selection.isEmpty()){
                        Object element = selection.getFirstElement();
                       
                        if(element instanceof IMethod)
                        {
                            IMethod method = ((IMethod) element);
                            indicesText.setText("Method Name: " + method.getElementName() + " \n \n " +
                            Utility.methodNames.get(method.getKey()));
                        }                      
                    }
	            } catch (Exception exp) {}
			}
		});
	}
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

}
