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

public class AddVIPInfor extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text text;
	private Text text_1;
	private Text text_2;
	private Text text_3;

	public AddVIPInfor(Shell parent, int style) {
		super(parent, style);
		setText("添加会员信息");
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
		label.setBounds(68, 44, 55, 15);
		label.setText("\u5361\u53F7");
		
		text = new Text(shell, SWT.BORDER);
		text.setBounds(129, 41, 100, 21);
		
		Label label_1 = new Label(shell, SWT.NONE);
		label_1.setBounds(68, 82, 55, 15);
		label_1.setText("\u59D3\u540D");
		
		Label label_2 = new Label(shell, SWT.NONE);
		label_2.setBounds(68, 119, 55, 15);
		label_2.setText("\u5BC6\u7801");
		
		Label label_3 = new Label(shell, SWT.NONE);
		label_3.setBounds(68, 155, 55, 15);
		label_3.setText("\u8054\u7CFB\u7535\u8BDD");
		
		text_1 = new Text(shell, SWT.BORDER);
		text_1.setBounds(129, 79, 100, 21);
		
		text_2 = new Text(shell, SWT.BORDER);
		text_2.setBounds(129, 116, 100, 21);
		
		text_3 = new Text(shell, SWT.BORDER);
		text_3.setBounds(129, 152, 100, 21);
		
		Button btnAdd = new Button(shell, SWT.NONE);
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(text.getText().isEmpty()||text_1.getText().isEmpty()||text_2.getText().isEmpty()||text_3.getText().isEmpty()){
					MessageBox message = new MessageBox(shell,SWT.ERROR);
					message.setText("系统提示信息");
					message.setMessage("请完善要添加的会员信息");
					message.open();
				}else{
					DBTools db = DBTools.getDBTools("cxy_ccshxt", "cxy", "cxy");
					result = db.updateNoIdReturnArrayByRCP("insert into vip (number,name,passwd,tel)values(?,?,?,?)", new Object[]{text.getText(),text_1.getText(),text_2.getText(),text_3.getText()});
					MessageBox message = new MessageBox(shell,SWT.CLOSE);
					message.setText("系统提示信息");
					message.setMessage("一条会员信息添加成功~");
					message.open();
					shell.close();
				}
			}
		});
		btnAdd.setBounds(199, 201, 75, 25);
		btnAdd.setText("\u6DFB\u52A0");

	}

}
