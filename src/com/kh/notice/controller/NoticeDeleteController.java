package com.kh.notice.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.member.model.vo.Member;
import com.kh.notice.model.service.NoticeService;
import com.kh.notice.model.vo.Notice;

/**
 * Servlet implementation class NoticeDeleteController
 */
@WebServlet("/delete.no")
public class NoticeDeleteController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NoticeDeleteController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String userNo = ((Member)request.getSession().getAttribute("loginUser")).getUserNo() + "";
		int nno = Integer.parseInt(request.getParameter("nno"));
		
		Notice n = new Notice();
		n.setNoticeWriter(userNo);
		n.setNoticeNo(nno);
		
		int result = new NoticeService().deleteNotice(n);
		
		if(result > 0) {
			
			// 삭제 성공 시
			request.getSession().setAttribute("alertMsg", "성공적으로 공지사항이 삭제되었습니다.");
			// 성공적으로 공지사항이 삭제되었습니다. => 리스트 페이지로 url 재요청
			response.sendRedirect(request.getContextPath());
			
		} else {
			
			// 삭제 실패 시
			// 공지사항 삭제 실패 => 에러페이지로 포워딩
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
