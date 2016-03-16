package com.shxt.supermarket.editor;

import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.shxt.supermarket.dialog.AddEmployeeInfor;
import com.shxt.supermarket.dialog.ChangeEmployeeInfor;
import com.shxt.supermarket.utils.DBTools;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class EmployeeInformation extends EditorPart implements IEditorInput{

	public static final String ID = "com.shxt.supermarket.editor.EmployeeInformation"; 
	private Table table;

	public EmployeeInformation() {
	}

	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		
		table = new Table(container, SWT.BORDER | SWT.FULL_SELECTION);
		table.setBounds(10, 10, 574, 454);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnNewColumn = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn.setWidth(36);
		tblclmnNewColumn.setText("\u5DE5\u53F7");
		
		TableColumn tblclmnName = new TableColumn(table, SWT.NONE);
		tblclmnName.setWidth(62);
		tblclmnName.setText("\u59D3\u540D");
		
		TableColumn tblclmnSex_1 = new TableColumn(table, SWT.NONE);
		tblclmnSex_1.setWidth(36);
		tblclmnSex_1.setText("\u6027\u522B");
		
		TableColumn tblclmnAge = new TableColumn(table, SWT.NONE);
		tblclmnAge.setWidth(36);
		tblclmnAge.setText("\u5E74\u9F84");
		
		TableColumn tblclmnAddress = new TableColumn(table, SWT.NONE);
		tblclmnAddress.setWidth(62);
		tblclmnAddress.setText("\u5730\u5740");
		
		TableColumn tblclmnSex = new TableColumn(table, SWT.NONE);
		tblclmnSex.setWidth(62);
		tblclmnSex.setText("\u767B\u5F55\u5BC6\u7801");
		
		TableColumn tblclmnPlus = new TableColumn(table, SWT.NONE);
		tblclmnPlus.setWidth(36);
		tblclmnPlus.setText("\u6743\u9650");
		
		TableColumn tableColumn_1 = new TableColumn(table, SWT.NONE);
		tableColumn_1.setWidth(120);
		tableColumn_1.setText("\u8054\u7CFB\u7535\u8BDD");
		
		TableColumn tableColumn = new TableColumn(table, SWT.NONE);
		tableColumn.setText("\u767B\u5F55\u65F6\u95F4");
		tableColumn.setWidth(120);
		
		Menu menu = new Menu(table);
		table.setMenu(menu);
		
		MenuItem mntmAdd = new MenuItem(menu, SWT.NONE);
		mntmAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			//添加员工信息
			public void widgetSelected(SelectionEvent e) {
				TableItem ti = new TableItem(table,SWT.NONE);
				AddEmployeeInfor edit = new AddEmployeeInfor(EmployeeInformation.this.getEditorSite().getShell(),SWT.APPLICATION_MODAL|SWT.CLOSE);
				String[] success = (String [])edit.open(ti);
				if(success.length>0)
					ti.setText(success);
			}
		});
		mntmAdd.setText("\u6DFB\u52A0");
		
		MenuItem mntmModify = new MenuItem(menu, SWT.NONE);
		mntmModify.addSelectionListener(new SelectionAdapter() {
			@Override
			//修改员工信息
			public void widgetSelected(SelectionEvent e) {
				if(table.getSelectionIndex()<=0){
					MessageBox box = new MessageBox(EmployeeInformation.this.getEditorSite().getShell(),SWT.ERROR);
					box.setMessage("请选中要修改的信息项");
					box.open();
				}else{
					TableItem ti = table.getItem(table.getSelectionIndex());
					ChangeEmployeeInfor edit = new ChangeEmployeeInfor(EmployeeInformation.this.getEditorSite().getShell(),SWT.APPLICATION_MODAL|SWT.CLOSE);
					String[] success = (String [])edit.open(ti);
					if(success.length>0)
						ti.setText(success);
				}
			}
		});
		mntmModify.setText("\u4FEE\u6539");
		
		MenuItem mntmDelete = new MenuItem(menu, SWT.NONE);
		mntmDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(table.getSelectionIndex()<=0){
					MessageBox box = new MessageBox(EmployeeInformation.this.getEditorSite().getShell(),SWT.ERROR);
					box.setMessage("请选中要删除的信息项");
					box.open();
				}else{
					DBTools db = DBTools.getDBTools("cxy_ccshxt", "cxy", "cxy");
					TableItem ti = table.getItem(table.getSelectionIndex());
					String id = ti.getText(0);
					int res = db.update("delete from employee where id=?", new Object[]{Integer.parseInt(id)});
					if(res>0){
						table.remove(table.getSelectionIndex());
					}
				}
			}
		});
		mntmDelete.setText("\u5220\u9664");
		
		DBTools db = DBTools.getDBTools("cxy_ccshxt", "cxy", "cxy");
		
		java.util.List<Map<String, Object>> list = db.findAll("select id,name,sex,age,address,password,elseinformation,telnumber,logiontime from employee", new Object[]{});
		
		for(Map<String,Object> map : list){
		
			TableItem tableItem = new TableItem(table, SWT.NONE);
			tableItem.setText(0, map.get("id").toString());
			tableItem.setText(1, map.get("name").toString());
			tableItem.setText(2, map.get("sex").toString());
			tableItem.setText(3, map.get("age").toString());
			tableItem.setText(4, map.get("address").toString());
			tableItem.setText(5, map.get("password").toString());
			tableItem.setText(6, map.get("elseinformation").toString());
			tableItem.setText(7, map.get("telnumber").toString());
			tableItem.setText(8,map.get("logiontime").toString());
		}
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
		setSite(site);
		setInput(input);	
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
		return "m";
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return "x";
	}
}
