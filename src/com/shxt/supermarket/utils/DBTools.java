package com.shxt.supermarket.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DBTools {

	/**
	 * Ĭ��ÿҳ��¼��
	 */
	public static int pageRecord = 10;
	/**
	 * ��������
	 */
	private String dbName = null;
	/**
	 * ���ݿ��ʺ�
	 */
	private String username = null;
	/**
	 * ���������
	 */
	private String password = null;
	
	/**
	 * DBToolsʵ������
	 */
	private static DBTools db = null;
	/**
	 * ����ģʽ
	 * @param dbName ���ݿ���
	 * @param username ���ݿ��ʺ�
	 * @param password ���ݿ�����
	 * @return DBTools
	 */
	public static DBTools getDBTools(String dbName,String username,String password){
		if(db==null){
			db = new DBTools(dbName,username,password);
		}
		return db;
	}
	
	/**
	 * ���췽��
	 * @param dbName ���ݿ���
	 * @param username ���ݿ��ʺ�
	 * @param password ���ݿ�����
	 */
	private DBTools(String dbName,String username,String password){
		this.dbName = dbName;
		this.username = username;
		this.password = password;
	}
	
	/**
	 * ����
	 */
	private Connection conn = null;
	/**
	 * ������
	 */
	private Statement stmt = null;
	/**
	 * Ԥ����������
	 */
	private PreparedStatement ps = null;
	/**
	 * �����
	 */
	private ResultSet rs = null;
	/**
	 * ��̬�����
	 */
	static {
		try {
			//�������ݿ�����
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	/**
	 * ��ȡ���ݿ�����
	 * @return Connection
	 */
	private Connection getConn(){
		try {
			if(conn==null||conn.isClosed()){
				conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+dbName+"",username,password);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	/**
	 * ��ӡ��޸ġ�ɾ������
	 * @param sql ���
	 * @param objs ����
	 * @return Ӱ������
	 */
	public int update(String sql,Object[] objs){
		int result = 0;
		try {
			ps = getConn().prepareStatement(sql);
			for(int i=0;i<objs.length;i++){
				ps.setObject(i+1, objs[i]);
			}
			result = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			close();
		}
		return result;
	}
	
	/**
	 * ��ӡ��޸ġ�ɾ������
	 * @param sql ���
	 * @param objs ����
	 * @return ����
	 */
	public int updateReturnId(String sql,Object[] objs){
		int id = 0;
		try {
			ps = getConn().prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			for(int i=0;i<objs.length;i++){
				ps.setObject(i+1, objs[i]);
			}
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			if(rs.next()){
				id = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			close();
		}
		return id;
	}
	public String[] updateReturnArrayByRCP(String sql,Object[] objs){
		String[] str = null;
		try {
			ps = getConn().prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			str = new String[objs.length+1];
			for(int i=0;i<objs.length;i++){
				ps.setObject(i+1, objs[i]);
				str[i+1] = objs[i].toString();
			}
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			if(rs.next()){
				str[0] = rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			close();
		}
		return str;
	}
	
	public String[] updateNoIdReturnArrayByRCP(String sql,Object[] objs){
		String[] str = null;
		try {
			ps = getConn().prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			str = new String[objs.length];
			for(int i=0;i<objs.length;i++){
				ps.setObject(i+1, objs[i]);
				str[i] = objs[i].toString();
			}
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			close();
		}
		return str;
	}
	/**
	 * ��������
	 * @param sqls SQL�������
	 * @return true or false
	 */
	public boolean updateForStmtBatch(String[] sqls){
		boolean result = true;
		try {
			getConn().setAutoCommit(false);
			stmt = getConn().createStatement();
			for(String sql : sqls){
				stmt.addBatch(sql);
			}
			int[] results = stmt.executeBatch();
			for(int r : results){
				if(r<=0){
					result = false;
					getConn().rollback();
				}
			}
		} catch (SQLException e) {
			result = false;
			try {
				getConn().rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				getConn().commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			close();
		}
		
		return result;
	}
	
	/**
	 * ��ͬһ��ʽ��SQL������������²�����Ԥ����ʽ��
	 * @param sql SQL���
	 * @param list ����ֵ
	 * @return true or false
	 */
	public boolean updateForPsBatch(String sql,List<Map<Integer,Object>> list){
		//һ��list�ж��map,ÿ��map��Ӧһ���������в���
		//insert into studentinfo (name,classid)values(?,?,?,?)->>һ��Map��Ӧһ��SQL���Ĳ�����key������Ӧ������λ�ã�value������Ӧ������ֵ
		//insert into studentinfo (name,classid)values(?,?,?,?)
		//insert into studentinfo (name,classid)values(?,?,?,?)
		//insert into studentinfo (name,classid)values(?,?,?,?)
		boolean result = true;
		try {
			getConn().setAutoCommit(false);
			ps = getConn().prepareStatement(sql);
			for(Map<Integer,Object> map : list){
				for(Map.Entry<Integer, Object> entry : map.entrySet()){
					ps.setObject(entry.getKey(), entry.getValue());
				}
				ps.addBatch();
			}
			int[] results = ps.executeBatch();
			for(int r:results){
				if(r<=0){
					result = false;
					getConn().rollback();
				}
			}
		} catch (SQLException e) {
			try {
				result = false;
				getConn().rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}//����ع�
			e.printStackTrace();
		} finally{
			try {
				getConn().commit();//�ֶ��ύ����
				close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	
	/**
	 * ��ѯ���,�˷����Ѿ���ʱ
	 * @param sql ���
	 * @param objs ����
	 * @return ResultSet �����
	 */
	@Deprecated
	private ResultSet query(String sql,Object[] objs){
		//"insert into studentinfo (name,classid)values(?,?)"
		//"insert into studentinfo (name,classid)values('����',4)"
		try {
			ps = getConn().prepareStatement(sql);
			for(int i=0;i<objs.length;i++){
				ps.setObject(i+1, objs[i]);
			}
			rs = ps.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	/**
	 * ��һ�еķ���
	 * @param sql
	 * @param objs
	 * @return
	 */
	public Map<String,Object> findForRow(String sql,Object[] objs){
		Map<String,Object> map = null;
		rs = query(sql,objs);
		try{
			if(rs!=null&&rs.next()){
				map = new HashMap<String,Object>();
				ResultSetMetaData rsmd = rs.getMetaData();
				for(int i=1;i<=rsmd.getColumnCount();i++){
					map.put(rsmd.getColumnName(i), rs.getObject(rsmd.getColumnName(i)));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close();
		}
		return map;
	}
	/**
	 * ��һ�еķ���
	 * @param sql
	 * @param objs
	 * @return
	 */
	public String[] findForRCPRow(String sql,Object[] objs){
		String[] str = null;
		rs = query(sql,objs);
		try{
			if(rs!=null&&rs.next()){
				ResultSetMetaData rsmd = rs.getMetaData();
				str = new String[rsmd.getColumnCount()];
				for(int i=0;i<rsmd.getColumnCount();i++){
					str[i]=rs.getString(rsmd.getColumnName(i+1));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close();
		}
		return str;
	}
	/**
	 * ��ѯ����
	 * @param sql ���
	 * @param objs ����
	 * @return List<Map<String,Object>> �����
	 */
	public List<Map<String,Object>> findAll(String sql,Object[] objs){
		//"insert into studentinfo (name,classid)values(?,?)"
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		rs = query(sql,objs);
		try {
			while(rs.next()){
				Map<String,Object> map = new HashMap<String,Object>();
				//��ȡ������е��ֶ���
				ResultSetMetaData rsmd = rs.getMetaData();
				for(int i=1;i<=rsmd.getColumnCount();i++){
					map.put(rsmd.getColumnName(i), rs.getObject(rsmd.getColumnName(i)));
				}
				list.add(map);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return list;
	}
	
	public List<String[]> findByRCPPage(String sql,Object[] objs){
		List<String[]> list = new ArrayList<String[]>();
		rs = query(sql,objs);
		try {
			while(rs.next()){
				//��ȡ������е��ֶ���
				ResultSetMetaData rsmd = rs.getMetaData();
				String[] result = new String[rsmd.getColumnCount()];
				for(int i=1;i<=rsmd.getColumnCount();i++){
					result[i-1]=rs.getString(rsmd.getColumnName(i));
				}
				list.add(result);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return list;
	}
	
	/**
	 * RCPר�ý����
	 * @param sql
	 * @param objs
	 * @return List<String[]>
	 */
	public List<String[]> findAllByRCP(String sql,Object[] objs){
		List<String[]> list = new ArrayList<String[]>();
		rs = query(sql,objs);
		try{
			while(rs.next()){
				int columnCount = rs.getMetaData().getColumnCount();
				String[] row = new String[columnCount];
				for(int i=1;i<=columnCount;i++){
					row[i-1] = rs.getString(i);
				}
				list.add(row);
			}
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			close();
		}
		return list;
	}
	
	/**
	 * ��ҳ��ѯ���������أ�,Ĭ��ÿҳ10��
	 * @param sql ���
	 * @param page ҳ��
	 * @return List<Map<String,Object>> �����
	 */
	public List<Map<String,Object>> findByPage(String sql,int page){
		return findAll("select temp.* from ( " + sql +" ) temp "+getWhere(null)+ getOrderBy(null)+" limit "+(page-1)*pageRecord+","+pageRecord,new Object[]{});
	}
	
	public List<String[]> findPageByRCP(String sql,int page){
		return findByRCPPage("select temp.* from ( " + sql +" ) temp "+getWhere(null)+ getOrderBy(null)+" limit "+(page-1)*pageRecord+","+pageRecord,new Object[]{});
	}
	/**
	 * ��ҳ��ѯ���������أ�,Ĭ��ÿҳ10��
	 * @param sql ���
	 * @param objs ����
	 * @param page ҳ��
	 * @return List<Map<String,Object>> �����
	 */
	public List<Map<String,Object>> findByPage(String sql,Object[] objs,int page){
		return findAll("select temp.* from ( " + sql +" ) temp "+getWhere(null)+ getOrderBy(null)+" limit "+(page-1)*pageRecord+","+pageRecord,objs);
	}
	/**
	 * ��ҳ��ѯ���������أ�,Ĭ��ÿҳ10��
	 * @param sql ���
	 * @param objs ����
	 * @param page ҳ��
	 * @param where ��ѯ����
	 * @return List<Map<String,Object>> �����
	 */
	public List<Map<String,Object>> findByPage(String sql,Object[] objs,int page,String where){
		return findAll("select temp.* from ( " + sql +" ) temp "+getWhere(where)+ getOrderBy(null)+" limit "+(page-1)*pageRecord+","+pageRecord,objs);
	}
	/**
	 * ��ҳ��ѯ���������أ�
	 * @param sql ���
	 * @param page ҳ��
	 * @param pageRecord ÿҳ��¼��
	 * @return List<Map<String,Object>> �����
	 */
	public List<Map<String,Object>> findByPage(String sql,int page,int pageRecord){
		return findAll("select temp.* from ( " + sql +" ) temp "+getWhere(null)+ getOrderBy(null)+" limit "+(page-1)*pageRecord+","+pageRecord,new Object[]{});
	}
	/**
	 * ��ҳ��ѯ���������أ�
	 * @param sql ���
	 * @param objs ����
	 * @param page ҳ��
	 * @param pageRecord ÿҳ��¼��
	 * @return List<Map<String,Object>> �����
	 */
	public List<Map<String,Object>> findByPage(String sql,Object[] objs,int page,int pageRecord){
		return findAll("select temp.* from ( " + sql +" ) temp "+getWhere(null)+ getOrderBy(null)+" limit "+(page-1)*pageRecord+","+pageRecord,objs);
	}
	/**
	 * ��ҳ��ѯ���������أ�
	 * @param sql ���
	 * @param objs ����
	 * @param page ҳ��
	 * @param pageRecord ÿҳ��¼��
	 * @param where ��ѯ����
	 * @return List<Map<String,Object>> �����
	 */
	public List<Map<String,Object>> findByPage(String sql,Object[] objs,int page,int pageRecord,String where){
		return findAll("select temp.* from ( " + sql +" ) temp "+getWhere(where)+ getOrderBy(null)+" limit "+(page-1)*pageRecord+","+pageRecord,objs);
	}
	/**
	 * ��ҳ��ѯ����
	 * @param sql ���
	 * @param objs ����
	 * @param page ҳ��
	 * @param pageRecord ÿҳ��¼��
	 * @param where ��ѯ����
	 * @param map ��������
	 * @return List<Map<String,Object>> �����
	 */
	public List<Map<String,Object>> findByPage(String sql,Object[] objs,int page,int pageRecord,String where,Map<String,String> map){
		return findAll("select temp.* from ( " + sql +" ) temp "+getWhere(where)+ getOrderBy(map)+" limit "+(page-1)*pageRecord+","+pageRecord,objs);
	}
	
	/**
	 * ��ȡ�ܼ�¼��
	 */
	public int count(String sql,Object[] objs){
		String countSQL = "select count(*) from ( "+sql+" ) temps";
		System.out.println("countSQL:"+countSQL);
		rs = query(countSQL,objs);
		int res=0;
		try{
			while(rs.next()){
				res = rs.getInt(1);
			}
		}catch(Exception e){
			
		}finally{
			close();
		}
		return res;
	}
	
	/**
	 * ƴ��where����
	 * @param where Ҫƴ�ӵ�where����
	 * @return String �µ�where����
	 */
	private String getWhere(String where){
		if(null==where){
			return "";
		}else{
			int index = where.indexOf("where");
			if(index>=0){
				return " " +where + " ";
			}else{
				return " where " + where +" ";
			}
		}
	}
	/**
	 * ƴ����������
	 * @param map Ҫƴ�ӵ���������
	 * @return String ƴ�Ӻ����������
	 */
	private String getOrderBy(Map<String,String> map){
		if(map!=null&&!map.isEmpty()){
			StringBuffer sb = new StringBuffer(" order by ");
			for(Map.Entry<String, String> entry:map.entrySet()){
				sb.append(entry.getKey()).append(" ").append(entry.getValue()).append(",");
			}
			return sb.deleteCharAt(sb.length()).toString() + " ";
		}else{
			return "";
		}
	}
	
	/**
	 * �ͷ���Դ
	 */
	private void close(){
		try {
			if(rs!=null&&!rs.isClosed()){
				rs.close();
			}
			if(ps!=null&&!ps.isClosed()){
				ps.close();
			}
			if(stmt!=null&&!stmt.isClosed()){
				stmt.close();
			}
			if(conn!=null&&!conn.isClosed()){
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int getSumPage(String sql, Object[] objs, int i) {
		int sum = this.count(sql, objs);
		return (sum%i==0)?(sum/i):(sum/i+1);
	}
}
