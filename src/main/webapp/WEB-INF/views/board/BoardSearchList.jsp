<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="java.util.List" %>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ page contentType="text/html; charset=utf-8"%>    

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<link rel="stylesheet" type="text/css" href="resources/css/boardStyle.css">
<title>글 목록</title>
</head>
<body>
<div class="top"><a href="afk">[레벨업 계산기]</a><a href="logOut">[로그아웃]</a></div>
<center>
	<h1>게시판</h1>
	
	<form action="search_board" method="post">
		<table border="1" cellpadding="0" cellspacing="0" width="800">
			<tr>
				<td align="right">
					<select name="Conditon">
						<option value="all">전체</option>
						<option value="title">제목</option>
						<option value="content">내용</option>
					</select>
					<input type="text" name="Keyword"/>
					<input type="submit" value="검색"/>
				</td>
			</tr>
		</table>
	</form>
		<table border="1" cellpadding="0" cellspacing="0" width="800">
			<tr>
				<th bgcolor="orange" width="100">번호</th>
				<th bgcolor="orange" width="200">제목</th>
				<th bgcolor="orange" width="150">작성자</th>
				<th bgcolor="orange" width="150">등록일</th>
				<th bgcolor="orange" width="100">조회수</th>
			</tr>
			
			<c:forEach items="${boardList.content }" var="board">
			<tr>
				<td>${board.num }</td>
				<td align="left"><a href="getBoard?num=${board.num}">${board.heading} ${board.title }</a></td>
				<td><center>${board.name }</center></td>
				<td>${board.regDate }</td>
				<td><center>${board.read_cnt }</center></td>
			</tr>
			</c:forEach>
			<tr>
			<td colspan="5">
				<a href="BoardList">[첫 페이지로]</a>
					<c:forEach var="i" begin="1" end="${pageCnt }">
						<a href="search_board?page=${i }">[${i }]</a>
					</c:forEach>
			</td>
		</tr>
		</table>
		<br>
		<a href="insertBoard">새 글 등록</a>
	
</center>

</body>
</html>