package servlet;

import com.theironyard.entity.Widget;
import com.theironyard.entity.WidgetType;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("")
public class WidgetListServlet extends AbstractServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            // get the parameters from the query string (if provided, otherwise null)
            String name = getParameterAsString(req, "name");
            req.setAttribute("name", name);

            Integer typeId = getParameterAsInt(req, "typeId");
            req.setAttribute("typeId", typeId);

            Integer id = getParameterAsInt(req, "id");
            req.setAttribute("id", id);

            // list the widget types we have
            List<WidgetType> types = widgetService.listWidgetTypes();
            req.setAttribute("types", types);

            // list the widgets matching the search criteria, if any
            List<Widget> widgets = widgetService.listWidgets(name, typeId, id);
            req.setAttribute("widgets", widgets);

        } catch (SQLException e) {
            throw new ServletException("Something went wrong doing stuff!", e);
        }

        // forward to the JSP file
        req.getRequestDispatcher("WEB-INF/widgetList.html").forward(req, resp);
    }
}
