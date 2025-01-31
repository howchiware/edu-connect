<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Teacher Main</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <h1>Teacher Dashboard</h1>

        <!-- 수강 요청 목록 -->
        <div class="mt-4">
            <h2>수강 요청 목록</h2>
            <div class="table-responsive">
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th>수업 이름</th>
                            <th>신청자</th>
                            <th>선택 시간</th>
                            <th>신청 상태</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="response" items="${responseList}">
                            <tr>
                                <td>${response.lessonName}</td>
                                <td>${response.userName} (${response.userId})</td>
                                <td>${response.selectedTime}</td>
                                <td>${response.requestsStatus}</td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty responseList}">
                            <tr>
                                <td colspan="4" class="text-center">등록된 수강 요청이 없습니다.</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
</html>
