package servlet;

import factory.ServiceFactory;
import com.theironyard.service.WidgetService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;

public class AbstractServlet extends HttpServlet{

    WidgetService widgetService;

    @Override
    public void init() throws ServletException {
        try {
            // get our widget service
            this.widgetService = ServiceFactory.getWidgetService();
        } catch (ClassNotFoundException | SQLException | IOException e) {
            throw new ServletException("Something went wrong initializing servlet" + this.getClass().getCanonicalName() , e);
        }
    }

    Integer getParameterAsInt(HttpServletRequest req, String name) {

        String param = req.getParameter(name);
        // make "" be null
        param = param == null || param.equals("") ? null : param;

        Integer paramAsInteger = null;
        if (param != null) {
            paramAsInteger = Integer.parseInt(param);
        }

        return paramAsInteger;
    }

    Double getParameterAsDouble(HttpServletRequest req, String name) {

        String param = req.getParameter(name);
        // make "" be null
        param = param == null || param.equals("") ? null : param;

        Double paramAsInteger = null;
        if (param != null) {
            paramAsInteger = Double.parseDouble(param);
        }

        return paramAsInteger;
    }

    String getParameterAsString(HttpServletRequest req, String name) {
        String param = req.getParameter(name);

        return param == null || param.equals("") ? null : param;
    }
}
