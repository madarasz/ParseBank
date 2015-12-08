package com.madarasz.parsebank.database;

import org.springframework.data.neo4j.annotation.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by imadaras on 12/7/15.
 */
@NodeEntity
public class Category {
    @GraphId
    private Long id;
    @Indexed(unique=true) private String title;
    private String regex;
    private int count;

    public Category() {
        this.count = 0;
    }

    public Category(String title, String regex) {
        this.title = title;
        this.regex = regex;
        this.count = 0;
    }

    public String getTitle() {
        return title;
    }

    public String getRegex() {
        return regex;
    }

    public int getCount() {
        return count;
    }

    public Long getId() {
        return id;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
