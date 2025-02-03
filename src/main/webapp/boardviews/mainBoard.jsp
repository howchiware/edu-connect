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
</head>
<!-- ✅ JavaScript 수정된 부분 -->
<script>
    function applyLesson(lessonId, lessonTitle, teacherId) {
        console.log("📢 신청할 lessonId:", lessonId);

        const requestData = {
            lessonId: parseInt(lessonId, 10), // 숫자로 변환
            lessonTitle: lessonTitle,
            teacherId: teacherId,
            userId: "${sessionScope.loginId}",  // 로그인된 사용자 ID
            userName: "${sessionScope.loginName}", // 로그인된 사용자 이름
            selectedTime: "기본 시간" // ✅ 기본값 설정 (A, B, C 중 선택하지 않아도 신청 가능)
        };

        console.log("📤 서버로 보낼 데이터:", requestData);

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
                alert('✅ 수업 신청이 완료되었습니다!');
            } else {
                alert('❌ 수업 신청에 실패했습니다: ' + data.error);
                console.error("🚨 신청 실패 이유:", data.error);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('❌ 수업 신청 중 오류가 발생했습니다.');
        });
    }
	

</script>

<body>

    <nav class="navbar bg-body-tertiary">
        <div class="container-fluid">
            <a class="navbar-brand" href="#">
                <img src="/images/logo.png" alt="Logo" width="30" height="24">
                MainBoard
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
                        <button type="submit" class="btn btn-outline-success me-2">로그인</button>
                        <button type="button" class="btn btn-outline-secondary" onclick="location.href='joinBoard.do'">회원가입</button>
                    </form>
                </c:when>
                <c:otherwise>
                    <div class="d-flex align-items-center">
                        <span class="me-3">반갑습니다, ${sessionScope.loginName} 님!</span>


                        <button type="button" class="btn btn-outline-danger" onclick="location.href='logout.do'">로그아웃</button>
                    </div>
					<div>
						<button type="buttom" onclick="location.href='usermodifyBoard.do?num=${usertable.num}'">수정 페이지</button>
						<button type="buttom" onclick="location.href='usermainBoard.do'">관리 페이지</button>
					<div>
                </c:otherwise>
            </c:choose>
        </div>
    </nav>

    <!-- 수업 리스트 -->
    <div class="container mt-4">
        <h2 class="text-center">수업 목록</h2>

        <c:forEach var="lesson" items="${lessonList}">
            <div class="card mb-3" style="max-width: 1080px; height:250px;">
                <div class="row g-0">
                    <div class="col-md-4">
                        <img src="${sessionScope.uploadedImage}" class="img-fluid rounded-start" alt="수업 이미지">
                    </div>
                    <div class="col-md-8">
                        <div class="card-body">
                            <h5 class="card-title">${lesson.title}</h5>
                            <p class="card-text">강사: ${lesson.teacherId}</p>
                            <p class="card-text"><small class="text-body-secondary">${lesson.description}</small></p>


                            <!-- ✅ 시간표 선택 dropdown 제거됨 -->
							
							<button type="button" class="btn btn-outline-secondary" onclick="location.href='detaillessonBoard.do?num=${lesson.num}'">상세보기</button>

                            <!-- ✅ 신청하기 버튼 -->
                            <button class="btn btn-outline-success" onclick="applyLesson(${lesson.num}, '${lesson.title}', '${lesson.teacherId}')">
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
