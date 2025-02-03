<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Lesson Detail Page</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f4f4f9;
        }
        .container {
            width: 80%;
            margin: 20px auto;
            background: white;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
            padding: 20px;
        }
        .photo {
            width: 100%;
            height: 300px;
            background: #ccc;
            border-radius: 8px;
            overflow: hidden;
            text-align: center;
            line-height: 300px;
            color: #888;
        }
        .details {
            display: grid;
            grid-template-columns: 1fr 2fr;
            gap: 20px;
            margin-top: 20px;
        }
        .details div {
            padding: 10px;
            background: #f9f9f9;
            border-radius: 8px;
        }
        .timetable, .board {
            margin-top: 30px;
            background: #f9f9f9;
            padding: 15px;
            border-radius: 8px;
        }
        .timetable h2, .board h2 {
            margin-bottom: 10px;
            font-size: 18px;
            color: #333;
        }
        .timetable p, .board p {
            margin: 0;
            color: #555;
        }
    </style>
</head>
<body>
    <div class="container">
        <!-- Photo Section -->
        <div class="photo">
            Lesson Photo (Placeholder)
        </div>

		
        <!-- Details Section -->
		<div class="details">
	    	<div class="title">
		        <strong>Title:</strong> <span>${lesson.title}</span>
		    </div>
		    <div class="teacherId">
		        <strong>Teacher ID:</strong> <span>${lesson.teacherId}</span>
		    </div>
		    <div class="description">
		        <strong>Description:</strong>
		        <p>${lesson.description}</p>
		    </div>
		</div>


        <!-- Timetable Section -->
        <div class="timetable">
            <h2>Timetable Section</h2>
            <p>This part is not implemented yet but can be extended later.</p>
        </div>

        <!-- Board Section -->		
		<div class="board">
		    <h2>문의 작성하기</h2>
		    <form action="insertenquiryProcBoard.do" method="post">
				<div>
					<input type="hidden" name="teacherId" value="${lesson.teacherId}">
					<input type="hidden" name="userId" value="${sessionScope.loginId}">
					<input type="hidden" name="num" value="${lesson.num}">
					<input type="hidden" name="lessonId" value="${lesson.lessonId}">
				</div>				
				<div class="input-group mb-3">
					<span class="input-group-text" id="inputGroup-sizing-default">제목</span>
					 <input type="text" class="form-control" aria-label="Sizing example input" aria-describedby="inputGroup-sizing-default"
					  	name="title">
				</div>		
				<div class="input-group mb-3">
					<span class="input-group-text" id="inputGroup-sizing-default">내용</span>
					 <input type="content" class="form-control" aria-label="Sizing example input" aria-describedby="inputGroup-sizing-default"
					  	name="content">
				</div>

				
		        <button type="submit">문의 등록</button>
		    </form>
		</div>

    </div>
</body>
</html>
