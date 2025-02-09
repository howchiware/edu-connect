<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Teacher Message Board</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
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
        h2 {
            margin-bottom: 20px;
            color: #333;
        }
        .input-group {
            margin-bottom: 1rem;
        }
        .input-group-text {
            background-color: #f1f1f1;
        }
        button {
            border-radius: 5px;
            background-color: #007bff;
            color: white;
            border: none;
            padding: 10px 20px;
        }
        button:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
    <div class="container">
		
		<div class="container-fluid mt-4">
		    <a class="navbar-brand" href="#">
		        <p onclick="location.href='mainBoard.do'">✌️ 수업 예약 사이트</p>
		    </a>
		</div>
        <!-- 답변 작성하기-->    
        <div class="board card">
            <h2>답변 작성하기</h2>
            <form action="teachermessageProcBoard.do" method="post">
                <div>
                    <input type="hidden" name="teacherId" value="${enquiry.teacherId}">
                    <!-- <input type="hidden" name="userId" value="${sessionScope.loginId}"> -->
                    <!--<input type="hidden" name="userId" value="${enquiry.userId}">-->
                    <input type="hidden" name="num" value="${enquiry.num}">
                </div>
                <div class="input-group mb-3">
                    <span class="input-group-text" id="inputGroup-sizing-default">제목</span>
                    <!-- 제목을 value로 입력 -->
                    <input type="text" class="form-control" aria-label="Sizing example input" 
                        aria-describedby="inputGroup-sizing-default" name="title" value="${enquiry.title}" readonly>
                </div>        
                <div class="input-group mb-3">
                    <span class="input-group-text" id="inputGroup-sizing-default">내용</span>
                    <input type="content" class="form-control" aria-label="Sizing example input" aria-describedby="inputGroup-sizing-default"
                        name="content" value="${enquiry.content}" readonly>
                </div>
                <div class="input-group mb-3">
                    <span class="input-group-text" id="inputGroup-sizing-default">작성자</span>
                    <input type="userId" class="form-control" aria-label="Sizing example input" aria-describedby="inputGroup-sizing-default"
                        name="userId" value="${enquiry.userId}" readonly>
                </div>
                
                <!-- 사용자 본인의 게시글은 숨기기 -->
                <c:if test="${enquiry.userId != sessionScope.loginId}">
                    <!-- 선생님이 작성할 수 있는 답변 영역 -->
                    <div class="input-group mb-3">
                        <span class="input-group-text" id="inputGroup-sizing-default">제목_선생님</span>
                        <input type="text" class="form-control" aria-label="Sizing example input" aria-describedby="inputGroup-sizing-default" name="content_teacher">
                    </div>                
                    <div class="input-group mb-3">
                        <span class="input-group-text" id="inputGroup-sizing-default">내용_선생님</span>
                        <input type="text" class="form-control" aria-label="Sizing example input" aria-describedby="inputGroup-sizing-default" name="title_teacher">
                    </div>
                </c:if>

                <button type="submit">답변 등록</button>
            </form>
        </div>
    </div>
</body>
</html>
