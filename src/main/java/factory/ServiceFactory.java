package factory;

import repository.NoteRepository;
import repository.WidgetRepository;
import service.WidgetService;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by doug on 9/15/16.
 */
public class ServiceFactory {

    // the JDBC url
    private static String jdbcUrl = "jdbc:postgresql://localhost/widgets";

    // the private widgetService
    private static WidgetService widgetService;

    /**
     * This method returns a single widgetService that is shared among anything
     * that uses this method to load it.
     * @return WidgetService
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws IOException
     */
    public static WidgetService getWidgetService() throws ClassNotFoundException, SQLException, IOException {
        // if the widget service hasn't been created yet, then we need to create it.
        if(ServiceFactory.widgetService == null){
            Class.forName("org.postgresql.Driver");

            Connection connection = DriverManager.getConnection(jdbcUrl);

            WidgetRepository widgetRepository = new WidgetRepository(connection);
            NoteRepository noteRepository = new NoteRepository(connection);

            ServiceFactory.widgetService = new WidgetService(widgetRepository, noteRepository);
        }

        return widgetService;
    }
}
