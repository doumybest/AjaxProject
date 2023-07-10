package com.kh.member.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kh.member.model.service.MemberService;
import com.kh.member.model.vo.Member;

/**
 * Servlet implementation class LoginController
 */
@WebServlet("/login.me")
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginController() {
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
		/*
		 * <HttpServletRequest request객체와 HttpServletResponse response객체>
		 * - request : 서버로 요청할 때 정보들이 담겨있는 객체(요청 시 전달한 값, 전송방식 ..)
		 * - response : 요청에 대해 응답해줄 때 필요한 객체
		 * 
		 * <Get방식과 POST방식 차이>
		 * - Get방식 : 사용자가 입력한 값들이 URL에 노출/ 데이터 길이 제한/ 즐겨찾기에 편리
		 * - POST방식 : 사용자가 입력한 값들이 URL에 노출 X/ 데터 길이 제한X/ 대신 Timeout존재
		 * 
		 */
		// 1) POST방식으로 요청이 들어올 경우 인코딩 처리 필수
		request.setCharacterEncoding("UTF-8");
		
		// 2) 요청 시 전달값 꺼내서 변수 또는 객체에 기록
		String userId = request.getParameter("userId");
		String userPwd = request.getParameter("userPwd");
		
		// 3) 해당 로그인 요청을 처리하는 서비스 클래스의 메서드 호출하기
		Member loginUser = new MemberService().loginMember(userId, userPwd);
		
		// 4) 처리된 결과를 가지고 사용자가 보게될 응답화면 지정 후 포워딩(forward) 혹은 url 재요청 보내기
		/*
		 *  * 응답 페이지에 전달할 값이 있을 경우 값을 담아줄 수 있는 변수들(Servlet에 내장되어있는 내장객체 4가지)
		 *  1) application scope : application에 다음 데이터는 웹 애플리케이션 전역에서 다 꺼내 쓸 수 있음
		 *  2) session scope : session에 담은 데이터는 모든 jsp와 Servlet에서 꺼내 쓸 수 있음
		 *  			 한번 담은 데이터는 내가 직접 지우기 전까지, 서버가 멈추기 전까지, 브라우저가 종료되기 전까지
		 *  			 접근해서 꺼내 쓸 수 있음
		 *  3) request scope : request에 담은 데이터는 해당 request를 포워딩한 응답 jsp에서만 꺼내 쓸 수 있음
		 *  4) page scope : 해당 jsp페이지에서만 꺼내 쓸 수 있음
		 *  
		 *  공통적으로 데이터를 담고자 할 때 setAttribute("키","벨류");
		 *  		데이터를 꺼내고자 할때는 getAttribute("키");
		 *  		데이터를 지우고자 할때는 removeAttribute("키");
		 */
		
		if(loginUser == null) { // 로그인 실패 => 에러페이지 응답
			
			request.setAttribute("errorMsg", "로그인에 실패하였습니다.");
			
			// 응답 페이지 jsp로 위임 시 필요한 객체(RequestDispatcher)
			RequestDispatcher view = request.getRequestDispatcher("views/common/errorPage.jsp");
			
			view.forward(request, response);
			
		} else { // 로그인 성공 => index페이지(메인페이지)응답
			
			// 로그인 한 회원의 정보를 로그아웃 전까지 계속 가져다 쓸 수 있도록 session영역 안에 데이터 담기
			
			// Servlet에서 JSP내장 객체인 sessoion에 접근하고자 한다면 session객체를 얻어와야함
			HttpSession session = request.getSession();
			
			session.setAttribute("loginUser", loginUser);
			
			session.setAttribute("alertMsg", "성공적으로 로그인이 되었습니다.");
			
			// 1. 포워딩 방식으로 응답화면 출력
			// 	  포워딩하고자 선택된 JSP가 보여질 뿐 URL은 변화 없음
//			request.getRequestDispatcher("index.jsp").forward(request, response);
			
			// 2. url 재요청 방식(sendRedirect 방식)
			response.sendRedirect(request.getContextPath());
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	

}
