package service;

import entity.Note;
import entity.Widget;
import entity.WidgetType;
import repository.NoteRepository;
import repository.WidgetRepository;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages widgets
 */
public class WidgetService {

    private WidgetRepository widgetRepository;
    private NoteRepository noteRepository;

    public WidgetService(WidgetRepository widgetRepository, NoteRepository noteRepository) {
        this.widgetRepository = widgetRepository;
        this.noteRepository = noteRepository;
    }

    public List<Widget> listWidgets() throws SQLException {
        ResultSet results = widgetRepository.listWidgets();

        ArrayList<Widget> widgets = new ArrayList<>();

        while (results.next()){
            Widget widget = new Widget(
                    results.getInt("id"),
                    results.getString("name"),
                    results.getString("type"),
                    results.getDouble("width"),
                    results.getDouble("height"),
                    results.getDouble("length"),
                    results.getDouble("weight")
            );
            widgets.add(widget);
        }

        return widgets;
    }

    public Widget getWidget(int id) throws SQLException {
        ResultSet results = widgetRepository.getWidget(id);

        Widget widget = null;

        if(results.next()){
            widget = new Widget(
                    results.getInt("id"),
                    results.getString("name"),
                    results.getString("type"),
                    results.getDouble("width"),
                    results.getDouble("height"),
                    results.getDouble("length"),
                    results.getDouble("weight")
            );

            // get the notes
            ResultSet noteResults = noteRepository.listNotes(widget.getId());

            while(noteResults.next()){
                Note note = new Note(
                        noteResults.getInt("id"),
                        noteResults.getString("text")
                );
                widget.getNotes().add(note);
            }
        }

        return widget;
    }

    public void createWidget(Widget widget) throws IOException, SQLException {
        widgetRepository.createWidget(widget);
    }

    public void updateWidget(Widget widget) throws IOException, SQLException {
        widgetRepository.updateWidget(widget);

        for(Note note : widget.getNotes()){
            if(note.getId() == 0){
                noteRepository.createNote(note, widget.getId());
            }
        }

    }

    public void deleteWidget(Widget widget) throws IOException, SQLException {
        widgetRepository.deleteWidget(widget);
    }

    public List<WidgetType> listWidgetTypes() throws SQLException {
        ResultSet results = widgetRepository.listWidgetTypes();

        ArrayList<WidgetType> widgetTypes = new ArrayList<>();

        while (results.next()){
            WidgetType widgetType = new WidgetType(
                    results.getInt("id"),
                    results.getString("type")
            );
            widgetTypes.add(widgetType);
        }

        return widgetTypes;
    }

    public List<Widget> listWidgets(String name, Integer typeId, Integer id) throws SQLException {
        ResultSet results = widgetRepository.listWidgets(name, typeId, id);

        ArrayList<Widget> widgets = new ArrayList<>();

        while (results.next()){
            Widget widget = new Widget(
                    results.getInt("id"),
                    results.getString("name"),
                    results.getString("type"),
                    results.getDouble("width"),
                    results.getDouble("height"),
                    results.getDouble("length"),
                    results.getDouble("weight")
            );
            widgets.add(widget);
        }

        return widgets;
    }

    public WidgetType getWidgetTypeById(int typeId) throws SQLException {
        ResultSet results = widgetRepository.getWidgetType(typeId);

        if(results.next()){
            return new WidgetType(
                    results.getInt("id"),
                    results.getString("type")
            );
        }

        return null;
    }
}
