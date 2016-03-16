package com.shxt.supermarket.core;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

import com.shxt.supermarket.action.OpenLeftMenu;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {
	private OpenLeftMenu OLM;

    public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
    }

    protected void makeActions(IWorkbenchWindow window) {
    	OLM = new OpenLeftMenu(window);
    	OLM.setText("普通用户");
    }

    protected void fillMenuBar(IMenuManager menuBar) {
    	IMenuManager menu = new MenuManager("权限");
    	menu.add(OLM);
    	menuBar.add(menu);
    }
    
}
