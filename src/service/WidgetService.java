package service;

import entity.Widget;
import repository.WidgetRepository;

import java.io.IOException;
import java.util.List;

/**
 * Manages widgets
 */
public class WidgetService {

    private WidgetRepository widgetRepository;

    public WidgetService(WidgetRepository widgetRepository) {
        this.widgetRepository = widgetRepository;
    }

    public List<Widget> listWidgets() {
        return widgetRepository.listWidgets();
    }

    public Widget getWidget(int index) {
        try {
            return widgetRepository.getWidget(index);
        } catch (IndexOutOfBoundsException e){
            return null;
        }
    }

    /**
     *
     * @param widget
     * @throws IOException
     */
    public void createWidget(Widget widget) throws IOException {
        widgetRepository.createWidget(widget);
    }

    public void updateWidget(int index, Widget widget) throws IOException {
        widgetRepository.updateWidget(index, widget);
    }

    public void deleteWidget(int index) throws IOException {
        try {
            widgetRepository.deleteWidget(index);
        } catch (IndexOutOfBoundsException e){
            // do nothing
        }
    }
}
