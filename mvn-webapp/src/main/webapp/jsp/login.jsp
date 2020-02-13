<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<h1>Login</h1>
<form action="${pageContext.request.contextPath}/login" method="post">
	<div class="row">
		<div class="col-1">Name</div>
		<div class="col">
			<input type="text" name="name" />
		</div>
	</div>
	<div class="row">
		<div class="col-1">Password</div>
		<div class="col">
			<input type="password" name="password" />
		</div>
	</div>
	<c:if test="${not empty errorMessage}">
		<div class="row">
			<div class="col error">
				<c:out value="${errorMessage}" />
			</div>
		</div>
	</c:if>
	<div class="row">
		<div class="col">
			<input type="submit" value="Submit" />
		</div>
	</div>
</form>