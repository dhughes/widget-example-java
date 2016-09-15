package entity;

import java.util.Date;

/**
 * Created by doug on 9/6/16.
 */
public class Note {

    private int id;
    private String text;
    private Date date;

    public Note(String text) {
        this.text = text;
    }

    public Note(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public Note(int id, String text, Date date) {
        this.id = id;
        this.text = text;
        this.date = date;
    }

    public Note(String text, Date date) {
        this.text = text;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
