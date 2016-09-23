package com.theironyard.entity;


import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Type {

    @Id
    @GeneratedValue
    @NotNull
    private Integer id;

    @NotBlank
    private String type;

    public Type() {
    }

    public Type(Integer id, String type) {
        this.id = id;
        this.type = type;
    }

    public Type(String type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
