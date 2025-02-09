<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>수업 상세 페이지</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            padding: 0;
            background-color: #f8f9fa;
        }
        .container {
            max-width: 600px;
            margin: auto;
        }
        .section {
            background: white;
            padding: 15px;
            margin-bottom: 15px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }
        .photo img {
            width: 100%;
            height: auto;
            border-radius: 8px;
        }
        .details div {
            margin: 5px 0;
        }
        .board input, .board button {
            width: 100%;
            padding: 8px;
            margin-top: 5px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }
        .board button {
            background-color: #007bff;
            color: white;
            border: none;
            cursor: pointer;
        }
        .board button:hover {
            background-color: #0056b3;
        }
        h3 {
            margin-bottom: 10px;
            color: #333;
        }
    </style>
</head>
<body>

    <div class="container">
        <h2 onclick="location.href='mainBoard.do'">✌️ 수업 예약 사이트</h2>

        <div class="section photo">
			<img src="${pageContext.request.contextPath}/images/${lessontable.photoPath}" alt="수업 이미지">
        </div>


		<p>수업 이미지 경로: ${pageContext.request.contextPath}/images/${lessontable.photoPath}</p>


        <div class="section details">
            <h3>수업 정보</h3>
            <div><strong>제목:</strong> ${lesson.title}</div>
            <div><strong>강사:</strong> ${lesson.teacherName}</div>
            <div><strong>설명:</strong> ${lesson.description}</div>
        </div>

        <div class="section board">
            <h3>문의 작성</h3>
            <form action="insertenquiryProcBoard.do" method="post">
                <input type="hidden" name="teacherId" value="${lesson.teacherId}">
                <input type="hidden" name="userId" value="${sessionScope.loginId}">
                <input type="hidden" name="num" value="${lesson.num}">
                <input type="hidden" name="lessonId" value="${lesson.lessonId}">
                
                <input type="text" name="title" placeholder="제목 입력">
                <input type="text" name="content" placeholder="내용 입력">
                <button type="submit">문의 등록</button>
            </form>
        </div>
    </div>

</body>
</html>
