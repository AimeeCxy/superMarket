package com.shxt.supermarket.action;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;

import com.shxt.supermarket.views.LeftViews;



public class OpenLeftMenu extends Action {
	private IWorkbenchWindow window;
	
	public OpenLeftMenu(IWorkbenchWindow window){
		this.window = window;
	}
	
	public void run() {
		try {
			window.getActivePage().showView(LeftViews.ID);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}
}
