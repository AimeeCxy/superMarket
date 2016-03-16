package com.shxt.supermarket.views;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import com.shxt.supermarket.editor.EncomeInformation;
import com.shxt.supermarket.editor.FirstEditor;
import com.shxt.supermarket.editor.SearchGoods;
import com.shxt.supermarket.editor.SecondEditor;

public class LeftViews extends ViewPart {

	public static final String ID = "com.shxt.supermarket.views.LeftViews"; //$NON-NLS-1$
	private Text text;

	public LeftViews() {
		setPartName("\u666E\u901A\u7528\u6237");
	}

	
	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.BOLD));
		container.setForeground(SWTResourceManager.getColor(128, 0, 0));
		{
			text = new Text(container, SWT.BORDER | SWT.READ_ONLY);
			text.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.BOLD));
			text.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_RED));
			text.setText("* \u666E\u901A\u7528\u6237 *");
			text.setBounds(48, 10, 118, 35);
		}
		{
			Button button = new Button(container, SWT.NONE);
			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					try {
						LeftViews.this.getViewSite().getPage().openEditor(new FirstEditor(), FirstEditor.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
				}
			});
			button.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_RED));
			button.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.NORMAL));
			button.setBounds(48, 80, 112, 35);
			button.setText("\u552E\u8D27");
		}
		{
			Button button = new Button(container, SWT.NONE);
			button.setText("\u67E5\u8BE2\u5546\u54C1");
			button.addSelectionListener(new SelectionAdapter() {
				@Override
				//查询商品库
				public void widgetSelected(SelectionEvent e) {
					try {
						LeftViews.this.getViewSite().getPage().openEditor(new SearchGoods(), SearchGoods.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
					
				}
			});
			button.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_RED));
			button.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.NORMAL));
			button.setBounds(48, 206, 112, 35);
		}
		{
			Button button = new Button(container, SWT.NONE);
			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					try {
						LeftViews.this.getViewSite().getPage().openEditor(new SecondEditor(), SecondEditor.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
				}
			});
			button.setText("\u8FDB\u8D27");
			button.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_RED));
			button.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.NORMAL));
			button.setBounds(48, 143, 112, 35);
		}
		{
			Button button = new Button(container, SWT.NONE);
			button.addSelectionListener(new SelectionAdapter() {
				@Override
				//查询营业额
				public void widgetSelected(SelectionEvent e) {					
					try {
						LeftViews.this.getViewSite().getPage().openEditor(new EncomeInformation(), EncomeInformation.ID);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
				}
			});
			button.setText("\u5F53\u524D\u8425\u4E1A\u989D");
			button.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_RED));
			button.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.NORMAL));
			button.setBounds(48, 269, 112, 35);
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
