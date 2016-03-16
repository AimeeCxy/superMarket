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

public class ChangeGoodsInfor extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text txtGoodsnumber;
	private Text txtName;
	private Text txtPrise;
	private Text txtHowmany;

	public ChangeGoodsInfor(Shell parent, int style) {
		super(parent, style);
		setText("修改商品信息");
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
		
		Label lblGoodsnumber = new Label(shell, SWT.NONE);
		lblGoodsnumber.setText("\u5546\u54C1\u7F16\u53F7");
		lblGoodsnumber.setBounds(57, 36, 55, 15);
		
		txtGoodsnumber = new Text(shell, SWT.BORDER | SWT.READ_ONLY);
		txtGoodsnumber.setText(ti.getText(0));
		txtGoodsnumber.addSelectionListener(new SelectionAdapter() {
			
		});
		txtGoodsnumber.setBounds(136, 33, 73, 21);
		
		Label lblName = new Label(shell, SWT.NONE);
		lblName.setBounds(57, 77, 55, 15);
		lblName.setText("\u5546\u54C1\u540D");
		
		txtName = new Text(shell, SWT.BORDER);
		txtName.setBounds(136, 74, 73, 21);
		
		Label lblPrise = new Label(shell, SWT.NONE);
		lblPrise.setBounds(57, 116, 55, 15);
		lblPrise.setText("\u5355\u4EF7");
		
		txtPrise = new Text(shell, SWT.BORDER);
		txtPrise.setBounds(136, 113, 73, 21);
		
		Label lblHowmany = new Label(shell, SWT.NONE);
		lblHowmany.setBounds(57, 157, 55, 15);
		lblHowmany.setText("\u6570\u91CF");
		
		txtHowmany = new Text(shell, SWT.BORDER);
		txtHowmany.setBounds(136, 154, 73, 21);
		
		Button btnModify = new Button(shell, SWT.NONE);
		btnModify.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(txtGoodsnumber.getText().isEmpty()||txtName.getText().isEmpty()||txtPrise.getText().isEmpty()||txtHowmany.getText().isEmpty()){
					MessageBox message = new MessageBox(shell,SWT.ERROR);
					message.setText("系统提示信息");
					message.setMessage("请完善要修改的商品信息");
					message.open();
				}else{
					DBTools db = DBTools.getDBTools("cxy_ccshxt", "cxy", "cxy");
					result = db.updateReturnArrayByRCP("update goods set name=?,prise=?,howmany=? where number=?", new Object[]{txtName.getText(),txtPrise.getText(),txtHowmany.getText(),txtGoodsnumber.getText()});
					shell.close();
				}
			}
		});
		btnModify.setBounds(209, 206, 75, 25);
		btnModify.setText("\u4FEE\u6539");

	}
}
