package com.theironyard.controller;

import com.theironyard.bean.Search;
import com.theironyard.entity.Widget;
import com.theironyard.repository.WidgetRepository;
import com.theironyard.service.WidgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class WidgetRestController {

    @Autowired
    private WidgetService widgetService;

    @RequestMapping(path = "/widgets")
    public Page<Widget> listWidgets(Search search, @PageableDefault(size = 5, sort = "name") Pageable pageable){

        // get the widgets matching this search
        return widgetService.listWidgets(search, pageable);
    }
}
