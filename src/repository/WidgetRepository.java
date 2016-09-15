package repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import entity.Widget;
import entity.WidgetType;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Manages persistence of widgets
 */
public class WidgetRepository {

    private final Connection connection;

    public WidgetRepository(Connection connection) throws IOException {
        this.connection = connection;
    }

    public void createWidget(Widget widget) throws IOException, SQLException {
        PreparedStatement statement = this.connection
                .prepareStatement("INSERT INTO widget (name, widgettypeid, width, height, length, weight) VALUES (?, ?, ?, ?, ?, ?) RETURNING id");

        // set values
        statement.setString(1, widget.getName());
        statement.setInt(2, getWidgetTypeIdForWidgetTypeName(widget.getType()));
        statement.setDouble(3, widget.getWidth());
        statement.setDouble(4, widget.getHeight());
        statement.setDouble(5, widget.getLength());
        statement.setDouble(6, widget.getWeight());

        // run my query
        ResultSet result = statement.executeQuery();

        // set the ID of the widget I just persisted
        while(result.next()){
            widget.setId(result.getInt("id"));
        }
    }

    private Integer getWidgetTypeIdForWidgetTypeName(String type) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT id FROM widgettype WHERE type = ?");

        statement.setString(1, type);

        ResultSet result = statement.executeQuery();

        if(result.next()){
            return result.getInt("id");
        }

        return null;
    }

    public ResultSet listWidgets() throws SQLException {
        return connection.createStatement().executeQuery("SELECT * FROM widget ORDER BY id");
    }

    public ResultSet getWidget(int index) throws SQLException {
        PreparedStatement statement = connection
                .prepareStatement("" +
                        "SELECT w.*, t.type " +
                        "FROM widget as w LEFT JOIN widgettype as t " +
                        "   ON w.widgettypeid = t.id " +
                        "WHERE w.id = ?");
        statement.setInt(1, index);

        return statement.executeQuery();
    }

    public void updateWidget(Widget widget) throws IOException, SQLException {
        PreparedStatement statement = connection
                .prepareStatement("UPDATE widget SET name = ?, width = ?, height = ?, length = ?, weight = ? WHERE id = ?");

        statement.setString(1, widget.getName());
        statement.setDouble(2, widget.getWidth());
        statement.setDouble(3, widget.getHeight());
        statement.setDouble(4, widget.getLength());
        statement.setDouble(5, widget.getWeight());
        statement.setInt(6, widget.getId());

        statement.execute();
    }

    public void deleteWidget(Widget widget) throws IOException, SQLException {
        PreparedStatement statement = connection
                .prepareStatement("DELETE FROM widget WHERE id = ?");

        statement.setInt(1, widget.getId());

        statement.execute();
    }

    public ResultSet listWidgetTypes() throws SQLException {
        return connection.createStatement().executeQuery("SELECT * FROM widgettype ORDER BY id");
    }
}
