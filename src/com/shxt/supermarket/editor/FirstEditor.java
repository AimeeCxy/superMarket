package com.shxt.supermarket.editor;

import java.text.SimpleDateFormat;
import java.util.Date;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import com.shxt.supermarket.dialog.FirstDialog;
import com.shxt.supermarket.utils.DBTools;

public class FirstEditor extends EditorPart implements IEditorInput{

	public static final String ID = "com.shxt.supermarket.editor.FirstEditor";
	private Text txtNumber;//��Ʒ���
	private Text txtPrise;//����
	private Text txtHowmany;//����
	private Text txtAddprise;//�ܼ�
	private Text txtShoukuan;//ʵ�տ�
	private Text txtZhaoling;//Ӧ����
	private Text text;//��Ա����
	private Text text_1;//��Ա����
	
	private Text text_2;//�ϼƣ�Ӧ�տ�

	protected Shell shell;
	
	public FirstEditor() {
	}

	
	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		
		Group group = new Group(container, SWT.NONE);
		group.setText("\u5546\u54C1\u5F55\u5165");
		group.setBounds(80, 106, 438, 213);
		
		Label lblNumber = new Label(group, SWT.NONE);
		lblNumber.setBounds(10, 50, 55, 15);
		lblNumber.setText("\u5546\u54C1\u7F16\u7801");
		
		txtNumber = new Text(group, SWT.BORDER);
		txtNumber.setBounds(71, 47, 104, 21);
		
		Label lblPrise = new Label(group, SWT.NONE);
		lblPrise.setBounds(216, 50, 38, 15);
		lblPrise.setText("\u5355\u4EF7");
		
		txtPrise = new Text(group, SWT.BORDER);
		txtPrise.setText("00.00");
		txtPrise.setBounds(254, 47, 85, 21);
		
		Label lblHowMany = new Label(group, SWT.NONE);
		lblHowMany.setBounds(10, 112, 55, 15);
		lblHowMany.setText("\u6570\u91CF");
		
		txtHowmany = new Text(group, SWT.BORDER);
		txtHowmany.setBounds(71, 109, 104, 21);
		
		Label lblAddprise = new Label(group, SWT.NONE);
		lblAddprise.setBounds(216, 112, 30, 15);
		lblAddprise.setText("\u603B\u4EF7");
		
		txtAddprise = new Text(group, SWT.BORDER);
		txtAddprise.setText("00.00");
		txtAddprise.setBounds(254, 109, 85, 21);
		
		Label label = new Label(group, SWT.NONE);
		label.setBounds(345, 112, 55, 15);
		label.setText("\u5143");
		
		Label label_1 = new Label(group, SWT.NONE);
		label_1.setBounds(345, 50, 55, 15);
		label_1.setText("\u5143");
		
