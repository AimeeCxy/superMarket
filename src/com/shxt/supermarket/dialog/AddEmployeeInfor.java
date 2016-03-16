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

public class AddEmployeeInfor extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text text;
	private Text text_1;
	private Text text_2;
	private Text text_3;
	private Text text_4;
	private Text text_5;
	private Text text_6;
	private Text text_7;

	public AddEmployeeInfor(Shell parent, int style) {
		super(parent, style);
		setText("添加员工信息");
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
		
		Label lblId = new Label(shell, SWT.NONE);
		lblId.setBounds(34, 24, 31, 15);
		lblId.setText("ID");
		
		Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setBounds(34, 63, 31, 15);
		lblNewLabel.setText("\u59D3\u540D");
		
		Label label = new Label(shell, SWT.NONE);
		label.setBounds(34, 105, 31, 15);
		label.setText("\u6027\u522B");
		
		Label label_1 = new Label(shell, SWT.NONE);
		label_1.setBounds(34, 148, 31, 15);
		label_1.setText("\u5E74\u9F84");
		
		text = new Text(shell, SWT.BORDER);
		text.setBounds(72, 21, 73, 21);
		
		text_1 = new Text(shell, SWT.BORDER);
		text_1.setBounds(72, 60, 73, 21);
		
		text_2 = new Text(shell, SWT.BORDER);
		text_2.setBounds(72, 102, 73, 21);
		
		text_3 = new Text(shell, SWT.BORDER);
		text_3.setBounds(72, 145, 73, 21);
		
		Label label_2 = new Label(shell, SWT.NONE);
		label_2.setBounds(173, 24, 31, 15);
		label_2.setText("\u5730\u5740");
		
		Label label_3 = new Label(shell, SWT.NONE);
		label_3.setBounds(173, 63, 31, 15);
		label_3.setText("\u5BC6\u7801");
		
		Label label_4 = new Label(shell, SWT.NONE);
		label_4.setBounds(173, 105, 31, 15);
		label_4.setText("\u9644\u52A0");
		
		Label label_5 = new Label(shell, SWT.NONE);
		label_5.setBounds(173, 148, 31, 15);
		label_5.setText("\u7535\u8BDD");
		
		text_4 = new Text(shell, SWT.BORDER);
		text_4.setBounds(210, 21, 130, 21);
		
		text_5 = new Text(shell, SWT.BORDER);
		text_5.setBounds(210, 60, 130, 21);
		
		text_6 = new Text(shell, SWT.BORDER);
		text_6.setBounds(210, 102, 130, 21);
		
		text_7 = new Text(shell, SWT.BORDER);
		text_7.setBounds(210, 145, 130, 21);
		
		Button btnAdd = new Button(shell, SWT.NONE);
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(text.getText().isEmpty()||text_1.getText().isEmpty()||text_2.getText().isEmpty()||text_3.getText().isEmpty()||text_4.getText().isEmpty()||text_5.getText().isEmpty()||text_6.getText().isEmpty()||text_7.getText().isEmpty()){
					MessageBox message = new MessageBox(shell,SWT.ERROR);
					message.setText("系统提示信息");
					message.setMessage("请完善要添加的员工信息");
					message.open();
				}else{
					DBTools db = DBTools.getDBTools("cxy_ccshxt", "cxy", "cxy");
					result = db.updateNoIdReturnArrayByRCP("insert into employee (id,name,sex,age,address,password,elseinformation,telnumber,logiontime)values(?,?,?,?,?,?,?,?,?)", new Object[]{text.getText(),text_1.getText(),text_2.getText(),text_3.getText(),text_4.getText(),text_5.getText(),text_6.getText(),text_7.getText(),"未登录"});
					MessageBox message = new MessageBox(shell,SWT.CLOSE);
					message.setText("系统提示信息");
					message.setMessage("一条员工信息添加成功~");
					message.open();
					shell.close();
				}
			}
		});
		btnAdd.setBounds(173, 202, 75, 25);
		btnAdd.setText("\u6DFB\u52A0");

	}
}
