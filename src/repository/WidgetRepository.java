package repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import entity.Widget;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Manages persistence of widgets
 */
public class WidgetRepository {

    private final Path filePath;
    private final Gson gson;

    private ArrayList<Widget> widgets = new ArrayList<>();

    public WidgetRepository(String file) throws IOException {
        gson = new GsonBuilder().setPrettyPrinting().create();

        filePath = Paths.get(file);

        if(Files.exists(filePath)) {
            String json = new String(Files.readAllBytes(filePath));
            Type listType = new TypeToken<ArrayList<Widget>>(){}.getType();

            widgets = gson.fromJson(json, listType);
        }
    }

    public void createWidget(Widget widget) throws IOException {
        widgets.add(widget);

        persist();
    }


    public List<Widget> listWidgets() {
        return widgets;
    }

    private void persist() throws IOException {
        String json = gson.toJson(widgets);
        Files.write(filePath, json.getBytes());
    }


    public Widget getWidget(int index) {
        return widgets.get(index);
    }

    public void updateWidget(int index, Widget widget) throws IOException {
        widgets.set(index, widget);

        persist();
    }

    public void deleteWidget(int index) throws IOException {
        widgets.remove(index);

        persist();
    }
}