		Button btnNext = new Button(group, SWT.NONE);
		btnNext.addSelectionListener(new SelectionAdapter() {
			@Override
			//¼��һ��������Ϣ
			public void widgetSelected(SelectionEvent e) {
				//��ʾ����
				DBTools db = DBTools.getDBTools("cxy_ccshxt", "cxy", "cxy");
				Map<String, Object> prisemap = null;
				prisemap = db.findForRow("select prise,discount from goods where number=?", new Object[]{txtNumber.getText().toString()});
				String prise = null;
				//�ж������Ʒ��Ŵ���ʱ
				if(db.update("update goods set number=? where number=?", new Object[]{txtNumber.getText().toString(),txtNumber.getText().toString()})==0){
					txtNumber.setText("");
					txtHowmany.setText("");
					MessageBox message = new MessageBox(FirstEditor.this.getEditorSite().getShell(),SWT.ERROR);
					message.setText("ϵͳ��ʾ��Ϣ");
					message.setMessage("�������Ʒ��ţ�");
					message.open();
				}else{//��Ʒ���û����ʱ
					prise = prisemap.get("prise").toString();
					String discount = prisemap.get("discount").toString();
					Map<String, Object> passwdmap = db.findForRow("select passwd from vip where number=?", new Object[]{text.getText().toString()});
					Map<String, Object> numbermap = db.findForRow("select number from vip where number=?", new Object[]{text.getText().toString()});
					if(text.getText().isEmpty()){//���ǻ�Ա������ͨ�û��ۻ�
						txtPrise.setText(prise);
						//��ʾ�ܼ�
						float addprise = Float.parseFloat(prise)*Integer.parseInt(txtHowmany.getText().toString());
						txtAddprise.setText(Float.toString(addprise));
						//���ܼۼӵ��ϼ���
						text_2.setText(Float.toString(Float.parseFloat(text_2.getText().toString())+Float.parseFloat(txtAddprise.getText().toString())));
						//�޸����ݿ�����Ʒ�����Ϣ
						Map<String,Object> map1 = (Map<String, Object>) db.findForRow("select howmany from goods where number=?", new Object[]{txtNumber.getText()});
						int i = Integer.valueOf(map1.get("howmany").toString()) - Integer.valueOf(txtHowmany.getText().toString());
						db.update("update goods set howmany=? where number=?" , new Object[]{i,txtNumber.getText().toString()});
						//�޸����ݿ���Ӫҵ����Ϣ
						long time = new Date().getTime();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
						int today = Integer.valueOf(sdf.format(time));
						db.update("insert into encome (saledate,goodsnumber,howmany,addprise,employeenumber)values(?,?,?,?,?)",new Object[]{today,txtNumber.getText(),txtHowmany.getText(),txtAddprise.getText(),FirstDialog.userid});
					}else{//�ǻ�Ա
						//�жϻ�Ա�����Ƿ���ȷ
						if(numbermap==null){
							text.setText("");
							text_1.setText("");
							text_2.setText("00.00");
							txtAddprise.setText("00.00");
							MessageBox message = new MessageBox(FirstEditor.this.getEditorSite().getShell(),SWT.ERROR);
							message.setText("ϵͳ��ʾ��Ϣ");
							message.setMessage("�����ڵĻ�Ա����");
							message.open();
						}else{//��Ա������ȷʱ���ж������Ƿ���ȷ
							if(passwdmap!=null){
							String passwd = passwdmap.get("passwd").toString();
								if(!passwd.equals(text_1.getText().toString())){
									text_1.setText("");
									text_2.setText("00.00");
									txtAddprise.setText("00.00");
									MessageBox message = new MessageBox(FirstEditor.this.getEditorSite().getShell(),SWT.ERROR);
									message.setText("ϵͳ��ʾ��Ϣ");
									message.setMessage("��Ա�������");
									message.open();
								}else{//����Ҳ��ȷʱ����ʼ���ջ�Ա���ۻ�
									prise = String.valueOf(Float.parseFloat(prise) * Float.parseFloat(discount));
									float p = Float.parseFloat(prise) * Float.parseFloat(discount);
									if(p<10){
										prise = prise.substring(0,3); 
									}else if(p<100){
										prise = prise.substring(0,4);
									}else if(p<1000){
										prise = prise.substring(0,5);
									}else if(p<10000){
										prise = prise.substring(0,6);
									}else if(p<100000){
										prise = prise.substring(0,7);
									}else{
										prise = prise.substring(0,8);
									}
									//��ʾ����
									txtPrise.setText(prise);
									//��ʾ�ܼ�
									String addprise = String.valueOf(Float.parseFloat(prise)*Integer.parseInt(txtHowmany.getText().toString()));
										//�������һλС��
									float a = Float.parseFloat(prise)*Integer.parseInt(txtHowmany.getText().toString());
									if(a<10){
										addprise = addprise.substring(0, 3);
									}else if(a<100){
										addprise = addprise.substring(0,4);
									}else if(a<1000){
										addprise = addprise.substring(0,5);
									}else if(a<10000){
										addprise = addprise.substring(0,6);
									}else if(a<100000){
										addprise = addprise.substring(0,7);
									}else{
										addprise = addprise.substring(0,8);
									}
									txtAddprise.setText(addprise);
									//���ܼۼӵ��ϼ���
									String heji = Float.toString(Float.parseFloat(text_2.getText().toString())+Float.parseFloat(txtAddprise.getText().toString()));
										//�������һλС��
									float h = Float.parseFloat(text_2.getText().toString())+Float.parseFloat(txtAddprise.getText().toString());
									if(h<10){
										heji = heji.substring(0, 3);
									}else if(h<100){
										heji = heji.substring(0,4);
									}else if(h<1000){
										heji = heji.substring(0,5);
									}else if(h<10000){
										heji = heji.substring(0,6);
									}else if(h<100000){
										heji = heji.substring(0,7);
									}else{
										heji = heji.substring(0,8);
									}
									text_2.setText(heji);
									//�޸����ݿ�����Ʒ�����Ϣ
									Map<String,Object> map1 = (Map<String, Object>) db.findForRow("select howmany from goods where number=?", new Object[]{txtNumber.getText()});
									int i = Integer.valueOf(map1.get("howmany").toString()) - Integer.valueOf(txtHowmany.getText().toString());
									db.update("update goods set howmany=? where number=?" , new Object[]{i,txtNumber.getText().toString()});
									//�޸����ݿ���Ӫҵ����Ϣ
									long time = new Date().getTime();
									SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
									int today = Integer.valueOf(sdf.format(time));
									db.update("insert into encome (saledate,goodsnumber,howmany,addprise,employeenumber)values(?,?,?,?,?)",new Object[]{today,txtNumber.getText(),txtHowmany.getText(),txtAddprise.getText(),FirstDialog.userid});
								}	
							}
						}
					}
				}
			}
		});
		btnNext.setBounds(242, 170, 75, 25);
		btnNext.setText("\u4E0B\u4E00\u9879");
		
