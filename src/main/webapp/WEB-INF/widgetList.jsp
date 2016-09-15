<%@ page import="entity.WidgetType" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="entity.Widget" %><%--
  Created by IntelliJ IDEA.
  User: doug
  Date: 9/15/16
  Time: 10:04 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Widget Warehouse</title>
    <link rel="stylesheet" href="/css/styles.css" />
</head>
<body>

<h1>Widget Warehouse</h1>

<ul>
    <li><a href="/">List Widgets</a></li>
    <li><a href="/widgetForm">Add a Widget</a></li>
</ul>

<h2>List Widgets</h2>

<form action="/" method="get">
    <label for="name">
        Search for a widget:
    </label>
    <input type="text" name="name" id="name" value="${name}" placeholder="Name">

    <select name="typeId">
        <option></option>
        <% for(WidgetType widgetType : (ArrayList<WidgetType>)request.getAttribute("types")) { %>
            <option value="<%= widgetType.getId() %>" <%= request.getAttribute("typeId") != null && widgetType.getId() == (int)request.getAttribute("typeId") ? "selected='true'" : "" %>  >
                <%= widgetType.getType() %>
            </option>
        <% } %>
    </select>

    <input type="text" name="id" id="id" value="${id}" placeholder="Id">

    <button>Search</button>
</form>

<!-- main section of my page -->
<section>
    <% for(Widget widget : (ArrayList<Widget>)request.getAttribute("widgets")) { %>

        <div class="widget">
            <img src="images/Margaret_Hamilton.gif" />

            <div class="detail">
                <a href="/widgetForm?id=<%= widget.getId() %>"><%= widget.getName()%></a><br/>
                <strong>Type:</strong> <%= widget.getType()%><br/>
                <strong>Width:</strong> <%= widget.getWidth()%><br/>
                <strong>Height:</strong> <%= widget.getHeight()%><br/>
                <strong>Length:</strong> <%= widget.getLength()%><br/>
                <strong>Weight:</strong> <%= widget.getWeight()%>
            </div>
        </div>

    <% } %>

</section>

</body>
</html>
