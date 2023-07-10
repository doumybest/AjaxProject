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
import com.kh.board.model.vo.Category;
import com.kh.member.model.vo.Member;
import com.kh.notice.model.service.NoticeService;
import com.kh.notice.model.vo.Notice;

/**
 * Servlet implementation class BoardDeleteController
 */
@WebServlet("/delete.bo")
public class BoardDeleteController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BoardDeleteController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		BoardService bService = new BoardService();
		
		int boardNo = Integer.parseInt(request.getParameter("bno"));
		
		Board b = bService.selectBoard(boardNo);
		
		Attachment at = bService.selectAttachment(boardNo);
		
		request.setAttribute("b", b);
		request.setAttribute("at", at);
		
		
		int result = bService.deleteBoard(b, at);
		
		
		if(result > 0) {
			
			request.getSession().setAttribute("alertMsg", "성공적으로 게시글이 삭제되었습니다.");
			response.sendRedirect(request.getContextPath()+"/list.bo");
			
		} else {
			request.getRequestDispatcher("views/common/errorPage.jsp").forward(request, response);
		}
		
		/*
		 * 	이 외에도 여러가지 방법을 사용할 수 있음
		 * 	
		 * 	위에서 굳이 보드객체는 통으로 넘겨줄 필요는 없고 -> boardNo만 넘겨줘도 됨
		 * 
		 */
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
