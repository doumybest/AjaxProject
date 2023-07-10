package com.kh.board.model.service;

import static com.kh.common.JDBCTemplate.close;
import static com.kh.common.JDBCTemplate.commit;
import static com.kh.common.JDBCTemplate.getConnection;
import static com.kh.common.JDBCTemplate.rollback;

import java.sql.Connection;
import java.util.ArrayList;

import com.kh.board.model.dao.BoardDao;
import com.kh.board.model.vo.Attachment;
import com.kh.board.model.vo.Board;
import com.kh.board.model.vo.Category;
import com.kh.common.model.vo.PageInfo;

public class BoardService {

	public int selectListCount() {
		
		Connection conn = getConnection();
		
		int listCount = new BoardDao().selectListCount(conn);
		
		close(conn);
		
		return listCount;
	}
	
	public ArrayList<Board> selectList(PageInfo pi) {
		
		Connection conn = getConnection();
		
		ArrayList<Board> list = new BoardDao().selectList(conn, pi);
		
		close(conn);
		
		return list;
	}
	
	public int increaseCount(int boardNo) {
		
		Connection conn = getConnection();
		
		int result = new BoardDao().increaseCount(conn, boardNo);
		
		if(result > 0) {
			commit(conn);
		} else {
			rollback(conn);
		}
		close(conn);
		
		return result;
	}
	
	public Board selectBoard(int boardNo) {
		
		Connection conn = getConnection();
		
		Board b = new BoardDao().selectBoard(conn, boardNo);
		
		close(conn);
		
		return b;
	}
	
	public Attachment selectAttachment(int boardNo) {
		
		Connection conn = getConnection();
		
		Attachment at = new BoardDao().selectAttachment(conn, boardNo);
		
		close(conn);
		
		return at;
		
	}
	
	public ArrayList<Category> selectCategoryList(){
		Connection conn = getConnection();
		
		ArrayList<Category> list = new BoardDao().selectCategoryList(conn);
		
		close(conn);
		
		return list;
		
	}
	
	public int insertBoard(Board b, Attachment at) {
		
		Connection conn = getConnection();
		
		int result1 = new BoardDao().insertBoard(conn, b);
		
		int result2 = 1;
		
		if(at != null) {
			result2 = new BoardDao().insertAttachment(conn, at);
		}
		
		// 트랜잭션 처리
		if(result1 > 0 && result2 > 0) {
			commit(conn);
		}else {
			rollback(conn);
		}
		close(conn);
		
		// 둘중 하나라도 실패하여서 0이 반환될 경우 실패값을 반환하기 위해서 곱셈결과를 리턴.
		return result1*result2;
	}
	
	public int updateBoard(Board b, Attachment at) {
		Connection conn = getConnection();
		
		int result1 = new BoardDao().updateBoard(conn, b);
		
		int result2 = 1;
		
		if(at != null) { // 새롭게 등록한 첨부파일이 있는 경우
			
			if(at.getFileNO() != 0) { // fileNo가 있으면 기존 첨부파일이 있는 경우 => Attachment Update
				
				result2 = new BoardDao().updateAttachment(conn, at);
				
			} else { // 기존의 첨부파일이 없는 경우 => Attachment Insert
				
				result2 = new BoardDao().insertNewAttachment(conn, at);
				
			}
			
		}
		
		if(result1 > 0 && result2 > 0) {
			commit(conn);
		}else {
			rollback(conn);
		}
		close(conn);
		
		return result1*result2;
	}
	
	public int deleteBoard(Board b, Attachment at) {
		Connection conn = getConnection();
		
		int result1 = new BoardDao().deleteBoard(conn, b);
		
		int result2 = 1;
		
		if(at != null) {
			
			result2 = new BoardDao().deleteAttachment(conn, at);
			
		}
		
		if(result1 > 0 && result2 > 0) {
			commit(conn);
		}else {
			rollback(conn);
		}
		close(conn);
		
		return result1*result2;
	}
	
	public int insertThumbnailBoard(Board b, ArrayList<Attachment> list) {
		Connection conn = getConnection();
		
		int result1 = new BoardDao().insertThumbnailBoard(conn, b);
		
		// 이번에는 대표이미지는 무조건 올리게 해놔서 if로 없을경우 조건을 세울필요가 없음
		int result2 = new BoardDao().insertAttachmentList(conn, list);
		
		if(result1>0 && result2 >0) {
			commit(conn);
		}else {
			rollback(conn);
		}
		close(conn);
		
		return result1 * result2;
		
	}
	
	public ArrayList<Board> selectThumbnailList(){
		
		Connection conn = getConnection();
		
		ArrayList<Board> list = new BoardDao().selectThumbnailList(conn);
		
		close(conn);
		
		return list;
		
	}
	
	public ArrayList<Attachment> selectAttachmentList(int boardNo){
		
		Connection conn = getConnection();
		
		ArrayList<Attachment> list = new BoardDao().selectAttachmentList(conn, boardNo);
		
		close(conn);
		
		return list;
	}
	
}
