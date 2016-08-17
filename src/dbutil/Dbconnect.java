package dbutil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Driver;

public class Dbconnect {


	public static void main(String[] args) {	
		insertDbData();
        }	
	public static void	insertDbData()
	{
		int sum=0;
              		int num=executeUpdate("insert into user(uid,uname,phone)values(?,?,?)",
              								new String[]{"12","yuanyun","13800138000"});
              			sum+=num;
              			System.out.println("已经完成纪录"+sum);
	}
	
	public  static Connection GetConnection()
	{
		Connection conn=null;
		try {
			Driver jdbcDriver=new Driver();
			DriverManager.registerDriver(jdbcDriver);
			String dbUrl= "jdbc:mysql://rdsf3o146rtp02xbi1x7o.mysql.rds.aliyuncs.com:3306/yuanyunfortest?characterEncoding=utf-8";//采用UTF-8编码方式读写数据库
			String dbUser="yuanyun";
			String dbPwd="Qm569282yy";
			conn=(Connection) DriverManager.getConnection(dbUrl, dbUser, dbPwd);
			} 
			catch (SQLException e) {
			System.out.println("连接服务器失败");
			e.printStackTrace();
		}
		return conn;
	}
	/**
	 * 关闭数据库连接，释放资源
	 * @param resultSet
	 * @param statement
	 * @param connection
	 */
	public static void close(ResultSet resultSet, PreparedStatement statement, Connection connection){
		
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 关闭数据库连接
	 * 直接关闭connection
	 */
	public static void close(){
		close(null, null, GetConnection());
	}
	public static int count(String sql,String[] params)
	{
		int num=-1;
		ResultSet resultSet=executeQuery(sql, params);
		try {
			while (resultSet.next()) {
				num=resultSet.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			close();
		}
		return num;
	}
	public static ResultSet executeQuery(String sql,String[] params)
	{
		Connection conn=GetConnection();
		PreparedStatement preparedStatement=null;
		ResultSet resultSet=null;
		try {
			preparedStatement=conn.prepareStatement(sql);
			if(params!=null)
			{
				for (int i = 0; i < params.length; i++) {
					preparedStatement.setString(i+1, params[i]);
				}	
			}
			resultSet=preparedStatement.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally
		{
			
		}
		return resultSet;
	}
	public static int executeUpdate(String sql, String[] params)
	{
		Connection conn = GetConnection();
		PreparedStatement preparedStatement = null;
		int result = -1;
		try {
			preparedStatement=conn.prepareStatement(sql);
			if(params!=null)
			{
				for (int i = 0; i < params.length; i++) {
					preparedStatement.setString(i+1,params[i]);
				}
				result=preparedStatement.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			close(null, preparedStatement, conn);
		}
		return result;
		
	}
}