		Button btnFinish = new Button(group, SWT.NONE);
		btnFinish.addSelectionListener(new SelectionAdapter() {
			@Override
			//һλ�˿͵Ĺ�����Ϣ¼�����
			public void widgetSelected(SelectionEvent e) {
				MessageBox m = new MessageBox(FirstEditor.this.getEditorSite().getShell(),SWT.CLOSE);
				m.setText("ϵͳ��ʾ��Ϣ");
				m.setMessage("�븶��ϼ�"+text_2.getText().toString()+"Ԫ");
				m.open();
			}
		});
		btnFinish.setBounds(336, 170, 75, 25);
		btnFinish.setText("\u5B8C\u6210");
		
		Label label_6 = new Label(group, SWT.NONE);
		label_6.setBounds(10, 170, 44, 15);
		label_6.setText("\u5408\u8BA1");
		
		text_2 = new Text(group, SWT.BORDER);
		text_2.setText("00.00");
		text_2.setBounds(71, 167, 104, 21);
		
		Label label_7 = new Label(group, SWT.NONE);
		label_7.setBounds(182, 170, 23, 15);
		label_7.setText("\u5143");
		
		Group group_1 = new Group(container, SWT.NONE);
		group_1.setText("\u6536\u94F6\u5F55\u5165");
		group_1.setBounds(80, 336, 438, 98);
		
		Label lblShoukuan = new Label(group_1, SWT.NONE);
		lblShoukuan.setBounds(10, 35, 48, 15);
		lblShoukuan.setText("\u6536\u6B3E");
		
		txtShoukuan = new Text(group_1, SWT.BORDER);
		txtShoukuan.setBounds(63, 32, 96, 21);
		
		Label lblZhaoling = new Label(group_1, SWT.NONE);
		lblZhaoling.setBounds(218, 35, 35, 15);
		lblZhaoling.setText("\u627E\u96F6");
		
		txtZhaoling = new Text(group_1, SWT.BORDER);
		txtZhaoling.setBounds(259, 32, 87, 21);
		
		Label label_2 = new Label(group_1, SWT.NONE);
		label_2.setBounds(163, 35, 20, 15);
		label_2.setText("\u5143");
		
		Label label_3 = new Label(group_1, SWT.NONE);
		label_3.setBounds(352, 35, 20, 15);
		label_3.setText("\u5143");
		
		Button btnOver = new Button(group_1, SWT.NONE);
		btnOver.addSelectionListener(new SelectionAdapter() {
			@Override
			//�տ�
			public void widgetSelected(SelectionEvent e) {
				float zhaoling = Float.parseFloat(txtShoukuan.getText().toString()) - Float.parseFloat(text_2.getText().toString());
				if(zhaoling<0){
					MessageBox message = new MessageBox(FirstEditor.this.getEditorSite().getShell(),SWT.ERROR);
					message.setText("ϵͳ��ʾ��Ϣ");
					message.setMessage("�������㣬��������");
					message.open();
					txtShoukuan.setText("");
				}else{
					int zl = (int) Float.parseFloat(String.valueOf(zhaoling));
					if(Float.parseFloat(String.valueOf(zhaoling))-zl==0){
						txtZhaoling.setText(String.valueOf(zhaoling));
					}else{
							//�������һλС��
						String z = String.valueOf(zhaoling);
						if(zhaoling<10){
							z = z.substring(0, 3);
						}else if(zhaoling<100){
							z = z.substring(0,4);
						}else if(zhaoling<1000){
							z = z.substring(0,5);
						}else if(zhaoling<10000){
							z = z.substring(0,6);
						}else if(zhaoling<100000){
							z = z.substring(0,7);
						}else{
							z = z.substring(0,8);
						}
						txtZhaoling.setText(z);
					}
				}
			}
		});
		btnOver.setBounds(247, 63, 75, 25);
		btnOver.setText("\u6536\u6B3E");
		
		Button button = new Button(group_1, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			//�տ����
			public void widgetSelected(SelectionEvent e) {
				//����Ա���һ���˿͸���
				txtNumber.setText("");
				txtPrise.setText("");
				txtHowmany.setText("");
				txtAddprise.setText("");
				text_2.setText("00.00");
				txtShoukuan.setText("");
				txtZhaoling.setText("");
				text.setText("");
				text_1.setText("");
			}
		});
		button.setBounds(338, 63, 75, 25);
		button.setText("\u5B8C\u6210");
		
		Group group_2 = new Group(container, SWT.NONE);
		group_2.setText("\u4F1A\u5458\u5165\u53E3");
		group_2.setBounds(80, 20, 438, 69);
		
		Label label_4 = new Label(group_2, SWT.NONE);
		label_4.setBounds(10, 35, 55, 15);
		label_4.setText("\u4F1A\u5458\u5361\u53F7");
		
		text = new Text(group_2, SWT.BORDER);
		text.setBounds(71, 32, 103, 21);
		
		Label label_5 = new Label(group_2, SWT.NONE);
		label_5.setBounds(214, 35, 24, 15);
		label_5.setText("\u5BC6\u7801");
		
		text_1 = new Text(group_2, SWT.BORDER);
		text_1.setBounds(254, 32, 82, 21);

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
		return "myToolTipText";
	}
}
