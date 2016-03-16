package com.shxt.supermarket.action;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;

import com.shxt.supermarket.editor.SecondEditor;

public class OpenEditors2 extends Action {
	
	private IWorkbenchWindow window;
	
	public OpenEditors2(IWorkbenchWindow window) {
		this.window = window;
	}

	@Override
	public void run() {
		try {
			window.getActivePage().openEditor(new SecondEditor(),SecondEditor.ID);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		super.run();
	}

}
