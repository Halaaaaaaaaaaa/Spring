package com.ezen.biz.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.springframework.stereotype.Repository;

import com.ezen.biz.common.JDBCUtil;
import com.ezen.biz.dto.UserVO;

@Repository
public class UserDAO {
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	private static final String GET_USER =
			"SELECT * FROM users WHERE id=? AND password=?";
	
	// 사용자 조회
	public UserVO getUser(UserVO vo) {
		System.out.println("===> JDBC로 getUser() 처리");
		UserVO result = null;
		
		try {
			// (1) 데이터베이스 연결
			conn = JDBCUtil.getConnection();
			
			// (2) SQL 구문 준비
			pstmt = conn.prepareStatement(GET_USER);
			pstmt.setString(1, vo.getId());
			pstmt.setString(2, vo.getPassword());
			
			// (3) SQL문 실행
			rs = pstmt.executeQuery();
			
			if (rs.next()) { // 조회된 결과가 있으면 UserVO 객체에 저장한다.
				result = new UserVO();
				result.setId(rs.getString("id"));
				result.setPassword(rs.getString("password"));
				result.setName(rs.getString("name"));
				result.setRole(rs.getString("role"));
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			// (4) 데이터베이스 연결 해지
			JDBCUtil.close(conn, pstmt, rs);
		}
		
		return result;
	}
}
