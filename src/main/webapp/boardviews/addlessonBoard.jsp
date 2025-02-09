<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <title>수업 추가</title>

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

<body>
    <div class="container mt-5">
		<a class="navbar-brand" href="#">
		    <p onclick="location.href='teachermainBoard.do'">🔙 이전 페이지 </p>
		</a>
        <h2 class="mb-4">수업 추가</h2>
		
        
        <!-- 에러 메시지 표시 -->
        <c:if test="${not empty error}">
            <div class="alert alert-danger" role="alert">
                ${error}
            </div>
        </c:if>
        
        <!-- 수업 추가 폼 -->
        <form action="addlessonBoardProc.do" method="post" enctype="multipart/form-data">
            <!-- 사진 업로드 -->
            <div class="mb-3">
                <label for="photo" class="form-label">사진 업로드</label>
                <input type="file" class="form-control" id="photo" name="photo" accept="image/*">
            </div>
            <!-- 제목 -->
            <div class="mb-3">
                <label for="title" class="form-label">수업 제목</label>
                <input type="text" class="form-control" id="title" name="title" maxlength="50" required>
            </div>
            <!-- 설명 -->
            <div class="mb-3">
                <label for="description" class="form-label">수업 설명</label>
                <textarea class="form-control" id="description" name="description" maxlength="200" rows="3" required></textarea>
            </div>
            <!-- 시간 -->
            <div class="mb-3">
                <label for="time" class="form-label">수업 시간</label>
                <select class="form-select" id="time" name="time" required>
                    <option value="" disabled selected>시간 선택</option>
                    <option value="A">A</option>
                    <option value="B">B</option>
                    <option value="C">C</option>
                </select>
            </div>
            <!-- 참여 인원 -->
            <div class="mb-3">
                <label for="people" class="form-label">참여 인원</label>
                <input type="number" class="form-control" id="people" name="people" min="1" max="100" required>
            </div>
            <!-- 제출 버튼 -->
            <button type="submit" class="btn btn-primary">수업 추가</button>
        </form>
    </div>
</body>
</html>
