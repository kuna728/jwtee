<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Dictionary</title>
</head>
<body>
<h1>
    Add to dictionary
</h1>
<br/>
<a href="ListServlet">Show dictionary</a>
<form method="post" action="hello-servlet">
    <label for="key">Key</label>
    <input id="key" name="key" type="text" /><br/>
    <label for="value">Value</label>
    <input id="value" name="value" type="text"/><br/>
    <button type="submit">Send</button>
</form>
</body>
</html>