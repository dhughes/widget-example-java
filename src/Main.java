import entity.Widget;
import repository.WidgetRepository;
import service.MenuService;
import service.WidgetService;

import java.io.IOException;
import java.util.List;

/**
 * Created by doug on 7/30/16.
 */
public class Main {
    public static void main(String[] args) throws IOException {

        WidgetRepository widgetRepository = new WidgetRepository("widgets.json");
        WidgetService widgetService = new WidgetService(widgetRepository);
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
                    widgetService.updateWidget(index, widget);
                } else {
                    menuService.displayNoSuchWidget();
                }

            } else if(primaryAction == MenuService.DELETE_WIDGET){
                // which widget?
                int index = menuService.promptForWidgetID();

                // delete the widget
                widgetService.deleteWidget(index);

            } else if(primaryAction == MenuService.QUIT){

                // break out of the loop
                break;

            }
        }

        System.out.println("Bye!!");

    }


}
