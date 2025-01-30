<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
	
	<!-- 부트스트랩 CSS 라이브러리 -->
	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
	<!-- 부트스트랩 JS 라이브러리 -->
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
    
	<title>Teacher Dashboard</title>
	<style>
		body {
			margin: 0;
			padding: 0;
		}
		.header {
			height: 100px;
			background-color: #f8f9fa;
			border-bottom: 1px solid #dee2e6;
			display: flex;
			align-items: center;
			justify-content: space-between;
			padding: 0 20px;
		}
		.container {
			display: flex;
			padding: 20px;
		}
		.main-content {
			flex: 2;
			height: auto;
			background-color: #e9ecef;
			border: 1px solid #dee2e6;
			margin-right: 10px;
			padding: 20px;
		}
		.sidebar {
			flex: 1;
			display: flex;
			flex-direction: column;
			gap: 10px;
		}
		.sidebar-item {
			height: auto;
			background-color: #f8f9fa;
			border: 1px solid #dee2e6;
			padding: 10px;
		}
	</style>
</head>
<body>
	<div class="header">
		<h1>Teacher Dashboard</h1>
		
		<!-- 로그인 상태에 따른 조건부 렌더링 -->
		<c:choose>
		  <c:when test="${not sessionScope.isLoggedIn}">
		    <form action="loginBoardProc.do" method="post" class="d-flex align-items-center">
		      <div class="input-group me-2">
		        <span class="input-group-text" id="inputGroup-sizing-default">아이디</span>
		        <input type="text" class="form-control" name="id" placeholder="ID">
		      </div>
		      <div class="input-group me-2">
		        <span class="input-group-text" id="inputGroup-sizing-default">패스워드</span>
		        <input type="password" class="form-control" name="pwd" placeholder="PASSWORD">
		      </div>
		      <div class="d-flex align-items-center">
		        <button type="submit" class="btn btn-outline-success me-2" style="height: 38px; width:100px;">로그인</button>
		        <button type="button" class="btn btn-outline-secondary" style="height: 38px; width:100px;" onclick="location.href='joinBoard.do'">회원가입</button>
		      </div>
		    </form>
		  </c:when>

		  <c:otherwise>
		    <div class="d-flex align-items-center">
		      <span class="me-3">반갑습니다 ${sessionScope.loginName} 님!</span>
		      <button type="button" class="btn btn-outline-danger" style="height: 38px; width:100px;" onclick="location.href='logout.do'">로그아웃</button>
		    </div>
		  </c:otherwise>
		</c:choose>
	</div>
	
	<div class="container">
		<!-- 메인 콘텐츠: 수강 요청 목록 -->
		<div class="main-content">
			<h2>수강 요청 목록</h2>
			<table class="table table-striped">
			    <thead>
			        <tr>
			            <th>수업 이름</th>
			            <th>신청자</th>
			            <th>선택 시간</th>
			            <th>신청 상태</th>
			        </tr>
			    </thead>
			    <tbody>
					<!-- 수강 요청 목록 반복 출력 -->
					<c:forEach var="response" items="${responseList}">
					    <tr>
					        <td>${response.lessonName}</td>
					        <td>${response.userName} (${response.userId})</td>
					        <td>${response.selectedTime}</td>
					        <td>${response.requestsStatus}</td>
					    </tr>
					</c:forEach>
					
					<!-- responseList가 비어 있을 경우 -->
					<c:if test="${empty responseList}">
					    <tr>
					        <td colspan="4" class="text-center">등록된 수강 요청이 없습니다.</td>
					    </tr>
					</c:if>
			    </tbody>
			</table>
		</div>
		
		<!-- 사이드바 -->
		<div class="sidebar">
			<!-- 진행 중인 수업 -->
			<div class="sidebar-item">
				<div style="display: flex; align-items: center; justify-content: space-between; margin-bottom: 10px;">
				    <h2 style="margin: 0;">진행 중인 수업</h2>
				    <button class="btn btn-success btn-sm" onclick="location.href='addlessonBoard.do'">수업 추가</button>
				</div>
				<div style="max-height: 200px; overflow-y: auto;"> <!-- 스크롤바 추가 -->
					<table class="table">
						<thead>
							<tr>
								<th>수업명</th>
								<th>시간</th>
								<th>정원</th>
								<th>작업</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="lesson" items="${lessonList}">
							    <tr>
							        <td>${lesson.title}</td>
							        <td>${lesson.time}</td>
							        <td>${lesson.people}</td>
							        <td>
							            <button class="btn btn-primary btn-sm" onclick="editLesson(${lesson.num})">수정</button>
							            <button class="btn btn-danger btn-sm" onclick="location.href='deleteLesson.do?num=${lesson.num}'">삭제</button>
									</td>
							    </tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
			
			<!-- 문의사항 -->
			<div class="sidebar-item">
				<h2>문의사항</h2>
				<table class="table">
				    <thead>
				        <tr>
				            <th>사용자</th>
				            <th>문의내용</th>
				            <th>작업</th>
				        </tr>
				    </thead>
				    <tbody>
				        <c:forEach var="inquiry" items="${inquiryList}">
				            <tr>
				                <td>${inquiry.userName}</td>
				                <td>${inquiry.message}</td>
				                <td>
				                    <button class="btn btn-primary btn-sm" onclick="replyInquiry(${inquiry.id})">답변</button>
				                </td>
				            </tr>
				        </c:forEach>
				    </tbody>
				</table>
			</div>
		</div>
	</div>
</body>
</html>
