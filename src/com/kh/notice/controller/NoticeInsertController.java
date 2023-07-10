package com.kh.notice.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.notice.model.service.NoticeService;
import com.kh.notice.model.vo.Notice;

/**
 * Servlet implementation class NoticeInsertController
 */
@WebServlet("/insert.no")
public class NoticeInsertController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NoticeInsertController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.getRequestDispatcher("views/notice/noticeEnrollForm.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		
		// input type hidden으로 가져올 경우 F12로 확인 가능
		String userNo = request.getParameter("userNo");
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		
		Notice n = new Notice();
		n.setNoticeTitle(title);
		n.setNoticeContent(content);
		n.setNoticeWriter(userNo);
		
		int result = new NoticeService().insertNotice(n);
		
		if(result > 0) { // 성공 시 =>/jsp2/list.no 리스트 페이지가 보여지도록 url재요청
			
			request.getSession().setAttribute("alertMsg", "공지사항이 성공적으로 등록되었습니다.");
			response.sendRedirect(request.getContextPath() + "/list.no");
			
		} else { // 실패 시 => 
			
			request.setAttribute("errorMsg", "공지사항 등록 실패!");
			request.getRequestDispatcher("views/common/errorPage.jsp").forward(request, response);
			
			
		}
		
		
		
	}

}
