package service;

import entity.Widget;
import org.junit.Before;
import org.junit.Test;
import repository.NoteRepository;
import repository.WidgetRepository;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class WidgetServiceTest {


    private WidgetService service;
    private WidgetRepository mockWidgetRepo;
    private NoteRepository mockNoteRepo;

    @Before
    public void setup() throws SQLException, IOException {
        this.mockWidgetRepo = mock(WidgetRepository.class);
        this.mockNoteRepo = mock(NoteRepository.class);

        // I want the listPeople method to return a result set
        ResultSet mockWidgetResultSet = mock(ResultSet.class);
        when(mockWidgetResultSet.getInt("id")).thenReturn(1).thenReturn(2);
        when(mockWidgetResultSet.getString("name")).thenReturn("Widget 1").thenReturn("Widget 2");
        when(mockWidgetResultSet.getDouble("width")).thenReturn(12.2).thenReturn(32.2);
        when(mockWidgetResultSet.getDouble("height")).thenReturn(12.2).thenReturn(32.2);
        when(mockWidgetResultSet.getDouble("length")).thenReturn(12.2).thenReturn(32.2);
        when(mockWidgetResultSet.getDouble("weight")).thenReturn(122.2).thenReturn(322.4);
        when(mockWidgetResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);

        when(mockWidgetRepo.listWidgets()).thenReturn(mockWidgetResultSet);

        when(mockWidgetRepo.getWidget(1)).thenReturn(mockWidgetResultSet);

        this.service = new WidgetService(mockWidgetRepo, mockNoteRepo);
    }

    @Test
    public void listWidgetsTest() throws SQLException {
        // arrange

        // act
        List<Widget> widgets = service.listWidgets();

        // assert
        assertThat(widgets.size(), is(2));
    }

    @Test
    public void getWidgetTest() throws SQLException {
        // arrange

        // act
        Widget widget = service.getWidget(1);

        // assert
        assertThat(widget.getName(), is("Widget 1"));
    }

    @Test
    public void createWidgetTest() throws IOException, SQLException {
        // arrange
        Widget widget = new Widget("Test Widget 302", 12, 12, 12, 232);

        // act
        service.createWidget(widget);

        // assert
        verify(this.mockWidgetRepo, times(1)).createWidget(widget);
    }

    @Test
    public void updateWidgetTest() throws IOException, SQLException {
        // arrange
        Widget widget = new Widget("Test Widget 302", 12, 12, 12, 232);

        // act
        service.updateWidget(widget);

        // assert
        verify(this.mockWidgetRepo, times(1)).updateWidget(widget);
    }

    @Test
    public void deleteWidgetTest() throws IOException, SQLException {
        // arrange
        Widget widget = new Widget("Test Widget 302", 12, 12, 12, 232);

        // act
        service.deleteWidget(widget);

        // assert
        verify(this.mockWidgetRepo, times(1)).deleteWidget(widget);
    }
}
