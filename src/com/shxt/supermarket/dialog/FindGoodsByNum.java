package com.shxt.supermarket.dialog;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import com.shxt.supermarket.utils.DBTools;

public class FindGoodsByNum extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text text;

	public FindGoodsByNum(Shell parent, int style) {
		super(parent, style);
		setText("通过编号查询商品");
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

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(450, 300);
		shell.setText(getText());
		
		Label label = new Label(shell, SWT.NONE);
		label.setBounds(56, 53, 96, 15);
		label.setText("\u8BF7\u8F93\u5165\u5546\u54C1\u7F16\u53F7\uFF1A");
		
		text = new Text(shell, SWT.BORDER);
		text.setBounds(169, 47, 176, 21);
		
		Button btnSearch = new Button(shell, SWT.NONE);
		btnSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DBTools db = DBTools.getDBTools("cxy_ccshxt", "cxy", "cxy");
				String [] str = db.findForRCPRow("select number,name,prise,howmany,plus from goods where number=?", new Object[]{text.getText()});
				result = str;
				if(result==null){
					MessageBox box = new MessageBox(shell,SWT.CLOSE);
					box.setMessage("编号为“"+text.getText()+"”的商品不存在");
					box.open();
				}
				shell.close();
			}

		});
		btnSearch.setBounds(169, 151, 75, 25);
		btnSearch.setText("\u67E5\u8BE2");

	}
}
