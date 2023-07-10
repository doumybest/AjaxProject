package com.kh.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.common.MyFileRenamePolicy;
import com.oreilly.servlet.MultipartRequest;

/**
 * Servlet implementation class AjaxFileUploadController
 */
@WebServlet("/fileUpload.do")
public class AjaxFileUploadController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AjaxFileUploadController() {
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
		
		String filePath = request.getServletContext().getRealPath("/upload/");
		
		MultipartRequest multiRequest = new MultipartRequest(request, filePath, 1024*1024*10, "UTF-8", new MyFileRenamePolicy());
		
		Enumeration e = multiRequest.getFileNames();
		// 업로드 된 파일의 key값이 담겨있음
		
//		ArrayList<String> filenames = new ArrayList();
		// 여러개 뽑아서 배열에 담아두고 테이블에 넣을 때 씀
		
		while(e.hasMoreElements()) {
			
			String key = (String) e.nextElement();
			System.out.println("파일의 원본 이름 : " + multiRequest.getOriginalFileName(key));
			System.out.println("파일의 수정된 이름 : " + multiRequest.getFilesystemName(key));
			
		}
		
	}

}
