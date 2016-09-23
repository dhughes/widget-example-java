package com.theironyard.controller;

import com.theironyard.bean.Search;
import com.theironyard.entity.Note;
import com.theironyard.entity.Type;
import com.theironyard.entity.Widget;
import com.theironyard.service.WidgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Controller
public class WidgetController {

    @Autowired
    WidgetService widgetService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String listWidgets(Search search, BindingResult bindingResult, Model model){

        // list the widget types we have
        model.addAttribute("types", widgetService.listWidgetTypes());

        // get the widgets matching this search
        List<Widget> widgets = widgetService.listWidgets(search);
        model.addAttribute("widgets", widgets);

        return "widgetList";
    }

    @RequestMapping(path = "/editWidget", method = RequestMethod.GET)
    public String widgetForm(Model model, Integer id){

        model.addAttribute("widget", widgetService.getWidget(id));

        // list the widget types we have
        model.addAttribute("types", widgetService.listWidgetTypes());

        return "widgetForm";
    }

    @RequestMapping(path = "/editWidget", method = RequestMethod.POST)
    public String editWidget(@Valid Widget widget, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            // list the widget types we have
            model.addAttribute("types", widgetService.listWidgetTypes());

            return "widgetForm";
        } else {
            widgetService.saveWidget(widget);
            return "redirect:/";
        }
    }

    @RequestMapping(path = "/widgetNotes", method = RequestMethod.GET)
    public String widgetNotes(Model model, Integer id){

        model.addAttribute("widget", widgetService.getWidget(id));
        model.addAttribute("note", new Note());

        return "widgetNotes";
    }

    @RequestMapping(path = "/widgetNotes", method = RequestMethod.POST)
    public String addNote(@Valid Note note, BindingResult bindingResult, Integer widgetId, Model model){

        if(bindingResult.hasErrors()){
            model.addAttribute("widget", widgetService.getWidget(widgetId));
            return "widgetNotes";
        } else {
            widgetService.addNoteToWidget(note, widgetId);
            return "redirect:/widgetNotes?id=" + widgetId;
        }


    }


}
