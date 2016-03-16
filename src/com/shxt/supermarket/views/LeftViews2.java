package com.shxt.supermarket.views;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import com.shxt.supermarket.editor.EmployeeInformation;
import com.shxt.supermarket.editor.EncomeInformation;
import com.shxt.supermarket.editor.GoodsInformation;
import com.shxt.supermarket.editor.StoreInformation;
import com.shxt.supermarket.editor.VIPInformation;

public class LeftViews2 extends ViewPart {

	public static final String ID = "com.shxt.supermarket.views.LeftViews2"; 
	private Text text;
	public static int commonlevel = 0;

	public LeftViews2() {
	}

	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		{
			text = new Text(container, SWT.BORDER | SWT.READ_ONLY);
			text.setText("* \u7BA1\u7406\u5458\u7528\u6237 *");
			text.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_RED));
			text.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.BOLD));
			text.setBounds(47, 10, 138, 35);
		}
		{
			Button button = new Button(container, SWT.NONE);
			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					try {
						LeftViews2.this.getViewSite().getPage().openEditor(new StoreInformation(), StoreInformation.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
				}
			});
			button.setText("\u4F9B\u8D27\u5546");
			button.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_RED));
			button.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.NORMAL));
			button.setBounds(47, 84, 113, 35);
		}
		{
			Button button = new Button(container, SWT.NONE);
			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					try {
						LeftViews2.this.getViewSite().getPage().openEditor(new EmployeeInformation(), EmployeeInformation.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
				}
			});
			button.setText("\u5458\u5DE5\u7BA1\u7406");
			button.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_RED));
			button.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.NORMAL));
			button.setBounds(47, 148, 113, 35);
		}
		{
			Button button = new Button(container, SWT.NONE);
			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					try {
						LeftViews2.this.getViewSite().getPage().openEditor(new GoodsInformation(), GoodsInformation.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
				}
			});
			button.setText("\u5546\u54C1\u5E93");
			button.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_RED));
			button.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.NORMAL));
			button.setBounds(47, 212, 113, 35);
		}
		{
			Button button = new Button(container, SWT.NONE);
			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					try {
						LeftViews2.this.getViewSite().getPage().openEditor(new VIPInformation(), VIPInformation.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
				}
			});
			button.setText("\u4F1A\u5458\u5E93");
			button.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_RED));
			button.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.NORMAL));
			button.setBounds(47, 275, 113, 35);
		}
		{
			Button button = new Button(container, SWT.NONE);
			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					try {
						LeftViews2.this.getViewSite().getPage().openEditor(new EncomeInformation(), EncomeInformation.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
				}
			});
			button.setText("\u8D85\u5E02\u8425\u4E1A\u989D");
			button.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_RED));
			button.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.NORMAL));
			button.setBounds(46, 341, 114, 35);
		}

		createActions();
		initializeToolBar();
		initializeMenu();
	}

	private void createActions() {
	}

	
	private void initializeToolBar() {
		IToolBarManager toolbarManager = getViewSite().getActionBars()
				.getToolBarManager();
	}

	private void initializeMenu() {
		IMenuManager menuManager = getViewSite().getActionBars()
				.getMenuManager();
	}

	@Override
	public void setFocus() {
	}

}
