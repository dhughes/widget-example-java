package com.theironyard.controller;

import com.theironyard.bean.Login;
import com.theironyard.bean.Search;
import com.theironyard.entity.Note;
import com.theironyard.entity.User;
import com.theironyard.entity.Widget;
import com.theironyard.service.WidgetService;
import com.theironyard.util.PasswordStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Controller
public class WidgetController {

    @Autowired
    WidgetService widgetService;

    @PostConstruct
    public void afterInit(){
        widgetService.createDefaultAdminUser();
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String listWidgets(Search search, @PageableDefault(size = 5, sort = "name") Pageable pageable, BindingResult bindingResult, Model model, HttpSession session){

        // list the widget types we have
        model.addAttribute("types", widgetService.listWidgetTypes());

        // get the widgets matching this search
        Page<Widget> widgets = widgetService.listWidgets(search, pageable);
        model.addAttribute("widgets", widgets);
        model.addAttribute("pageable", pageable);

        // get the user (or null if not logged in)
        model.addAttribute("user", widgetService.getUserOrNull((Integer)session.getAttribute("userId")));

        return "widgetList";
    }

    @RequestMapping(path = "/editWidget", method = RequestMethod.GET)
    public String widgetForm(Model model, Integer id, HttpSession session){

        // the user must be logged in
        if(session.getAttribute("userId") == null){
            return "redirect:/login";
        }

        model.addAttribute("widget", widgetService.getWidget(id));

        // list the widget types we have
        model.addAttribute("types", widgetService.listWidgetTypes());

        // get the user (or null if not logged in)
        model.addAttribute("user", widgetService.getUserOrNull((Integer) session.getAttribute("userId")));

        return "widgetForm";
    }

    @RequestMapping(path = "/editWidget", method = RequestMethod.POST)
    public String editWidget(@Valid Widget widget, BindingResult bindingResult, @RequestParam(name = "file") MultipartFile file, Model model, HttpSession session){

        // the user must be logged in
        if(session.getAttribute("userId") == null){
            return "redirect:/login";
        }

        if(bindingResult.hasErrors()){
            // list the widget types we have
            model.addAttribute("types", widgetService.listWidgetTypes());

            return "widgetForm";
        } else {

            if (!file.isEmpty()) {
                try {
                    widget.setImage(file.getBytes());
                    widget.setContentType(file.getContentType());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            widgetService.saveWidget(widget);
            return "redirect:/";
        }
    }

    @RequestMapping(path = "/widgetNotes", method = RequestMethod.GET)
    public String widgetNotes(Model model, Integer id, HttpSession session){

        model.addAttribute("widget", widgetService.getWidget(id));
        model.addAttribute("note", new Note());

        // get the user (or null if not logged in)
        model.addAttribute("user", widgetService.getUserOrNull((Integer)session.getAttribute("userId")));

        return "widgetNotes";
    }

    @RequestMapping(path = "/widgetNotes", method = RequestMethod.POST)
    public String addNote(@Valid Note note, BindingResult bindingResult, Integer widgetId, Model model, HttpSession session){

        // the user must be logged in to add a note
        if(session.getAttribute("userId") == null){
            return "redirect:/login";
        }

        if(bindingResult.hasErrors()){
            model.addAttribute("widget", widgetService.getWidget(widgetId));
            return "widgetNotes";
        } else {
            widgetService.addNoteToWidget(note, widgetId);
            return "redirect:/widgetNotes?id=" + widgetId;
        }
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String loginForm(Login login, Model model){

        model.addAttribute("login", login);

        return "loginForm";
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(@Valid Login login, BindingResult bindingResult, Model model, HttpSession session){

        // try to validate the user
        User user = widgetService.authenticateUser(login);

        if(user != null){
            // record the user's id in the session
            session.setAttribute("userId", user.getId());
            return "redirect:/";
        } else {
            return "loginForm";
        }
    }

    @RequestMapping(path = "/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }

    @RequestMapping(path = "/listUsers")
    public String listUsers(Model model, HttpSession session){
        // the user must be logged in to add a note
        if(session.getAttribute("userId") == null){
            return "redirect:/login";
        }

        // get the list of users
        model.addAttribute("users", widgetService.listUsers());

        // get the current user
        model.addAttribute("user", widgetService.getUserOrNull((Integer)session.getAttribute("userId")));

        return "userList";
    }

    @RequestMapping(path = "/editUser", method = RequestMethod.GET)
    public String userForm(Integer id, Model model, HttpSession session){

        // the user must be logged in
        if(session.getAttribute("userId") == null){
            return "redirect:/login";
        }

        // get the user to edit
        model.addAttribute("editUser", widgetService.getUser(id));

        // get the current user
        model.addAttribute("user", widgetService.getUserOrNull((Integer)session.getAttribute("userId")));

        return "userForm";
    }

    @RequestMapping(path = "/editUser", method = RequestMethod.POST)
    public String editUser(@Valid @ModelAttribute(name = "editUser") User editUser, BindingResult bindingResult, Model model, HttpSession session){

        // the user must be logged in
        if(session.getAttribute("userId") == null){
            return "redirect:/login";
        }

        if(bindingResult.hasErrors()){

            // get the current user (or null)
            model.addAttribute("user", widgetService.getUserOrNull((Integer)session.getAttribute("userId")));

            return "userForm";
        } else {
            try {
                // try to save the user
                widgetService.saveUser(editUser);
                return "redirect:/listUsers";

            } catch (PasswordStorage.CannotPerformOperationException e) {
                // there was a problem encrypting the password
                // in the real world we'd want to go to some level of effor to
                // make sure we handle this correctly and provide a useful error
                // message.

                editUser.setPassword("");
                return "userForm";

            }

        }
    }

    @RequestMapping(path = "/deleteUser")
    public String deleteUser(Integer id, HttpSession session){

        // the user must be logged in
        if(session.getAttribute("userId") == null){
            return "redirect:/login";
        }

        widgetService.deleteUser(id);

        return "redirect:/listUsers";
    }

    @GetMapping("/widget/image")
    @ResponseBody
    public ResponseEntity serveFile(Integer id) throws URISyntaxException {

        Widget widget = widgetService.getWidget(id);


        if(widget.getContentType() != null){
            return ResponseEntity
                    .ok()
                    .header(HttpHeaders.CONTENT_TYPE, widget.getContentType())
                    .body(widget.getImage());
        } else {
            return ResponseEntity
                    .status(301)
                    .location(new URI("/images/questionMark.png"))
                    .build();
        }
    }


}
