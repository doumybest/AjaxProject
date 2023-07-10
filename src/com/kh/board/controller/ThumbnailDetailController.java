package com.kh.board.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.board.model.service.BoardService;
import com.kh.board.model.vo.Attachment;
import com.kh.board.model.vo.Board;

/**
 * Servlet implementation class ThumbnailDetailController
 */
@WebServlet("/detail.th")
public class ThumbnailDetailController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ThumbnailDetailController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int boardNo = Integer.parseInt(request.getParameter("bno"));
		
		// 일반게시판에서 작성한 조회수 증가 메소드 실행
		int result = new BoardService().increaseCount(boardNo);
		
		if(result > 0) { // 조회수증가 성공 => 게시글 정보, 첨부파일들의 정보를 조회해서 상세조회화면으로 이동
			Board b = new BoardService().selectBoard(boardNo);
			ArrayList<Attachment> list = new BoardService().selectAttachmentList(boardNo);
			
			request.setAttribute("b",b);
			request.setAttribute("list", list);
			
			request.getRequestDispatcher("views/board/thumbnailDetailView.jsp").forward(request, response);
		} else {
			request.setAttribute("errorMsg", "사진게시글 조회 실패");
			request.getRequestDispatcher("views/common/errorPage.jsp").forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
