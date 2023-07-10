package com.kh.board.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.board.model.service.BoardService;
import com.kh.board.model.vo.Board;
import com.kh.common.model.vo.PageInfo;

/**
 * Servlet implementation class BoardListController
 */
@WebServlet("/list.bo")
public class BoardListController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BoardListController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//--------------------------- 페이징 처리 시 필요한 객체 -----------------------------------
		int listCount; // 현재 총 게시글 갯수(1000개)
		int currentPage; // 현재 페이지(즉, 사용자가 요청한 페이지)
		int pageLimit; // 페이지 하단에 보여질 최대 페이지 갯수(10개씩 예정)
		int boardLimit; // 한 페이지에 보여질 게시글의 최대 갯수(10개씩 예정)
		
		int maxPage; // 가장 마지막 페이지가 몇 번째 페이지인지(총 페이지 갯수)
		int startPage; // 페이지 하단에 보여질 페이징바의 시작수
		int endPage; // 페이지 하단에 보여질 페이징바의 마지막수
		
		// * listCount : 총 게시글 갯수
		listCount = new BoardService().selectListCount();
		
		// * currentPage : 현재 페이지(즉, 사용자가 요청한 페이지)
		currentPage = Integer.parseInt(request.getParameter("currentPage") == null ? "1" : request.getParameter("currentPage"));
		
		// * pageLimit : 페이지 하단에 보여질 최대 페이지 갯수(페이지 목록들을 몇 개 단위로 출력할것인지)
		pageLimit = 10;
		
		// * boardLimit : 한 페이지에 보여질 게시글의 최대 갯수(게시글 몇 개 단위로 출력할지)
		boardLimit = 10;
		
		// * maxPage : 가장 마지막 페이지가 몇 번째 페이지인지(총 페이지 수)
		/*
		 * listCount, boardLimit에 영향을 받음
		 * 
		 * - 공식 구하기(boardLimit가 10이라는 가정하에 규칙을 세움)
		 * 	 
		 * 			총 갯수				boardLimit				maxPage
		 * 			1000개					10					  100
		 * 			1001개					10					  101
		 * 			1005개					10					  101
		 * 			1010개					10					  101
		 * 			1011개					10					  102
		 * 
		 * 		=> 나숫셈 연산한 결과를 올림처리하면 maxPage가 된다
		 * 
		 * 		1) listCount를 double자료형으로 형변환
		 * 		2) listCount / boardList 실행
		 * 		3) 결과값에 올림처리해주는 Math.ceil() 메서드 호출
		 * 		4) 결과값을 int로 강제형변환 
		 */
		maxPage = (int)Math.ceil((double)listCount/boardLimit);
		
		// * startPage : 페이지 하단에 보여질 페이징바의 시작 수
		/*
		 * pageLimit, currentPage에 영향을 받음
		 * 
		 * - 공식 구하기(pageLimit가 10이라는 가정하에 규칙을 세움)
		 * 
		 * startPage : 1, 11, 21, 31, 41, 51 .. => n * 10 + 1 => 10의 배수 + 1
		 * 
		 * 즉, n * pageLimit + 1
		 * 
		 * 			currentPage			startPage
		 * 				1					1			=>		n*pageLimit + 1 => n = 0
		 * 				10					1			=>		n*pageLimit + 1 => n = 0
		 * 				11					11			=>		n*pageLimit + 1 => n = 1
		 * 				20					11			=>		n*pageLimit + 1 => n = 1
		 * 				21					21			=>		n*pageLimit + 1 => n = 2				
		 * 
		 * 		=>  1 ~ 10 : n = 0
		 * 		=> 11 ~ 20 : n = 1
		 * 		=> 21 ~ 30 : n = 2
		 * 		...			 n = currentPage - 1 / pageLimit 
		 * 					   => 0 ~ 9 / 10 => 0
		 * 					   => 10 ~ 19 / 10 => 1
		 * 					   => 20 ~ 29 / 10 => 2
		 * 		startPage = n * pageLimit + 1
		 * 				  => (currentPage - 1) / pageLimit * pageLimit + 1
		 */
		startPage = (currentPage - 1) / pageLimit * pageLimit + 1;
		
		// * endPage : 페이지 하단에 보여질 페이징바의 끝 수
		/*
		 * startPage, pageLimit에 영향을 받음 + maxPage에도 영향을 받음
		 * 
		 *  - 공식구하기
		 *  	
		 *  	startPage : 1 => endPage : 10
		 *  	startPage : 11 => endPage : 20
		 *  	startPage : 21 => endPage : 30
		 *  
		 *  	endPage = startPage + pageLimit - 1;
		 */
		endPage = startPage + pageLimit - 1;
		// startPage 21이어서 endPage가 30으로 설정이 됐지만, maxPage가 고장 25페이지까지만 나온다면
		// => endPage는 30이 아니라 25로 설정해야함
		if(endPage > maxPage) {
			endPage = maxPage;
		}
		
		// 페이지 정보들을 하나로 모아서 vo클래스에 담기
		// 1. 페이징바를 만들 때 필요한 객체
		PageInfo pi = new PageInfo(listCount, currentPage, pageLimit, boardLimit, maxPage, startPage, endPage);
		
		request.setAttribute("pi", pi);

		// 2. 현재 사용자가 요청한 페이지(currentPage)에 맞는 게시글 리스트 가져오기
		ArrayList<Board> list = new BoardService().selectList(pi);
		
		request.setAttribute("list", list);
		
		request.getRequestDispatcher("views/board/boardListView.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
