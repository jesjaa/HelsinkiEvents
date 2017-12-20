package jessej.helsinkievents;

/**
 * Created by Jesse on 29.11.2017.
 */

public class Keyword {

    String id;
    String name;
    String category;
    boolean selected;

    public Keyword() {
    }

    public Keyword(String id, String name, String category) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.selected = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
