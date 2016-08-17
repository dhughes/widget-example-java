package service;

import entity.Widget;

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
    public static final int QUIT = 6;

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
                "6) Quit\n");

        return waitForInt("Please choose an option:", 1, 6);
    }

    public Widget promptForWidgetData() {
        System.out.println("\n-- Create a Widget --\n");

        String name = waitForString("Name: ", true);
        double width = waitForDouble("Width: ", true);
        double height = waitForDouble("Height: ", true);
        double length = waitForDouble("Length: ", true);
        double weight = waitForDouble("Weight: ", true);

        return new Widget(name, width, height, length, weight);
    }

    public Widget promptForWidgetData(Widget widget) {
        System.out.println("\n-- Edit a Widget --\n");

        String name = waitForString(
                String.format("Name [%s]: ", widget.getName()),
                widget.getName());
        double width = waitForDouble(
                String.format("Width [%s]: ", widget.getWidth()),
                widget.getWidth());
        double height = waitForDouble(
                String.format("Height [%s]: ", widget.getHeight()),
                widget.getWidth());
        double length = waitForDouble(
                String.format("Length [%s]: ", widget.getWidth()),
                widget.getWidth());
        double weight = waitForDouble(
                String.format("Weight [%s]: ", widget.getWeight()),
                widget.getWeight());

        return new Widget(name, width, height, length, weight);
    }

    public int promptForWidgetID() {
        return waitForInt("Which widget do you want?: ");
    }

    public void displayWidgets(List<Widget> widgets) {
        System.out.println("\n-- List Widgets --\n");

        if(widgets.size() == 0){
            System.out.println("\nNo Widgets were found.\n");
        } else {
            for (int x = 0; x < widgets.size(); x++) {
                Widget widget = widgets.get(x);
                System.out.printf("%s) %s\n", x, widget.getName());
            }
        }
    }

    public void displayWidget(Widget widget) {
        System.out.printf("\n-- View a Widget --\n" +
                "\n" +
                "Name: %s\n" +
                "Width: %s\n" +
                "Height: %s\n" +
                "Length: %s\n" +
                "Weight: %s\n",
                widget.getName(),
                widget.getWidth(),
                widget.getHeight(),
                widget.getLength(),
                widget.getWeight());
    }

    public void displayNoSuchWidget() {
        System.out.println("\nSorry, I couldn't find that widget.\n");
    }
}
