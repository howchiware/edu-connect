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
            container.style.display = "block";
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

<body>
    <div class="container mt-5">
		<a class="navbar-brand" href="#">
		    <p onclick="location.href='teachermainBoard.do'">🔙 이전 페이지 </p>
		</a>
        <h2 class="mb-4">수업 수정</h2>


        <!-- 에러 메시지 표시 -->
        <c:if test="${not empty error}">
            <div class="alert alert-danger" role="alert">
                ${error}
            </div>
        </c:if>


        <form action="/lessonmodifyProcBoard.do" method="post" enctype="multipart/form-data">
			<!-- 기존 입력 필드 아래 추가 -->
			<input type="hidden" name="num" value="${lessontable.num}">
			<input type="hidden" name="photoPath" value="${lessontable.photoPath}">

            <!-- 기존 이미지 표시 -->
			<c:if test="${not empty lessontable.photoPath}">
			    <div class="mb-3">
			        <p class="form-label">현재 이미지</p>
			        <img src="${lessontable.photoPath}" alt="현재 이미지" class="img-fluid" style="max-width: 100%;">
			    </div>
			</c:if>
			
			<!-- 미리보기 이미지 -->
			<div class="mb-3" id="imagePreviewContainer">
			    <img id="imagePreview" src="" alt="미리보기 이미지" class="img-fluid" style="max-width: 100%;">
			</div>


			<div class="mb-3">
			    <label for="photoFile" class="form-label">사진 업로드</label>
			    <input type="file" class="form-control" id="photoFile" name="photoFile" accept="image/*" onchange="previewImage(event)">
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
                <button type="button" class="btn btn-danger2" onclick="location.href='lessontable.do?num=${lesson.num}'">삭제</button>
            </div>
        </form>
    </div>
</body>
</html>
