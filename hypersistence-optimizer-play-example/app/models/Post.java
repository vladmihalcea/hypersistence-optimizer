package models;

import javax.persistence.*;

@Entity
public class Post {

    @Id
    @GeneratedValue
    public Long id;

    public String title;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
