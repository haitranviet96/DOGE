package com.haitr.doge.Object;

import com.haitr.doge.Object.Dish;

/**
 * Created by haitr on 5/19/2017.
 */

public class Food extends Dish {
    private int id;
    private String name, type, course, description, image;

    public Food(String name, String description, String image) {
        this.name = name;
        this.description = description;
        this.image = image;
    }

    public Food(int id, String name, String type, String course, String description, String image) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.course = course;
        this.description = description;
        this.image = image;
    }

    public Food() {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
