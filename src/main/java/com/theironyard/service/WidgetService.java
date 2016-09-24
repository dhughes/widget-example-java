package com.theironyard.service;

import com.theironyard.bean.Search;
import com.theironyard.entity.Note;
import com.theironyard.entity.Type;
import com.theironyard.entity.Widget;
import com.theironyard.repository.TypeRepository;
import com.theironyard.repository.WidgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WidgetService {

    @Autowired
    private TypeRepository typeRepository;
    @Autowired
    private WidgetRepository widgetRepository;

    public List<Type> listWidgetTypes() {
        return typeRepository.findAll(new Sort(Sort.Direction.ASC, "type"));
    }

    public Page<Widget> listWidgets(Search search, Pageable pageable) {
        // take note of the getNameForSearch() method. It returns null OR the search string with % before and after it
        return widgetRepository.search(search.getNameForSearch(), search.getTypeId(), search.getId(), pageable);
    }

    @Transactional // this is required to be able to load the notes
    public Widget getWidget(Integer id) {
        id = id != null ? id : 0;
        Widget widget = widgetRepository.findOne(id);

        if (widget == null) {
            return new Widget();
        } else {
            return widget;
        }
    }

    public void saveWidget(Widget widget) {
        widgetRepository.save(widget);
    }

    public void addNoteToWidget(Note note, Integer widgetId) {
        Widget widget = getWidget(widgetId);
        widget.getNotes().add(note);
        widgetRepository.save(widget);
    }
}
