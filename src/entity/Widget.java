package entity;

import java.util.ArrayList;

/**
 * Represents the general concept of a Widget
 */
public class Widget {

    private int id;
    private String name;
    private double width;
    private double height;
    private double length;
    private double weight;
    private ArrayList<Note> notes = new ArrayList<>();
    private String type;

    // no arg constructor
    public Widget() {
    }

    public Widget(String name, double width, double height, double length, double weight) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.length = length;
        this.weight = weight;
    }

    public Widget(int id, String name, double width, double height, double length, double weight) {
        this.id = id;
        this.name = name;
        this.width = width;
        this.height = height;
        this.length = length;
        this.weight = weight;
    }

    public Widget(int id, String name, String type, double width, double height, double length, double weight) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.width = width;
        this.height = height;
        this.length = length;
        this.weight = weight;
    }

    public Widget(String name, String type, double width, double height, double length, double weight) {
        this.name = name;
        this.type = type;
        this.width = width;
        this.height = height;
        this.length = length;
        this.weight = weight;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public ArrayList<Note> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<Note> notes) {
        this.notes = notes;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}



