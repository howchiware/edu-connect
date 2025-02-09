<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
	
	<!-- 부트스트랩 CSS 라이브버리-->
	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
	<!-- 부트스트랩 Js 라이브러리-->
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
    
	<title>회원가입</title>
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
	
	<div class="container-fluid mt-4">
	    <a class="navbar-brand" href="#">
	        <p onclick="location.href='mainBoard.do'">✌️ 수업 예약 사이트</p>
	    </a>
	</div>

	<form action="joinBoardProc.do" method="post">
		<div class="input-group mb-3">
			<span class="input-group-text" id="inputGroup-sizing-default">아이디</span>
			 <input type="text" class="form-control" name="id" placeholder="ID">
		</div>
		<div class="input-group mb-3">
			<span class="input-group-text" id="inputGroup-sizing-default">패스워드</span>
			<input type="text" class="form-control" name="pwd" placeholder="PASSWORD" >
		</div>	
		<div class="input-group mb-3">
			<span class="input-group-text" id="inputGroup-sizing-default">이름</span>
			<input type="text" class="form-control" name="name" placeholder="NAME">
		</div>			

	  <div class="d-grid gap-2 d-md-flex justify-content-md-end">
		  <button type="submit" class="btn btn-primary">회원가입</button>
		  <button type="reset" class="btn btn-danger">초기화</button>
	  </div>
	</form>		

	
</body>
</html>