package entity;

/**
 * Created by doug on 9/15/16.
 */
public class WidgetType {

    private int id;
    private String type;

    public WidgetType(int id, String type) {
        this.id = id;
        this.type = type;
    }

    public WidgetType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
