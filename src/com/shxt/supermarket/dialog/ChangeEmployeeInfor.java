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

public class ChangeEmployeeInfor extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text text;//ID
	private Text text_1;//姓名
	private Text text_2;//密码
	private Text text_3;//联系电话
	private Text text_4;//性别
	private Text text_5;//年龄
	private Text text_6;//地址
	private Text text_7;//权限

	public ChangeEmployeeInfor(Shell parent, int style) {
		super(parent, style);
		setText("修改员工信息");
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
		
		Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setBounds(28, 27, 23, 15);
		lblNewLabel.setText("ID");
		
		text = new Text(shell, SWT.BORDER | SWT.READ_ONLY);
		text.setText(ti.getText(0));
		text.setBounds(91, 24, 123, 21);
		
		Label label = new Label(shell, SWT.NONE);
		label.setBounds(28, 70, 29, 15);
		label.setText("\u59D3\u540D");
		
		Label label_1 = new Label(shell, SWT.NONE);
		label_1.setText("\u5BC6\u7801");
		label_1.setBounds(28, 111, 29, 15);
		
		Label label_2 = new Label(shell, SWT.NONE);
		label_2.setBounds(28, 153, 55, 15);
		label_2.setText("\u8054\u7CFB\u7535\u8BDD");
		
		text_1 = new Text(shell, SWT.BORDER);
		text_1.setBounds(91, 67, 123, 21);
		
		text_2 = new Text(shell, SWT.BORDER);
		text_2.setBounds(91, 108, 123, 21);
		
		text_3 = new Text(shell, SWT.BORDER);
		text_3.setBounds(91, 150, 123, 21);
		
		Button btnModify = new Button(shell, SWT.NONE);
		btnModify.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(text.getText().isEmpty()||text_1.getText().isEmpty()||text_2.getText().isEmpty()||text_3.getText().isEmpty()||text_4.getText().isEmpty()||text_5.getText().isEmpty()||text_6.getText().isEmpty()||text_7.getText().isEmpty()){
					MessageBox message = new MessageBox(shell,SWT.ERROR);
					message.setText("系统提示信息");
					message.setMessage("请完善要修改的员工信息");
					message.open();
				}else{
				DBTools db = DBTools.getDBTools("cxy_ccshxt", "cxy", "cxy");
				result = db.updateReturnArrayByRCP("update employee set name=?,sex=?,age=?,address=?,password=?,elseinformation=?,telnumber=?,logiontime=? where id=?", new Object[]{text_1.getText(),text_4.getText(),text_5.getText(),text_6.getText(),text_2.getText(),text_7.getText(),text_3.getText(),"未登录",text.getText()});
				shell.close();
				}
			}
		});
		btnModify.setBounds(201, 205, 75, 25);
		btnModify.setText("\u4FEE\u6539");
		
		Label label_3 = new Label(shell, SWT.NONE);
		label_3.setBounds(252, 27, 41, 15);
		label_3.setText("\u6027\u522B");
		
		text_4 = new Text(shell, SWT.BORDER);
		text_4.setBounds(301, 24, 95, 21);
		
		Label label_4 = new Label(shell, SWT.NONE);
		label_4.setBounds(252, 70, 41, 15);
		label_4.setText("\u5E74\u9F84");
		
		text_5 = new Text(shell, SWT.BORDER);
		text_5.setBounds(301, 67, 95, 21);
		
		Label label_5 = new Label(shell, SWT.NONE);
		label_5.setBounds(252, 111, 34, 15);
		label_5.setText("\u5730\u5740");
		
		text_6 = new Text(shell, SWT.BORDER);
		text_6.setBounds(301, 108, 95, 21);
		
		Label label_6 = new Label(shell, SWT.NONE);
		label_6.setBounds(252, 153, 41, 15);
		label_6.setText("\u6743\u9650");
		
		text_7 = new Text(shell, SWT.BORDER);
		text_7.setBounds(301, 150, 95, 21);

		
	}

}
