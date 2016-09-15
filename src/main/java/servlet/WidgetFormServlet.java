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

/**
 * Created by doug on 9/15/16.
 */
@WebServlet("/widgetForm")
public class WidgetFormServlet extends HttpServlet {

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



        // get widget
        try {
            // get widget id
            String id = req.getParameter("id");

            Widget widget = new Widget();

            if (id != null) {
                // get a specific widget
                widget = widgetService.getWidget(Integer.parseInt(id));
            }

            req.setAttribute("widget", widget);

            // widget types for dropdown
            List<WidgetType> types = widgetService.listWidgetTypes();
            req.setAttribute("types", types);

        } catch (SQLException e) {
            throw new ServletException("D'oh!", e);
        }

        req.getRequestDispatcher("WEB-INF/widgetForm.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // widget id
            Integer id = getParameterAsInt(req, "id");
            req.setAttribute("id", id);

            // name
            String name = req.getParameter("name");
            name = name == null || name.equals("") ? null : name;
            req.setAttribute("name", name);

            // type
            Integer typeId = getParameterAsInt(req, "typeId");
            req.setAttribute("typeId", typeId);

            // width
            Double width = getParameterAsDouble(req, "width");
            req.setAttribute("width", width);

            // height
            Double height = getParameterAsDouble(req, "height");
            req.setAttribute("height", height);

            // length
            Double length = getParameterAsDouble(req, "length");
            req.setAttribute("length", length);

            // weight
            Double weight = getParameterAsDouble(req, "weight");
            req.setAttribute("weight", weight);

            // get the widget type
            WidgetType type = widgetService.getWidgetTypeById(typeId);

            Widget widget;
            if (id != 0) {
                // get the widget I'm editing
                widget = widgetService.getWidget(id);

                // update the widget data
                widget.setName(name);
                widget.setType(type.getType());
                widget.setWidth(width);
                widget.setHeight(height);
                widget.setLength(length);
                widget.setWeight(weight);
            } else {
                widget = new Widget(name, type.getType(), width, length, height, weight);
            }

            // TODO: validate the data provided....
            boolean valid = true;

            // if valid (do the work above!!)
            if(valid) {

                if (id != 0) {
                    widgetService.updateWidget(widget);
                } else {
                    widgetService.createWidget(widget);
                }

                // redirect to the list
                resp.sendRedirect("/listWidgets");
            } else {
                // add the error messages to the attributes!!
                req.getRequestDispatcher("WEB-INF/widgetForm.jsp").forward(req, resp);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    private Integer getParameterAsInt(HttpServletRequest req, String name) {

        String param = req.getParameter(name);
        // make "" be null
        param = param == null || param.equals("") ? null : param;

        Integer paramAsInteger = null;
        if (param != null) {
            paramAsInteger = Integer.parseInt(param);
        }

        return paramAsInteger;
    }



    private Double getParameterAsDouble(HttpServletRequest req, String name) {

        String param = req.getParameter(name);
        // make "" be null
        param = param == null || param.equals("") ? null : param;

        Double paramAsInteger = null;
        if (param != null) {
            paramAsInteger = Double.parseDouble(param);
        }

        return paramAsInteger;
    }
}


