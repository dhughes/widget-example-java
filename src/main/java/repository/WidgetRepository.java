package repository;

import entity.Widget;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;

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
        return connection.createStatement().executeQuery("" +
                "SELECT w.*, t.type " +
                "FROM widget as w LEFT JOIN widgettype as t " +
                "   ON w.widgettypeid = t.id");
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

    public ResultSet listWidgets(String name, Integer typeId, Integer id) throws SQLException {
        String sql = "SELECT w.*, t.type " +
                "FROM widget as w LEFT JOIN widgettype as t " +
                "   ON w.widgettypeid = t.id " +
                "WHERE 1 = 1 ";

        if(name != null){
            sql += " AND lower(w.name) like lower(?) ";
        }
        if(typeId != null){
            sql += " AND t.id = ? ";
        }
        if(id != null){
            sql += " AND w.id = ? ";
        }

        PreparedStatement statement = connection.prepareStatement(sql);

        int indexOfQuestionMark = 0;
        if(name != null){
            statement.setString(++indexOfQuestionMark, "%" + name + "%");
        }
        if(typeId != null){
            statement.setInt(++indexOfQuestionMark, typeId);
        }
        if(id != null){
            statement.setInt(++indexOfQuestionMark, id);
        }

        return statement.executeQuery();
    }

    public ResultSet getWidgetType(int typeId) throws SQLException {
        PreparedStatement statement = connection
                .prepareStatement("SELECT * FROM widgetType WHERE id = ?");
        statement.setInt(1, typeId);

        return statement.executeQuery();
    }
}
