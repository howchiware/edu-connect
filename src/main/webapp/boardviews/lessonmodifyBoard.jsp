<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <title>수업 수정</title>
</head>

<script>
function previewImage(event) {
    var file = event.target.files[0];
    if (!file) {
        console.log("파일이 선택되지 않았습니다.");
        return;
    }

    var reader = new FileReader();
    reader.onload = function() {
        var output = document.getElementById('imagePreview');
        var container = document.getElementById('imagePreviewContainer');

        if (output && container) {
            output.src = reader.result;
            container.style.display = "block"; // 보이도록 설정
        } else {
            console.log("미리보기 요소가 존재하지 않습니다.");
        }
    };
    reader.readAsDataURL(file);
}
</script>

<style>
    #imagePreviewContainer {
        display: none; /* 처음에는 숨김 */
    }
</style>

<body>
    <div class="container mt-5">
        <h2 class="mb-4">수업 수정</h2>

        <!-- 에러 메시지 표시 -->
        <c:if test="${not empty error}">
            <div class="alert alert-danger" role="alert">
                ${error}
            </div>
        </c:if>

        <!-- 수업 수정 폼 -->
        <form action="lessonmodifyProcBoard.do" method="post" enctype="multipart/form-data">
            
            <!-- 사진 업로드 -->
            <div class="mb-3">
                <label for="photo" class="form-label">사진 업로드</label>
                <input type="file" class="form-control" id="photo" name="photo" accept="image/*" onchange="previewImage(event)">
            </div>

            <!-- 기존 이미지 표시 -->
            <c:if test="${not empty lessontable.photoPath}">
                <div class="mb-3">
                    <label class="form-label">현재 이미지</label>
                    <img src="${lessontable.photoPath}" alt="현재 이미지" class="img-fluid" style="max-width: 100%;">
                </div>
            </c:if>

            <!-- 미리보기 이미지 -->
            <div class="mb-3" id="imagePreviewContainer">
                <img id="imagePreview" src="" alt="미리보기 이미지" class="img-fluid" style="max-width: 100%;">
            </div>

			<div class="mb-3">
			    <label for="title" class="form-label">수업 제목</label>
			    <input type="text" class="form-control" id="title" name="title" maxlength="50" value="${lessontable.title}" required>
			</div>

			<div class="mb-3">
			    <label for="description" class="form-label">수업 설명</label>
			    <textarea class="form-control" id="description" name="description" maxlength="200" rows="3" required>${lessontable.description}</textarea>
			</div>

			<div class="mb-3">
			    <label for="time" class="form-label">수업 시간</label>
			    <select class="form-select" id="time" name="time" required>
			        <option value="" disabled selected>시간 선택</option>
			        <option value="A" ${lessontable.time == 'A' ? 'selected' : ''}>A</option>
			        <option value="B" ${lessontable.time == 'B' ? 'selected' : ''}>B</option>
			        <option value="C" ${lessontable.time == 'C' ? 'selected' : ''}>C</option>
			    </select>
			</div>

			<div class="mb-3">
			    <label for="people" class="form-label">참여 인원</label>
			    <input type="number" class="form-control" id="people" name="people" min="1" max="100" required value="${lessontable.people}">
			</div>

            <!-- 제출 버튼 -->
            <div>
                <button type="submit" class="btn btn-primary">수정</button>
                <button class="btn btn-danger2" onclick="location.href='lessontable.do?num=${lesson.num}'">삭제</button>
            </div>
        </form>
    </div>
</body>
</html>
