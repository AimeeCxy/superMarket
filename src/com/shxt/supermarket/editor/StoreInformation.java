package com.shxt.supermarket.editor;

import java.util.ArrayList;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.shxt.supermarket.dialog.AddStoreInfor;
import com.shxt.supermarket.dialog.ChangeStoreInfor;
import com.shxt.supermarket.dialog.FindStoreByName;
import com.shxt.supermarket.dialog.FindStoreByNumber;
import com.shxt.supermarket.utils.DBTools;

public class StoreInformation extends EditorPart implements IEditorInput {

	public static final String ID = "com.shxt.supermarket.editor.StoreInformation"; 
	private Table table;

	public StoreInformation() {
	}

	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		
		table = new Table(container, SWT.BORDER | SWT.FULL_SELECTION);
		table.setBounds(10, 10, 574, 454);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnNumber = new TableColumn(table, SWT.NONE);
		tblclmnNumber.setWidth(100);
		tblclmnNumber.setText("\u7F16\u53F7");
		
		TableColumn tblclmnName = new TableColumn(table, SWT.NONE);
		tblclmnName.setWidth(100);
		tblclmnName.setText("\u4F9B\u8D27\u5546\u540D");
		
		TableColumn tblclmnAddress = new TableColumn(table, SWT.NONE);
		tblclmnAddress.setWidth(100);
		tblclmnAddress.setText("\u5730\u5740");
		
		TableColumn tblclmnTel = new TableColumn(table, SWT.NONE);
		tblclmnTel.setWidth(100);
		tblclmnTel.setText("\u8054\u7CFB\u7535\u8BDD");
		
		TableColumn tblclmnHistory = new TableColumn(table, SWT.NONE);
		tblclmnHistory.setWidth(171);
		tblclmnHistory.setText("\u8FDB\u8D27\u5386\u53F2");
		
		Menu menu = new Menu(table);
		table.setMenu(menu);
		
		MenuItem menuItem = new MenuItem(menu, SWT.NONE);
		menuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			//显示全部供货商信息
			public void widgetSelected(SelectionEvent e) {
				DBTools db = DBTools.getDBTools("cxy_ccshxt", "cxy", "cxy");
				java.util.List<Map<String, Object>> list = db.findAll("select number,name,address,tel,history from store", new Object[]{});
				for(Map<String,Object> map : list){
					TableItem tableItem = new TableItem(table, SWT.NONE);
					tableItem.setText(0, map.get("number").toString());
					tableItem.setText(1, map.get("name").toString());
					tableItem.setText(2, map.get("address").toString());
					tableItem.setText(3, map.get("tel").toString());
					tableItem.setText(4, map.get("history").toString());
					
				}
			}
		});
		menuItem.setText("\u5168\u90E8\u4F9B\u8D27\u5546");
		
		//通过编号查找供货商信息
		MenuItem mntmNumbersearch = new MenuItem(menu, SWT.NONE);
		mntmNumbersearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FindStoreByNumber edit = new FindStoreByNumber(new Shell(),SWT.APPLICATION_MODAL|SWT.CLOSE);
				String[] success = (String[])edit.open();
				if(success==null){
					//直接关闭dialog，不改变表单显示内容
				}else{
					table.removeAll();
					TableItem ti = new TableItem(table,SWT.NONE);
					ti.setText(success);
				}
			}
		});
		mntmNumbersearch.setText("\u901A\u8FC7\u7F16\u53F7\u67E5\u627E");
		
		//通过名称查找供货商信息
		MenuItem mntmNamesearch = new MenuItem(menu, SWT.NONE);
		mntmNamesearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FindStoreByName edit = new FindStoreByName(new Shell(),SWT.APPLICATION_MODAL|SWT.CLOSE);
				@SuppressWarnings("rawtypes")
				ArrayList success = (ArrayList) edit.open();
				if(success.size()==0){
					MessageBox box = new MessageBox(StoreInformation.this.getEditorSite().getShell(),SWT.CLOSE);
					box.setMessage("所查询的供货商不存在");
					box.open();
				}else{
					table.removeAll();
					for(int i=0;i<success.size();i++){
						TableItem ti = new TableItem(table,SWT.NONE);
						ti.setText((String[]) success.get(i));
					}
				}
			}
		});
		mntmNamesearch.setText("\u901A\u8FC7\u540D\u79F0\u67E5\u627E");
		
		//添加供货商信息
		MenuItem mntmAdd = new MenuItem(menu, SWT.NONE);
		mntmAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem ti = new TableItem(table,SWT.NONE);
				AddStoreInfor edit = new AddStoreInfor(StoreInformation.this.getEditorSite().getShell(),SWT.APPLICATION_MODAL|SWT.CLOSE);
				String[] success = (String [])edit.open(ti);
				if(success.length>0)
					ti.setText(success);
			}
		});
		mntmAdd.setText("\u6DFB\u52A0");
		
		//修改供货商信息
		MenuItem mntmRefresh = new MenuItem(menu, SWT.NONE);
		mntmRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(table.getSelectionIndex()<=0){
					MessageBox box = new MessageBox(StoreInformation.this.getEditorSite().getShell(),SWT.ERROR);
					box.setMessage("请选中要修改的信息项");
					box.open();
				}else{
					TableItem ti = table.getItem(table.getSelectionIndex());
					ChangeStoreInfor edit = new ChangeStoreInfor(StoreInformation.this.getEditorSite().getShell(),SWT.APPLICATION_MODAL|SWT.CLOSE);
					String[] success = (String[]) edit.open(ti);
					if(success.length>0)
						ti.setText(success);
				}
			}
		});
		mntmRefresh.setText("\u4FEE\u6539");
		
		//删除供货商信息
		MenuItem mntmDelete = new MenuItem(menu, SWT.NONE);
		mntmDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(table.getSelectionIndex()<=0){
					MessageBox box = new MessageBox(StoreInformation.this.getEditorSite().getShell(),SWT.ERROR);
					box.setMessage("请选中要删除的信息项");
					box.open();
				}else{
					DBTools db = DBTools.getDBTools("cxy_ccshxt", "cxy", "cxy");
					TableItem ti = table.getItem(table.getSelectionIndex());
					String id = ti.getText(0);
					int res = db.update("delete from store where number=?", new Object[]{Integer.parseInt(id)});
					if(res>0)
						table.remove(table.getSelectionIndex());
				}
			}
		});
		mntmDelete.setText("\u5220\u9664");
		
		DBTools db = DBTools.getDBTools("cxy_ccshxt", "cxy", "cxy");
		java.util.List<Map<String, Object>> list = db.findAll("select number,name,address,tel,history from store", new Object[]{});
		for(Map<String,Object> map : list){
			TableItem tableItem = new TableItem(table, SWT.NONE);
			tableItem.setText(0, map.get("number").toString());
			tableItem.setText(1, map.get("name").toString());
			tableItem.setText(2, map.get("address").toString());
			tableItem.setText(3, map.get("tel").toString());
			tableItem.setText(4, map.get("history").toString());
			
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
		return "bcd";
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return "abc";
	}
}
