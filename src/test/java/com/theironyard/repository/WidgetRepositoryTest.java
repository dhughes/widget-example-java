package com.theironyard.repository;

import com.theironyard.entity.Type;
import com.theironyard.entity.Widget;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class WidgetRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private WidgetRepository widgetRepository;

    Type type1;
    Type type2;
    Type type3;
    Widget widget1;
    Widget widget2;
    Widget widget3;
    Widget widget4;

    @Before
    public void setup(){
        // setup some types
        type1 = new Type("type 1");
        type2 = new Type("type 2");
        type3 = new Type("type 3");
        // persist the types
        entityManager.persist(type1);
        entityManager.persist(type2);
        entityManager.persist(type3);

        // setup some widgets
        widget1 = new Widget("widget 1", 1d, 2d, 3d, 4d);
        widget1.getTypes().add(type1);

        widget2 = new Widget("widget experiment 2", 2d, 3d, 4d, 1d);
        widget2.getTypes().add(type2);

        widget3 = new Widget("test widget 3", 3d, 4d, 1d, 2d);
        widget3.getTypes().add(type1);

        widget4 = new Widget("test 4", 4d, 1d, 2d, 3d);
        widget4.getTypes().add(type3);

        // persist the widgets
        entityManager.persist(widget1);
        entityManager.persist(widget2);
        entityManager.persist(widget3);
        entityManager.persist(widget4);
    }

    /**
     * Given no name, type, or id
     * When searching for widgets
     * Then all widgets are returned.
     */
    @Test
    public void whenSearchingForEverythingThenEverythingReturned(){
        // arrange

        // act
        List<Widget> widgets = widgetRepository.search("", null, null);

        // assert
        assertThat(widgets.size(), is(4));
    }

    /**
     * Given a name that matches three records, and no type or id
     * When searching for widgets
     * Then three widgets are returned.
     */
    @Test
    public void whenSearchingForWidgetThenThreeReturned(){
        // arrange

        // act
        List<Widget> widgets = widgetRepository.search("%widget%", null, null);

        // assert
        assertThat(widgets.size(), is(3));
    }

    /**
     * Given a name that matches one record, and no type or id
     * When searching for widgets
     * Then one widget is returned.
     */
    @Test
    public void whenSearchingForExampleThenOneReturned(){
        // arrange

        // act
        List<Widget> widgets = widgetRepository.search("%experiment%", null, null);

        // assert
        assertThat(widgets.size(), is(1));
    }

    /**
     * Given a name that matches two widgets, but with different casing, no type and no id
     * When searching for widgets
     * Then widgets are returned despite their case.
     */
    @Test
    public void whenSearchingForTestCaseInsensitiveThenTwoReturned(){
        // arrange

        // act
        List<Widget> widgets = widgetRepository.search("%TesT%", null, null);

        // assert
        assertThat(widgets.size(), is(2));
    }

    /**
     * Given a name and type that match two widgets, but no id
     * When searching for widgets
     * Then two widgets are returned.
     */
    @Test
    public void whenSearchingForWidgetAndType1ThenTwoReturned(){
        // arrange

        // act
        List<Widget> widgets = widgetRepository.search("%widget%", type1, null);

        // assert
        assertThat(widgets.size(), is(2));
    }

    /**
     * Given a type that matches two widgets, no name or id
     * When searching for widgets
     * Then two widgets are returned.
     */
    @Test
    public void whenSearchingForType1ThenTwoReturned(){
        // arrange

        // act
        List<Widget> widgets = widgetRepository.search("", type1, null);

        // assert
        assertThat(widgets.size(), is(2));
    }

    /**
     * Given a id, no name or type
     * When searching for widgets
     * Then one widget is returned.
     */
    @Test
    public void whenSearchingForIdThenOneReturned(){
        // arrange
        Integer id = widget3.getId();

        // act
        List<Widget> widgets = widgetRepository.search("", null, id);

        // assert
        assertThat(widgets.size(), is(1));
    }

    /**
     * Given a id and a name and a type that all match one record
     * When searching for widgets
     * Then one widget is returned.
     */
    @Test
    public void whenSearchingForEverythingMatchingThenOneReturned(){
        // arrange

        // act
        List<Widget> widgets = widgetRepository.search("%widget%", widget3.getTypes().get(0), widget3.getId());

        // assert
        assertThat(widgets.size(), is(1));
    }

}
