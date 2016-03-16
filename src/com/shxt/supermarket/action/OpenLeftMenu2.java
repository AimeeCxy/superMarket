package com.shxt.supermarket.action;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;

import com.shxt.supermarket.views.LeftViews2;

public class OpenLeftMenu2 extends Action {
private IWorkbenchWindow window;
	
	public OpenLeftMenu2(IWorkbenchWindow window){
		this.window = window;
	}
	
	public void run() {
		try {
			window.getActivePage().showView(LeftViews2.ID);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}

}
