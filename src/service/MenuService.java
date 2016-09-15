package service;

import entity.Note;
import entity.Widget;
import entity.WidgetType;

import java.util.List;
import java.util.Scanner;

/**
 * Manages textual menus
 */
public class MenuService {

    public static final int CREATE_WIDGET = 1;
    public static final int LIST_WIDGETS = 2;
    public static final int VIEW_WIDGET = 3;
    public static final int EDIT_WIDGET = 4;
    public static final int DELETE_WIDGET = 5;
    public static final int ADD_NOTE = 6;
    public static final int QUIT = 7;

    public static final int GEAR_WIDGET = 1;
    public static final int SPRING_WIDGET = 2;

    Scanner scanner = new Scanner(System.in);

    private int waitForInt(String message, int min, int max) {

        System.out.println(message);

        String input = scanner.nextLine();

        int value;
        try {
            value = Integer.parseInt(input);

            if(!(value >= min && value <= max)){
                throw new Exception("Invalid option.");
            }
        } catch(Exception e){
            System.out.println("\nPlease provide a number between " + min + " and " + max + ".\n");

            value = waitForInt(message, min, max);
        }

        return value;
    }

    private int waitForInt(String message) {

        System.out.println(message);

        String input = scanner.nextLine();

        int value;
        try {
            value = Integer.parseInt(input);

        } catch(Exception e){
            System.out.println("\nPlease provide a number.\n");

            value = waitForInt(message);
        }

        return value;
    }

    private Double waitForDouble(String message, boolean required){
        System.out.println(message);

        String input = scanner.nextLine();

        Double value = null;
        try {
            value = Double.parseDouble(input);

        } catch(Exception e){
            if(required) {
                System.out.println("\nPlease provide a number.\n");

                value = waitForDouble(message, required);
            }
        }

        return value;
    }

    private Double waitForDouble(String message, Double defaultValue){
        Double value = waitForDouble(message, false);

        if(value == null){
            return defaultValue;
        } else {
            return value;
        }
    }

    private String waitForString(String message, boolean required) {
        System.out.println(message);

        String value =  scanner.nextLine();

        if(required && value.trim().length() == 0){
            System.out.println("\nPlease provide a value.\n");

            value = waitForString(message, required);
        }

        return value.trim();
    }

    private String waitForString(String message, String defaultValue){
        String value = waitForString(message, false);

        if(value.isEmpty()){
            return defaultValue;
        } else {
            return value;
        }
    }

    public int promptForMainMenuSelection() {
        System.out.println("\n-- Main Menu --\n" +
                "\n" +
                "1) Create a Widget\n" +
                "2) List Widgets\n" +
                "3) View a Widget\n" +
                "4) Edit a Widget\n" +
                "5) Delete a Widget\n" +
                "6) Add a note to a Widget\n" +
                "7) Quit\n");

        return waitForInt("Please choose an option:", 1, 6);
    }

    public void promptForWidgetNoteData(Widget widget) {
        displayWidget(widget);

        System.out.printf("\n-- Create a note on 'Widget %s' --\n\n", widget.getName());

        String text = waitForString("Note: ", true);

        Note note = new Note(text);

        widget.getNotes().add(note);
    }

    private String waitForStringInList(String prompt, List<WidgetType> options){

        String validOptions = "";
        for (WidgetType type : options){
            validOptions += type.getType() + ", ";
        }

        String fullPrompt = prompt + "(" + validOptions.substring(0, validOptions.length()-2) + ")";

        String input = waitForString(fullPrompt, true);

        // make sure my input is one of the valid options
        for(WidgetType type : options){
            if(type.getType().equals(input)){
                return input;
            }
        }

        System.out.println("Please select from the list of valid options!");

        // prompt until someone does something not wrong
        return waitForStringInList(prompt, options);
    }

    private String waitForStringInList(String prompt, List<WidgetType> options, String def){

        String validOptions = "";
        for (WidgetType type : options){
            validOptions += type.getType() + ", ";
        }

        String fullPrompt = prompt + "(" + validOptions.substring(0, validOptions.length()-2) + ")";

        String input = waitForString(fullPrompt, def);

        // make sure my input is one of the valid options
        for(WidgetType type : options){
            if(type.getType().equals(input)){
                return input;
            }
        }

        System.out.println("Please select from the list of valid options!");

        // prompt until someone does something not wrong
        return waitForStringInList(prompt, options);


    }

    public Widget promptForWidgetData(List<WidgetType> options) {
        System.out.println("\n-- Create a Widget --\n");

        String name = waitForString("Name: ", true);
        String type = waitForStringInList("Type: ", options);
        double width = waitForDouble("Width: ", true);
        double height = waitForDouble("Height: ", true);
        double length = waitForDouble("Length: ", true);
        double weight = waitForDouble("Weight: ", true);

        return new Widget(name, type, width, height, length, weight);
    }

    public Widget promptForWidgetData(Widget widget, List<WidgetType> options) {
        System.out.println("\n-- Edit a Widget --\n");

        String name = waitForString(
                String.format("Name [%s]: ", widget.getName()),
                widget.getName());
        String type = waitForStringInList(
                String.format("Type [%s]: ", widget.getType()),
                options,
                widget.getType());
        double width = waitForDouble(
                String.format("Width [%s]: ", widget.getWidth()),
                widget.getWidth());
        double height = waitForDouble(
                String.format("Height [%s]: ", widget.getHeight()),
                widget.getHeight());
        double length = waitForDouble(
                String.format("Length [%s]: ", widget.getLength()),
                widget.getLength());
        double weight = waitForDouble(
                String.format("Weight [%s]: ", widget.getWeight()),
                widget.getWeight());

        widget.setName(name);
        widget.setWidth(width);
        widget.setLength(length);
        widget.setHeight(height);
        widget.setWeight(weight);

        return widget;
    }

    public int promptForWidgetID() {
        return waitForInt("Which widget do you want?: ");
    }

    public void displayWidgets(List<Widget> widgets) {
        System.out.println("\n-- List Widgets --\n");

        if(widgets.size() == 0){
            System.out.println("\nNo Widgets were found.\n");
        } else {
            for (Widget widget : widgets) {
                System.out.printf("%s) %s\n", widget.getId(), widget.getName());
            }
        }
    }

    public void displayWidget(Widget widget) {
        System.out.printf("\n-- View a Widget --\n" +
                "\n" +
                "Name: %s\n" +
                "Type: %s\n" +
                "Width: %s\n" +
                "Height: %s\n" +
                "Length: %s\n" +
                "Weight: %s\n",
                widget.getName(),
                widget.getType(),
                widget.getWidth(),
                widget.getHeight(),
                widget.getLength(),
                widget.getWeight());

        System.out.println("\nNotes: \n");

        if(widget.getNotes().size() > 0) {
            for (Note note : widget.getNotes()) {
                System.out.printf("\t- %s\n", note.getText());
            }

        } else {
            System.out.println("\tNo notes found.");
        }
    }

    public void displayNoSuchWidget() {
        System.out.println("\nSorry, I couldn't find that widget.\n");
    }
}
