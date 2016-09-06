import entity.Widget;
import repository.NoteRepository;
import repository.WidgetRepository;
import service.MenuService;
import service.WidgetService;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by doug on 7/30/16.
 */
public class Main {
    public static void main(String[] args) throws IOException, SQLException {

        String jdbcUrl = "jdbc:postgresql://localhost/widgets";
        Connection connection = DriverManager.getConnection(jdbcUrl);

        WidgetRepository widgetRepository = new WidgetRepository(connection);
        NoteRepository noteRepository = new NoteRepository(connection);

        WidgetService widgetService = new WidgetService(widgetRepository, noteRepository);
        MenuService menuService = new MenuService();

        while(true) {
            int primaryAction = menuService.promptForMainMenuSelection();

            if(primaryAction == MenuService.CREATE_WIDGET){
                // collect the widget data
                Widget widget = menuService.promptForWidgetData();

                // save the widget
                widgetService.createWidget(widget);

            } else if(primaryAction == MenuService.LIST_WIDGETS){
                // list the widgets
                List<Widget> widgets = widgetService.listWidgets();

                // show the list of widgets
                menuService.displayWidgets(widgets);

            } else if(primaryAction == MenuService.VIEW_WIDGET){
                // which widget?
                int index = menuService.promptForWidgetID();

                // read the widget
                Widget widget = widgetService.getWidget(index);

                if(widget != null){
                    // display the widget
                    menuService.displayWidget(widget);
                } else {
                    menuService.displayNoSuchWidget();
                }

            } else if(primaryAction == MenuService.EDIT_WIDGET){
                // which widget?
                int index = menuService.promptForWidgetID();

                // read the widget
                Widget widget = widgetService.getWidget(index);

                if(widget != null){
                    // update the widget data
                    widget = menuService.promptForWidgetData(widget);

                    // update the widget
                    widgetService.updateWidget(widget);
                } else {
                    menuService.displayNoSuchWidget();
                }

            } else if(primaryAction == MenuService.DELETE_WIDGET){
                // which widget?
                int id = menuService.promptForWidgetID();
                Widget widget = widgetService.getWidget(id);

                // delete the widget
                widgetService.deleteWidget(widget);


            } else if(primaryAction == MenuService.ADD_NOTE){
                // which widget?
                int id = menuService.promptForWidgetID();
                Widget widget = widgetService.getWidget(id);

                // prompt for widget note
                menuService.promptForWidgetNoteData(widget);

                // save my widget
                widgetService.updateWidget(widget);

            } else if(primaryAction == MenuService.QUIT){

                // break out of the loop
                break;

            }
        }

        System.out.println("Bye!!");

    }


}
