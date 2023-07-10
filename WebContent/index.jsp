<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
</head>
<body>
	<h1>Ajax 개요</h1>
	<p>
		Asynchronus JavaScript And Xml의 약자로 <br>
		서버로부터 데이터를 가져와 전체 페이지를 새로 고치지 않고 일부만 로드할 수 있게 하는 기법 <br>
		우리가 기존에 a 태그로 요청 및 form을 통해 요청했던 방식은 동기식 요청방식<br>
		=> 응답페이지가 돌아와야 볼 수 있음(페이지화면 깜빡거림)<br>
		비동기식 요청을 보내기 위해서는 AJAX라는 기술이 필요함<br><br>
		
		* 동기식 / 비동기식 <br>
		- 동기식 : 요청 처리 후 그에 해당하는 응답페이지가 돌아와야만 그 다음 작업 가능<br>
		만약 서버에서 호출된 결과까지의 시간이 지연되면 무조건 계속 기다려야함(흰화면만 보여지게 됨)<br>
		전체 페이지가 리로드 됨(새로고침 즉, 페이지가 기본적으로 깜빡거림) <br><br>
		
		- 비동기식 : 현재 페이지를 그대로 유지하면서 중간중간마다 추가적인 요청을 보내줄 수 있음<br>
		요청을 한다고해서 다른페이지로 넘어가지 않음(현재 페이지 그대로)<br>
		요청을 보내놓고 그에 해당하는 응답이 돌아올 때까지 현재 페이지에서 다른작업을 할 수 있음. <br>
		ex) 검색어 자동완성기능 or 아이디 중복체크기능 등
		
		* 비동기식 단점 <br>
		- 현재 페이지에 지속적으로 리소스가 쌓임 => 페이지가 느려질 수 있음 <br>
		- 페이지 내 복잡도가 증가할 수 있다 => 유지보수가 힘들어짐<br>
		- 요청 후 들어온 응답데이터를 가지고 현재페이지에서 새로운 요소를 만들어서 뿌려줘야함
		=> DOM요소 생성구문을 숙지 못할 시 힘들수가 있음<br>
	</p>
	
	<h1>javascript방식을 이용한 AJAX테스트</h1>
	
	<h3>1. 버튼 클리깃 get방식으로 서버에 데이터 전송 및 응답받기</h3>
	
	<h3>자바스크립트를 이용한 ajax통신</h3>
	
	<button onclick="ajaxTest1()">js ajax 테스트 1(get)</button>
	
	
	
	
	<h3>2. 버튼 클릭시 post방식으로 서버에 데이터 전송 및 응답받기</h3>
	
	<button onclick="ajaxTest2();">js ajax 테스트 2(post)</button>
	
	<div id="target"></div>
	
	<script>
		const ajaxTest1 = () => {
			// ajax로 서버와 통신하기
			// 1. XMLHttpRequest 객체 생성
			const xhr = new XMLHttpRequest();
			
			// 2. xhr 객체를 성정하기 -> 전송방식(get/post), 요청을 보내는 주소/ [동기식, 비동기식]
			// 객체 설정시 open()함수를 이용해서 통신값을 설정함.
			xhr.open("get", "<%= request.getContextPath() %>/ajaxTest.do?id=admin");
			
			// 3. 요청에 대한 서버응답을 처리할 함수를 지정
			// xhr 객체의 onreadystatechange 라는 속성에 함수를 대입
			// xhr 객체의 내부 상태를 관리하는 속성
			// 1) readyState : 전송상태를 관리
			// 2) status : 응답결과 관리 (404,500,503,200 등)
			xhr.onreadystatechange = () => {
				if(xhr.readyState == 4){ // 정상적으로 요청이 완료되면 4의 값을 반환할것
					
					if(xhr.status == 200){ // 응답메시지 정상 수신시 200
						// 서버가 응답한 내용은 xhr객체의 responseText라는 속성에 자동으로 추가됨.
						console.log(xhr, xhr.responseText);
						document.getElementById("target").innerHTML += "<h3>" + xhr.responseText + "</h3>";
					} else if(xhr.status == 404){
						alert("찾는 페이지가 존재하지 않습니다.");
					} else {
						alert("에러가 발생했습니다.");
					}
				
				}
			}
			// 모든값 설정 완료 후 서버로 요청을 전송
			xhr.send();
		}
		
		function ajaxTest2(){
			const xhr = new XMLHttpRequest();
			
			xhr.open("post", "<%= request.getContextPath()%>/ajaxTest.do");
			
			xhr.onreadystatechange = () => {
				if(xhr.readyState == 4){
					if(xhr.status == 200){
						document.getElementById("target").innerHTML += "<h4>" + xhr.responseText + "</h4>";
					}
				}
			}
			// post방식으로 데이터 전송시 파라미터 설정방법
			// url에 데이터 내용을 작성하지 않고, send함수의 매개변수로 작성.
			// 전송방식도 수정해줘야함
			xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
			xhr.send("id=user01");
		}
	</script>
	
	<pre>
		* jQuery 방식에서의 AJAX 통신
		
		$.ajax({
			속성명 : 속성값,
			속성명 : 속성값,
			...
		});
		
		* 주요속성
		- *url : 요청할 url(필수)
		- *type|method : 요청전송방식(get/post, 생략시 기본값 get)
		- *data : 요청시 전달할 값
		- *success : ajax 통신 성공시 실행할 함수를 정의(status == 200일때)
		- error : ajax통신 실패시 실행할 함수를 정의(status != 200일때)
		- complete : ajax통신에 실패했든 성공했든 무조건 실행할 함수를 정의
		
		* 부수적인 속성
		- *async : 서버와의 비동기 처리방식 설정 여부(기본값 true)
		- *contenType : request의 데이터인코딩 방식 정의(보내는 측의 데이터 인코딩)
		- dataType : 서버에서 response로 넘어오는 데이터의 데이터 자료형설정. 값이 없다면 알아서 판단함 !!!
					xml : 트리형태의 구조
					json : 맵형태의 구조(일반적인 데이터 구조)
					script : javascript 및 일반 STRING 형태의 데이터
					html : html 태그 자체를 return하는 방식
					text : String 데이터
		- aceept : 파라미터의 타입 설정(사용자 특화된 파라미터 타입 설정 가능)
		- *beforeSend : ajax 요청을 하기 전 실행되는 이벤트 함수를 정의(데이터 가공, header관련 설정을 함)
		- cache : 요청 및 결과값을  scope에서 갖고있지 않도록 하는 것(기본값 true)
		- contents : jQuery에서 response의 데이터를 파싱하는 방식 정의
		- crossDomain : 타 도메인 호출 가능 여부 설정(기본값 false)
		- dataFilter : response 데이터를 받았을 때 정상적인 값을 return 할 수 있도록 데이터와 데이터타입 설정
		- global : 기본 이벤트 사용 여부(ajaxStart, ajaxStop) => 선처리 작업시 사용
		- password : 서버에 접속권한이 필요한 경우 설정
		- *processData : 서버로 보내는 값에 대한 형태 설정 여부(기본데이터를 원하는 경우 false 설정)
		- timeout : 서버 요청시 응답 대기 시간설정
	</pre>
	
	
	<h1>Jquery 방식을 이용한 ajax 테스트</h1>
	
	<h3>1. 버튼 클릭시 get방식으로 서버에 데이터 전송 및 응답</h3>
	 
	입력 : <input type="text" id="input1">
	<button id="btn1">전송</button>
	<br>
	
	응답 : <label id="output1">응답대기중..</label>
	
	<script>
		$(function(){
			$("#btn1").click(function(){
				$.ajax({
					url : "jqAjax1.do",
					data : {input : $("#input1").val()},
					type : "get",
					success : function(result){ // 요청 성공시 실행할 콜백함수 정의
						// result : 서블릿으로부터 전달받은 값(xhr.responseText와 동일)
						$("#output1").text(result);
					},
					error : function(){
						console.log("ajax통신 실패");
					},
					complete : function(){
						console.log("ajax통신 성공 여부와 상관없이 항상 실행될 것");
					}
				});
			});
		});
	</script>
	
	<hr>
	<br>
	
	<h3>2. 버튼클릭시 post방식으로 서버에 데이터 전송 및 응답받기</h3>
	
	이름 : <input type="text" id="input2_1"><br>
	나이 : <input type="text" id="input2_2"><br>
	<button onclick="test2();">전송</button>
	<br>
	
	응답 : <label id="output2">현재 응답 없음</label>
	
	<script>
		// 버전 1) 문자열 데이터 응답방식
		/* function test2(){
			$.ajax({
				url : "jqAjax2.do",
				data: {
					name : $("#input2_1").val(),
					age : $("#input2_2").val()
				},
				type : "post",
				success : function(result){
					$("#output2").text(result);
				}
			})
		} */
		
		
	
	// 버전 2) JSON데이터 응답방식
	function test2(){
		$.ajax({
			url : "jqAjax2.do",
			data: {
				name : $("#input2_1").val(),
				age : $("#input2_2").val()
			},
			type : "post",
			//dataType: "json",
			success : function(result){
				console.log(result);
				
				// $("#output2").text("이름 : " + result[0] +", 나이 : " + result[1]);
				$("#output2").text("이름 : "+result.name +", 나이 : " + result["age"]); 
				// 값 뽑아올때 .써도 되고 []써도 되는데 []가 더 좋음
				
			}
		});
	}
	
	</script>
	
	
	<h3>3. 서버로 데이터 전송 후, 조회된 객체를 응답데이터로 받기</h3>
	
	회원 아이디 : <input type="text" id="userId">
	회원 비밀번호 : <input type="password" id="userPwd">
	<button onclick="test3()">조회</button>
	
	<div id="output3"></div>
	
	<script>
		function test3(){
			
			$.ajax({
				url : "jqAjax3.do",
				data : {
					userId : $("#userId").val(),
					userPwd : $("#userPwd").val()
				},
				success : function(result){
					// console.log(result);
					
					let resultStr = "회원번호 : "+result.userNo+"<br>"
									+"이름 : " +result.userName+"<br>"
									+"이메일 : "+result.email+"<br>";
									
					$("#output3").html(resultStr);
				},
				error : function(req, status, error){
					console.log(req, status, error);
				}
			})
			
		}
	</script>
	
	
	<h4>4. 응답데이터로 여러개의 객체들이 담겨있는 ArrayList 받기</h4>
	
	<button onclick="test4()">일반게시판 조회</button>
	
	<table>
		<thead>
			<tr>
				<th width="70">글번호</th>
				<th width="70">카테고리</th>
				<th width="300">제목</th>
				<th width="100">작성자</th>
				<th width="50">조회수</th>
				<th width="100">작성일</th>
			</tr>
		</thead>
		<tbody>
		
		</tbody>
	</table>
	
	<script>
		function test4(){
			$.ajax({
				url : "jqAjax4.do",
				success : function(result){
					console.log(result);
					
					let str = "";
					
					for(let i = 0; i < result.length; i++){
						str += "<tr>" 
							+ "<td>" + result[i].boardNo + "</td>" 
							+ "<td>" + result[i].category + "</td>"
							+ "<td>" + result[i].boardTitle + "</td>"
							+ "<td>" + result[i].boardWriter + "</td>"
							+ "<td>" + result[i].count + "</td>"
							+ "<td>" + result[i].createDate + "</td>"
							+ "</tr>";
					}
					$("#output4 tbody").html(str);
				}
			});
		}
	</script>
	
	<h2>5. Ajax로 html파일 받아오기</h2>
	
	<button id="htmlAjax">html문서받기</button>
	
	<div></div>
	
	<script>
		$(function(){
			$("#htmlAjax").click(function(){
				$.ajax({
					url : "jqHtmlTest.do",
					type : "get",
					dataType : "html",
					success : function(data){
						console.log(data);
						$("#htmlAjax+div").html(data);
					}
				});
			});
		});
	</script>
	
	<h2>6. XML데이터 가져오기</h2>
	<button id="xmlTest">xml 데이터 가져오기</button>
	
	<div id="fiction">
		<h2>소설</h2>
		<table id="fiction-info">
		
		</table>
	</div>
	
	<div id="it">
		<h2>프로그래밍</h2>
		<table id="it-info">
		
		</table>
	</div>
	
	
	<!-- xml에 소설, it, 만화 항목을 작성해놨는데 만화빼고 소설, it만 뽑아와보기 -->
	<script>
		$(function(){
			$("#xmlTest").click( () =>{
				$.ajax({
					url : "books.xml",
					success : data => { // 매개변수 하나일떄는 () 생략가능
						console.log(data);
						let ficheader = "<tr><th>제목</th><th>저자</th></tr>";
						let itheader = ficheader;
						
						let entity = $(data).find(":root");
						console.log(entity);
						
						let books = $(entity).find("book");
						
						books.each(function(index, value){
							let info = "<tr><td>" + $(value).find("title").text()+"</td>"
										+ "<td>" + $(value).find("author").text()+"</td></tr>";
										
							let subject = $(value).find("subject").text();
							if(subject == "소설"){
								ficheader += info;
							} else if(subject == "프로그래밍"){
								itheader += info;
							}
						});
						
						$("#fiction-info").html(ficheader);
						$("#it-info").html(itheader);
					}
				});
			});
		});
	</script>
	
	<h2>7. Ajax를 이용한 파일업로드</h2>
	<input type="file" id="upfile" multiple>
	<button onclick="sendFile()";>파일 전송</button>
	
	<script>
		function sendFile(){
			// 파일 전송시 FormData 객체를 생성해서 FormData 내부에 파일을 넣어준 후, 요청을 보내야함.
			let form = new FormData();
			
			// 아래 둘다 동일한 기능, 다른 방법
			form.append("upfile", $("#upfile")[0].files[0]);
			form.append("upfile", document.getElementById("upfile").files[0]);
			
			$.each($("#upfile")[0].files, function(i, file){
				console.log(i, file)
			});
			
			$.ajax({
				url : "fileUpload.do",
				data : form, 
				type : "post",
				processData : false, // 기본값 true
				contentType : false,
				success : (data) => {
					alert("업로드 성공!");
					$("#upfile").val("");
				}
			});
		}
	</script>
	
	
	
</body>
</html>