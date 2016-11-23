<html>
<head><title>First JSP</title></head>
<body>

<h2>The date and time when the page was requested is</h2>
<p>(<%out.println(new java.util.Date().toString()); %>)</p>

<h2>The broswer that requested the page is</h2>
<p>(<%out.println(request.getHeader("User-Agent")); %>)</p>

<h2>The IP address of the broswer that requested the page is</h2>
<p>(<%out.println(request.getRemoteAddr()); %>)</p>

<!-- ${pageContext.request.contextPath} -->
  <a href="<%= request.getRequestURI() %>"><h3>Try Again</h3></a>
</body>
</html>