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

public class FindGoodsByName extends Dialog {

	protected Object result;
	protected Shell shell;
	public static Text text;
	public FindGoodsByName(Shell parent, int style) {
		super(parent, style);
		setText("通过名称查询商品");
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
		label.setBounds(58, 67, 96, 15);
		label.setText("\u8BF7\u8F93\u5165\u5546\u54C1\u540D\u79F0\uFF1A");
		
		text = new Text(shell, SWT.BORDER);
		text.setBounds(160, 64, 215, 21);
		
		Button button = new Button(shell, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DBTools db = DBTools.getDBTools("cxy_ccshxt", "cxy", "cxy");
				List<String[]> str = db.findByRCPPage("select number,name,prise,howmany,plus from goods where name=?", new Object[]{text.getText()});
				result = str;
				shell.close();
			}
		});
		button.setBounds(160, 154, 75, 25);
		button.setText("\u67E5\u8BE2");

	}

}
