package com.shxt.supermarket.editor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.shxt.supermarket.dialog.AddVIPInfor;
import com.shxt.supermarket.dialog.ChangeVIPInfor;
import com.shxt.supermarket.utils.DBTools;

public class VIPInformation extends EditorPart implements IEditorInput{

	public static final String ID = "com.shxt.supermarket.editor.VIPInformation"; //$NON-NLS-1$
	private Table table;
	int page = 1;

	public VIPInformation() {
	}

	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		
		table = new Table(container, SWT.BORDER | SWT.FULL_SELECTION);
		table.setBounds(10, 10, 574, 454);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnNumber = new TableColumn(table, SWT.NONE);
		tblclmnNumber.setWidth(120);
		tblclmnNumber.setText("\u5361\u53F7");
		
		TableColumn tblclmnName = new TableColumn(table, SWT.NONE);
		tblclmnName.setWidth(100);
		tblclmnName.setText("\u59D3\u540D");
		
		TableColumn tblclmnPasswd = new TableColumn(table, SWT.NONE);
		tblclmnPasswd.setWidth(100);
		tblclmnPasswd.setText("\u5BC6\u7801");
		
		TableColumn tblclmnTel = new TableColumn(table, SWT.NONE);
		tblclmnTel.setWidth(120);
		tblclmnTel.setText("\u8054\u7CFB\u7535\u8BDD");
		
		Menu menu = new Menu(table);
		table.setMenu(menu);

		MenuItem mntmAdd = new MenuItem(menu, SWT.NONE);
		mntmAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			//添加会员信息
			public void widgetSelected(SelectionEvent e) {
				TableItem ti = new TableItem(table,SWT.NONE);
				AddVIPInfor edit = new AddVIPInfor(VIPInformation.this.getEditorSite().getShell(),SWT.APPLICATION_MODAL|SWT.CLOSE);
				String[] success = (String [])edit.open(ti);
				if(success.length>0)
					ti.setText(success);
			}
		});
		mntmAdd.setText("\u6DFB\u52A0");
		
		MenuItem mntmModify = new MenuItem(menu, SWT.NONE);
		mntmModify.addSelectionListener(new SelectionAdapter() {
			@Override
			//修改会员信息
			public void widgetSelected(SelectionEvent e) {
				if(table.getSelectionIndex()<=0){
					MessageBox box = new MessageBox(VIPInformation.this.getEditorSite().getShell(),SWT.CLOSE);
					box.setMessage("请选中要修改的信息项");
					box.open();
				}else{
					TableItem ti = table.getItem(table.getSelectionIndex());
					ChangeVIPInfor edit = new ChangeVIPInfor(VIPInformation.this.getEditorSite().getShell(),SWT.APPLICATION_MODAL|SWT.CLOSE);
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
					MessageBox box = new MessageBox(VIPInformation.this.getEditorSite().getShell(),SWT.CLOSE);
					box.setMessage("请选中要删除的信息项");
					box.open();
				}else{
					DBTools db = DBTools.getDBTools("cxy_ccshxt", "cxy", "cxy");
					TableItem ti = table.getItem(table.getSelectionIndex());
					String id = ti.getText(0);
					int res = db.update("delete from vip where number=?", new Object[]{Integer.parseInt(id)});
					if(res>0){
						table.remove(table.getSelectionIndex());
					}
				}
			}
		});
		mntmDelete.setText("\u5220\u9664");

		DBTools db = DBTools.getDBTools("cxy_ccshxt", "cxy", "cxy");
		java.util.List<String[]> list = db.findPageByRCP("select number,name,passwd,tel from vip",page);
		for(String[] str : list){
			TableItem tableItem = new TableItem(table, SWT.NONE);
			tableItem.setText(str);
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
		return "p";
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return "l";
	}
}
