package com.shxt.supermarket.dialog;

import java.util.List;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import com.shxt.supermarket.utils.DBTools;

public class FindGoodsByPlus extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text text;

	public FindGoodsByPlus(Shell parent, int style) {
		super(parent, style);
		setText("通过备注信息查询商品");
	}

	public Object open() {
		createContents();
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

	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(450, 300);
		shell.setText(getText());
		
		Label label = new Label(shell, SWT.NONE);
		label.setBounds(53, 70, 129, 15);
		label.setText("\u8BF7\u8F93\u5165\u5546\u54C1\u5907\u6CE8\u4FE1\u606F\uFF1A");
		
		text = new Text(shell, SWT.BORDER);
		text.setBounds(188, 67, 203, 21);
		
		Button button = new Button(shell, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DBTools db = DBTools.getDBTools("cxy_ccshxt", "cxy", "cxy");
				List<String[]> str = db.findByRCPPage("select number,name,prise,howmany,plus from goods where plus=?", new Object[]{text.getText()});
				result = str;
				shell.close();
			}
		});
		button.setBounds(177, 170, 75, 25);
		button.setText("\u67E5\u8BE2");

	}

}
