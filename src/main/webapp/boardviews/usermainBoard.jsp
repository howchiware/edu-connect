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
        body {
            font-family: Arial, sans-serif;
        }
        .header {
            height: 80px;
            background-color: #f1f3f5;
            border-bottom: 2px solid #ced4da;
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 0 20px;
            font-size: 1.2rem;
        }
        .container {
            display: flex;
            padding: 20px;
            gap: 15px;
        }
        .main-content {
            flex: 2;
            background-color: #ffffff;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            padding: 20px;
        }
        .sidebar {
            flex: 1;
            display: flex;
            flex-direction: column;
            gap: 15px;
        }
        .sidebar-item {
            background-color: #ffffff;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            padding: 15px;
        }
        .navbar-brand {
            font-size: 1.5rem;
            font-weight: bold;
            color: #007bff;
            text-decoration: none;
        }
        .navbar-brand:hover {
            color: #0056b3;
        }
		th {
		    white-space: nowrap;
		}
    </style>
</head>

<script>
    function cancelRequest(num) {
        if (confirm("정말로 신청을 취소하시겠습니까?")) {
            fetch('/cancelLessonRequest.do', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: 'num=' + encodeURIComponent(num)
            }).then(response => response.json())
              .then(data => {
                  if (data.success) {
                      alert("신청이 취소되었습니다.");
                      location.reload();  
                  } else {
                      alert("취소에 실패했습니다: " + data.error);
                  }
              }).catch(error => {
                  console.error("오류 발생:", error);
              });
        }
    }
</script>

<body>
    <div class="header">
        <a class="navbar-brand" href="#" onclick="location.href='mainBoard.do'">✌️ 수업 예약 사이트</a>
        <div class="d-flex align-items-center">
            <span class="me-3">반갑습니다, ${sessionScope.loginName} 님!</span>
            <button type="button" class="btn btn-outline-danger" onclick="location.href='logout.do'">로그아웃</button>
        </div>
    </div>

    <div class="container">
        <div class="main-content">
            <h2>수강 목록</h2>
            <table class="table table-hover">
                <thead>
                    <tr>
                        <th>수업 이름</th>
                        <th>강사</th>
                        <th>선택 시간</th>
                        <th> </th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="lesson" items="${enrolledLessons}">
                        <tr>
                            <td>${lesson.lessonName}</td>
                            <td>${lesson.teacherName}</td>
                            <td>${lesson.selectedTime}</td>
                            <td>
                                <button type="button" class="btn btn-outline-secondary" onclick="location.href='detaillessonBoard.do?num=${lesson.lessonId}'">상세보기</button>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty enrolledLessons}">
                        <tr>
                            <td colspan="4" class="text-center">수강 중인 수업이 없습니다.</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>

        <div class="sidebar">
            <div class="sidebar-item">
                <h2>신청 목록</h2>
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
                        <c:forEach var="lesson" items="${pendingAndRejectedLessons}">
                            <tr>
                                <td>${lesson.lessonName}</td>
                                <td>${lesson.teacherName}</td>
                                <td>${lesson.selectedTime}</td>
                                <td>${lesson.requestsStatus}</td>
                                <td>
                                    <c:if test="${lesson.requestsStatus == 'PENDING'}">
                                        <button class="btn btn-danger btn-sm" onclick="cancelRequest(${lesson.num})">취소</button>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty pendingAndRejectedLessons}">
                            <tr>
                                <td colspan="5" class="text-center">수강 신청 내역이 없습니다.</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>

            <div class="sidebar-item">
                <h2>문의사항</h2>
                <table class="table">
                    <thead>
                        <tr>
                            <th>답변</th>
                            <th>작업</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="enquiry" items="${enquiryList}">
                            <tr>
                                <td>${enquiry.title_teacher}</td>
                                <td>${enquiry.content_teacher}</td>
                                <td>
                                    <button class="btn btn-primary btn-sm">답변</button>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty enquiryList}">
                            <tr>
                                <td colspan="3" class="text-center">문의사항이 없습니다.</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
</html>
