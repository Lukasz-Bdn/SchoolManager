<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@include file="../jspf/includes.jspf"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="../jspf/headconfig.jspf"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Insert title here</title>
</head>
<body>
	<c:choose>
		<c:when test="${thisSchoolAdmin != null}">
			<%@ include file="../jspf/school_admin_menu.jspf"%>
		</c:when>
		<c:when test="${thisTeacher != null}">
			<%@ include file="../jspf/teacher_menu.jspf"%>
		</c:when>
		<c:when test="${thisStudent != null}">
			<%@ include file="../jspf/student_menu.jspf"%>
		</c:when>
		<c:otherwise>
			<%@ include file="../jspf/main_menu.jspf"%>
		</c:otherwise>
	</c:choose>

	<div class="jumbotron">
		<legend>All sent message</legend>

		<table class="table table-bordered">
			<thead>
				<tr class="table-light">
					<th scope="col">TO</th>
					<th scope="col">TITLE</th>
					<th scope="col">DATE</th>
					<th scope="col">REMOVE</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${sentMessages}" var="sent">
					<c:choose>
						<c:when test="${sent.openBySender eq false}">
							<tr class="table-warning">
						</c:when>
						<c:otherwise>
							<tr class="table-light">
						</c:otherwise>
					</c:choose>
					<td scope="row"><span>${sent.receiverDescription} <b>(${sent.responsesNum})</b></span></td>
						<td><c:out value="${sent.title}" /></td>
						<td><c:out value="${sent.created}" /></td>
						<td><c:choose>
								<c:when test="${remove eq sent.id}">
									<form:form method="post">
										<%@ include file="../jspf/confirm.jspf"%>
									</form:form>
								</c:when>
								<c:otherwise>
									<div class="btn-group">
										<div class="btn-group">
											<button type="button" class="btn btn-primary dropdown-toggle"
												data-toggle="dropdown"></button>
											<div class="dropdown-menu">
												<a class="dropdown-item"
													href="${pageContext.request.contextPath}/message/view/${sent.id}">DETAILS</a>
												<a class="dropdown-item"
													href="${pageContext.request.contextPath}/message/remove/sent/${sent.id}">REMOVE</a>
											</div>
										</div>
									</div>

								</c:otherwise>
							</c:choose></td>

					</tr>
				</c:forEach>
			</tbody>
		</table>

	</div>

	<%@ include file="../jspf/footer.jspf"%>
</body>
</html>