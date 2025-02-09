<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <!-- 부트스트랩 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>

    <title>MainBoard</title>
    
    <style>
        body {
            background-color: #f8f9fa;
            font-family: Arial, sans-serif;
        }
        .container {
            max-width: 1080px;
            margin: auto;
        }
        .card {
            background: white;
            border-radius: 10px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            padding: 10px;
            margin-bottom: 20px;
        }
        .navbar {
            background-color: #ffffff !important;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }
        h2 {
            margin-bottom: 20px;
            color: #333;
        }
        .btn {
            border-radius: 5px;
        }
    </style>
</head>

<script>

    function applyLesson(lessonId, lessonTitle, teacherId, teacherName) {
        console.log("신청할 lessonId:", lessonId);

        const requestData = {
            lessonId: parseInt(lessonId, 10),
            lessonTitle: lessonTitle,
            teacherId: teacherId,
			teacherName: teacherName,
            userId: "${sessionScope.loginId}",
            userName: "${sessionScope.loginName}",
            selectedTime: "기본 시간"
        };

        console.log("서버로 보낼 데이터:", requestData);

        fetch('applyLesson.do', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(requestData),
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert('수업 신청이 완료되었습니다!');
            } else {
                alert('수업 신청에 실패했습니다: ' + data.error);
                console.error("신청 실패 이유:", data.error);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('수업 신청 중 오류가 발생했습니다.');
        });
    }
</script>

<body>

    <nav class="navbar navbar-expand-lg navbar-light">
        <div class="container-fluid">
            <a class="navbar-brand" href="#">
                <p onclick="location.href='mainBoard.do'">✌️ 수업 예약 사이트</p>
            </a>

            <c:choose>
                <c:when test="${not sessionScope.isLoggedIn}">
                    <form action="loginBoardProc.do" method="post" class="d-flex align-items-center">
                        <div class="input-group me-2">
                            <span class="input-group-text">아이디</span>
                            <input type="text" class="form-control" name="id">
                        </div>
                        <div class="input-group me-2">
                            <span class="input-group-text">패스워드</span>
                            <input type="password" class="form-control" name="pwd">
                        </div>
                        <div class="d-flex">
                            <button type="submit" class="btn btn-outline-success me-2" style="width: 100px; height: 40px;">로그인</button>
                            <button type="button" class="btn btn-outline-secondary" onclick="location.href='joinBoard.do'"style="width: 100px; height: 40px;">회원가입</button>
                        </div>
                    </form>
                </c:when>
                <c:otherwise>
                    <div class="d-flex align-items-center">
                        <span class="me-3">반갑습니다, ${sessionScope.loginName} 님!</span>
                        <button type="button" class="btn btn-outline-danger" onclick="location.href='logout.do'">로그아웃</button>
                    </div>
					<div>
					    <button type="button" class="btn btn-outline-primary" onclick="location.href='usermodifyBoard.do?num=${usertable.num}'">수정 페이지</button>
					    <form action="/managePage.do" method="get" class="d-inline">
					        <button type="submit" class="btn btn-outline-primary">관리 페이지</button>
					    </form>
					</div>

                </c:otherwise>
            </c:choose>
        </div>
    </nav>

    <!-- 수업 리스트 -->
    <div class="container mt-4">
        <h2 class="text-center">수업 목록</h2>

        <c:forEach var="lesson" items="${lessonList}">
            <div class="card">
                <div class="row g-0">
                    <div class="col-md-4">
                        <img src="${lesson.photoPath}" class="img-fluid rounded-start" alt="수업 이미지">
                    </div>
                    <div class="col-md-8">
                        <div class="card-body">
                            <h5 class="card-title">${lesson.title}</h5>
                            <p class="card-text">강사: ${lesson.teacherName}</p>
                            <p class="card-text"><small class="text-muted">${lesson.description}</small></p>

                            <button type="button" class="btn btn-outline-secondary" onclick="location.href='detaillessonBoard.do?num=${lesson.num}'">상세보기</button>
							<button class="btn btn-outline-success" onclick="applyLesson(${lesson.num}, '${lesson.title}', '${lesson.teacherId}', '${lesson.teacherName}')">
							    신청하기
							</button>


                        </div>
                    </div>
                </div>
            </div>
        </c:forEach>

        <c:if test="${empty lessonList}">
            <p class="text-center text-muted">등록된 수업이 없습니다.</p>
        </c:if>
    </div>

</body>
</html>
