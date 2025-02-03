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
    
	<title></title>
	<style>
		body {
			
		}
		

	</style>
</head>
<body>
	
	<div class="container-fluid">
	  <a class="navbar-brand" href="#">
	    <img src="/images/logo.png" alt="Logo" width="30" height="24" class="d-inline-block align-text-top" onclick="location.href='mainBoard.do'">
	    User Modify Board
	  </a>
	</div>

	<form action="usermodifyBoardroc.do" method="post">
		<div class="input-group mb-3">
			<input type="hidden" name="num" value="${usertable.num}">
		</div>
		<div class="input-group mb-3">
			<span class="input-group-text" id="inputGroup-sizing-default">아이디</span>
			 <input type="text" class="form-control" name="id" placeholder="ID"
			 value="${usertable.id}" readonly>
		</div>
		<div class="input-group mb-3">
			<span class="input-group-text" id="inputGroup-sizing-default">패스워드</span>
			<input type="text" class="form-control" name="pwd" placeholder="PASSWORD" value="${usertable.pwd}">
		</div>	
		<div class="input-group mb-3">
			<span class="input-group-text" id="inputGroup-sizing-default">이름</span>
			<input type="text" class="form-control" name="name" placeholder="NAME" value="${usertable.name}">
		</div>			

	  <div class="d-grid gap-2 d-md-flex justify-content-md-end">
		  <button type="submit" class="btn btn-primary">수정</button>
		  <button type="reset" class="btn btn-danger">초기화</button>
	  </div>
	</form>		

	
</body>
</html>