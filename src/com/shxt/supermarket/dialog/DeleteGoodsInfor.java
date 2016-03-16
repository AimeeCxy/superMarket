package com.shxt.supermarket.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import com.shxt.supermarket.utils.DBTools;

public class DeleteGoodsInfor extends Dialog {

	protected Object result;
	protected Shell shell;
	
	public DeleteGoodsInfor(Shell parent, int style) {
		super(parent, style);
		setText("删除商品条目");
	}

	public Object open(int id) {
		createContents(id);
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	private void createContents(final int id) {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(450, 300);
		shell.setText(getText());
		
		Label label = new Label(shell, SWT.NONE);
		label.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		label.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		label.setFont(SWTResourceManager.getFont("宋体", 14, SWT.BOLD));
		label.setAlignment(SWT.CENTER);
		label.setBounds(70, 74, 270, 19);
		label.setText("\u786E\u5B9A\u8981\u5220\u9664\u6B64\u6761\u5546\u54C1\u4FE1\u606F\u5417\uFF1F");
		
		Button button = new Button(shell, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DBTools db = DBTools.getDBTools("cxy_ccshxt", "cxy", "cxy");
				result = db.update("delete from goods where number=?", new Object[]{id});
				shell.close();
			}
			
		});
		button.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		button.setBounds(70, 144, 75, 25);
		button.setText("\u662F");
		
		Button button_1 = new Button(shell, SWT.NONE);
		button_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				result = 0;
				shell.close();
			}
		});
		button_1.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		button_1.setBounds(170, 144, 75, 25);
		button_1.setText("\u5426");
		
		Button button_2 = new Button(shell, SWT.NONE);
		button_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				result = 0;
				shell.close();
			}
		});
		button_2.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		button_2.setBounds(265, 144, 75, 25);
		button_2.setText("\u53D6\u6D88");

	}

}
