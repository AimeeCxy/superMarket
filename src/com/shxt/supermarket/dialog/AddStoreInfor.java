package com.shxt.supermarket.dialog;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import com.shxt.supermarket.utils.DBTools;

public class AddStoreInfor extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text text;
	private Text text_1;
	private Text text_2;
	private Text text_3;
	private Text text_4;

	public AddStoreInfor(Shell parent, int style) {
		super(parent, style);
		setText("添加供货商信息");
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
		
		Label lblNumber = new Label(shell, SWT.NONE);
		lblNumber.setBounds(41, 29, 55, 15);
		lblNumber.setText("\u7F16\u53F7");
		
		Label lblName = new Label(shell, SWT.NONE);
		lblName.setBounds(41, 69, 55, 15);
		lblName.setText("\u540D\u79F0");
		
		Label lblAddress = new Label(shell, SWT.NONE);
		lblAddress.setBounds(41, 113, 55, 15);
		lblAddress.setText("\u5730\u5740");
		
		Label lblTel = new Label(shell, SWT.NONE);
		lblTel.setBounds(41, 154, 55, 15);
		lblTel.setText("\u7535\u8BDD");
		
		text = new Text(shell, SWT.BORDER);
		text.setBounds(102, 26, 95, 21);
		
		text_1 = new Text(shell, SWT.BORDER);
		text_1.setBounds(102, 66, 95, 21);
		
		text_2 = new Text(shell, SWT.BORDER);
		text_2.setBounds(102, 110, 95, 21);
		
		text_3 = new Text(shell, SWT.BORDER);
		text_3.setBounds(102, 151, 95, 21);
		
		Label lblHistory = new Label(shell, SWT.NONE);
		lblHistory.setBounds(222, 29, 55, 15);
		lblHistory.setText("\u4F9B\u8D27\u5386\u53F2");
		
		text_4 = new Text(shell, SWT.BORDER);
		text_4.setBounds(283, 29, 121, 140);
		
		Button btnAdd = new Button(shell, SWT.NONE);
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(text.getText().isEmpty()||text_1.getText().isEmpty()||text_2.getText().isEmpty()||text_3.getText().isEmpty()||text_4.getText().isEmpty()){
					MessageBox message = new MessageBox(shell,SWT.ERROR);
					message.setText("系统提示信息");
					message.setMessage("请完善要添加的供货商信息");
					message.open();
				}else{
					DBTools db = DBTools.getDBTools("cxy_ccshxt", "cxy", "cxy");
					result = db.updateNoIdReturnArrayByRCP("insert into store (number,name,address,tel,history)values(?,?,?,?,?)", new Object[]{text.getText(),text_1.getText(),text_2.getText(),text_3.getText(),text_4.getText()});
					MessageBox message = new MessageBox(shell,SWT.CLOSE);
					message.setText("系统提示信息");
					message.setMessage("一条供货商信息添加成功~");
					message.open();
					shell.close();
				}
			}
		});
		btnAdd.setBounds(202, 204, 75, 25);
		btnAdd.setText("\u6DFB\u52A0");

	}

}
