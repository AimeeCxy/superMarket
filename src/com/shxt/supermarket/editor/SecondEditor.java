package com.shxt.supermarket.editor;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import com.shxt.supermarket.utils.DBTools;

public class SecondEditor extends EditorPart implements IEditorInput {

	public static final String ID = "com.shxt.supermarket.editor.SecondEditor";
	private Text text;//商品编号
	private Text text_1;//商品名
	private Text text_2;//数量
	private Text text_3;//生产日期
	private Text text_4;//保质期
	private Text text_5;//备注
	private Text text_6;//单价
	private Text text_7;//会员折扣

	public SecondEditor() {
	}

	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		
		Group grpInformation = new Group(container, SWT.NONE);
		grpInformation.setText("\u5546\u54C1\u5165\u5E93\u4FE1\u606F");
		grpInformation.setBounds(59, 42, 469, 341);
		
		Label lblNumber = new Label(grpInformation, SWT.NONE);
		lblNumber.setBounds(10, 43, 55, 15);
		lblNumber.setText("\u5546\u54C1\u7F16\u53F7");
		
		Label lblName = new Label(grpInformation, SWT.NONE);
		lblName.setBounds(10, 103, 55, 15);
		lblName.setText("\u5546\u54C1\u540D");
		
		Label lblHowmany = new Label(grpInformation, SWT.NONE);
		lblHowmany.setBounds(10, 158, 55, 15);
		lblHowmany.setText("\u6570\u91CF");
		
		Label lblStartdate = new Label(grpInformation, SWT.NONE);
		lblStartdate.setBounds(229, 103, 55, 15);
		lblStartdate.setText("\u751F\u4EA7\u65E5\u671F");
		
		Label lblFreshdate = new Label(grpInformation, SWT.NONE);
		lblFreshdate.setBounds(229, 158, 55, 15);
		lblFreshdate.setText("\u4FDD\u8D28\u671F");
		
		Label lblElse = new Label(grpInformation, SWT.NONE);
		lblElse.setBounds(229, 219, 55, 15);
		lblElse.setText("\u5907\u6CE8");
		
		text = new Text(grpInformation, SWT.BORDER);
		text.setBounds(75, 43, 110, 21);
		
		text_1 = new Text(grpInformation, SWT.BORDER);
		text_1.setBounds(75, 100, 110, 21);
		
		text_2 = new Text(grpInformation, SWT.BORDER);
		text_2.setBounds(75, 155, 110, 21);
		
		text_3 = new Text(grpInformation, SWT.BORDER);
		text_3.setBounds(290, 100, 110, 21);
		
		text_4 = new Text(grpInformation, SWT.BORDER);
		text_4.setBounds(290, 155, 110, 21);
		
		text_5 = new Text(grpInformation, SWT.BORDER);
		text_5.setBounds(290, 216, 110, 21);
		
		Button btnNext = new Button(grpInformation, SWT.NONE);
		btnNext.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DBTools db = DBTools.getDBTools("cxy_ccshxt","cxy","cxy");
				if(text.getText().isEmpty()||text_1.getText().isEmpty()||text_2.getText().isEmpty()||text_3.getText().isEmpty()||text_4.getText().isEmpty()||text_5.getText().isEmpty()||text_6.getText().isEmpty()||text_7.getText().isEmpty()){
					MessageBox message = new MessageBox(SecondEditor.this.getEditorSite().getShell(),SWT.ERROR);
					message.setText("系统提示信息");
					message.setMessage("请完善入库商品信息");
					message.open();
				}else{
					db.update("insert into goods (number,name,prise,howmany,startdate,freshdate,plus,discount) values(?,?,?,?,?,?,?,?)", new Object[]{text.getText(),text_1.getText(),text_6.getText(),text_2.getText(),text_3.getText(),text_4.getText(),text_5.getText(),text_7.getText()});
					//将进货历史写入供货商表里
					if(db.update("update store set name=? where name=?",new Object[]{text_5.getText(),text_5.getText()})!=0){
						long time = new Date().getTime();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
						String logiontime = sdf.format(time);
						db.update("update store set history=? where name=?",new Object[]{logiontime.toString()+","+text_1.getText().toString(),text_5.getText()});
					}else{
						MessageBox message = new MessageBox(SecondEditor.this.getEditorSite().getShell(),SWT.CLOSE);
						message.setText("系统提示信息");
						message.setMessage("新的供货商，如需要请更新供货商库");
						message.open();
					}
					//提示信息
					MessageBox message = new MessageBox(SecondEditor.this.getEditorSite().getShell(),SWT.CLOSE);
					message.setText("系统提示信息");
					message.setMessage("一条进货信息添加成功~");
					message.open();
					//清空以便录入下一条进货信息
					text.setText("");
					text_1.setText("");
					text_2.setText("");
					text_3.setText("");
					text_4.setText("");
					text_5.setText("");
					text_6.setText("");
					text_7.setText("");
				}
			}
		});
		btnNext.setBounds(229, 278, 75, 25);
		btnNext.setText("\u4E0B\u4E00\u9879");
		
		Button btnFinish = new Button(grpInformation, SWT.NONE);
		btnFinish.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DBTools db = DBTools.getDBTools("cxy_ccshxt","cxy","cxy");
				db.update("insert into goods (number,name,prise,howmany,startdate,freshdate,plus,discount) values(?,?,?,?,?,?,?,?)", new Object[]{Integer.parseInt(text.getText().toString()),text_1.getText().toString(),Float.parseFloat(text_6.getText().toString()),text_2.getText().toString(),text_3.getText().toString(),text_4.getText().toString(),text_5.getText().toString(),Float.parseFloat(text_7.getText().toString())});
				MessageBox message = new MessageBox(SecondEditor.this.getEditorSite().getShell(),SWT.CLOSE);
				message.setText("系统提示信息");
				message.setMessage("本次进货完成~");
				message.open();
				//清空以便录入下次进货
				text.setText("");
				text_1.setText("");
				text_2.setText("");
				text_3.setText("");
				text_4.setText("");
				text_5.setText("");
				text_6.setText("");
				text_7.setText("");
			}
		});
		btnFinish.setBounds(352, 278, 75, 25);
		btnFinish.setText("\u5B8C\u6210");
		
		Label lblPrise = new Label(grpInformation, SWT.NONE);
		lblPrise.setBounds(229, 43, 55, 15);
		lblPrise.setText("\u5355\u4EF7");
		
		text_6 = new Text(grpInformation, SWT.BORDER);
		text_6.setBounds(290, 40, 110, 21);
		
		Label lblDiscount = new Label(grpInformation, SWT.NONE);
		lblDiscount.setBounds(10, 219, 55, 15);
		lblDiscount.setText("\u4F1A\u5458\u6298\u6263");
		
		text_7 = new Text(grpInformation, SWT.BORDER);
		text_7.setBounds(75, 216, 110, 21);

	}

	@Override
	public void setFocus() {
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		this.setSite(site);
		this.setInput(input);
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public String getName() {
		return "myName";
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return "myToolYipText";
	}
}
