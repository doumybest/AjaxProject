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
 * Servlet implementation class MemberUpdateController
 */
@WebServlet("/update.me")
public class MemberUpdateController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MemberUpdateController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 마이페이지로 이동
		request.getRequestDispatcher("views/member/myPage.jsp").forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 사용자 정보 변경
		request.setCharacterEncoding("UTF-8");
		
		String userId = request.getParameter("userId");
		String userName = request.getParameter("userName");
		String phone = request.getParameter("phone");
		String email = request.getParameter("email");
		String address = request.getParameter("address");
		String[] interestArr = request.getParameterValues("interest");
		
		String interest = "";
		
		if(interestArr != null) {
			interest = String.join(", ", interestArr);
		}
		
		Member m = new Member(userId, userName, phone, email, address, interest);
		
		Member updateMember = new MemberService().updateMember(m);
		
		if(updateMember == null) { // 실패
			
			request.setAttribute("errorMsg", "회원정보 수정에 실패하였습니다.");
			request.getRequestDispatcher("views/common/errorPage.jsp").forward(request, response);
			
		} else { // 성공
			
			// session영역 안에 업데이트된 사용자 정보 담아주기
			HttpSession session = request.getSession();
			session.setAttribute("alertMsg", "성공적으로 회원 정보를 수정하였습니다.");
			session.setAttribute("loginUser", updateMember); // 같은 키값으로 존재할 수 없기에 덮어쓰기 함
			
			// 응답펭지ㅣ 재요청
			response.sendRedirect(request.getContextPath() + "/update.me");
		}
		
	}

}
