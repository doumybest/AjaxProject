package com.kh.member.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.member.model.service.MemberService;
import com.kh.member.model.vo.Member;

/**
 * Servlet implementation class MemberInsertController
 */
@WebServlet("/insert.me")
public class MemberInsertController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MemberInsertController() {
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
		
		// 1) 인코딩 설정
		request.setCharacterEncoding("UTF-8");
		
		// 2) 요청시 전달값을 뽑아서 변수 및 객체에 기록
		String userId = request.getParameter("userId");
		String userPwd = request.getParameter("userPwd");
		String userName = request.getParameter("userName");
		String phone = request.getParameter("phone");
		String email = request.getParameter("email");
		String address = request.getParameter("address");
		String[] interestArr = request.getParameterValues("interest");
		
		String interest = "";
		
		if(interestArr != null) {
			interest = String.join(", ", interestArr);
		}
		
		// 매개변수 있는 생성자를 이용해서 Member객체에 담김
		Member m = new Member(userId, userPwd, userName, phone, email, address, interest);
		
		// 3) 요청 처리(서비스 매서드 호출 및 결과 받기)
		int result = new MemberService().insertMember(m);
		
		// 4) 처리 결과를 가지고 사용자가 보게될 응답 뷰 지정
		if(result > 0) { // 성공 => 메인페이지 url재요청 보내기
			
			request.getSession().setAttribute("alertMsg", "회원가입에 성공했습니다.");
			
			response.sendRedirect(request.getContextPath());
			
		} else { // 실패 => 에러페이지로 포워딩
			
			request.setAttribute("errorMsg", "회원가입에 실패했습니다.");
			
			request.getRequestDispatcher("views/common/errorPage.jsp").forward(request, response);
		}
		
		
	}

}
