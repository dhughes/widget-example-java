package servlet;

import entity.Note;
import entity.Widget;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;

/**
 * Created by doug on 9/15/16.
 */
@WebServlet("/widgetNotes")
public class WidgetNoteServlet extends AbstractServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            // get the widget id
            Integer id = getParameterAsInt(req, "id");
            req.setAttribute("id", id);

            // get the widget
            Widget widget = widgetService.getWidget(id);

            req.setAttribute("widget", widget);

        } catch (SQLException e) {
            throw new ServletException("Error showing widget notes", e);
        }

        req.getRequestDispatcher("WEB-INF/widgetNotes.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            // get the id of the widget we're adding a note to
            Integer id = getParameterAsInt(req, "id");
            req.setAttribute("id", id);

            // get the widget
            Widget widget = widgetService.getWidget(id);

            // get the text of the note
            String noteText = getParameterAsString(req, "note");

            // make a note
            Note note = new Note(noteText, new java.util.Date());

            // add the note to the widget
            widget.getNotes().add(note);

            // save the widget
            widgetService.updateWidget(widget);

            resp.sendRedirect("/widgetNotes?id=" + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
