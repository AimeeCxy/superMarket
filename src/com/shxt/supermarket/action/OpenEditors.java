package com.shxt.supermarket.action;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import com.shxt.supermarket.editor.FirstEditor;

public class OpenEditors extends Action {
	
	private IWorkbenchWindow window;
	
	public OpenEditors(IWorkbenchWindow window) {
		this.window = window;
	}

	@Override
	public void run() {
		try {
			window.getActivePage().openEditor((IEditorInput) new FirstEditor(),FirstEditor.ID);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		super.run();
	}

}
