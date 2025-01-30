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
    
	<title>페이지 레이아웃</title>
	<style>
		body {
			margin: 0;
			padding: 0;
		}
		.header {
			height: 100px;
			background-color: #f8f9fa;
			border-bottom: 1px solid #dee2e6;
			display: flex;
			align-items: center;
			justify-content: center;
		}
		.container {
			display: flex;
			padding: 20px;
		}
		.main-content {
			flex: 2;
			height: 400px;
			background-color: #e9ecef;
			border: 1px solid #dee2e6;
			margin-right: 10px;
		}
		.sidebar {
			flex: 1;
			display: flex;
			flex-direction: column;
			gap: 10px;
		}
		.sidebar-item {
			height: 190px;
			background-color: #f8f9fa;
			border: 1px solid #dee2e6;
		}
	</style>
</head>
<body>
	<div class="header">
		<h1>Header Section</h1>
	</div>
	<div class="container">
		<div class="main-content">
			<p>Main Content Area</p>
		</div>
		<div class="sidebar">
			<div class="sidebar-item">Sidebar Item 1</div>
			<div class="sidebar-item">Sidebar Item 2</div>
		</div>
	</div>
</body>
</html>
