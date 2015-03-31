package tracing.views;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
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
import org.eclipse.ui.dialogs.IOverwriteQuery;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.wizards.datatransfer.FileSystemStructureProvider;
import org.eclipse.ui.wizards.datatransfer.ImportOperation;

import utility.Utility;

public class MethodIndicesView extends ViewPart implements ISelectionProvider{
	
	public static Text indicesText;
	public static Label titleLabel;
	public static HashMap<String, String> methodNames = new HashMap<String, String>();
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
		openProject();
		int methodCount = 0;
		try {
			methodCount = processRootDirectory();
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
                            methodNames.get(method.getKey()));
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
	 /***
     * This function takes astring of code, ignores the comments and tokenizes the rest
     * @param strList
     * @return processed code
     */
    public static String processCode(String originalCode){
		originalCode = originalCode.trim();
		originalCode = originalCode.replaceAll("(\\r|\\n|\\r\\n)+", "\n");
		String newline = "baocooperdavidmatt";
		originalCode = originalCode.replaceAll("(\\n)+", newline + " ");
		List<String> codeSegments = new ArrayList<String>();
		boolean quoteFix = false;
		
		while(true){
			int singlePosition = originalCode.indexOf("//", 0);
			int multiPosition = originalCode.indexOf("/*", 0);
			int commentPosition = -1;
			String endCommentString = "";
			boolean commentOrNewLine = false;
			
			if(singlePosition != -1 && (singlePosition < multiPosition || multiPosition == -1)){
				//A single line comment is being processed
				commentPosition = singlePosition;
				endCommentString = newline;
				commentOrNewLine = true;
			}
			else if(multiPosition != -1 && (multiPosition < singlePosition  || singlePosition == -1)){
				//A multi line comment is being processed
				commentPosition = multiPosition;
				endCommentString = "*/";
			}
			else{
				//No comments found
				codeSegments.add(Utility.tokenizeCode(originalCode.substring(0)));
				break;
			}
			
			//Check to see if comment is within quotes
			int countDouble = 0;
			for(int i =0; i < originalCode.substring(0, commentPosition).length(); i++){
			    if(originalCode.charAt(i) == '"')
			    	countDouble++;
			}
			if(quoteFix) countDouble++;
			
			if(countDouble % 2 != 0){
				codeSegments.add(Utility.tokenizeCode(originalCode.substring(0, commentPosition)));
				originalCode = originalCode.substring(commentPosition + 2);
				quoteFix = true;
				//Comments within quotes are not actually comments
				continue;
			}else{
				quoteFix = false;
			}
			
			//Tokenize words before the comment starts
			codeSegments.add(Utility.tokenizeCode(originalCode.substring(0, commentPosition)));
			originalCode = originalCode.substring(commentPosition);
			
			//Ignore everything within a comment
			int endOfComment = originalCode.indexOf(endCommentString, 0);
			if(endOfComment == -1){
				//The rest of the file is a comment
				codeSegments.add(originalCode.substring(0));
				break;
			}
			int endLength = 2;
			if(commentOrNewLine) endLength = newline.length();
			codeSegments.add(originalCode.substring(0, endOfComment + endLength));
			originalCode = originalCode.substring(endOfComment + endLength);
			
			if(originalCode.length() <= 0){
				break;
			}
		}
		
		String[] stringArray = codeSegments.toArray(new String[codeSegments.size()]);
		return Utility.convertArrayToString(stringArray).replaceAll(newline, "\n");
	}
    
    public void openProject(){
    	IOverwriteQuery overwriteQuery = new IOverwriteQuery() {
            public String queryOverwrite(String file) { return ALL; }
	    };
	
	    String baseDir = "C:\\iTrust";
	    Path path = new Path(baseDir);
	    ImportOperation importOperation = new ImportOperation(path,
	            new File(baseDir), FileSystemStructureProvider.INSTANCE, overwriteQuery);
	    importOperation.setCreateContainerStructure(false);
	    try{
	    importOperation.run(new NullProgressMonitor());
	    }
	    catch(Exception exp){};
    }

    public static int processRootDirectory() throws JarException,
	CoreException {
	IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
	
	IProject[] projects = root.getProjects();
	// process each project
	int totalMethod = 0;
	for (IProject project : projects) {
		
		if (project.isNatureEnabled("org.eclipse.jdt.core.javanature")) {
			IJavaProject javaProject = JavaCore.create(project);
			IPackageFragment[] packages = javaProject.getPackageFragments();
	
			// process each package
			for (IPackageFragment aPackage : packages) {
	
				// We will only look at the package from the source folder
				// K_BINARY would include also included JARS, e.g. rt.jar
				// only process the JAR files
				if (aPackage.getKind() == IPackageFragmentRoot.K_SOURCE) {
	
					for (ICompilationUnit unit : aPackage
							.getCompilationUnits()) {
						IType[] allTypes = unit.getAllTypes();
						for (IType type : allTypes) {
	
							IMethod[] methods = type.getMethods();
							
							for (IMethod method : methods) {
								
								String result = processCode(method.getSource());
								methodNames.put(method.getKey(), result);
								totalMethod++;
							}
						}
					}
				}
			}
	
		}
	}
	return totalMethod;
}  

}
