package com.kh.board.controller;

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
import com.kh.common.MyFileRenamePolicy;
import com.oreilly.servlet.MultipartRequest;

/**
 * Servlet implementation class ThumbnailInsertController
 */
@WebServlet("/insert.th")
public class ThumbnailInsertController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ThumbnailInsertController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.getRequestDispatcher("views/board/thumbnailEnrollForm.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if(ServletFileUpload.isMultipartContent(request)) {
			
			// 1_1. 전송용량제한
			int maxSize = 10*1024*1024;
			
			// 1_2. 저장할 폴더의 물리적인 경로
			String savePath = request.getServletContext().getRealPath("/resources/thumbnail_upfiles/");
			
			// 2. 전달된 파일명 수정 작업 후 서버에 업로드
			MultipartRequest multi = new MultipartRequest(request,savePath,maxSize,"UTF-8",new MyFileRenamePolicy());
			
			// 3. DB에 전달할 값 뽑기
			// Board테이블에 Insert시 필요한 데이터 뽑기
			Board b = new Board();
			b.setBoardWriter(multi.getParameter("userNo"));
			b.setBoardTitle(multi.getParameter("title"));
			b.setBoardContent(multi.getParameter("content"));
			
			// Attachment 테이블에 여러번 insert할 데이터 뽑기
			// 단, 여러개의 첨부파일이 있을 것이기 떄문에 Attachment들을 ArrayList에 담기
			// => 적어도 1개이상은 담길 것 ( 썸네일은 필수입력사항이였으므로)
			
			ArrayList<Attachment> list = new ArrayList();
			
			for(int i =1; i <=4; i++) { // 파일의 최대갯수는 4개이므로 최대 4회 반복
				// file1~4번의 이름이 있냐? 없냐?
				String key = "file"+i;
				if(multi.getOriginalFileName(key) != null) {
					// 첨부파일이 존재할 경우
					// Attachment 객체 생성 + 원본명, 수정명, 저장경로, 파일레벨을 담아서, list에 넣어주기
					Attachment at = new Attachment();
					at.setOriginName(multi.getOriginalFileName(key));
					at.setChangeName(multi.getFilesystemName(key));
					at.setFilePath("resources/thumbnail_upfiles/");
					
					if(i == 1) { // 대표이미지라면
						at.setFileLevel(1);
					} else { // 상세이미지라면
						at.setFileLevel(2);
					}
					
					list.add(at);
				}
			}
			
			int result = new BoardService().insertThumbnailBoard(b,list);
			
			
			if(result > 0) { // 성공시 => list.th 요청 => 사진게시판 리스트 이미지
				request.getSession().setAttribute("alertMsg", "성공적으로 업로드되었습니다.");
				response.sendRedirect(request.getContextPath()); // 사진게시판 리스트 작업 완료후 변경예정
			} else { // 실패 => 에러페이지
				request.setAttribute("errorMsg", "사진게시판 업로드 실패");
				request.getRequestDispatcher("views/common/errorPage.jsp").forward(request, response);
			}
			
			
			
		}
		
	}

}
