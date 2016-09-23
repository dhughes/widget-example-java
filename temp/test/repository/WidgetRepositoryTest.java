package repository;

import com.theironyard.entity.Widget;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import static junit.framework.TestCase.fail;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;

public class WidgetRepositoryTest {

    private WidgetRepository repo;

    @Before
    public void setup() throws SQLException, IOException {
        String jdbcUrl = "jdbc:postgresql://localhost/widgetstest";
        Connection connection = DriverManager.getConnection(jdbcUrl);

        connection.createStatement().execute("DELETE FROM note");
        connection.createStatement().execute("DELETE FROM widget");

        this.repo = new WidgetRepository(connection);
    }

    @Test
    public void createWidgetTest() throws IOException, SQLException {
        // arrange
        Widget widget = new Widget("Test Widget", 12, 12, 12, 100);

        // act
        this.repo.createWidget(widget);

        // assert
        assertThat(widget.getId(), not(equals(0)));
    }

    @Test
    public void listWidgetsTest() throws SQLException, IOException {
        // arrange
        Widget widget = new Widget("Test Widget 2", 12, 12, 12, 100);
        this.repo.createWidget(widget);

        // act
        ResultSet widgets = repo.listWidgets();

        // assert
        if(widgets.next()){
            assertThat(widgets.getString("name"), is("Test Widget 2"));
        } else {
            fail("Didn't get any rows!");
        }

    }

    @Test
    public void getWidgetTest() throws IOException, SQLException {
        // arrange
        Widget widget = new Widget("Test Widget 3", 12, 12, 12, 100);
        this.repo.createWidget(widget);

        // act
        ResultSet widgetRes = repo.getWidget(widget.getId());

        // assert
        if(widgetRes.next()){
            assertThat(widgetRes.getString("name"), is("Test Widget 3"));
        } else {
            fail("Didn't get any rows!");
        }
    }

    @Test
    public void updateWidgetTest() throws IOException, SQLException {
        // arrange
        Widget widget = new Widget("Test Widget 4", 12, 12, 12, 100);
        this.repo.createWidget(widget);

        widget.setName("Test Widget 4 Updated!");
        widget.setWeight(200.4);

        // act
        this.repo.updateWidget(widget);

        // assert
        assertThat(widget.getName(), is("Test Widget 4 Updated!"));
        assertThat(widget.getWeight(), is(200.4));
    }

    @Test
    public void deleteWidgetTest() throws IOException, SQLException {
        // arrange
        Widget widget = new Widget("Test Widget 4", 12, 12, 12, 100);
        this.repo.createWidget(widget);

        // act
        this.repo.deleteWidget(widget);

        // assert
        ResultSet widget2 = this.repo.getWidget(widget.getId());
        assertThat(widget2.next(), is(false));
    }


}
