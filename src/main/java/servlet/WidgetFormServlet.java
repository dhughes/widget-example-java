package servlet;

import entity.Widget;
import entity.WidgetType;
import factory.ServiceFactory;
import service.WidgetService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by doug on 9/15/16.
 */
@WebServlet("/widgetForm")
public class WidgetFormServlet extends AbstractServlet {

    private WidgetService widgetService;

    @Override
    public void init() throws ServletException {
        try {
            // get our widget service
            this.widgetService = ServiceFactory.getWidgetService();
        } catch (ClassNotFoundException | SQLException | IOException e) {
            throw new ServletException("Something went wrong initializing servlet" + this.getClass().getCanonicalName() , e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // get widget
        try {
            // get widget id from the url
            String id = getParameterAsString(req, "id");

            // create a blank widget
            Widget widget = new Widget();

            // if we've specified an id, then get that specific widget
            if (id != null) {
                // get a specific widget
                widget = widgetService.getWidget(Integer.parseInt(id));
            }

            // put the widget into the attributes
            req.setAttribute("widget", widget);

            // widget types for dropdown
            List<WidgetType> types = widgetService.listWidgetTypes();
            req.setAttribute("types", types);

        } catch (SQLException e) {
            throw new ServletException("D'oh!", e);
        }

        // forward to the form
        req.getRequestDispatcher("WEB-INF/widgetForm.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // get the posted data

            // widget id
            Integer id = getParameterAsInt(req, "id");
            req.setAttribute("id", id);

            // name
            String name = getParameterAsString(req, "name");
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

            // get the widget type based on the numeric id that was posted
            WidgetType type = widgetService.getWidgetTypeById(typeId);

            // declare a widget
            Widget widget;

            // if the id != 0 then we're editing an existing widget
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
                // create a new widget using the provided data. No need to set the id, it's already 0
                widget = new Widget(name, type.getType(), width, length, height, weight);
            }

            // TODO: validate the data provided....
            boolean valid = true;

            // if valid (do the work above!!)
            if(valid) {

                // if the id isn't zero we're updating the widget
                if (widget.getId() != 0) {
                    widgetService.updateWidget(widget);
                } else {
                    widgetService.createWidget(widget);
                }

                // redirect to the list of widgets
                resp.sendRedirect("/listWidgets");
            } else {
                // TODO: add error messages to the attributes!!
                req.getRequestDispatcher("WEB-INF/widgetForm.jsp").forward(req, resp);
            }

        } catch (SQLException e) {
            throw new ServletException("Something went wrong", e);
        }

    }


}


