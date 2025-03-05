package com.example.helloworld.service;

import com.example.helloworld.dao.PersonDao;
import com.example.helloworld.pojo.Movie;
import com.example.helloworld.pojo.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class PersonService {
    @Autowired
    PersonDao personDao;

    public Person findByName(String name){
        return personDao.findByName(name);
    }

    public Iterable<Person> findAll(){
        return personDao.findAll();
    }

    public Set<Movie> getMoviesByRelationship(Person person,String rel){
        switch (rel) {
            case "actedby":
                return person.getActedInMovie();
            case "directed":
                return person.getDirectedMovie();
            case "produced":
                return person.getProducedMovie();
            case "reviewed":
                return person.getReviewedMovie();
            case "wrote":
                return person.getWroteMovie();
            default:
                return null;
        }
    }
}
