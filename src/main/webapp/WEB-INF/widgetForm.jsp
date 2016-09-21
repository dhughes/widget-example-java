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
    <title>Add / Edit Widget</title>
    <link rel="stylesheet" href="/css/styles.css" />
</head>
<body>

<h1>Widget Warehouse</h1>

<ul>
    <li><a href="/">List Widgets</a></li>
    <li><a href="/widgetForm">Add a Widget</a></li>
</ul>

<h2>Add / Edit a Widget</h2>

<p>All fields are required</p>

**${errorMessages}**

<form action="/widgetForm" method="post">

    <input type="hidden" name="id" value="${widget.getId()}" />

    <div>
        <label for="name">
            Name:
        </label>
        <input type="text" name="name" id="name" value="${widget.getName()}">
    </div>

    <div>
        <label for="typeid">
            Type:
        </label>
        <select name="typeId" id="typeid">
            <option></option>
            <% for(WidgetType widgetType : (ArrayList<WidgetType>)request.getAttribute("types")) { %>
                <option value="<%= widgetType.getId() %>" <%= widgetType.getType().equals(((Widget)request.getAttribute("widget")).getType()) ? "selected='true'" : "" %>>
                    <%= widgetType.getType() %>
                </option>
            <% } %>
        </select>
    </div>

    <div>
        <label for="width">
            Width:
        </label>
        <input type="text" name="width" id="width" value="${widget.getWidth()}">
    </div>

    <div>
        <label for="length">
            Length:
        </label>
        <input type="text" name="length" id="length" value="${widget.getLength()}">
    </div>

    <div>
        <label for="height">
            Height:
        </label>
        <input type="text" name="height" id="height" value="${widget.getHeight()}">
    </div>

    <div>
        <label for="weight">
            Weight:
        </label>
        <input type="text" name="weight" id="weight" value="${widget.getLength()}">
    </div>

    <!-- image upload would go here -->

    <button>Save Widget</button>
</form>

</body>
</html>
