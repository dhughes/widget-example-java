<%@ page import="entity.WidgetType" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="entity.Widget" %>
<%@ page import="entity.Note" %>
<%--
  Created by IntelliJ IDEA.
  User: doug
  Date: 9/15/16
  Time: 10:04 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Widget Notes</title>
    <link rel="stylesheet" href="/css/styles.css" />
</head>
<body>

<h1>Widget Warehouse</h1>

<ul>
    <li><a href="/">List Widgets</a></li>
    <li><a href="/widgetForm">Add a Widget</a></li>
</ul>

<h2>Widget Notes</h2>

<div class="widget">
    <img src="images/Margaret_Hamilton.gif" />

    <div class="detail">
        <a href="/widgetForm?id=${widget.getId()}">${widget.getName()}</a><br/>
        <strong>Type:</strong> ${widget.getType()}<br/>
        <strong>Width:</strong> ${widget.getWidth()}<br/>
        <strong>Height:</strong> ${widget.getHeight()}<br/>
        <strong>Length:</strong> ${widget.getLength()}<br/>
        <strong>Weight:</strong> ${widget.getWidth()}<br/>
    </div>
</div>

<table>
    <tr>
        <th>Date</th>
        <th>Note</th>
        <th></th>
    </tr>

    <% Widget widget = (Widget)request.getAttribute("widget"); %>
    <% for(Note note : widget.getNotes()){ %>
        <tr>
            <td>
                <%= note.getDate() %>
            </td>
            <td>
                <%= note.getText() %>
            </td>
            <td>
                <a href="/deleteNote?widgetId=<%= widget.getId() %>&noteId=<%= note.getId() %>"><img src="images/delete.png" alt="Delete" /></a>
            </td>
        </tr>
    <% } %>
</table>

<form action="/widgetNotes" method="post">

    <input type="hidden" name="id" value="${widget.getId()}" />

    <div>
        <label for="note" class="noteLabel">
            Add a note:
        </label>
        <textarea name="note" id="note"></textarea>

    </div>

    <button>Add Note</button>
</form>

</body>
</html>
