package repository;

import com.theironyard.entity.Note;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NoteRepository {


    private final Connection connection;

    public NoteRepository(Connection connection) throws IOException {
        this.connection = connection;
    }

    public void createNote(Note note, int widgetId) throws SQLException {
        PreparedStatement statement = this.connection
                .prepareStatement("INSERT INTO note (text, widgetid) VALUES (?, ?) RETURNING id");

        // set values
        statement.setString(1, note.getText());
        statement.setInt(2, widgetId);

        // run my query
        ResultSet result = statement.executeQuery();

        // set the ID of the widget I just persisted
        while(result.next()){
            note.setId(result.getInt("id"));
        }
    }

    public ResultSet listNotes(int widgetId) throws SQLException {
        PreparedStatement statement = this.connection
                .prepareStatement("SELECT * FROM note WHERE widgetid = ?");

        statement.setInt(1, widgetId);

        return statement.executeQuery();

    }

    public void deleteNoteById(Integer noteId) throws SQLException {
        PreparedStatement statement = this.connection
                .prepareStatement("DELETE FROM note WHERE id = ?");

        statement.setInt(1, noteId);

        statement.execute();
    }


}
