package com.example.helloworld.pojo;

import org.springframework.data.neo4j.core.schema.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Node
public class Person {
    @Id
    @GeneratedValue
    private Long id;



    private String name;
    private Long born;


    @Relationship(type = "ACTED_IN",direction = Relationship.Direction.OUTGOING)
    private Set<Movie> actedInMovie=new HashSet<>();
    @Relationship(type="DIRECTED",direction = Relationship.Direction.OUTGOING)
    private Set<Movie> directedMovie=new HashSet<>();
//    @Relationship(type="FOLLOWS",direction = Relationship.Direction.OUTGOING)
//    private Set<Person> followsPerson=new HashSet<>();
    @Relationship(type="PRODUCED",direction = Relationship.Direction.OUTGOING)
    private Set<Movie> producedMovie=new HashSet<>();
    @Relationship(type="REVIEWED",direction = Relationship.Direction.OUTGOING)
    private Set<Movie> reviewedMovie=new HashSet<>();
    @Relationship(type="WROTE",direction = Relationship.Direction.OUTGOING)
    private Set<Movie> wroteMovie=new HashSet<>();


    private Person() {
        // Empty constructor required as of Neo4j API 2.0.5
    };

//    public Person(Map<String, Object> properties) {
//        this.properties=properties;
//        this.name=(String) properties.get("name");
//        this.born=(Long) properties.get("born");
//    }
    public Person(Long born, String name) {
        this.born = born;
        this.name = name;
    }


    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public Long getBorn() {return born;}

    public void setBorn(Long born) {this.born = born;}

    public Set<Movie> getActedInMovie() {return actedInMovie;}

    public void setActedInMovie(Set<Movie> actedInMovie) {this.actedInMovie = actedInMovie;}

    public Set<Movie> getDirectedMovie() {return directedMovie;}

    public void setDirectedMovie(Set<Movie> directedMovie) {this.directedMovie = directedMovie;}

//    public Set<Person> getFollowsPerson() {return followsPerson;}

//    public void setFollowsPerson(Set<Person> followsPerson) {this.followsPerson = followsPerson;}

    public Set<Movie> getProducedMovie() {return producedMovie;}

    public void setProducedMovie(Set<Movie> producedMovie) {this.producedMovie = producedMovie;}

    public Set<Movie> getReviewedMovie() {return reviewedMovie;}

    public void setReviewedMovie(Set<Movie> reviewedMovie) {this.reviewedMovie = reviewedMovie;}

    public Set<Movie> getWroteMovie() {return wroteMovie;}

    public void setWroteMovie(Set<Movie> wroteMovie) {this.wroteMovie = wroteMovie;}
}
