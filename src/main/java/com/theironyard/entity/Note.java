package com.theironyard.entity;


import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class Note {

    @Id
    @GeneratedValue
    private Integer id;

    @NotBlank
    @Length(max = 10000)
    @Column(length = 10000)
    private String text;

    @NotNull
    private Date date = new Date();

    public Note() {
    }

    public Note(String text) {
        this.text = text;
    }

    public Note(Integer id, String text) {
        this.id = id;
        this.text = text;
    }

    public Note(Integer id, String text, Date date) {
        this.id = id;
        this.text = text;
        this.date = date;
    }

    public Note(String text, Date date) {
        this.text = text;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
