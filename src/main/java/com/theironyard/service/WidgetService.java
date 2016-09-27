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
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
public class WidgetService {

    @Autowired
    private TypeRepository typeRepository;
    @Autowired
    private WidgetRepository widgetRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    // The resourceLoader is a class provided by Spring that makes it easy to
    // find files in the resources directory. You don't need to know this, perse
    // but I'm using it to read images when populating a new database
    private ResourceLoader resourceLoader;

    public List<Type> listWidgetTypes() {
        return typeRepository.findAll(new Sort(Sort.Direction.ASC, "type"));
    }

    @Transactional
    public Page<Widget> listWidgets(Search search, Pageable pageable) {
        // take note of the getNameForSearch() method. It returns null OR the search string with % before and after it
        Type type = null;
        if(search.getTypeId() != null){
            type = typeRepository.getOne(search.getTypeId());
        }
        return widgetRepository.search(search.getNameForSearch(), type, search.getId(), pageable);
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

    public void saveWidget(Widget widget, MultipartFile file) {
        if (!file.isEmpty()) {
            // an image was submitted
            try {
                widget.setImage(file.getBytes());
                widget.setContentType(file.getContentType());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if(widget.getId() != 0) {
            // no image was submitted, but this is an existing widget.

            // when the form was submitted it didn't include the image itself.
            // we want to make sure we keep the old image so I'm reading it from
            // the database and using it to populate the new widget version.
            Widget oldWidget = widgetRepository.findOne(widget.getId());
            widget.setImage(oldWidget.getImage());
            widget.setContentType(oldWidget.getContentType());
        }

        // save the widget!
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

    public void deleteWidget(Integer id) {
        widgetRepository.delete(id);
    }

    public void createDefaultTypes() {
        if(typeRepository.count() == 0) {
            Type apparatus = new Type("Apparatus");
            typeRepository.save(apparatus);

            Type contraption = new Type("Contraption");
            typeRepository.save(contraption);

            Type contrivance = new Type("Contrivance");
            typeRepository.save(contrivance);

            Type tool = new Type("Tool");
            typeRepository.save(tool);

            Type whatchamacallit = new Type("Whatchamacallit");
            typeRepository.save(whatchamacallit);

            Type doodad = new Type("Doodad");
            typeRepository.save(doodad);

            Type doohickey = new Type("Doohickey");
            typeRepository.save(doohickey);

            Type gizmo = new Type("Gizmo");
            typeRepository.save(gizmo);

            Type thing = new Type("Thing");
            typeRepository.save(thing);

            Type thingamabob = new Type("Thingamabob");
            typeRepository.save(thingamabob);

            Type thingamajig = new Type("Thingamajig");
            typeRepository.save(thingamajig);
        }
    }

    public void createDefaultWidgets() throws IOException {
        if(widgetRepository.count() == 0) {
            Widget nut = new Widget("Hex Nut", 7 / 16d, 17 / 64d, 7 / 16d, 0.095);
            nut.getTypes().add(typeRepository.findByType("Apparatus"));
            nut.getTypes().add(typeRepository.findByType("Thingamajig"));
            nut.getTypes().add(typeRepository.findByType("Tool"));
            nut.setContentType("image/png");
            nut.setImage(readImage("./src/main/resources/static/images/nut.png"));
            widgetRepository.save(nut);

            Widget joint = new Widget("Slip-on Expansion Joint with Clamps", 1d, 1d, 1.25, 0.5);
            joint.getTypes().add(typeRepository.findByType("Contraption"));
            joint.getTypes().add(typeRepository.findByType("Thingamabob"));
            joint.getTypes().add(typeRepository.findByType("Thing"));
            joint.setContentType("image/png");
            joint.setImage(readImage("./src/main/resources/static/images/joint.png"));
            widgetRepository.save(joint);

            Widget saddle = new Widget("Flexible Saddle Tap for Pipe", 4.125, 4d, 14d, 1.1);
            saddle.getTypes().add(typeRepository.findByType("Contrivance"));
            saddle.getTypes().add(typeRepository.findByType("Doohickey"));
            saddle.setContentType("image/png");
            saddle.setImage(readImage("./src/main/resources/static/images/saddle.png"));
            widgetRepository.save(saddle);

            Widget regulator = new Widget("Gas Regulator", 3.75, 4.2, 3.75, 1.5);
            regulator.getTypes().add(typeRepository.findByType("Tool"));
            regulator.getTypes().add(typeRepository.findByType("Gizmo"));
            regulator.getTypes().add(typeRepository.findByType("Thingamajig"));
            regulator.setContentType("image/png");
            regulator.setImage(readImage("./src/main/resources/static/images/regulator.png"));
            widgetRepository.save(regulator);

            Widget speedReducer = new Widget("Flexible-Mount Right-Angle Speed Reducer", 6.5, 6.75, 5d, 8.94);
            speedReducer.getTypes().add(typeRepository.findByType("Whatchamacallit"));
            speedReducer.getTypes().add(typeRepository.findByType("Doohickey"));
            speedReducer.getTypes().add(typeRepository.findByType("Apparatus"));
            speedReducer.getTypes().add(typeRepository.findByType("Thingamajig"));
            speedReducer.setContentType("image/png");
            speedReducer.setImage(readImage("./src/main/resources/static/images/speedReducer.png"));
            widgetRepository.save(speedReducer);

            Widget demagnetizer = new Widget("Loop Demagnetizer", 4.25, 8d, 8d, 10d);
            demagnetizer.getTypes().add(typeRepository.findByType("Doodad"));
            demagnetizer.getTypes().add(typeRepository.findByType("Doohickey"));
            demagnetizer.getTypes().add(typeRepository.findByType("Apparatus"));
            demagnetizer.setContentType("image/png");
            demagnetizer.setImage(readImage("./src/main/resources/static/images/demagnetizer.png"));
            widgetRepository.save(demagnetizer);

            Widget separator = new Widget("Rare-Earth Magnetic Separator", 6d, 6d, 3d, 4.123);
            separator.getTypes().add(typeRepository.findByType("Doohickey"));
            separator.getTypes().add(typeRepository.findByType("Whatchamacallit"));
            separator.setContentType("image/png");
            separator.setImage(readImage("./src/main/resources/static/images/separator.png"));
            widgetRepository.save(separator);

            Widget lanyard = new Widget("Shock-Absorbing Fall-Arrest Lanyard", 6d, 9.5, 18.25, 310d);
            lanyard.getTypes().add(typeRepository.findByType("Gizmo"));
            lanyard.getTypes().add(typeRepository.findByType("Tool"));
            lanyard.getTypes().add(typeRepository.findByType("Thingamabob"));
            lanyard.setContentType("image/png");
            lanyard.setImage(readImage("./src/main/resources/static/images/lanyard.png"));
            widgetRepository.save(lanyard);

            Widget keyHolder = new Widget("Retractable Key Holder with Ball Socket/Belt Clip", 48d, 2.75, 2.375, 1d);
            keyHolder.getTypes().add(typeRepository.findByType("Thing"));
            keyHolder.getTypes().add(typeRepository.findByType("Contrivance"));
            keyHolder.getTypes().add(typeRepository.findByType("Whatchamacallit"));
            keyHolder.setContentType("image/png");
            keyHolder.setImage(readImage("./src/main/resources/static/images/keyHolder.png"));
            widgetRepository.save(keyHolder);

            Widget ballJoint = new Widget("Super-Swivel Ball Joint Rod End", 1.125, 2.125, 1.125, 10171d);
            ballJoint.getTypes().add(typeRepository.findByType("Thingamabob"));
            ballJoint.getTypes().add(typeRepository.findByType("Contraption"));
            ballJoint.setContentType("image/png");
            ballJoint.setImage(readImage("./src/main/resources/static/images/ballJoint.png"));
            widgetRepository.save(ballJoint);

            Widget hoistRing = new Widget("Nickel-Coated Steel Hoist Ring for Lifting", 9/16d, 13/16d, 3.25, 800d);
            hoistRing.getTypes().add(typeRepository.findByType("Thingamajig"));
            hoistRing.getTypes().add(typeRepository.findByType("Apparatus"));
            hoistRing.getTypes().add(typeRepository.findByType("Gizmo"));
            hoistRing.setContentType("image/png");
            hoistRing.setImage(readImage("./src/main/resources/static/images/hoistRing.png"));
            widgetRepository.save(hoistRing);
        }
    }

    // this is a utility function to make loading default widgets easier
    private byte[] readImage(String path){
        try {
            return Files.readAllBytes(Paths.get(path));
        } catch (IOException ignored) {
            ignored.printStackTrace();
        }
        return new byte[0];
    }
}
