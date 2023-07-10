package com.kh.board.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import com.kh.board.model.service.BoardService;
import com.kh.board.model.vo.Attachment;
import com.kh.board.model.vo.Board;
import com.kh.board.model.vo.Category;
import com.kh.common.MyFileRenamePolicy;
import com.oreilly.servlet.MultipartRequest;

/**
 * Servlet implementation class BoardInsertController
 */
@WebServlet("/insert.bo")
public class BoardInsertController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BoardInsertController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ArrayList<Category> list = new BoardService().selectCategoryList();
		
		request.setAttribute("list",list);
		request.getRequestDispatcher("views/board/boardEnrollForm.jsp").forward(request,response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		// request.getParameter("title"); <- request로 값뽑기 여기에서 사용불가
		// 폼 전송을 일반방식이 아닌 multipart/form-data로 전송하는 경우에는 사용불가함
		// multipart라는 객체에 값을 이관시킨 후 다뤄야한다.
		
		// enctype이 multipart/form-data로 전송되었을 경우에만 내용이 수정되도록 조건을 추가
		if(ServletFileUpload.isMultipartContent(request)) { // 전송방식이 multipart면 true 아니면 false
			// 파일 업로드를 위한 라이브러리 : cos.jar
			
			// 1. 전송되는 파일을 처리할 작업 내용(전송되는 파일의 용량제한, 전달된 파일을 저장할 폴더 경로)
			// 1_1. 전송되는 파일 용량 제한하기 => byte단위로 기술해야한다.
			/*
			 * byte -> kbyte -> mbyte -> gbyte -> tbyte...
			 * 1 kbyte == 1024byte
			 * 1 mbyte == 1024byte
			 * ...
			 * 
			 */
			int maxSize = 10 * 1024 * 1024; // 10Mbyte;
			
			// 1_2. 전달된 파일을 저장할 서버의 폴더 경로 알아내기(String savePath)
			// 세션객체에서 제공하는 getRealPath메소드를 통해 알아내기
			// 다만 WebContents 폴더로부터 board_upfiles(저장할폴더)폴더까지의 경로는 매개변수로 제시해줄 예정
			// 그리고 결론적으로는 board_upfiles폴더 내부에 파일들이 저장될 예정이기 때문에 /를 하나 더 추가해줘야한다.
			// getresources는 현재폴더의 바로 상위까지밖에 못봐서 다른 폴더에 있는거 꺼내기 위해 realpath로 경로 설정해야함
			String savePath = request.getSession().getServletContext().getRealPath("/resources/board_upfiles/");
			System.out.println(savePath);
			
			/*
			 * 2. 전달된 파일명 수정 및 서버에 업로드 작업
			 * -HttpServletRequest request => MultipartRequest multiRequest로 변환해주기
			 * 
			 * 매개변수 생성자로 생성(cos.jar에서 제공하는 객체임)
			 * MultipartRequest multiRequest = new MultipartRequest(request객체, 저장할 폴더 경로, 용량제한, 인코딩값, 파일명 수정 해주는 객체);
			 * 
			 * 위 구문 한줄 실행만으로 넘어온 첨부파일들이 해당 폴더에 무조건 업로드 됨.
			 * 
			 * 그리고 사용자가 올린 파일명은 그대로 해당 폴더에 업로드하지 않는게 일반적임.
			 * - 같은 파일명이 있을 경우 덮어씌워지거나, 에러가 발생할수가 있음.
			 * 
			 * 기본적으로 파일명 수정작업을 해주는 객체
			 * - DefaultFileRenamePolicy 객체 (cos.jar에서 제공)
			 * - 내부적으로 rename()이라는 메소드가 실행이 되면서 파일명 수정이 진행됨
			 * - 기본적으로 동일한 파일명이 존재할 경우 뒤에 간단하게 카운팅된 숫자를 붙여준다.
			 * 
			 * - 하지만 우리 입맛대로 파일명이 절대 안겹치게끔 rename 함수를 overriding 할 예정
			 */
			MultipartRequest multi = new MultipartRequest(request, savePath, maxSize, "UTF-8", new MyFileRenamePolicy());
			
			// 3. DB에 기록할 데이터들을 뽑아서 Board와 Attachment 객체에 담기
			// - board : 카테고리 번호, 제목, 내용, 작성자번호 정보를 뽑아서 Board테이블에 Insert
			// - attachment : 넘어온 첨부파일이 있을 경우에만, 원본명, 수정명, 폴더 경로를 뽑아서 Attachment 테이블에 insert
			String category = multi.getParameter("category");
			String boardTitle = multi.getParameter("title");
			String boardContent = multi.getParameter("content");
			String boardWriter = multi.getParameter("userNo");
			
			Board b = new Board();
			b.setCategory(category);
			b.setBoardTitle(boardTitle);
			b.setBoardContent(boardContent);
			b.setBoardWriter(boardWriter);
			
			Attachment at = null; // 처음에는 널값으로 초기화, 첨부파일이 있다면 그 떄 객체 생성
			
			// multi.getOriginalFileName("키");
			// => 키값에 해당하는 첨부파일이 있을 경우 원본명 / 없을 경우 null을 반환
			
			if(multi.getOriginalFileName("upfile") != null) {
				at = new Attachment();
				at.setOriginName(multi.getOriginalFileName("upfile")); // 원본명
				at.setChangeName(multi.getFilesystemName("upfile")); // 수정명(실제 서버에 업로드 되어 있는 파일명)
				at.setFilePath("resources/board_upfiles/");
			}
			
			// 4. 서비스 요청
			int result = new BoardService().insertBoard(b, at);
			
			if(result > 0 ) { // 성공시 => list.bo url 재요청보내기
				request.getSession().setAttribute("alertMsg", "게시글 작성 성공");
				response.sendRedirect(request.getContextPath()+"/list.bo");
			} else { // 실패시 => 업로드하고자하는 첨부파일이 이미 있었을 경우 저장실패, 이미 있는걸 또 보관할 필요가 없다.
				if(at != null) {
					// 삭제하고자하는 파일객체 생성 후, delete메소드 호출
					new File(savePath+at.getChangeName()).delete();
				}
				request.setAttribute("errorMsg", "게시글 작성 실패");
				request.getRequestDispatcher("views/common/errorPage.jsp").forward(request, response);
			}
			
		} else {
			request.setAttribute("errorMsg", "전송방식이 잘못되었습니다.");
			request.getRequestDispatcher("views/common/errorPage.jsp").forward(request, response);
		}
	}

}
