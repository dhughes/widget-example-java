package servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by doug on 9/15/16.
 */
@WebServlet("/deleteNote")
public class DeleteWidgetServlet extends AbstractServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            // get the widget id
            Integer widgetId = getParameterAsInt(req, "widgetId");

            // get the note id
            Integer noteId = getParameterAsInt(req, "noteId");

            // delete the note
            widgetService.deleteNoteById(noteId);

            // redirect back to the list of notes
            resp.sendRedirect("/widgetNotes?id=" + widgetId);
        } catch (SQLException e) {
            throw new ServletException("Couldn't delete a note", e);
        }
    }
}
