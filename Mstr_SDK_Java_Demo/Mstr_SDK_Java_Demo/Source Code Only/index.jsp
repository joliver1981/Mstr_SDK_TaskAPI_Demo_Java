<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>MicroStrategy SDK</title>
	<link rel="stylesheet" href="styles.css">
</head>
<body>
	<h1>MicroStratgy SDK Task API Demo</h1>
	<hr/>
	<form action="MSServlet" method="get">
		<label>ReportID:</label>
		<input name="reportId" type="text" />
		<input type="submit" value="Run Report" />
	</form>
</body>
</html>
