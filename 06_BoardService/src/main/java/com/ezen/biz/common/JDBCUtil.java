package com.ezen.biz.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class JDBCUtil {
	
	//오라클 연동
	public static Connection getConnection() {
	
		Connection conn = null;
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String uid = "spring_user";
		String pass = "1234";
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(url, uid, pass);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

	//오라클 db 연결 해지 - insert, update, delete 구문에 대해 사용
	public static void close(Connection conn, Statement stmt) {
		try {
			if(stmt != null) stmt.close();
			if(conn != null) conn.close();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//오라클 연결 해지 - 조회 구문에 대해 사용
	public static void close(Connection conn, Statement stmt, ResultSet rs) {
		try {
			if(rs != null) rs.close();
			if(stmt != null) stmt.close();
			if(conn != null) conn.close();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
			
	}

}
