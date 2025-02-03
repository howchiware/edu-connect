<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Teacher Dashboard</title>
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
    <script>
		function changeStatus(num, status) {
		    if (num === 0) {
		        alert("❌ 잘못된 요청입니다. num 값이 0입니다.");
		        return;
		    }

		    if (confirm("정말로 상태를 변경하시겠습니까?")) {
		        fetch("/updateRequestStatus", {
		            method: "POST",
		            headers: {
		                "Content-Type": "application/json",
		            },
		            body: JSON.stringify({
		                num: num,  // ✅ num 값이 0이 아닐 때만 전송
		                status: status
		            }),
		        })
		        .then((response) => {
		            if (!response.ok) {
		                throw new Error(`HTTP error! Status: ${response.status}`);
		            }
		            return response.json();
		        })
		        .then((data) => {
		            if (data.success) {
		                alert("✅ 상태가 성공적으로 변경되었습니다.");
		                location.reload();
		            } else {
		                alert(`❌ 상태 변경에 실패했습니다: ${data.error}`);
		            }
		        })
		        .catch((error) => {
		            alert(`❌ 상태 변경 요청 실패: ${error.message}`);
		            console.error("🚨 오류 발생:", error);
		        });
		    }
		}
    </script>
</head>
<body>
    <div class="header">
        <h1>Teacher Dashboard</h1>
        <div class="d-flex align-items-center">
            <span class="me-3">반갑습니다, ${sessionScope.loginName} 님!</span>
            <button type="button" class="btn btn-outline-danger" onclick="location.href='logout.do'">로그아웃</button>
        </div>
    </div>

    <div class="container">
        <!-- 📌 수강 요청 목록 -->
        <div class="main-content">
            <h2>수강 요청 목록</h2>
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>수업 이름</th>
                        <th>신청자</th>
                        <th>선택 시간</th>
                        <th>신청 상태</th>
                        <th>작업</th>
                    </tr>
                </thead>
                <tbody>
                    <!-- ✅ `responseList`를 기준으로 수강 신청 정보 표시 -->
                    <c:forEach var="response" items="${responseList}">
                        <tr>
                            <td>${response.lessonName}</td>
                            <td>${response.userName} (${response.userId})</td>
                            <td>${response.selectedTime}</td>
                            <td>${response.requestsStatus}</td>
                            <td>
                                <button class="btn btn-success btn-sm" onclick="changeStatus(${response.num}, 'ACCEPTED')">수락</button>
                                <button class="btn btn-danger btn-sm" onclick="changeStatus(${response.num}, 'REJECTED')">거절</button>
                            </td>
                        </tr>
                    </c:forEach>

                    <!-- 수강 요청이 없을 경우 -->
                    <c:if test="${empty responseList}">
                        <tr>
                            <td colspan="5" class="text-center">등록된 수강 요청이 없습니다.</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>

        <!-- 📌 진행 중인 수업 -->
        <div class="sidebar">
            <div class="sidebar-item">
                <h2>진행 중인 수업</h2>
				<div>
					<button class="btn btn-primary btn-sm" onclick="location.href='addlessonBoard.do'">추가</button>
				</div>
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
									<button class="btn btn-primary btn-sm">수정</button>	
								</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

           
            <div class="sidebar-item">
                <h2>문의사항</h2>
                <table class="table">
                    <thead>
                        <tr>
                            <th>제목</th>
                            <th>작성자</th>
                            <th>문의내용</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="enquiry" items="${enquiryList}">
                            <tr>
                                <td>${enquiry.title}</td>
                                <td>${enquiry.userId}</td>
								<td class="truncate-content">${enquiry.content}</td>
                                <td>
                                    <button class="btn btn-primary btn-sm" onclick="replyInquiry(${inquiry.num})">답변</button>
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
