package com.shxt.supermarket.editor;

import java.util.HashMap;
import java.util.List;
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
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import com.shxt.supermarket.dialog.ChangeGoodsInfor;
import com.shxt.supermarket.dialog.DeleteGoodsInfor;
import com.shxt.supermarket.utils.DBTools;
import org.eclipse.swt.widgets.Button;

public class GoodsInformation extends EditorPart implements IEditorInput {

	public static final String ID = "com.shxt.supermarket.editor.GoodsInformation"; 
	private Table table;
	public int page=1;
	public int sumPage=1;

	public GoodsInformation() {
	}

	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		
		table = new Table(container, SWT.BORDER | SWT.FULL_SELECTION);
		table.setBounds(10, 10, 574, 410);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnNumber = new TableColumn(table, SWT.NONE);
		tblclmnNumber.setWidth(70);
		tblclmnNumber.setText("\u7F16\u53F7");
		
		TableColumn tblclmnName = new TableColumn(table, SWT.NONE);
		tblclmnName.setWidth(70);
		tblclmnName.setText("\u5546\u54C1\u540D");
		
		TableColumn tblclmnPrise = new TableColumn(table, SWT.NONE);
		tblclmnPrise.setWidth(50);
		tblclmnPrise.setText("\u5355\u4EF7");
		
		TableColumn tblclmnHowmany = new TableColumn(table, SWT.NONE);
		tblclmnHowmany.setWidth(70);
		tblclmnHowmany.setText("\u5E93\u5B58\u6570");
		
		TableColumn tblclmnStartdate = new TableColumn(table, SWT.NONE);
		tblclmnStartdate.setWidth(100);
		tblclmnStartdate.setText("\u751F\u4EA7\u65E5\u671F");
		
		TableColumn tblclmnFreshdate = new TableColumn(table, SWT.NONE);
		tblclmnFreshdate.setWidth(50);
		tblclmnFreshdate.setText("\u4FDD\u8D28\u671F");
		
		TableColumn tblclmnElse = new TableColumn(table, SWT.NONE);
		tblclmnElse.setWidth(110);
		tblclmnElse.setText("\u5907\u6CE8\u4FE1\u606F");
		
		Menu menu = new Menu(table);
		table.setMenu(menu);
		
		MenuItem mntmModify = new MenuItem(menu, SWT.NONE);
		mntmModify.addSelectionListener(new SelectionAdapter() {
			@Override
			//修改商品信息
			public void widgetSelected(SelectionEvent e) {
				if(table.getSelectionIndex()<=0){
					MessageBox box = new MessageBox(GoodsInformation.this.getEditorSite().getShell(),SWT.CLOSE);
					box.setMessage("请选中要修改的信息项");
					box.open();
				}else{
					TableItem ti = table.getItem(table.getSelectionIndex());
					ChangeGoodsInfor edit = new ChangeGoodsInfor(GoodsInformation.this.getEditorSite().getShell(),SWT.APPLICATION_MODAL|SWT.CLOSE);
					String[] success = (String [])edit.open(ti);
					if(success.length>0)
						ti.setText(success);
				}
			}
		});
		mntmModify.setText("\u4FEE\u6539\u4FE1\u606F");
		
		MenuItem mntmDelete = new MenuItem(menu, SWT.NONE);
		mntmDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(table.getSelectionIndex()<=0){
					MessageBox box = new MessageBox(GoodsInformation.this.getEditorSite().getShell(),SWT.CLOSE);
					box.setMessage("请选中要删除的信息项");
					box.open();
				}else{
		        //打开DeleteGoodsInfor的 dialog
				DeleteGoodsInfor del = new DeleteGoodsInfor(GoodsInformation.this.getEditorSite().getShell(),SWT.APPLICATION_MODAL|SWT.CLOSE);
				TableItem ti = table.getItem(table.getSelectionIndex());
				String id = ti.getText(0);
				int res = (Integer) del.open(Integer.parseInt(id));
				if(res>0)
					table.remove(table.getSelectionIndex());
				}
			}
		});
		mntmDelete.setText("\u5220\u9664\u6761\u76EE");
		
		TableColumn tblclmnDiscount = new TableColumn(table, SWT.NONE);
		tblclmnDiscount.setWidth(50);
		tblclmnDiscount.setText("\u6298\u6263");
		
		Button btnPageup = new Button(container, SWT.NONE);
		btnPageup.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				page = (page - 1)<1?1:(page-1);
				getPageInfo(page);
			}
		});
		btnPageup.setBounds(374, 439, 75, 25);
		btnPageup.setText("\u4E0A\u4E00\u9875");
		
		Button btnPagedown = new Button(container, SWT.NONE);
		btnPagedown.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				page= (page+1)>sumPage?sumPage:(page+1);
				getPageInfo(page);
			}
		});
		btnPagedown.setBounds(474, 439, 75, 25);
		btnPagedown.setText("\u4E0B\u4E00\u9875");
		
		DBTools db = DBTools.getDBTools("cxy_ccshxt", "cxy", "cxy");

		sumPage = db.getSumPage("select * from goods",new Object[]{},15);
		
		java.util.List<String[]> list = db.findPageByRCP("select number,name,prise,howmany,startdate,freshdate,plus,discount from goods",page);
		for(String[] str : list){
			TableItem tableItem = new TableItem(table, SWT.NONE);
			tableItem.setText(str);
		}

	}
	
	public void getPageInfo(int page){
		//1.移除原table中的所有行
		table.removeAll();
		//2.连接数据库
		DBTools db = DBTools.getDBTools("cxy_ccshxt", "cxy", "cxy");
		//db.findByPage("select * from goods", 1, 15);
		//3.通过工具类中的分页方法得到所需的结果集
		Map<String,String> orderBy = new HashMap<String,String>();
		orderBy.put("id", "desc");
		List<String[]> list = db.findPageByRCP("select * from goods", page);
		//4.把结果集中的内容显示在table上
		for(int i=0;i<list.size();i++){
			TableItem ti = new TableItem(table,i);
			ti.setText(list.get(i));
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
		return "cxy";
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return "rr";
	}
}
