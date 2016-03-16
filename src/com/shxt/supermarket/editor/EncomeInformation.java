package com.shxt.supermarket.editor;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.swt.widgets.TableColumn;

import com.shxt.supermarket.dialog.FirstDialog;
import com.shxt.supermarket.utils.DBTools;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class EncomeInformation extends EditorPart implements IEditorInput{

	public static final String ID = "com.shxt.supermarket.editor.EncomeInformation"; //$NON-NLS-1$
	private Table table_1;
	int page = 1;
	
	public EncomeInformation() {
	}

	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		
		table_1 = new Table(container, SWT.BORDER | SWT.FULL_SELECTION);
		table_1.setBounds(10, 10, 574, 454);
		table_1.setHeaderVisible(true);
		table_1.setLinesVisible(true);
		
		TableColumn tblclmnDate = new TableColumn(table_1, SWT.NONE);
		tblclmnDate.setWidth(110);
		tblclmnDate.setText("\u65E5\u671F");
		
		TableColumn tblclmnNewColumn = new TableColumn(table_1, SWT.NONE);
		tblclmnNewColumn.setWidth(120);
		tblclmnNewColumn.setText("\u5546\u54C1\u7F16\u53F7");
		
		TableColumn tblclmnHowmany = new TableColumn(table_1, SWT.NONE);
		tblclmnHowmany.setWidth(110);
		tblclmnHowmany.setText("\u6570\u91CF");
		
		TableColumn tblclmnPrise = new TableColumn(table_1, SWT.NONE);
		tblclmnPrise.setWidth(110);
		tblclmnPrise.setText("\u603B\u4EF7");
		
		TableColumn tblclmnEmployeenumber = new TableColumn(table_1, SWT.NONE);
		tblclmnEmployeenumber.setWidth(90);
		tblclmnEmployeenumber.setText("\u6536\u94F6\u5458\u7F16\u53F7");
		
		Menu menu = new Menu(table_1);
		table_1.setMenu(menu);
		
		MenuItem mntmSearch = new MenuItem(menu, SWT.NONE);
		mntmSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			//查询本机流水
			public void widgetSelected(SelectionEvent e) {
				DBTools db = DBTools.getDBTools("cxy_ccshxt", "cxy", "cxy");	
				java.util.List<String[]> list = db.findByRCPPage("select saledate,goodsnumber,howmany,addprise,employeenumber from encome where employeenumber=?",new Object[]{FirstDialog.userid});
				table_1.removeAll();
					for(String[] str : list){
					TableItem tableItem = new TableItem(table_1, SWT.NONE);
					if(str==null){
						MessageBox box = new MessageBox(EncomeInformation.this.getEditorSite().getShell(),SWT.CLOSE);
						box.setMessage("没有符合条件的记录");
						box.open();
					}else{
						tableItem.setText(str);
					}
				}
			}
		});
		
		mntmSearch.setText("\u67E5\u8BE2\u672C\u673A\u6D41\u6C34");
		
		MenuItem menuItem = new MenuItem(menu, SWT.NONE);
		menuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			//查询当天流水
			public void widgetSelected(SelectionEvent e) {
				DBTools db = DBTools.getDBTools("cxy_ccshxt", "cxy", "cxy");
				
				//获取当前时间
				long time = new Date().getTime();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				int today = Integer.valueOf(sdf.format(time));
				
				java.util.List<String[]> list = db.findByRCPPage("select saledate,goodsnumber,howmany,addprise,employeenumber from encome where saledate=?",new Object[]{today});
				table_1.removeAll();
				for(String[] str : list){
					TableItem tableItem = new TableItem(table_1, SWT.NONE);
					if(str==null){
						MessageBox box = new MessageBox(EncomeInformation.this.getEditorSite().getShell(),SWT.CLOSE);
						box.setMessage("没有符合条件的记录");
						box.open();
					}else{
						tableItem.setText(str);
					}
				}
			}
		});
		menuItem.setText("\u67E5\u8BE2\u5F53\u5929\u6D41\u6C34");

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
		return "r";
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return "j";
	}
}
