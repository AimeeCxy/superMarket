package com.shxt.supermarket.core;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.shxt.supermarket.dialog.FirstDialog;
import com.shxt.supermarket.views.LeftViews;
import com.shxt.supermarket.views.LeftViews2;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		//��viewֱ����ʾ��RCP����ҳ��
		
		if(FirstDialog.power == 0){
			layout.addView(LeftViews.ID, IPageLayout.LEFT, 0.3f, layout.getEditorArea());
		}else{
			layout.addView(LeftViews2.ID, IPageLayout.LEFT, 0.3f, layout.getEditorArea());
		}
		
		
		layout.getViewLayout(LeftViews.ID).setCloseable(false);
		layout.getViewLayout(LeftViews.ID).setMoveable(false);
	}
}
