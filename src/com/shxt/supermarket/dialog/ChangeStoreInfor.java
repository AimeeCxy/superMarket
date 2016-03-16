package com.shxt.supermarket.dialog;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import com.shxt.supermarket.utils.DBTools;

public class ChangeStoreInfor extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text text;
	private Text text_1;
	private Text text_2;
	private Text text_3;
	private Text text_4;

	public ChangeStoreInfor(Shell parent, int style) {
		super(parent, style);
		setText("修改供货商信息");
	}

	public Object open(TableItem ti) {
		createContents(ti);
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

	private void createContents(TableItem ti) {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(450, 300);
		shell.setText(getText());
		
		Label label = new Label(shell, SWT.NONE);
		label.setBounds(29, 10, 36, 15);
		label.setText("\u7F16\u53F7");
		
		text = new Text(shell, SWT.BORDER | SWT.READ_ONLY);
		text.setText(ti.getText(0));
		text.setBounds(71, 7, 153, 21);
		
		Label label_1 = new Label(shell, SWT.NONE);
		label_1.setBounds(29, 48, 36, 15);
		label_1.setText("\u540D\u79F0");
		
		text_1 = new Text(shell, SWT.BORDER);
		text_1.setBounds(71, 45, 153, 21);
		
		Label label_2 = new Label(shell, SWT.NONE);
		label_2.setBounds(29, 88, 36, 15);
		label_2.setText("\u5730\u5740");
		
		text_2 = new Text(shell, SWT.BORDER);
		text_2.setBounds(71, 85, 153, 21);
		
		Label label_3 = new Label(shell, SWT.NONE);
		label_3.setBounds(29, 126, 36, 15);
		label_3.setText("\u7535\u8BDD");
		
		text_3 = new Text(shell, SWT.BORDER);
		text_3.setBounds(71, 123, 153, 21);
		
		Label label_4 = new Label(shell, SWT.NONE);
		label_4.setBounds(29, 168, 36, 15);
		label_4.setText("\u5386\u53F2");
		
		text_4 = new Text(shell, SWT.BORDER);
		text_4.setBounds(71, 168, 153, 94);
		
		Button button = new Button(shell, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(text.getText().isEmpty()||text_1.getText().isEmpty()||text_2.getText().isEmpty()||text_3.getText().isEmpty()||text_4.getText().isEmpty()){
					MessageBox message = new MessageBox(shell,SWT.ERROR);
					message.setText("系统提示信息");
					message.setMessage("请完善要修改的供货商信息");
					message.open();
				}else{
				DBTools db = DBTools.getDBTools("cxy_ccshxt", "cxy", "cxy");
				result = db.updateReturnArrayByRCP("update store set name=?,address=?,tel=?,history=? where number=?", new Object[]{text_1.getText(),text_2.getText(),text_3.getText(),text_4.getText(),text.getText()});
				shell.close();
				}
			}
		});
		button.setBounds(307, 237, 75, 25);
		button.setText("\u4FEE\u6539");

	}
}
