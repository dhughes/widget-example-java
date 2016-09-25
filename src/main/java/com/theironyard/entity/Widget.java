package com.theironyard.entity;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents the general concept of a Widget
 */
@Entity
public class Widget {

    @Id
    @GeneratedValue
    private Integer id;

    @NotBlank
    @Length(max = 255)
    @Column(unique = true)
    private String name;

    // Very important note: make sure you're adding the correct @NotNull
    @NotNull
    private Double width;

    @NotNull
    private Double height;

    @NotNull
    private Double length;

    @NotNull
    private Double weight;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "widget_id", nullable = false)
    private Set<Note> notes = new HashSet<>();

    @ManyToMany
    @NotNull
    private List<Type> types = new ArrayList<>();

    @Lob
    @Basic(fetch=FetchType.EAGER)
    private byte[] image;

    private String contentType;

    // no arg constructor
    public Widget() {
    }

    public Widget(String name, Double width, Double height, Double length, Double weight) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.length = length;
        this.weight = weight;
    }

    public Widget(Integer id, String name, Double width, Double height, Double length, Double weight) {
        this.id = id;
        this.name = name;
        this.width = width;
        this.height = height;
        this.length = length;
        this.weight = weight;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Set<Note> getNotes() {
        return notes;
    }

    public void setNotes(Set<Note> notes) {
        this.notes = notes;
    }

    public List<Type> getTypes() {
        return types;
    }

    public void setTypes(List<Type> types) {
        this.types = types;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}



