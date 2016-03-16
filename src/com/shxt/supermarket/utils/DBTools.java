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
	 * 默认每页记录数
	 */
	public static int pageRecord = 10;
	/**
	 * 数据名称
	 */
	private String dbName = null;
	/**
	 * 数据库帐号
	 */
	private String username = null;
	/**
	 * 数组库密码
	 */
	private String password = null;
	
	/**
	 * DBTools实例对象
	 */
	private static DBTools db = null;
	/**
	 * 单例模式
	 * @param dbName 数据库名
	 * @param username 数据库帐号
	 * @param password 数据库密码
	 * @return DBTools
	 */
	public static DBTools getDBTools(String dbName,String username,String password){
		if(db==null){
			db = new DBTools(dbName,username,password);
		}
		return db;
	}
	
	/**
	 * 构造方法
	 * @param dbName 数据库名
	 * @param username 数据库帐号
	 * @param password 数据库密码
	 */
	private DBTools(String dbName,String username,String password){
		this.dbName = dbName;
		this.username = username;
		this.password = password;
	}
	
	/**
	 * 连接
	 */
	private Connection conn = null;
	/**
	 * 语句对象
	 */
	private Statement stmt = null;
	/**
	 * 预处理语句对象
	 */
	private PreparedStatement ps = null;
	/**
	 * 结果集
	 */
	private ResultSet rs = null;
	/**
	 * 静态代码块
	 */
	static {
		try {
			//加载数据库驱动
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取数据库连接
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
	 * 添加、修改、删除方法
	 * @param sql 语句
	 * @param objs 参数
	 * @return 影响行数
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
	 * 添加、修改、删除方法
	 * @param sql 语句
	 * @param objs 参数
	 * @return 主键
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
	 * 批量更新
	 * @param sqls SQL语句数组
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
	 * 对同一格式的SQL语句做批量更新操作（预处理方式）
	 * @param sql SQL语句
	 * @param list 参数值
	 * @return true or false
	 */
	public boolean updateForPsBatch(String sql,List<Map<Integer,Object>> list){
		//一个list中多个map,每个map对应一条语句的所有参数
		//insert into studentinfo (name,classid)values(?,?,?,?)->>一个Map对应一条SQL语句的参数，key用来对应参数的位置，value用来对应参数的值
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
			}//事务回滚
			e.printStackTrace();
		} finally{
			try {
				getConn().commit();//手动提交事务
				close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	
	/**
	 * 查询结果,此方法已经过时
	 * @param sql 语句
	 * @param objs 参数
	 * @return ResultSet 结果集
	 */
	@Deprecated
	private ResultSet query(String sql,Object[] objs){
		//"insert into studentinfo (name,classid)values(?,?)"
		//"insert into studentinfo (name,classid)values('张三',4)"
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
	 * 查一行的方法
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
	 * 查一行的方法
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
	 * 查询方法
	 * @param sql 语句
	 * @param objs 参数
	 * @return List<Map<String,Object>> 结果集
	 */
	public List<Map<String,Object>> findAll(String sql,Object[] objs){
		//"insert into studentinfo (name,classid)values(?,?)"
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		rs = query(sql,objs);
		try {
			while(rs.next()){
				Map<String,Object> map = new HashMap<String,Object>();
				//获取结果集中的字段名
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
				//获取结果集中的字段名
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
	 * RCP专用结果集
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
	 * 分页查询方法（重载）,默认每页10条
	 * @param sql 语句
	 * @param page 页数
	 * @return List<Map<String,Object>> 结果集
	 */
	public List<Map<String,Object>> findByPage(String sql,int page){
		return findAll("select temp.* from ( " + sql +" ) temp "+getWhere(null)+ getOrderBy(null)+" limit "+(page-1)*pageRecord+","+pageRecord,new Object[]{});
	}
	
	public List<String[]> findPageByRCP(String sql,int page){
		return findByRCPPage("select temp.* from ( " + sql +" ) temp "+getWhere(null)+ getOrderBy(null)+" limit "+(page-1)*pageRecord+","+pageRecord,new Object[]{});
	}
	/**
	 * 分页查询方法（重载）,默认每页10条
	 * @param sql 语句
	 * @param objs 参数
	 * @param page 页数
	 * @return List<Map<String,Object>> 结果集
	 */
	public List<Map<String,Object>> findByPage(String sql,Object[] objs,int page){
		return findAll("select temp.* from ( " + sql +" ) temp "+getWhere(null)+ getOrderBy(null)+" limit "+(page-1)*pageRecord+","+pageRecord,objs);
	}
	/**
	 * 分页查询方法（重载）,默认每页10条
	 * @param sql 语句
	 * @param objs 参数
	 * @param page 页数
	 * @param where 查询条件
	 * @return List<Map<String,Object>> 结果集
	 */
	public List<Map<String,Object>> findByPage(String sql,Object[] objs,int page,String where){
		return findAll("select temp.* from ( " + sql +" ) temp "+getWhere(where)+ getOrderBy(null)+" limit "+(page-1)*pageRecord+","+pageRecord,objs);
	}
	/**
	 * 分页查询方法（重载）
	 * @param sql 语句
	 * @param page 页数
	 * @param pageRecord 每页记录数
	 * @return List<Map<String,Object>> 结果集
	 */
	public List<Map<String,Object>> findByPage(String sql,int page,int pageRecord){
		return findAll("select temp.* from ( " + sql +" ) temp "+getWhere(null)+ getOrderBy(null)+" limit "+(page-1)*pageRecord+","+pageRecord,new Object[]{});
	}
	/**
	 * 分页查询方法（重载）
	 * @param sql 语句
	 * @param objs 参数
	 * @param page 页数
	 * @param pageRecord 每页记录数
	 * @return List<Map<String,Object>> 结果集
	 */
	public List<Map<String,Object>> findByPage(String sql,Object[] objs,int page,int pageRecord){
		return findAll("select temp.* from ( " + sql +" ) temp "+getWhere(null)+ getOrderBy(null)+" limit "+(page-1)*pageRecord+","+pageRecord,objs);
	}
	/**
	 * 分页查询方法（重载）
	 * @param sql 语句
	 * @param objs 参数
	 * @param page 页数
	 * @param pageRecord 每页记录数
	 * @param where 查询条件
	 * @return List<Map<String,Object>> 结果集
	 */
	public List<Map<String,Object>> findByPage(String sql,Object[] objs,int page,int pageRecord,String where){
		return findAll("select temp.* from ( " + sql +" ) temp "+getWhere(where)+ getOrderBy(null)+" limit "+(page-1)*pageRecord+","+pageRecord,objs);
	}
	/**
	 * 分页查询方法
	 * @param sql 语句
	 * @param objs 参数
	 * @param page 页数
	 * @param pageRecord 每页记录数
	 * @param where 查询条件
	 * @param map 排序条件
	 * @return List<Map<String,Object>> 结果集
	 */
	public List<Map<String,Object>> findByPage(String sql,Object[] objs,int page,int pageRecord,String where,Map<String,String> map){
		return findAll("select temp.* from ( " + sql +" ) temp "+getWhere(where)+ getOrderBy(map)+" limit "+(page-1)*pageRecord+","+pageRecord,objs);
	}
	
	/**
	 * 获取总记录数
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
	 * 拼接where条件
	 * @param where 要拼接的where条件
	 * @return String 新的where条件
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
	 * 拼接排序条件
	 * @param map 要拼接的排序条件
	 * @return String 拼接后的排序条件
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
	 * 释放资源
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
