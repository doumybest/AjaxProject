package com.kh.member.model.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import static com.kh.common.JDBCTemplate.*;

import com.kh.member.model.vo.Member;

public class MemberDao {

	private Properties prop = new Properties();

	public MemberDao() {

		String fileName = MemberDao.class.getResource("/sql/member/member-mapper.xml").getPath();

		try {
			prop.loadFromXML(new FileInputStream(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Member loginMember(Connection conn, String userId, String userPwd) {

		// Select문 실행예정 => ResultSet객체에 담아줘야함
		Member m = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;

		String sql = prop.getProperty("loginMember");

		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, userId);
			pstmt.setString(2, userPwd);

			rset = pstmt.executeQuery();

			if (rset.next()) {
				m = new Member(rset.getInt("USER_NO"), rset.getString("USER_ID"), rset.getString("USER_PWD"),
						rset.getString("USER_NAME"), rset.getString("PHONE"), rset.getString("EMAIL"),
						rset.getString("ADDRESS"), rset.getString("INTEREST"), rset.getDate("ENROLL_DATE"),
						rset.getDate("MODIFY_DATE"), rset.getString("STATUS"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close(rset);
			close(pstmt);
		}
		return m;
	}

	public int insertMember(Connection conn, Member m) {

		// insert문 => 처리된 행의 갯수를 반환
		int result = 0;

		PreparedStatement pstmt = null;

		String sql = prop.getProperty("insertMember");

		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, m.getUserId());
			pstmt.setString(2, m.getUserPwd());
			pstmt.setString(3, m.getUserName());
			pstmt.setString(4, m.getPhone());
			pstmt.setString(5, m.getEmail());
			pstmt.setString(6, m.getAddress());
			pstmt.setString(7, m.getInterest());

			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
		return result;
	}

	public int updateMember(Connection conn, Member m) {

		// UPDATE => 처리된 행의 갯수
		int result = 0;

		PreparedStatement pstmt = null;

		String sql = prop.getProperty("updateMember");

		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, m.getUserName());
			pstmt.setString(2, m.getPhone());
			pstmt.setString(3, m.getEmail());
			pstmt.setString(4, m.getAddress());
			pstmt.setString(5, m.getInterest());
			pstmt.setString(6, m.getUserId());

			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
		return result;
	}

	public Member selectMember(Connection conn, String userId) {

		// SELECT문 -> ResultSet객체 => 조회된 행의 결과가 1행이거나 0행
		Member m = null;

		PreparedStatement pstmt = null;

		ResultSet rset = null;

		String sql = prop.getProperty("selectMember");

		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, userId);

			rset = pstmt.executeQuery();

			if (rset.next()) {
				m = new Member(rset.getInt("USER_NO"), rset.getString("USER_ID"), rset.getString("USER_PWD"),
						rset.getString("USER_NAME"), rset.getString("PHONE"), rset.getString("EMAIL"),
						rset.getString("ADDRESS"), rset.getString("INTEREST"), rset.getDate("ENROLL_DATE"),
						rset.getDate("MODIFY_DATE"), rset.getString("STATUS"));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close(rset);
			close(pstmt);
		}
		return m;
	}

	public int updatePwdMember(Connection conn, Member m) {

		int result = 0;

		PreparedStatement pstmt = null;

		String sql = prop.getProperty("updatePwdMember");

		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, m.getUserPwd());
			pstmt.setString(2, m.getUserId());

			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
		return result;
	}

	public int deleteMember(Connection conn, String userId, String userPwd) {

		int result = 0;

		PreparedStatement pstmt = null;

		String sql = prop.getProperty("deleteMember");

		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, userPwd);
			pstmt.setString(2, userId);

			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
		return result;
	}

	public Member deleteSelectMember(Connection conn, String userId) {
		// SELECT문 -> ResultSet객체 => 조회된 행의 결과가 1행이거나 0행
		Member m = null;

		PreparedStatement pstmt = null;

		ResultSet rset = null;

		String sql = prop.getProperty("deleteSelectMember");

		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, userId);

			rset = pstmt.executeQuery();

			if (rset.next()) {
				m = new Member(rset.getInt("USER_NO"), rset.getString("USER_ID"), rset.getString("USER_PWD"),
						rset.getString("USER_NAME"), rset.getString("PHONE"), rset.getString("EMAIL"),
						rset.getString("ADDRESS"), rset.getString("INTEREST"), rset.getDate("ENROLL_DATE"),
						rset.getDate("MODIFY_DATE"), rset.getString("STATUS"));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close(rset);
			close(pstmt);
		}
		return m;

	}

}
