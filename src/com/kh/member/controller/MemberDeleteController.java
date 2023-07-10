package com.kh.member.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kh.member.model.service.MemberService;
import com.kh.member.model.vo.Member;

/**
 * Servlet implementation class MemberDeleteController
 */
@WebServlet("/delete.me")
public class MemberDeleteController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MemberDeleteController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		
		// 삭제 처리 시 delete문이 아닌 update로 진행
		String userPwd = request.getParameter("userPwd");

		HttpSession session = request.getSession();
		
		Member loginUser = (Member)session.getAttribute("loginUser");
		
		String userId = loginUser.getUserId();
		
		int result = new MemberService().deleteMember(userId, userPwd);
		
		if(result > 0) {
			
			// 메세지 내용 : 성공적으로 회원 탈퇴되었습니다. 그동안 이용해주셔서 감사합니다.
			session.setAttribute("alertMsg", "성공적으로 회원 탈퇴되었습니다. 그동안 이용해주셔서 감사합니다.");
			
			// invalidate() 메서드를 사용 시 세션이 만료되어서 alert가 보이지 않음
			// 따라서 removeAttribute를 이용하여 현재 로그안한 사용자의 정보를 지워주는 방식으로 로그아웃 시키기
			// session에 저장된 회원정보 제거
			session.removeAttribute("loginUser");
			// 성공시 => 메인페이지 url재요청
			response.sendRedirect(request.getContextPath());
			
		} else {
			
			// 메세지 내용 : 회원탈퇴에 실패했습니다
			request.setAttribute("errorMsg", "회원탈퇴에 실패했습니다");
			// 실패시 => 에러페이지로 포워딩
			request.getRequestDispatcher("views/common/errorPage.jsp").forward(request, response);
		}
		
		
		
	}

}
