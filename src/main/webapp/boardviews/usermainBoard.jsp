<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    
    <title>User Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>

    <style>
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
            background-color: #f8f9fa;
            border: 1px solid #dee2e6;
            padding: 10px;
        }
    </style>
</head>
<body>
    <div class="header">
        <h1>User Dashboard</h1>
        <div class="d-flex align-items-center">
            <span class="me-3">반갑습니다, ${sessionScope.loginName} 님!</span>
            <button type="button" class="btn btn-outline-danger" onclick="location.href='logout.do'">로그아웃</button>
        </div>
    </div>

    <div class="container">
        <!-- 📌 수강 요청 목록 -->
        <div class="main-content">
            <h2>수강 목록</h2>
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>수업 이름</th>
                        <th>강사</th>
                        <th>선택 시간</th>
                        <th> </th>
                    </tr>
                </thead>
                <tbody>
                    <!-- ✅ `responseList`를 기준으로 수강 신청 정보 표시 -->
                    <c:forEach var="response" items="${responseList}">
                        <tr>
                            <td>${response.lessonName}</td>
                            <td>${response.teacherName}</td>
                            <td>${response.selectedTime}</td>
							<td>
								<button class="btn btn-primary btn-sm" >상세보기</button>
							</td>
                        </tr>
                    </c:forEach>

                    <!-- 수강 요청이 없을 경우 -->
                    <c:if test="${empty responseList}">
                        <tr>
                            <td colspan="4" class="text-center">수강 중인 수업이 없습니다.</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>

        <!-- 📌 진행 중인 수업 -->
        <div class="sidebar">
            <div class="sidebar-item">
                <h2>수강 신청 목록</h2>
                <table class="table">
                    <thead>
                        <tr>
                            <th>수업명</th>
                            <th>강사명</th>
                            <th>시간대</th>
							<th>신청 상태</th>
							<th> </th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="response" items="${responseList}">
                            <tr>
                                <td>${response.title}</td>
                                <td>${response.teacherName}</td>
                                <td>${response.selectedTime}</td>
								<td>${response.requestsStatus}</td>
								<td>
									<button class="btn btn-primary btn-sm" >취소</button>
								</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

            <!-- 📌 문의사항 -->
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
