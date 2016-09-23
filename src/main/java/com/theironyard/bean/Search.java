package com.theironyard.bean;

import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by doug on 9/21/16.
 */
public class Search {

    private String name = null;
    private Integer typeId = null;
    private Integer id = null;

    public Search() {
    }

    public Search(String name, Integer typeId, Integer id) {
        setName(name);
        this.typeId = typeId;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getNameForSearch(){
        return name == null || name.equals("") ? "" : "%" + name + "%";
    }

    public void setName(String name) {
        // if the provided name is "" then set to null
        this.name = (name == null || name.equals("") ? null : name);
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
