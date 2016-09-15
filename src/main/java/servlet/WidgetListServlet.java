package servlet;

import entity.Widget;
import entity.WidgetType;
import repository.NoteRepository;
import repository.WidgetRepository;
import service.WidgetService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/listWidgets")
public class WidgetListServlet extends HttpServlet {

    private WidgetService widgetService;

    @Override
    public void init() throws ServletException {
        try {
            Class.forName("org.postgresql.Driver");

            String jdbcUrl = "jdbc:postgresql://localhost/widgets";
            Connection connection = DriverManager.getConnection(jdbcUrl);

            WidgetRepository widgetRepository = new WidgetRepository(connection);
            NoteRepository noteRepository = new NoteRepository(connection);

            this.widgetService = new WidgetService(widgetRepository, noteRepository);

        } catch (ClassNotFoundException | SQLException | IOException e) {
            throw new ServletException("Something went wrong starting up!", e);
        }


    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            String name = req.getParameter("name");
            name = name == null || name.equals("") ? null : name;
            req.setAttribute("name", name);

            String typeId = req.getParameter("typeId");
            typeId = typeId == null || typeId.equals("") ? null : typeId;
            Integer typeIdInt = null;
            if (typeId != null) {
                typeIdInt = Integer.parseInt(typeId);
            }
            req.setAttribute("typeId", typeIdInt);
            req.setAttribute("hasSelectedTypeId", typeId != null);

            String id = req.getParameter("id");
            id = id == null || id.equals("") ? null : id;
            Integer idInt = null;
            if (id != null) {
                idInt = Integer.parseInt(id);
            }
            req.setAttribute("id", idInt);

            List<WidgetType> types = widgetService.listWidgetTypes();
            req.setAttribute("types", types);

            List<Widget> widgets = widgetService.listWidgets(name, typeIdInt, idInt);
            req.setAttribute("widgets", widgets);

        } catch (SQLException e) {
            throw new ServletException("Something went wrong doing stuff!", e);
        }

        req.getRequestDispatcher("WEB-INF/widgetList.jsp").forward(req, resp);
    }
}
