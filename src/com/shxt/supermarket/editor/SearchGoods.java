package com.shxt.supermarket.editor;

import java.util.ArrayList;

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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import com.shxt.supermarket.dialog.FindGoodsByName;
import com.shxt.supermarket.dialog.FindGoodsByNum;
import com.shxt.supermarket.dialog.FindGoodsByPlus;

//普通用户用以查询商品

public class SearchGoods extends EditorPart implements IEditorInput{

	public static final String ID = "com.shxt.supermarket.editor.SearchGoods";
	private Table table;

	protected Shell shell;
	
	public SearchGoods() {
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
		tblclmnName.setText("\u5546\u54C1\u540D");
		
		TableColumn tblclmnPrise = new TableColumn(table, SWT.NONE);
		tblclmnPrise.setWidth(100);
		tblclmnPrise.setText("\u5355\u4EF7");
		
		TableColumn tblclmnHowmany = new TableColumn(table, SWT.NONE);
		tblclmnHowmany.setWidth(100);
		tblclmnHowmany.setText("\u5E93\u5B58\u6570");
		
		TableColumn tblclmnPlus = new TableColumn(table, SWT.NONE);
		tblclmnPlus.setWidth(170);
		tblclmnPlus.setText("\u5907\u6CE8");
		
		Menu menu = new Menu(table);
		table.setMenu(menu);
		
		//通过编号查询商品
		MenuItem mntmFindbynumber = new MenuItem(menu, SWT.NONE);
		mntmFindbynumber.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FindGoodsByNum bynumber = new FindGoodsByNum(SearchGoods.this.getEditorSite().getShell(),SWT.APPLICATION_MODAL|SWT.CLOSE);
				String[] success = (String[]) bynumber.open();
				if(success!=null){
					table.removeAll();
					TableItem ti = new TableItem(table,SWT.NONE);
					ti.setText(success);
				}
			}
		});
		mntmFindbynumber.setText("\u7F16\u53F7\u67E5\u8BE2");
		
		//通过商品名查询
		MenuItem mntmFindbyname = new MenuItem(menu, SWT.NONE);
		mntmFindbyname.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FindGoodsByName byname = new FindGoodsByName(SearchGoods.this.getEditorSite().getShell(),SWT.APPLICATION_MODAL|SWT.CLOSE);
				@SuppressWarnings("rawtypes")
				ArrayList success =  (ArrayList) byname.open();
				table.removeAll();
				if(success.size()==0){
					MessageBox box = new MessageBox(SearchGoods.this.getEditorSite().getShell(),SWT.CLOSE);
					box.setMessage("所查询的商品不存在");
					box.open();
				}else{
					for(int i=0;i<success.size();i++){
					TableItem ti = new TableItem(table,SWT.NONE);
					ti.setText((String[]) success.get(i));
				    }
				}
			}
		});
		mntmFindbyname.setText("\u5546\u54C1\u540D\u67E5\u8BE2");
		
		//通过备注信息（供货商）查询商品
		MenuItem mntmFindbyplus = new MenuItem(menu, SWT.NONE);
		mntmFindbyplus.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FindGoodsByPlus byplus = new FindGoodsByPlus(SearchGoods.this.getEditorSite().getShell(),SWT.APPLICATION_MODAL|SWT.CLOSE);
				@SuppressWarnings("rawtypes")
				ArrayList success = (ArrayList) byplus.open();
				table.removeAll();
				if(success.size()==0){
					MessageBox box = new MessageBox(SearchGoods.this.getEditorSite().getShell(),SWT.CLOSE);
					box.setMessage("所查询的备注信息不存在");
					box.open();
				}else{
					for(int i=0;i<success.size();i++){
						TableItem ti = new TableItem(table,SWT.NONE);
						ti.setText((String[]) success.get(i));
					}
				}
			}
		});
		mntmFindbyplus.setText("\u5907\u6CE8\u4FE1\u606F\u67E5\u8BE2");

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
		return "you";
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return "me";
	}
}
