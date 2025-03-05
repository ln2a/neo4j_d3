package com.example.helloworld.pojo;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;

public class Movie {
    @Id
    @GeneratedValue
    private Long id;

    private String tagline;
    private String title;
    private Long released;

    public Movie() {

    }

    public Movie(String tagline, String title, Long released) {
        this.tagline = tagline;
        this.title = title;
        this.released = released;
    }

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public String getTagline() {return tagline;}

    public void setTagline(String tagline) {this.tagline = tagline;}

    public String getTitle() {return title;}

    public void setTitle(String title) {this.title = title;}

    public Long getReleased() {return released;}

    public void setReleased(Long released) {this.released = released;}


}
