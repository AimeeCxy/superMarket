package com.shxt.supermarket.dialog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;

import com.shxt.supermarket.utils.DBTools;

public class FirstDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private static Text txtUsername;
	private Text txtPassword;

	public static Integer userid = null;
	
	public static String logiontime = "未登录";
	
	public static int power;//用户权限
	
	private boolean isOpen = false;
	
	public FirstDialog(Shell parent, int style) {
		super(parent, style);
		setText("*登录窗*");
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
	
	//记录登录时间的方法
	public static void Setlogiontime(){
		long time = new Date().getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		logiontime = sdf.format(time);
		DBTools db = DBTools.getDBTools("cxy_ccshxt", "cxy", "cxy");
		db.update("update employee set logiontime=? where name=?", new Object[]{logiontime,txtUsername.getText()});
	}
	
	private void createContents() {
		shell = new Shell(getParent(), SWT.DIALOG_TRIM);
		shell.setModified(true);
		
		shell = new Shell(getParent(), getStyle());
		shell.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				if(isOpen){
					result = true;
				}
				else{
					result = false;
				}
			}
		});

		shell.setSize(450, 506);
		shell.setText(getText());
		
		Label lblUsername = new Label(shell, SWT.NONE);
		lblUsername.setBounds(67, 99, 55, 15);
		lblUsername.setText("\u7528\u6237\u540D");
		
		txtUsername = new Text(shell, SWT.BORDER);
		txtUsername.setBounds(128, 96, 113, 21);
		
		Label lblPassword = new Label(shell, SWT.NONE);
		lblPassword.setBounds(67, 156, 55, 15);
		lblPassword.setText("\u5BC6\u7801");
		
		txtPassword = new Text(shell, SWT.BORDER | SWT.PASSWORD);
		txtPassword.setBounds(128, 153, 113, 21);
		
		Button btnLogin = new Button(shell, SWT.NONE);
		btnLogin.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Setlogiontime();
				MessageBox message = new MessageBox(shell,SWT.ERROR);
				message.setText("系统提示信息");
				String username = txtUsername.getText();
				DBTools db = DBTools.getDBTools("cxy_ccshxt","cxy","cxy");
				Map<String,Object> map = (Map<String, Object>) db.findForRow("select id,password from employee where name=?", new Object[]{username});
//				if(!txtPassnum.getText().equals("HepG")){
//					txtPassnum.setText("");
//					message.setMessage("您输入的验证码有误！");
//					message.open();
//				}else{
					if(map!=null){
						if(map.get("password").equals(txtPassword.getText())){
							userid=Integer.parseInt(map.get("id").toString());
							result = true;
							isOpen = true;
							shell.close();
						}else{
							txtUsername.setText("");
							txtPassword.setText("");
							message.setMessage("您输入的用户名或密码有误！");
							message.open();
						}
					}else{
						txtUsername.setText("");
						txtPassword.setText("");
						message.setMessage("您输入的用户名或密码有误！");
						message.open();
					}
//				}

				Map<String,Object> map1 = (Map<String, Object>) db.findForRow("select elseinformation from employee where name=?", new Object[]{username});
				power =Integer.parseInt(map1.get("elseinformation").toString());
				
			}
		});
		
		btnLogin.setBounds(166, 326, 75, 25);
		btnLogin.setText("\u767B\u5F55");

	}
}
