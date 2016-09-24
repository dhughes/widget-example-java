package com.theironyard.service;

import com.theironyard.bean.Login;
import com.theironyard.bean.Search;
import com.theironyard.entity.Note;
import com.theironyard.entity.Type;
import com.theironyard.entity.User;
import com.theironyard.entity.Widget;
import com.theironyard.repository.TypeRepository;
import com.theironyard.repository.UserRepository;
import com.theironyard.repository.WidgetRepository;
import com.theironyard.util.PasswordStorage;
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
    @Autowired
    private UserRepository userRepository;

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

    public User getUser(Integer id) {
        if(id != null) {
            return userRepository.findOne(id);
        } else {
            return new User();
        }
    }

    public User getUserOrNull(Integer id){
        if(id != null) {
            return userRepository.findOne(id);
        } else {
            return null;
        }
    }

    public User authenticateUser(Login login) {

        try {
            // get the user by email address
            User user = userRepository.findByEmail(login.getEmail());

            // did we find the user? does their password validate?
            if(user != null && PasswordStorage.verifyPassword(login.getPassword(), user.getPassword())){
                return user;
            }
        } catch (PasswordStorage.CannotPerformOperationException | PasswordStorage.InvalidHashException e) {
            e.printStackTrace();
        }

        // user did not validate
        login.setLoginFailed(true);
        return null;
    }

    public void createDefaultAdminUser() {
        if(userRepository.count() == 0){
            try {
                userRepository.save(new User("Default Administrator", "admin@admin.com", PasswordStorage.createHash("password")));
            } catch (PasswordStorage.CannotPerformOperationException e) {
                e.printStackTrace();
            }
        }
    }

    public List<User> listUsers() {
        return userRepository.findAll();
    }

    public void saveUser(User user) throws PasswordStorage.CannotPerformOperationException {
        /*
            The user may have changed their password. If they did, it's not
            going to be their old hashed password. We'll read the user's current
            values (if they're not new) and only change the password if it doesn't
            match the hash of the old one.
        */

        // is this an existing user being saved?
        if(user.getId() != null){
            // read the old version of the user
            User oldUser = userRepository.findOne(user.getId());

            // is the old hashed password the different than the current password?
            if(!oldUser.getPassword().equals(user.getPassword())){
                // we have a new password
                user.setPassword(PasswordStorage.createHash(user.getPassword()));
            }
        } else {
            // this is a new user
            user.setPassword(PasswordStorage.createHash(user.getPassword()));
        }

        userRepository.save(user);
    }

    public void deleteUser(Integer id) {
        userRepository.delete(id);
    }
}
